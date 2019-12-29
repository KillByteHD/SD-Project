package Server;

import Common.Model.Data;
import Common.Protocol.Request;
import Server.Utils.Tuple;

public class WorkerPool
{
    // Constraints
    private final int WORKERS;

    // Varibles
    private Thread[] ts;
    private BoundedBuffer<Tuple<ConnectionMutex, Request>> buffer;
    private Data data;

    public WorkerPool(Data data, BoundedBuffer<Tuple<ConnectionMutex, Request>> bb, int workers)
    {
        this.WORKERS = workers;
        this.ts = new Thread[this.WORKERS];
        this.buffer = bb;
        this.data = data;
    }

    public void init()
    {
        for (int i = 0; i < this.WORKERS; i++)
            ts[i] = new Worker(this.buffer,this.data);

        //for (int i = 0; i < this.WORKERS; i++)
        //    ts[i].setName("Worker " + (i+1));

        for (int i = 0; i < this.WORKERS; i++)
            ts[i].start();
    }
}
