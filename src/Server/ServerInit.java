package Server;

import Common.Model.Data;

import java.net.ServerSocket;

public class ServerInit
{
    public static void main(String[] args) throws Exception
    {
        ServerSocket ss = new ServerSocket(1111);
        Logger.started();
        Data data = new ServerData();

        while(true)
        {
            new Thread(new ServerThread(ss.accept(),data))
                    .start();
        }
    }
}
