package Server.Controller.ThreadPools;

import Common.Model.Data;
import Common.Protocol.Request;
import Server.Controller.BoundedBuffer;
import Server.Controller.ConnectionMutex;
import Server.Utils.Tuple;

//Used as generic work pool
public class WorkerPool
{
    // Constraints
    protected final int WORKERS;

    // Varibles
    protected Thread[] ts;
    protected BoundedBuffer<Tuple<ConnectionMutex, Request>> buffer;
    protected Data data;

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
