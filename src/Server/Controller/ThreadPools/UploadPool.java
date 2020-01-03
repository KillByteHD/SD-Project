package Server.Controller.ThreadPools;

import Common.Model.Data;
import Common.Protocol.Request;
import Server.Controller.BoundedBuffer;
import Server.Controller.ConnectionMutex;
import Server.Controller.Notifier;
import Server.Utils.Tuple;

public class UploadPool extends WorkerPool
{
    // Varibles
    private Notifier notifier;

    public UploadPool(Data data, BoundedBuffer<Tuple<ConnectionMutex, Request>> bb, Notifier notifier, int workers)
    {
        super(data,bb,workers);
        this.notifier = notifier;
    }

    @Override
    public void init()
    {
        for (int i = 0; i < super.WORKERS; i++)
            ts[i] = new UploadWorker(super.buffer,super.data,this.notifier);

        //for (int i = 0; i < this.WORKERS; i++)
        //    ts[i].setName("Uploader " + (i+1));

        for (int i = 0; i < this.WORKERS; i++)
            ts[i].start();
    }

}
