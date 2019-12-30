package Server;

import Common.Model.Data;
import Common.Protocol.Request;
import Server.Utils.Tuple;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;

public class ServerInit
{
    // Server Constraints
    public static int SIZE_DOWN;
    public static int SIZE_UP;
    public static int SIZE_OTHER;
    public static int MAX_DOWN;
    public static int MAX_UP;
    public static int MAX_OTHER;
    public static int PORT;

    public static final String SERVER_PATH = ServerInit.class.getResource("./").getPath();


    public static void main(String[] args) throws Exception
    {
        // Load Server Configuration
        try (FileInputStream input = new FileInputStream(SERVER_PATH + "config.properties"))
        {
            Properties properties = new Properties();
            // load config file
            properties.load(input);

            SIZE_DOWN = Integer.parseInt(properties.getProperty("size_down"));
            SIZE_UP = Integer.parseInt(properties.getProperty("size_up"));
            SIZE_OTHER = Integer.parseInt(properties.getProperty("size_other"));
            MAX_DOWN = Integer.parseInt(properties.getProperty("max_down"));
            MAX_UP = Integer.parseInt(properties.getProperty("max_up"));
            MAX_OTHER = Integer.parseInt(properties.getProperty("max_other"));
            PORT = Integer.parseInt(properties.getProperty("port"));
        }
        catch (IOException | NumberFormatException ex)
        {
            // Assign default values
            SIZE_DOWN = 10;
            SIZE_UP = 10;
            SIZE_OTHER = 10;
            MAX_DOWN = 5;
            MAX_UP = 5;
            MAX_OTHER = 5;
            PORT = 1111;
        }

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