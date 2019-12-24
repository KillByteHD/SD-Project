package Server;

import Common.Exceptions.UserAlreadyExists;
import Common.Model.Data;
import Common.Model.Music;
import Common.Model.User;
import Common.Exceptions.InvalidLogin;
import Common.Model.Utils;

import java.io.File;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerData implements Data
{
    private Map<String, User> users;
    private Set<Music> musics;


    public ServerData()
    {
        this.users = new HashMap<>();
        this.musics = new HashSet<>();

        this.users.put("root",new User("root","root"));
    }


    @Override
    public String login(String username, String password) throws InvalidLogin
    {
        User u;
        try
        { u = this.users.get(username); }
        catch (NullPointerException | ClassCastException e)
        { throw new InvalidLogin(); }

        if(u == null || !u.checkPassword(password))
            throw new InvalidLogin();

        return u.authID();
    }

    public void register(String username, String password) throws UserAlreadyExists
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
    public File download()
    {
        return null;
    }

    @Override
    public void upload(File music)
    {

    }
}
