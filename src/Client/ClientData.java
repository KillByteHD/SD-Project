package Client;

import Common.Exceptions.*;
import Common.Protocol.C2DReply;
import Common.Protocol.C2DRequest;
import Common.Model.Data;
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

    public void register(String username, String password) throws UserAlreadyExists, ConnectException
    {
        try
        {
            Request request = new C2DRequest.Register(username,password);
            this.pw.println(request.write());

            String in = this.br.readLine();
            if(in == null)
                throw new ConnectException();

            try
            {
                C2DReply.Register reply = (C2DReply.Register) C2DReply.parse(in);
            }
            catch (ClassCastException cce)
            {
                C2DReply.Login reply = (C2DReply.Login) C2DReply.parse(in);

                if(reply.getStatus() == ExceptionCode.UserAlreadyExists)
                    throw new UserAlreadyExists();
            }
        }
        catch (IOException | ProtocolParseError e)
        {
            throw new ConnectException();
        }
    }

    @Override
    public void download(String id_music) throws ConnectException
    {

    }

    @Override
    public void upload(String file_path) throws ConnectException
    {
        /*try
        {

        }
        catch (IOException | ProtocolParseError e)
        {
            throw new ConnectException();
        }*/
    }
}
