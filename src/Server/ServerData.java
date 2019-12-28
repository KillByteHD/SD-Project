package Server;

import Common.Exceptions.InvalidMusic;
import Common.Exceptions.MusicAlreadyExists;
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
        Music m1 = new Music("hello world","bytes", Genre.COMEDY,"computer","server_music/hello_world.mp3");
        Music m2 = new Music("Demons","joji", Genre.UNDEFINED,"joji","server_music/Demons.mp3");
        Music m3 = new Music("tmp","tmp", Genre.UNDEFINED,"tmp","server_music/tmp.txt");
        System.out.println("hello_world ID: " + m1.getID());
        System.out.println("Demons ID: " + m2.getID());
        System.out.println("tmp ID: " + m3.getID());
        this.musics.put(m1.getID(),m1);
        this.musics.put(m2.getID(),m2);
        this.musics.put(m3.getID(),m3);
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
    public synchronized void upload(Music music) throws MusicAlreadyExists
    {
        try
        {
            Music tmp = this.musics.get(music.getID());
            if(tmp != null)
                throw new MusicAlreadyExists();

            this.musics.put(music.getID(),music);
        }
        catch(NullPointerException | ClassCastException cce)
        { throw new MusicAlreadyExists(); }
    }
}
