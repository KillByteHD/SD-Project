package Client;

import Common.Exceptions.InvalidLogin;
import Common.Model.Data;

public class ClientInit
{
    public static void main(String[] args)
    {
        Data data = new ClientData();

        try
        {
            data.login("root","root");
            System.out.println("Login Successfull");
        }
        catch (InvalidLogin e)
        {
            System.err.println(e.getCode());
        }
    }
}
