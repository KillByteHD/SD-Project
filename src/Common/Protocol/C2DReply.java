package Common.Protocol;

import Common.Exceptions.ExceptionCode;
import Common.Exceptions.ProtocolParseError;

public class C2DReply
{
    //TODO: FAKHSDFKASHDBF O NOME TA IGUAL AO REQUEST CARALHO ... AQUI NAO HA NAMESPACES HO MONGA DE MERDA
    public static class Login implements Reply
    {
        private ExceptionCode status;

        public Login()
        {
            this.status = null;
        }

        public Login(ExceptionCode code)
        {
            this.status = code;
        }

        public ExceptionCode getStatus()
        {
            return status;
        }

        public String write()
        {
            return (this.status == null) ? "ok" : "err:"+status.ordinal();
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
                    return new C2DReply.Login();
            }
        }
        catch (Exception e) { }

        throw new ProtocolParseError("Protocol Parse Error");
    }
}
