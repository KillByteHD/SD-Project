package Common.Model;

import Common.Exceptions.InvalidLogin;

import java.io.File;
import java.net.ConnectException;

public interface Data
{
    String login(String username, String password) throws InvalidLogin, ConnectException;
    File download() throws ConnectException;
    void upload(File music) throws ConnectException;
}
