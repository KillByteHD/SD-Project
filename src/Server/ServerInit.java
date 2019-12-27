package Server;

import Common.Model.Data;
import Common.Protocol.Request;
import Server.Utils.Tuple;

import java.net.ServerSocket;

public class ServerInit
{
    public static void main(String[] args) throws Exception
    {
        ServerSocket ss = new ServerSocket(1111);
        Logger.started();

        BoundedBuffer<Tuple<ConnectionMutex, Request>> buffer = new BoundedBuffer<>(5);
        //Receiver rc = new Receiver(buffer); //equivalente ao ServerThread
        Data data = new ServerData();
        WorkerPool pool = new WorkerPool(data,buffer);
        pool.init();

        while(true)
        {
            new ServerThread(ss.accept(),buffer)
                    .start();
        }
    }
}
