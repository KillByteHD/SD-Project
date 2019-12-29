package Server;

import Common.Exceptions.ProtocolParseError;
import Common.Model.Data;
import Common.Protocol.C2DRequest;
import Common.Protocol.Reply;
import Common.Protocol.Request;
import Server.Utils.Tuple;

import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread
{
    private ConnectionMutex cm;
    private BoundedBuffer<Tuple<ConnectionMutex, Request>> buffer;
    private BoundedBuffer<Tuple<ConnectionMutex, Request>> down_buffer;
    private BoundedBuffer<Tuple<ConnectionMutex, Request>> up_buffer;

    public ServerThread(Socket socket,
                        BoundedBuffer<Tuple<ConnectionMutex, Request>> buffer,
                        BoundedBuffer<Tuple<ConnectionMutex, Request>> down_buffer,
                        BoundedBuffer<Tuple<ConnectionMutex, Request>> up_buffer)
    {
        this.cm = new ConnectionMutex(socket);
        this.buffer = buffer;
        this.down_buffer = down_buffer;
        this.up_buffer = up_buffer;
        Logger.connected(this.cm.getSocket());
    }

    public void run()
    {
        while(!this.cm.isClosed())
        {
            Request request = null;

            try
            {
                String in = this.cm.readln();
                Logger.received(this.cm.getSocket(), in);
                request = C2DRequest.parse(in);
            }
            catch (ProtocolParseError | IOException e)
            { break; }


            // Insert the Request in the right queue
            if(request instanceof C2DRequest.Download)
                this.down_buffer.put(new Tuple<>(this.cm,request));

            else if(request instanceof C2DRequest.Upload)
                this.up_buffer.put(new Tuple<>(this.cm,request));

            else
                this.buffer.put(new Tuple<>(this.cm,request));
        }

        try
        {
            this.cm.close();
            Logger.disconnected(this.cm.getSocket());
        }
        catch (IOException e)
        { e.printStackTrace(); }
    }
}
