package Client;

import Common.Exceptions.ExceptionCode;
import Common.Exceptions.ProtocolParseError;
import Common.Exceptions.ServerError;
import Common.Protocol.C2DReply;
import Common.Protocol.C2DRequest;
import Common.Model.Data;
import Common.Exceptions.InvalidLogin;
import Common.Protocol.Reply;
import Common.Protocol.Request;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class ClientData implements Data
{
    private final int CONNECT_DELAY = 3;

    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;

    public ClientData()
    {
        while(true)
        {
            try
            {
                this.socket = new Socket("localhost",1111);
                this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                this.pw = new PrintWriter(this.socket.getOutputStream(),true);
                break;
            }
            catch (IOException e)
            {
                System.out.println("Unable to connect to server ... Retrying in " + CONNECT_DELAY + " secs ...");
                try { Thread.sleep(CONNECT_DELAY * 1000); }
                catch (InterruptedException ignored) { }
            }
        }
    }

    @Override
    public String login(String username, String password) throws InvalidLogin, ConnectException
    {
        try
        {
            Request request = new C2DRequest.Login(username,password);
            this.pw.println(request.write());

            String in = this.br.readLine();
            if(in == null)
                throw new ConnectException();
            C2DReply.Login reply = (C2DReply.Login) C2DReply.parse(in);

            if(reply.getStatus() == ExceptionCode.InvalidLogin)
                throw new InvalidLogin();

            return reply.getAuth();
        }
        catch (IOException | ProtocolParseError e)
        {
            throw new ConnectException();
        }
    }

    @Override
    public File download()
    {
        return null;
    }

    @Override
    public void upload(File music)
    {

    }
}
