package Server;

import java.net.Socket;

public class Logger
{

    public Logger()
    {

    }

    public static void connected(String host)
    {
        System.out.println(host + " > Connected");
    }


    public static void disconnected(String host)
    {
        System.out.println(host + " > Closed Connection");
    }

    public static void received(String host, String content)
    {
        System.out.println(host + " > Received - " + content);
    }

    public static void sended(String content)
    {
        System.out.println("Server > Sended - " + content);
    }

    public static void started()
    {
        System.out.println("Server > Running . . .");
    }


}
