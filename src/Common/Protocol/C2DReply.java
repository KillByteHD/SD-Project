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
            return (this.status == null) ? "ok:"+this.auth : "err:"+status.ordinal();
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
                case "ok":
                    return new C2DReply.Login(args[1]);
            }
        }
        catch (Exception e) { }

        throw new ProtocolParseError("Protocol Parse Error");
    }
}
