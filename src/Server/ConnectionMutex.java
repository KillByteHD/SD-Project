package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionMutex
{
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;

    public ConnectionMutex(Socket socket)
    {
        try
        {
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.pw = new PrintWriter(this.socket.getOutputStream(),true);
        }
        catch(IOException e) { }
    }

    public BufferedReader getBufferedReader()
    {
        return br;
    }

    public PrintWriter getPrintWriter()
    {
        return pw;
    }
}
