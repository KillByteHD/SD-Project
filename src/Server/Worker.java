package Server;

import Client.ClientInit;
import Common.Exceptions.*;
import Common.Model.Data;
import Common.Model.Music;
import Common.Protocol.C2DReply;
import Common.Protocol.C2DRequest;
import Common.Protocol.Reply;
import Common.Protocol.Request;
import Server.Utils.Tuple;

import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

public class Worker extends Thread
{
    private final int MAX_SIZE = 8*1024;

    private BoundedBuffer<Tuple<ConnectionMutex, Request>> bb;
    private Data data;

    private boolean isAlive = true;

    public Worker(BoundedBuffer<Tuple<ConnectionMutex, Request>> bb, Data data)
    {
        this.bb = bb;
        this.data = data;
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
            if(request instanceof C2DRequest.Login)
                login_work((C2DRequest.Login) request,cm);

            else if(request instanceof C2DRequest.Logout)
                logout_work((C2DRequest.Logout) request,cm);

            else if(request instanceof C2DRequest.Register)
                register_work((C2DRequest.Register) request,cm);

            else if(request instanceof C2DRequest.Download)
                download_work((C2DRequest.Download) request,cm);

            else if(request instanceof C2DRequest.Upload)
                upload_work((C2DRequest.Upload) request,cm);
        }
    }


    private void login_work(C2DRequest.Login request, ConnectionMutex cm)
    {
        Reply reply = null;

        try
        {
            String auth = this.data.login(request.getUsername(),request.getPassword());
            reply = new C2DReply.Login(auth);
        }
        catch (InvalidLogin ile)
        {
            reply = new C2DReply.Login(ile.getCode());
        }
        catch (ConnectException ce)
        {
            // Not possible to happen in this implementation
            reply = new C2DReply.Login(ExceptionCode.ServerError);
        }
        finally
        {
            cm.println(reply.write());
            Logger.sended(cm.getSocket(),reply.write());
        }
    }

    private void logout_work(C2DRequest.Logout request, ConnectionMutex cm)
    {
        Reply reply = null;

        try
        {
            this.data.logout(request.getAuth());
            reply = new C2DReply.Logout();
        }
        catch (NotLoggedIn nli)
        {
            reply = new C2DReply.Logout(nli.getCode());
        }
        catch (ConnectException ce)
        {
            // Not possible to happen in this implementation
            reply = new C2DReply.Logout(ExceptionCode.ServerError);
        }
        finally
        {
            cm.println(reply.write());
            Logger.sended(cm.getSocket(),reply.write());
        }
    }

    private void register_work(C2DRequest.Register request, ConnectionMutex cm)
    {
        Reply reply = null;

        try
        {
            this.data.register(request.getUsername(),request.getPassword());
            reply = new C2DReply.Register();
        }
        catch (UserAlreadyExists uae)
        {
            reply = new C2DReply.Register(uae.getCode());
        }
        catch (ConnectException ce)
        {
            // Nao seria possivel acontecer nesta implementacao
            reply = new C2DReply.Register(ExceptionCode.ServerError);
        }
        finally
        {
            cm.println(reply.write());
            Logger.sended(cm.getSocket(),reply.write());
        }
    }

    private void download_work(C2DRequest.Download request, ConnectionMutex cm)
    {
        Reply reply = null;

        try
        {
            Music m = this.data.download(request.getAuth(),request.getIDmusic());
            //Debug//System.out.println("Music : " + m.getName());

            // See file length
            File file = new File(Worker.class.getResource("../").getPath() + m.getFilePath());
            long length = file.length();
            //Debug//System.out.println("File size: " + length);
            reply = new C2DReply.Download(m.getName(),m.getAuthor(),m.getGenre(),m.getArtist(),m.getFileName(),length);

            // Send meta data (preparing to download file)
            cm.println(reply.write());
            Logger.sended(cm.getSocket(),reply.write());

            // Download file as byte[]
            try(FileInputStream fis = new FileInputStream(file))
            {
                byte[] bytes = new byte[MAX_SIZE];

                //Debug//System.out.println("Sending File");
                int count;
                cm.lock();
                while((count = fis.read(bytes)) > 0)
                {
                    //Debug//System.out.println("Sended:" + count + " bytes");
                    cm.write(bytes,count);
                }
                cm.unlock();
            }
            catch (IOException ioe)
            {
                System.out.println(ioe.getMessage());
                ioe.printStackTrace();
            }
        }
        catch (Unauthorized | InvalidMusic im)
        {
            reply = new C2DReply.Download(im.getCode());
            cm.println(reply.write());
            Logger.sended(cm.getSocket(),reply.write());
        }
        catch (ConnectException ce)
        {
            // Nao seria possivel acontecer nesta implementacao
            reply = new C2DReply.Download(ExceptionCode.ServerError);
            cm.println(reply.write());
        }
    }

    private void upload_work(C2DRequest.Upload request, ConnectionMutex cm)
    {
        //This upload work doesn't notify all connected users
        Reply reply = null;

        try
        {
            Music m = new Music(request.getName(),request.getAuthor(),
                    request.getGenre(),request.getArtist(),
                    "server_music/" + request.getFileName());
            this.data.upload(request.getAuth(),m);


            // Send upload confirmation and port to create secure socket (preparing to upload file)
            ServerSocket upload_ss = new ServerSocket(0);

            reply = new C2DReply.Upload(upload_ss.getLocalPort());
            cm.println(reply.write());
            Logger.sended(cm.getSocket(),reply.write());

            ///////////////////////////////////////////////////////////////////

            try(Socket upload_s = upload_ss.accept())
            {
                File file = new File(Worker.class.getResource("../").getPath() + m.getFilePath());
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
