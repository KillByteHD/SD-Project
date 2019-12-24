package Client;

import Common.Exceptions.InvalidLogin;
import Common.Exceptions.UserAlreadyExists;
import Common.Model.Data;

import java.lang.reflect.Method;
import java.net.ConnectException;
import java.util.Scanner;

public class CommandHandler
{
    private View view;
    private Data data;
    private String auth;

    public CommandHandler(View v, Data d)
    {
        this.view = v;
        this.data = d;
        this.auth = null;
    }


    public void handler()
    {
        Scanner s = new Scanner(System.in);
        this.view.welcome();

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
                    case "logout":
                        logout();
                        break;
                    case "register":
                        register(/*args*/);
                        break;
                    case "help":
                        help(args);
                        break;
                    case "exit":
                        exit();
                        break;
                    default:
                        this.view.unknown_command();
                        break;
                }
            }
            catch (ConnectException ce)
            {
                this.view.error("Unable to connect to server");
                this.data = new ClientData();
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
            this.auth = this.data.login(username,password);
            this.view.println(" - Login Successfull - ");
        }
        catch (InvalidLogin il)
        {
            this.view.error("Invalid Login / Please try again");
        }
    }

    @Command(name = "logout", description = "Logout Process")
    public void logout()
    {
        if(this.auth != null)
        {
            this.view.println(" - Logout Successful -");
            this.auth = null;
        }
        else
            this.view.println(" - Unable to Logout - ");
    }

    @Command(name = "register", description = "Register a new User"/*, args = {"username" , "password", "confirm_password"}*/)
    public void register(/*String[] args*/) throws ConnectException
    {
        String username = this.view.get_field("Username");
        String password = this.view.get_field("Password");
        String password_confirmation = this.view.get_field("Confirm Password");

        if(!password.equals(password_confirmation))
            this.view.error("Passwords do not match");
        else
        {
            try
            {
                this.data.register(username,password);
                this.view.println(" - User created - ");
            }
            catch (UserAlreadyExists userAlreadyExists)
            {
                this.view.error("User already exists");
            }
        }
    }


    @Command(name = "help", description = "Help Command", args = {"command"})
    public void help(String[] args)
    {
        String command = null;
        try { command = args[1]; }
        catch (Exception e) { }

        if(command == null)
        {
            StringBuilder help = new StringBuilder();
            int i = 0;
            for(Method m : CommandHandler.class.getMethods())
            {
                Command c = m.getAnnotation(Command.class);
                if(c != null)
                {
                    if(i != 0)
                        help.append('\n');
                    i++;
                    String[] cmd_agrs = c.args();

                    help.append(" ").append(c.name());
                    for(String argi : cmd_agrs)
                        help.append(" [").append(argi).append("]");
                    help.append(" - ").append(c.description());
                }
            }

            this.view.help_block(help.toString());
        }
        else
        {
            try
            {
                Method m = CommandHandler.class.getMethod(command);
                StringBuilder help = new StringBuilder();
                Command c = m.getAnnotation(Command.class);
                if(c != null)
                {
                    help.append(" Command Name: ").append(c.name()).append('\n');
                    help.append(" Description: ").append(c.description()).append('\n');
                    help.append(" Usage: \n");

                    String[] cmd_args = c.args();

                    help.append(" \t").append(c.name());
                    for(String argi : cmd_args)
                        help.append(" [").append(argi).append("]");

                    this.view.help_block(help.toString());
                }

            }
            catch (NoSuchMethodException e)
            { this.view.invalid_arguments(); }
        }
    }


    @Command(name = "exit", description = "Exits Program")
    public void exit()
    {
        System.exit(0);
    }

}
