package Client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable
{
    private Socket socket;

    public ClientThread(Socket socket)
    {
        this.socket = socket;
    }

    public void run()
    {
        Scanner input = new Scanner(System.in);

        String str;
        System.out.print("> ");
        while((str = input.nextLine()) != null)
        {
            try
            {
                PrintWriter pw = new PrintWriter(this.socket.getOutputStream());
                pw.println(str);
                pw.flush();
                Scanner br = new Scanner(this.socket.getInputStream());
                if(br.hasNextLine())
                    System.out.println("Server : " + br.nextLine());
            } catch(Exception e) { e.printStackTrace(); }
            System.out.print("> ");
        }
    }
}
