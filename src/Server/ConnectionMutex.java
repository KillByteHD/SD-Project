package Server;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionMutex
{
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private DataInputStream dis;
    private DataOutputStream dos;

    private Lock lock;
    /*private Condition condition;*/

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
            /*this.condition = this.lock.newCondition();*/
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
        // Closing socket also closes Input/Output Stream
        this.socket.close();
    }


    public /*synchronized*/ void println(String str)
    {
        this.lock.lock();
        this.pw.println(str);
        this.pw.flush();
        this.lock.unlock();
    }


    public String readln() throws IOException
    {
        /*while(true)
        {
            try
            {
                this.condition.await();
                break;
            }
            catch (InterruptedException ignored)
            { }
        }*/

        //this.read_lock.lock();
        //DEGBUG//System.out.println("ENTROU NO READLN");
        String tmp = this.br.readLine();
        //this.read_lock.unlock();
        return tmp;
    }

    public void write(byte[] bytes, int len) throws IOException
    {
        this.dos.write(bytes,0,len);
    }

    public int read(byte[] bytes, int len) throws IOException
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
