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
            //Ask to download a music with id id_music
            Request request = new C2DRequest.Download(id_music);
            this.pw.println(request.write());

            String in = this.br.readLine();
            //Debug//System.out.println((in != null) ? in : "(null)");
            if(in == null)
                throw new ConnectException();

            // Receive meta data
            C2DReply.Download reply = (C2DReply.Download) C2DReply.parse(in);
            if(reply.getStatus() == ExceptionCode.InvalidMusic)
                throw new InvalidMusic();

            // Receive file bytes
            final String file_path = "client_music/"+reply.getFileName();
            File file = new File(ClientInit.class.getResource("../").getPath() + file_path);
            // Create file if not exists
            System.out.println("path : " + file.getPath());
            System.out.println("FIle created : " + file.createNewFile());


            try(FileOutputStream fos = new FileOutputStream(file))
            {
                int count;
                byte[] bytes = new byte[MAX_SIZE];
                long length = reply.getFileLength();
                for(; length > 0 ; length -= count)
                {
                    System.out.println("length : " + length);
                    System.out.println("Trying to write : " + ((MAX_SIZE > length) ? length : MAX_SIZE));

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
                    reply.getArtist(), file_path);
        }
        catch (IOException | ProtocolParseError e)
        {
            throw new ConnectException();
        }
    }

    @Override
    public void upload(Music music) throws MusicAlreadyExists, ConnectException
    {

        try
        {
            // This is just to get the file length
            File file = new File(ClientData.class.getResource("../").getPath() + "client_music/"+music.getFileName());

            System.out.println(file.getPath());

            //Send request to upload with meta data already included
            Request request = new C2DRequest.Upload(music.getName(),
                    music.getAuthor(),music.getGenre(),music.getArtist(),
                    music.getFileName(),file.length());
            this.pw.println(request.write());

            String in = this.br.readLine();
            if(in == null)
                throw new ConnectException();

            // Receive confirmation to upload
            C2DReply.Upload reply = (C2DReply.Upload) C2DReply.parse(in);
            if(reply.getStatus() == ExceptionCode.MusicAlreadyExists)
                throw new MusicAlreadyExists();


            try(FileInputStream fis = new FileInputStream(file))
            {
                byte[] bytes = new byte[MAX_SIZE];
                int count;
                while((count = fis.read(bytes)) > 0)
                {
                    System.out.println("Sended:" + count + " bytes");
                    this.dos.write(bytes,0,count);
                }
                System.out.println("exited loop");
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        catch (IOException | ProtocolParseError e)
        {
            throw new ConnectException();
        }
    }
}
