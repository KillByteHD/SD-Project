package Server;

import Common.Model.Data;
import Common.Model.Music;
import Common.Model.User;
import Common.Exceptions.InvalidLogin;
import Common.Model.Utils;

import java.io.File;
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
        catch (Exception e) //TODO: Mudar para as respetivas excecoes (exception apanha tudo - e nao queremos isso)
        { throw new InvalidLogin(); }

        if(u == null || !u.checkPassword(password))
            throw new InvalidLogin();

        return u.authID();
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
