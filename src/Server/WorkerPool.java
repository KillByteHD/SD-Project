package Server;

import Common.Model.Data;
import Common.Protocol.Request;

import java.net.ServerSocket;

public class WorkerPool
{
    // Constants
    private final int WORKERS = 10;
    private final int BUFFER_SIZE = 5;

    // Varibles
    private Thread[] ts;
    private BoundedBuffer<Request> buffer;
    private ServerSocket serverSocket;
    private Data data;

    public WorkerPool(ServerSocket serverSocket, Data data)
    {
        this.ts = new Thread[WORKERS];
        this.buffer = new BoundedBuffer<>(BUFFER_SIZE);
        this.serverSocket = serverSocket;
        this.data = data;
    }

    public void init()
    {
        for (int i = 0; i < WORKERS; i++)
            ts[i] = new Worker(this.buffer,this.data);

        for (int i = 0; i < WORKERS; i++)
            ts[i].setName("Worker " + (i+1));

        for (int i = 0; i < WORKERS; i++)
            ts[i].start();
    }
}
