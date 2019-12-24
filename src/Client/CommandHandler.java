package Client;

import Common.Exceptions.InvalidLogin;
import Common.Model.Data;

import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.CookiePolicy;
import java.util.Scanner;

public class CommandHandler
{
    private View view;
    private Data data;

    public CommandHandler(View v, Data d)
    {
        this.view = v;
        this.data = d;
    }


    public void handler()
    {
        Scanner s = new Scanner(System.in);

        String in;
        while(true)
        {
            in = this.view.readLine();
            String[] args = in.split(" ");

            try
            {
                switch (args[0])
                {
                    case "login":
                        login();
                        break;
                    case "help":
                        help();
                        break;
                    case "exit":
                        exit();
                        break;
                }
            }
            catch (ConnectException ce)
            {
                this.view.error("Unable to connect to server");
                System.out.println("NOT OK");
            }
        }
    }

    @Command(name = "login", description = "Login Process")
    public void login() throws ConnectException
    {
        String username = this.view.get_field("Username");
        String password = this.view.get_field("Password");

        try
        {
            this.data.login(username,password);
        }
        catch (InvalidLogin il)
        {
            this.view.error("Invalid Login / Please try again");
        }

        this.view.println(" - Login Successfull - ");
    }

    @Command(name = "help", description = "Help Command")
    public void help()
    {
        System.out.println(" ========== [ HELP ] ========== ");
        for(Method m : CommandHandler.class.getMethods())
        {
            Command c = m.getAnnotation(Command.class);
            if(c != null)
            {
                String[] args = c.args();

                if(args.length == 0)
                    System.out.println(" # " + c.name() + " - " + c.description());
                else
                {
                    String tmp = " # " + c.name();
                    for(String argi : args)
                        tmp += " [" + argi + "]";
                    System.out.println(tmp + " - " + c.description());
                }
            }
        }
        System.out.println(" ============================== ");
    }

    @Command(name = "exit", description = "Exits Program")
    public void exit()
    {
        System.exit(0);
    }

}
