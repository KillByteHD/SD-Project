package Server;

import Common.Model.Data;
import Common.Protocol.Request;
import Server.Controller.*;
import Server.Controller.ThreadPools.UploadPool;
import Server.Controller.ThreadPools.WorkerPool;
import Server.Model.ServerData;
import Server.Utils.Tuple;
import Server.View.Logger;

import java.net.ServerSocket;
import static Server.Controller.Config.*;

public class ServerInit
{
    public static final String SERVER_PATH = ServerInit.class.getResource("../").getPath();

    public static void main(String[] args) throws Exception
    {
        // Load Server Configuration
        Config.loadConfig();

        // Start Server
        ServerSocket ss = new ServerSocket(PORT);
        Logger.started();

        Data data = new ServerData();

        Notifier notifier = new Notifier();

        BoundedBuffer<Tuple<ConnectionMutex, Request>> buffer = new BoundedBuffer<>(SIZE_OTHER);
        BoundedBuffer<Tuple<ConnectionMutex, Request>> down_buffer = new BoundedBuffer<>(SIZE_DOWN);
        BoundedBuffer<Tuple<ConnectionMutex, Request>> up_buffer = new BoundedBuffer<>(SIZE_UP);

        WorkerPool pool = new WorkerPool(data,buffer,MAX_OTHER);
        WorkerPool download_pool = new WorkerPool(data,down_buffer,MAX_DOWN);
        UploadPool upload_pool = new UploadPool(data,up_buffer,notifier,MAX_UP);

        pool.init();
        download_pool.init();
        upload_pool.init();
        new Thread(notifier).start();

        while(true)
            new ServerThread(ss.accept(),buffer,down_buffer,up_buffer,notifier)
                    .start();
    }
}