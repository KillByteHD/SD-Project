package Server;

import Common.Model.Data;
import Common.Protocol.Request;

public class Worker extends Thread
{
    private BoundedBuffer<Request> bb;
    private Data data;

    private boolean isAlive = true;

    public Worker(BoundedBuffer<Request> bb, Data data)
    {
        this.bb = bb;
        this.data = data;

    }

    @Override
    public void run()
    {
        while(this.isAlive)
        {
            Request r = bb.get();
            // Continue
        }
    }

    public void killWorker()
    {
        this.isAlive = false;
    }
}
