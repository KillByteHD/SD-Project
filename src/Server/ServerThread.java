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

    public ServerThread(Socket socket, BoundedBuffer<Tuple<ConnectionMutex, Request>> buffer)
    {

        this.cm = new ConnectionMutex(socket);
        this.buffer = buffer;
        Logger.connected(this.cm.getSocket());
    }

    public void run()
    {
        while(!this.cm.isClosed())
        {
            Request request = null;

            try
            {
                while(true)
                {
                    break;
                }

                String in = this.cm.readln();
                Logger.received(this.cm.getSocket(), in);
                request = C2DRequest.parse(in);
            }
            catch (ProtocolParseError | IOException e)
            { break; }

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
