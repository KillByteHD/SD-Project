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

// TODO: Consider adding ClassCastException
public class ClientData implements Data
{
    // Constraints
    private final int CONNECT_DELAY = 3;
    private final int MAX_SIZE = 8*1024;
    private final String SERVER_ADDRESS = "localhost";

    // Variables
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private DataInputStream dis;

    public ClientData()
    {
        while(true)
        {
            try
            {
                this.socket = new Socket(SERVER_ADDRESS,1111);
                this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                this.pw = new PrintWriter(this.socket.getOutputStream(),true);
                this.dis = new DataInputStream(this.socket.getInputStream());
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
    public void logout(String auth) throws NotLoggedIn, ConnectException
    {
        try
        {
            Request request = new C2DRequest.Logout(auth);
            this.pw.println(request.write());

            String in = this.br.readLine();
            if(in == null)
                throw new ConnectException();
            C2DReply.Logout reply = (C2DReply.Logout) C2DReply.parse(in);

            if(reply.getStatus() == ExceptionCode.NotLoggedIn)
                throw new NotLoggedIn();
        }
        catch (IOException | ProtocolParseError e)
        {
            throw new ConnectException();
        }
    }

    @Override
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
    public Music download(String auth, String id_music) throws Unauthorized, InvalidMusic, ConnectException
    {
        try
        {
            //Ask to download a music with id id_music
            Request request = new C2DRequest.Download(auth,id_music);
            this.pw.println(request.write());

            String in = this.br.readLine();
            if(in == null)
                throw new ConnectException();

            // Receive meta data
            C2DReply.Download reply = (C2DReply.Download) C2DReply.parse(in);
            if(reply.getStatus() == ExceptionCode.InvalidMusic)
                throw new InvalidMusic();
            else if(reply.getStatus() == ExceptionCode.Unauthorized)
                throw new Unauthorized();

            // Receive file bytes
            final String file_path = "client_music/"+reply.getFileName();
            File file = new File(ClientInit.class.getResource("../").getPath() + file_path);
            // Create file if not exists
            //System.out.println("path : " + file.getPath());
            //System.out.println("File created : " + file.createNewFile());
            file.createNewFile();

            // Background Download
            new Thread(() ->
            {
                try(FileOutputStream fos = new FileOutputStream(file))
                {
                    int count;
                    byte[] bytes = new byte[MAX_SIZE];
                    long length = reply.getFileLength();
                    for(; length > 0 ; length -= count)
                    {
                        count = this.dis.read(bytes,0,(MAX_SIZE > length) ? (int) length : MAX_SIZE);
                        //System.out.println("Received: " + count + " bytes");
                        fos.write(bytes,0,count);
                    }
                    //DEBUG//System.out.println("exited loop");
                    System.out.print("[ Download Finished ]\n> ");
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }).start();

            return new Music(reply.getName(), reply.getAuthor(), reply.getGenre(),
                    reply.getArtist(), file_path);
        }
        catch (IOException | ProtocolParseError e)
        {
            throw new ConnectException();
        }
    }

    @Override
    public void upload(String auth, Music music) throws Unauthorized, MusicAlreadyExists, ConnectException
    {

        try
        {
            // This is just to get the file length
            File file = new File(ClientData.class.getResource("../").getPath() + "client_music/"+music.getFileName());

            //Send request to upload with meta data already included
            Request request = new C2DRequest.Upload(auth,music.getName(),
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
            else if(reply.getStatus() == ExceptionCode.Unauthorized)
                throw new Unauthorized();


            // Background Upload
            new Thread(() ->
            {
                try(Socket upload_s = new Socket(SERVER_ADDRESS,reply.getPort()))
                {
                    DataOutputStream upload_dos = new DataOutputStream(upload_s.getOutputStream());
                    try(FileInputStream fis = new FileInputStream(file))
                    {
                        byte[] bytes = new byte[MAX_SIZE];
                        int count;
                        while((count = fis.read(bytes)) > 0)
                        {
                            //DEBUG//System.out.println("Sended:" + count + " bytes");
                            upload_dos.write(bytes,0,count);
                            upload_dos.flush(); //Not necessary in this context but good practice
                        }
                        //DEBUG//System.out.println("exited loop");
                        System.out.print("[ Upload Finished ]\n> ");
                    }
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }).start();
        }
        catch (IOException | ProtocolParseError e)
        {
            throw new ConnectException();
        }
    }
}
