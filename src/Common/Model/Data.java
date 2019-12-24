package Common.Model;

import Common.Exceptions.InvalidLogin;
import Common.Exceptions.UserAlreadyExists;

import java.io.File;
import java.net.ConnectException;

public interface Data
{
    String login(String username, String password) throws InvalidLogin, ConnectException;
    void register(String username, String password) throws UserAlreadyExists, ConnectException;
    File download() throws ConnectException;
    void upload(File music) throws ConnectException;
}
