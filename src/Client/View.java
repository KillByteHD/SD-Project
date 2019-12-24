package Client;

import java.util.Scanner;

public class View
{
    private Scanner sc;

    public View()
    {
        this.sc = new Scanner(System.in);
    }


    public void welcome()
    {
        System.out.println(" - Welcome - Enter 'help' to list all commands - ");
    }


    public String readLine()
    {
        System.out.print("> ");
        return this.sc.nextLine();
    }


    public void println(String str)
    {
        System.out.println(str);
    }


    public void error(String err)
    {
        System.out.println(" - Error : " + err + " -");
    }


    public String get_field(String field)
    {
        System.out.print(field + " > ");
        return this.sc.nextLine();
    }


    public void unknown_command()
    {
        System.out.println(" - Unknown Command - ");
    }

    public void invalid_arguments()
    {
        System.out.println(" - Invalid Arguments - ");
    }

    public void help_block(String help)
    {
        System.out.println(" ========== [ HELP ] ========== ");
        System.out.println(help);
        System.out.println(" ============================== ");
    }
}