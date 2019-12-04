package Common.Protocol;

import Common.Exceptions.ProtocolParseError;

public class C2DRequest
{
    //TODO: FAKHSDFKASHDBF O NOME TA IGUAL AO REPLY CARALHO ... AQUI NAO HA NAMESPACES HO MONGA DE MERDA
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
}



