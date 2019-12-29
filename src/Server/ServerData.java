package Server;

import Common.Exceptions.*;
import Common.Model.*;

import java.util.HashMap;
import java.util.Map;

public class ServerData implements Data
{
    private Map<String, User> users;
    private Map<String,Music> musics;
    private SessionCache session_cache;


    public ServerData()
    {
        this.users = new HashMap<>();
        this.musics = new HashMap<>();
        this.session_cache = new SessionCache();

        //Temporary Populate
        this.users.put("root",new User("root","root"));
        /*Music m1 = new Music("hello world","bytes", Genre.COMEDY,"computer","server_music/hello_world.mp3");
        Music m2 = new Music("Demons","joji", Genre.UNDEFINED,"joji","server_music/Demons.mp3");
        Music m3 = new Music("tmp","tmp", Genre.UNDEFINED,"tmp","server_music/tmp.txt");
        System.out.println("hello_world ID: " + m1.getID());
        System.out.println("Demons ID: " + m2.getID());
        System.out.println("tmp ID: " + m3.getID());
        this.musics.put(m1.getID(),m1);
        this.musics.put(m2.getID(),m2);
        this.musics.put(m3.getID(),m3);*/
    }


    @Override
    public synchronized String login(String username, String password) throws InvalidLogin
    {
        try
        {
            User u = this.users.get(username);

            if(u == null || !u.checkPassword(password))
                throw new InvalidLogin();

            // Generation of session authentication token
            String auth_token = Utils.sha256String(u.getID() + Utils.saltGenerator(8));
            session_cache.add(auth_token);
            return auth_token;
        }
        catch (NullPointerException | ClassCastException e)
        { throw new InvalidLogin(); /* Theoretically impossible */ }
    }

    @Override
    public synchronized void logout(String auth) throws NotLoggedIn
    {
        if(!this.session_cache.close_session(auth))
            throw new NotLoggedIn();
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
        catch(NullPointerException cce)
        { /* Theoretically impossible */ }
    }

    @Override
    public synchronized Music download(String auth, String id_music) throws InvalidMusic, Unauthorized
    {
        if(!this.session_cache.contains(auth))
            throw new Unauthorized();
        try
        {
            Music m = this.musics.get(id_music);

            if(m == null)
                throw new InvalidMusic();

            m.incrementDownloads();
            return m;
        }
        catch(NullPointerException | ClassCastException cce)
        { throw new InvalidMusic(); }
    }

    @Override
    public synchronized void upload(String auth, Music music) throws MusicAlreadyExists, Unauthorized
    {
        if(!this.session_cache.contains(auth))
            throw new Unauthorized();
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
