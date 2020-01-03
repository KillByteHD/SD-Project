package Server.Controller.ThreadPools;

import Common.Exceptions.ExceptionCode;
import Common.Exceptions.MusicAlreadyExists;
import Common.Exceptions.Unauthorized;
import Common.Model.Data;
import Common.Model.Music;
import Common.Protocol.*;
import Server.Controller.BoundedBuffer;
import Server.Controller.ConnectionMutex;
import Server.Controller.Notifier;
import Server.ServerInit;
import Server.Utils.Tuple;
import Server.View.Logger;
import static Server.Controller.Config.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

public class UploadWorker extends Thread
{
    private BoundedBuffer<Tuple<ConnectionMutex, Request>> bb;
    private Data data;
    private Notifier notifier;

    private boolean isAlive = true;

    public UploadWorker(BoundedBuffer<Tuple<ConnectionMutex, Request>> bb, Data data, Notifier notifier)
    {
        this.bb = bb;
        this.data = data;
        this.notifier = notifier;
    }


    @Override
    public void run()
    {
        while(this.isAlive)
        {
            Tuple<ConnectionMutex, Request> tuple = bb.get();
            ConnectionMutex cm = tuple.fst();
            Request request = tuple.snd();

            // Which type of work is it?
            if(request instanceof C2DRequest.Upload)
                upload_work((C2DRequest.Upload) request,cm);
        }
    }

    private void upload_work(C2DRequest.Upload request, ConnectionMutex cm)
    {
        Reply reply = null;

        try
        {
            Music m = new Music(request.getName(),request.getAuthor(),
                    request.getGenre(),request.getArtist(),
                    "server_music/" + request.getFileName());
            this.data.upload(request.getAuth(),m,request.getTags());


            // Send upload confirmation and port to create secure socket (preparing to upload file)
            ServerSocket upload_ss = new ServerSocket(0);

            reply = new C2DReply.Upload(upload_ss.getLocalPort());
            cm.println(reply.write());
            Logger.sended(cm.getSocket(),reply.write());

            ///////////////////////////////////////////////////////////////////

            try(Socket upload_s = upload_ss.accept())
            {
                File file = new File(ServerInit.SERVER_PATH + m.getFilePath());
                // Create file if not exists
                file.createNewFile();


                DataInputStream dis = new DataInputStream(upload_s.getInputStream());
                try(FileOutputStream fos = new FileOutputStream(file))
                {
                    int count;
                    byte[] bytes = new byte[MAX_SIZE];
                    long length = request.getFileLength();

                    for(; length > 0 ; length -= count)
                    {
                        count = dis.read(bytes,0,(MAX_SIZE > length) ? (int) length : MAX_SIZE);
                        Logger.received(upload_s,count + " bytes");
                        //System.out.println("Received: " + count + " bytes");
                        fos.write(bytes,0,count);
                        //fos.flush(); // Flush not necessary because we are in a try-with-resources clause (fos will be closed and flushed)
                    }
                    this.notifier.put(m);
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }
        catch (Unauthorized | MusicAlreadyExists mae)
        {
            reply = new C2DReply.Upload(mae.getCode());
            cm.println(reply.write());
            Logger.sended(cm.getSocket(),reply.write());
        }
        catch (ConnectException ce)
        {
            // Nao seria possivel acontecer nesta implementacao
            reply = new C2DReply.Upload(ExceptionCode.ServerError);
            cm.println(reply.write());
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public void killWorker()
    {
        this.isAlive = false;
    }
}
