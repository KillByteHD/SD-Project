package Common.Model;

import Common.Exceptions.InvalidLogin;

import java.io.File;

public interface Data
{
    void login(String username, String password) throws InvalidLogin;
    File download();
    void upload(File music);
}
