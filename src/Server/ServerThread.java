package Server;

import Common.Exceptions.InvalidLogin;
import Common.Exceptions.ProtocolParseError;
import Common.Model.Data;
import Common.Protocol.C2DReply;
import Common.Protocol.C2DRequest;
import Common.Protocol.Reply;
import Common.Protocol.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable
{
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;

    private Data data;

    public ServerThread(Socket socket, Data data)
    {
        try
        {
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.pw = new PrintWriter(this.socket.getOutputStream(),true);
            this.data = data;
            Logger.connected(this.socket.getInetAddress().getHostAddress());
        }
        catch(IOException e) { }
    }

    public void run()
    {
        while(!this.socket.isClosed())
        {
            Request request = null;
            Reply reply = null;

            try
            {
                String in = this.br.readLine();
                Logger.received(this.socket.getInetAddress().getHostAddress(), in);
                request = Request.parse(in);
            }
            catch (ProtocolParseError | IOException e) { break; }

            //TODO: WTF IS THIS SHIT? PLS FIX ... FAGGOT

            if(request instanceof C2DRequest.Login)
            {
                C2DRequest.Login tmp = (C2DRequest.Login) request;
                try
                {
                    this.data.login(tmp.getUsername(),tmp.getPassword());
                    reply = new C2DReply.Login();
                }
                catch (InvalidLogin e)
                {
                    reply = new C2DReply.Login(e.getCode());
                }
                finally
                {

                    this.pw.println(reply.write());
                    Logger.sended(reply.write());
                }
            }
        }

        try
        {
            this.socket.close();
            Logger.disconnected(this.socket.getInetAddress().getHostAddress());
        }
        catch (IOException e)
        { e.printStackTrace(); }
    }
}
