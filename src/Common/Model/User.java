package Common.Model;

import java.util.Objects;

public class User
{
    private String username;
    private String password_hash;
    private String salt;


    public User(String username, String password)
    {
        this.username = username;
        this.salt = Utils.saltGenerator(8);
        this.password_hash = Utils.sha256String(password + this.salt);
    }

    public String getUsername()
    {
        return username;
    }
    public String getPasswordHash()
    {
        return password_hash;
    }
    public String getSalt()
    {
        return salt;
    }


    public boolean checkPassword(String inPassword)
    {
        return Utils.sha256String(inPassword + salt).equals(this.password_hash);
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password_hash, user.password_hash) &&
                Objects.equals(salt, user.salt);
    }

    /*@Override
    public int hashCode()
    {
        return Objects.hash(username, password_hash, salt);
    }*/
}
