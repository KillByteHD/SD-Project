package Common.Protocol;

import Common.Exceptions.ProtocolParseError;

public class C2DRequest
{
    public static class Login implements Request
    {
        private String username;
        private String password;

        public Login(String username, String password)
        {
            this.username = username;
            this.password = password;
        }

        public String getUsername()
        {
            return username;
        }

        public String getPassword()
        {
            return password;
        }

        public String write()
        {
            return "login:" + username + ":" + password;
        }
    }

    public static Request parse(String str) throws ProtocolParseError
    {
        try
        {
            String[] args = str.split(":");

            switch (args[0])
            {
                case "login":
                    return new C2DRequest.Login(args[1],args[2]);
            }
        }
        catch (Exception e) { }
        throw new ProtocolParseError("Protocol Parse Error");
    }
}



