package Client;

import Common.Exceptions.*;
import Common.Model.Music;
import Common.Protocol.C2DReply;
import Common.Protocol.C2DRequest;
import Common.Model.Data;
import Common.Protocol.Request;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class ClientData implements Data
{
    private final int CONNECT_DELAY = 3;
    private final int MAX_SIZE = 8*1024;

    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ClientData()
    {
        while(true)
        {
            try
            {
                this.socket = new Socket("localhost",1111);
                this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                this.pw = new PrintWriter(this.socket.getOutputStream(),true);
                this.dis = new DataInputStream(this.socket.getInputStream());
                this.dos = new DataOutputStream(this.socket.getOutputStream());
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

            C2DReply.Register reply = (C2DReply.Register) C2DReply.parse(in);
            if(reply.getStatus() == ExceptionCode.InvalidMusic)
                throw new UserAlreadyExists();
        }
        catch (IOException | ProtocolParseError e)
        {
            throw new ConnectException();
        }
    }

    @Override
    public Music download(String id_music) throws InvalidMusic, ConnectException
    {
        try
        {
            Request request = new C2DRequest.Download(id_music);
            this.pw.println(request.write());

            String in = this.br.readLine();
            System.out.println((in != null) ? in : "(null)");
            if(in == null)
                throw new ConnectException();



            // Receive meta data
            C2DReply.Download reply = (C2DReply.Download) C2DReply.parse(in);
            if(reply.getStatus() == ExceptionCode.InvalidMusic)
                throw new InvalidMusic();


            // Receive file bytes
            File file = new File(ClientInit.class.getResource("../").getPath() + "client_music/hello_world.mp3");
            //System.out.println("Directory created: " + file.mkdir());
            System.out.println("File created: " + file.createNewFile());

            try(FileOutputStream fos = new FileOutputStream(file);)
            {
                int count;
                byte[] bytes = new byte[MAX_SIZE];
                long length = reply.getFileLength();
                for(; length > 0 ; length -= count)
                {
                    System.out.println("length : " + length);
                    System.out.println("Trying to read : " + ((MAX_SIZE > length) ? length : MAX_SIZE));

                    count = this.dis.read(bytes,0,(MAX_SIZE > length) ? (int) length : MAX_SIZE);
                    System.out.println("Received: " + count + " bytes");

                    fos.write(bytes,0,count);
                }
                System.out.println("exited loop");
            }
            catch (IOException ioe)
            {
                System.out.println("Connection error");
            }



            return new Music(reply.getName(), reply.getAuthor(), reply.getGenre(),
                    reply.getArtist(), "client_music/hello_world.mp3");
        }
        catch (IOException e)
        {
            System.out.println("IOException");
            throw new ConnectException();
        }
        catch (ProtocolParseError ppe)
        {
            System.out.println("ProtocolParseError");
            throw new ConnectException();
        }
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
