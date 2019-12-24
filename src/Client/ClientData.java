package Client;

import Common.Exceptions.ProtocolParseError;
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
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;

    public ClientData() throws ConnectException
    {
        try
        {
            this.socket = new Socket("localhost",1111);
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.pw = new PrintWriter(this.socket.getOutputStream(),true);
        }
        catch (IOException e)
        {
            throw new ConnectException();
        }
    }

    @Override
    public void login(String username, String password) throws InvalidLogin, ConnectException
    {
        try
        {
            Request request = new C2DRequest.Login(username,password);
            this.pw.println(request.write());
            System.out.println("OK1");

            String asd = this.br.readLine();
            Reply reply = C2DReply.parse(asd);
            System.out.println("OK2");
            System.out.println(reply.write());
            System.out.println("OK3");

        }
        catch (IOException ioe)
        {
            throw new ConnectException();
        }
        catch (ProtocolParseError ppe)
        {
            throw new InvalidLogin();
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
