package Server;

import java.io.*;
import java.net.Socket;

public class ConnectionMutex
{
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ConnectionMutex(Socket socket)
    {
        try
        {
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.pw = new PrintWriter(this.socket.getOutputStream());
            this.dis = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
            this.dos = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
            this.pw.flush();
        }
        catch(IOException e) { }
    }


    public Socket getSocket()
    {
        return socket;
    }

    public boolean isClosed()
    {
        return this.socket.isClosed();
    }

    public void close() throws IOException
    {
        this.socket.close();
    }


    public synchronized void println(String str)
    {
        this.pw.println(str);
        this.pw.flush();
    }

    //TODO: Quando o cliente for multithreaded acrescentar o syncronized aqui
    public /*synchronized*/ String readln() throws IOException
    {
        return this.br.readLine();
    }

    public void write(byte[] bytes, int len) throws IOException
    {
        this.dos.write(bytes,0,len);
    }

    public byte[] read()
    {
        //this.dis.read();
        return null;
    }
}
