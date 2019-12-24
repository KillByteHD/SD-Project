package Server;

import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger
{

    public Logger()
    {

    }

    private static String time()
    {
        return "[" + LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + "]";
    }

    private static String socket_to_ip(Socket s)
    {
        return s.getInetAddress().getHostAddress() + ":" + s.getPort();
    }


    public static void connected(Socket host)
    {
        System.out.println(time() + " " + socket_to_ip(host) + " > Connected");
    }

    public static void disconnected(Socket host)
    {
        System.out.println(time() + " " + socket_to_ip(host) + " > Closed Connection");
    }

    public static void received(Socket host, String content)
    {
        System.out.println(time() + " " + socket_to_ip(host) + " > Received - " + content);
    }

    public static void sended(Socket host, String content)
    {
        System.out.println(time() + " " + socket_to_ip(host) + " > Sended - " + content);
    }

    public static void started()
    {
        System.out.println(time() + " " + "Server > Running . . .");
    }


}
