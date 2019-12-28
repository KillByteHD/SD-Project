package Common.Model;

import Common.Exceptions.InvalidLogin;
import Common.Exceptions.InvalidMusic;
import Common.Exceptions.MusicAlreadyExists;
import Common.Exceptions.UserAlreadyExists;

import java.net.ConnectException;

public interface Data
{
    String login(String username, String password) throws InvalidLogin, ConnectException;
    void register(String username, String password) throws UserAlreadyExists, ConnectException;
    Music download(String id_music) throws InvalidMusic, ConnectException;
    void upload(Music music) throws MusicAlreadyExists, ConnectException;
}
