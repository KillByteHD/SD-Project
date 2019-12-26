package Server;

import Common.Exceptions.ExceptionCode;
import Common.Exceptions.InvalidLogin;
import Common.Exceptions.InvalidMusic;
import Common.Exceptions.UserAlreadyExists;
import Common.Model.Data;
import Common.Protocol.C2DReply;
import Common.Protocol.C2DRequest;
import Common.Protocol.Reply;
import Common.Protocol.Request;
import Server.Utils.Tuple;

import java.net.ConnectException;

public class Worker extends Thread
{
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
            Reply reply = null;
            // Continue

            if(request instanceof C2DRequest.Login)
            {
                C2DRequest.Login tmp = (C2DRequest.Login) request;
                try
                {
                    String auth = this.data.login(tmp.getUsername(),tmp.getPassword());
                    reply = new C2DReply.Login(auth);
                }
                catch (InvalidLogin ile)
                {
                    reply = new C2DReply.Login(ile.getCode());
                }
                catch (ConnectException ce)
                {
                    // Nao seria possivel acontecer nesta implementacao
                    reply = new C2DReply.Login(ExceptionCode.ServerError);
                }
                finally
                {
                    cm.println(reply.write());
                    Logger.sended(cm.getSocket(),reply.write());
                }
            }
            else if(request instanceof C2DRequest.Register)
            {
                C2DRequest.Register tmp = (C2DRequest.Register) request;
                try
                {
                    this.data.register(tmp.getUsername(),tmp.getPassword());
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
            else if(request instanceof C2DRequest.Download)
            {
                C2DRequest.Download tmp = (C2DRequest.Download) request;
                try
                {
                    this.data.download(tmp.getIDmusic());
                }
                catch (InvalidMusic im)
                {
                    reply = new C2DReply.Download(im.getCode());
                }
                catch (ConnectException ce)
                {
                    // Nao seria possivel acontecer nesta implementacao
                    reply = new C2DReply.Download(ExceptionCode.ServerError);
                }
            }
        }
    }

    public void killWorker()
    {
        this.isAlive = false;
    }
}
