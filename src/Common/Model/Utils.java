package Common.Model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;

public class Utils
{
    public static final String bytesToHexString(byte[] hash)
    {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++)
        {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static final String saltGenerator(int size)
    {
        if(size < 0 || size > 32)
            return "";

        Random r = new Random();
        //String alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuffer salt = new StringBuffer(size);
        for (int i = 0; i < size; i++)
            // random character between ascii [48,122] ['0','z']
            salt.append((char) (r.nextInt(75) + 48));

        return salt.toString();
    }


    public static final byte[] sha256(String str)
    {
        try
        {
            return MessageDigest.getInstance("SHA-256")
                    .digest(str.getBytes(StandardCharsets.UTF_8));
        }
        catch(Exception e) { return null; }
    }

    public static final String sha256String(String str)
    {
        return bytesToHexString(sha256(str));
    }
}