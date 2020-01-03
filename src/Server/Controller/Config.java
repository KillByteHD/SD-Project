package Server.Controller;

import Server.ServerInit;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config
{
    // Server Constraints
    public static int SIZE_DOWN;
    public static int SIZE_UP;
    public static int SIZE_OTHER;
    public static int MAX_DOWN;
    public static int MAX_UP;
    public static int MAX_OTHER;
    public static int PORT;
    public static int MAX_SIZE;

    public static boolean loadConfig()
    {
        try (FileInputStream input = new FileInputStream(ServerInit.SERVER_PATH + "Server/config.properties"))
        {
            Properties properties = new Properties();
            // load config file
            properties.load(input);

            SIZE_DOWN = Integer.parseInt(properties.getProperty("size_down"));
            SIZE_UP = Integer.parseInt(properties.getProperty("size_up"));
            SIZE_OTHER = Integer.parseInt(properties.getProperty("size_other"));
            MAX_DOWN = Integer.parseInt(properties.getProperty("max_down"));
            MAX_UP = Integer.parseInt(properties.getProperty("max_up"));
            MAX_OTHER = Integer.parseInt(properties.getProperty("max_other"));
            PORT = Integer.parseInt(properties.getProperty("port"));
            MAX_SIZE = Integer.parseInt(properties.getProperty("max_size"));;
            return true;
        }
        catch (IOException | NumberFormatException ex)
        {
            // Assign default values
            SIZE_DOWN = 10;
            SIZE_UP = 10;
            SIZE_OTHER = 10;
            MAX_DOWN = 5;
            MAX_UP = 5;
            MAX_OTHER = 5;
            PORT = 1111;
            MAX_SIZE = 8192;
            return false;
        }
    }
}
