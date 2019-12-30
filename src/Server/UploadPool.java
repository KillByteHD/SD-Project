package Server;

import Common.Model.Data;
import Common.Protocol.Request;
import Server.Utils.Tuple;

public class UploadPool
{
    // Constraints
    private final int WORKERS;

    // Varibles
    private Thread[] ts;
    private BoundedBuffer<Tuple<ConnectionMutex, Request>> buffer;
    private Data data;
    private Notifier notifier;

    public UploadPool(Data data, BoundedBuffer<Tuple<ConnectionMutex, Request>> bb, Notifier notifier, int workers)
    {
        this.WORKERS = workers;
        this.ts = new Thread[this.WORKERS];
        this.buffer = bb;
        this.data = data;
        this.notifier = notifier;
    }

    public void init()
    {
        for (int i = 0; i < this.WORKERS; i++)
            ts[i] = new UploadWorker(this.buffer,this.data,this.notifier);

        //for (int i = 0; i < this.WORKERS; i++)
        //    ts[i].setName("Uploader " + (i+1));

        for (int i = 0; i < this.WORKERS; i++)
            ts[i].start();
    }

}
