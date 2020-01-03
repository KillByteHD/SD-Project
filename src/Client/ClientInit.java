package Client;

import Client.Controller.CommandHandler;
import Client.Model.ClientData;
import Client.View.View;
import Common.Model.Data;

public class ClientInit
{
    public static final String CLIENT_PATH = ClientInit.class.getResource("../").getPath();

    public static void main(String[] args)
    {
        View view = new View();
        Data data = new ClientData();
        CommandHandler cp = new CommandHandler(view,data);
        cp.handler();
    }
}