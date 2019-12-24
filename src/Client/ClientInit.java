package Client;

import Common.Model.Data;

import java.net.ConnectException;

public class ClientInit
{
    private static final int CONNECT_DELAY = 3;

    public static void main(String[] args)
    {
        View view = new View();

        Data data = null;
        while(true)
        {
            try
            {
                data = new ClientData();
                break;
            }
            catch (ConnectException e)
            {
                System.out.println("Unable to connect to server ... Retrying in " + CONNECT_DELAY + " secs ...");
                try { Thread.sleep(CONNECT_DELAY * 1000); }
                catch (InterruptedException ignored) { }
            }
        }

        CommandHandler cp = new CommandHandler(view,data);
        cp.handler();
    }
}