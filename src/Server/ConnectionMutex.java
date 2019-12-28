package Server;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionMutex
{
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private DataInputStream dis;
    private DataOutputStream dos;

    //private Lock read_lock;
    //private Lock write_lock;
    private Lock lock;

    public ConnectionMutex(Socket socket)
    {
        try
        {
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.pw = new PrintWriter(this.socket.getOutputStream());
            this.dis = new DataInputStream(this.socket.getInputStream());
            this.dos = new DataOutputStream(this.socket.getOutputStream());
            this.pw.flush();

            this.lock = new ReentrantLock();
            /*
            this.read_lock = new ReentrantLock();
            this.write_lock = new ReentrantLock();
            */
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
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


    public /*synchronized*/ void println(String str)
    {
        this.lock.lock();
        this.pw.println(str);
        this.pw.flush();
        this.lock.unlock();
    }

    //TODO: Quando o cliente for multithreaded acrescentar o syncronized aqui
    public /*synchronized*/ String readln() throws IOException
    {
        //this.read_lock.lock();
        String tmp = this.br.readLine();
        //this.read_lock.unlock();
        return tmp;
    }

    public /*synchronized*/ void write(byte[] bytes, int len) throws IOException
    {
        this.dos.write(bytes,0,len);
    }

    public /*synchronized*/ int read(byte[] bytes, int len) throws IOException
    {
        int tmp = this.dis.read(bytes,0,len);
        return tmp;
    }

    public void lock()
    {
        this.lock.lock();
    }

    public void unlock()
    {
        this.lock.unlock();
    }
}
