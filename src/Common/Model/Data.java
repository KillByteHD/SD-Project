package Common.Model;

import Common.Exceptions.*;

import java.net.ConnectException;
import java.util.List;

public interface Data
{
    String login(String username, String password) throws InvalidLogin, ConnectException;
    void logout(String auth) throws NotLoggedIn, ConnectException;
    void register(String username, String password) throws UserAlreadyExists, ConnectException;
    Music download(String auth, String id_music) throws Unauthorized, InvalidMusic, ConnectException;
    void upload(String auth, Music music, List<String> tags) throws Unauthorized, MusicAlreadyExists, ConnectException;
    List<Music> search(String auth, String tag) throws Unauthorized, NothingFound, ConnectException;
}
