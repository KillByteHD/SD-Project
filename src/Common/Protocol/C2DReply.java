package Common.Protocol;

import Common.Exceptions.ExceptionCode;
import Common.Exceptions.ProtocolParseError;

public class C2DReply
{
    //TODO: FAKHSDFKASHDBF O NOME TA IGUAL AO REQUEST CARALHO ... AQUI NAO HA NAMESPACES HO MONGA DE MERDA
    public static class Login implements Reply
    {
        private ExceptionCode status;
        private String auth;

        public Login(String auth)
        {
            this.status = null;
            this.auth = auth;
        }

        public Login(ExceptionCode code)
        {
            this.status = code;
        }


        public ExceptionCode getStatus()
        {
            return this.status;
        }
        public String getAuth()
        {
            return this.auth;
        }

        public String write()
        {
            return (this.status == null) ? "logged:"+this.auth : "err:"+status.ordinal();
        }
    }

    public static class Register implements Reply
    {
        private ExceptionCode status;

        public Register()
        {
            this.status = null;
        }

        public Register(ExceptionCode code)
        {
            this.status = code;
        }


        @Override
        public String write()
        {
            return (this.status == null) ? "registered" : "err:"+status.ordinal();
        }
    }

    public static Reply parse(String str) throws ProtocolParseError
    {
        try
        {
            String[] args = str.split(":");
            switch (args[0])
            {
                case "err":
                    return new C2DReply.Login(ExceptionCode.values()[Integer.parseInt(args[1])]);
                case "logged":
                    return new C2DReply.Login(args[1]);
                case "registered":
                    return new C2DReply.Register();

            }
        }
        catch (Exception e) { }

        throw new ProtocolParseError("Protocol Parse Error");
    }
}
