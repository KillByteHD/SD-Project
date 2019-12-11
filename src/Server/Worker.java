package Server;

import Common.Model.Data;
import Common.Protocol.Request;
import Server.Utils.Tuple;

public class Worker extends Thread
{
    private BoundedBuffer<Tuple<ConnectionMutex, Request>> bb;
    private Data data;

    private boolean isAlive = true;

    public Worker(BoundedBuffer<Tuple<ConnectionMutex, Request>> bb, Data data)
    {
        this.bb = bb;
        this.data = data;

    }

    @Override
    public void run()
    {
        while(this.isAlive)
        {
            Request r = bb.get().getSnd();
            // Continue
        }
    }

    public void killWorker()
    {
        this.isAlive = false;
    }
}
