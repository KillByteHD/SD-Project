package Client;

import Common.Exceptions.ProtocolParseError;
import Common.Protocol.C2DRequest;
import Common.Model.Data;
import Common.Exceptions.InvalidLogin;
import Common.Protocol.Reply;
import Common.Protocol.Request;

import java.io.*;
import java.net.Socket;

public class ClientData implements Data
{
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;

    public ClientData()
    {
        try
        {
            this.socket = new Socket("localhost",1111);
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.pw = new PrintWriter(this.socket.getOutputStream(),true);
        }
        catch (IOException e) { }
    }

    @Override
    public void login(String username, String password) throws InvalidLogin
    {
        try
        {
            Request request = new C2DRequest.Login(username,password);
            this.pw.println(request.write());
            Reply reply = Reply.parse(this.br.readLine());
            System.out.println(reply.write());
        }
        catch (IOException | ProtocolParseError e)
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
