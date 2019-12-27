package Server;

import Common.Exceptions.InvalidMusic;
import Common.Exceptions.UserAlreadyExists;
import Common.Model.*;
import Common.Exceptions.InvalidLogin;

import java.io.File;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerData implements Data
{
    private Map<String, User> users;
    private Map<String,Music> musics;


    public ServerData()
    {
        this.users = new HashMap<>();
        this.musics = new HashMap<>();

        //Temporary Populate
        this.users.put("root",new User("root","root"));
        Music m = new Music("hello world","bytes", Genre.COMEDY,"computer","server_music/hello_world.mp3");
        System.out.println("hello_world ID: " + m.getID());
        this.musics.put(m.getID(),m);
    }


    @Override
    public synchronized String login(String username, String password) throws InvalidLogin
    {
        try
        {
            User u = this.users.get(username);

            if(u == null || !u.checkPassword(password))
                throw new InvalidLogin();

            return u.authID();
        }
        catch (NullPointerException | ClassCastException e)
        { throw new InvalidLogin(); }
    }

    public synchronized void register(String username, String password) throws UserAlreadyExists
    {
        try
        {
            User u = this.users.get(username);

            if(u == null)
                this.users.put(username,new User(username,password));
            else
                throw new UserAlreadyExists();
        }
        catch(NullPointerException | ClassCastException cce)
        { }
    }

    @Override
    public synchronized Music download(String id_music) throws InvalidMusic
    {
        try
        {
            Music m = this.musics.get(id_music);

            if(m == null)
                throw new InvalidMusic();

            return m;
        }
        catch(NullPointerException | ClassCastException cce)
        { throw new InvalidMusic(); }
    }

    @Override
    public synchronized void upload(String id_music)
    {

    }
}
