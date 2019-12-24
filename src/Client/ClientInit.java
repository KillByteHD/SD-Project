package Client;

import Common.Model.Data;

public class ClientInit
{
    private static final int CONNECT_DELAY = 3;

    public static void main(String[] args)
    {
        View view = new View();
        Data data = new ClientData();
        CommandHandler cp = new CommandHandler(view,data);
        cp.handler();
    }
}