package Server;

import Common.Model.Data;
import Common.Protocol.Request;
import Server.Utils.Tuple;

public class WorkerPool
{
    // Constants
    private final int WORKERS = 10;
    private final int BUFFER_SIZE = 5;

    // Varibles
    private Thread[] ts;
    private BoundedBuffer<Tuple<ConnectionMutex, Request>> buffer;
    private Data data;

    public WorkerPool(Data data, BoundedBuffer<Tuple<ConnectionMutex, Request>> bb)
    {
        this.ts = new Thread[WORKERS];
        this.buffer = bb;
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
