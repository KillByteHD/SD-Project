package Common.Protocol;

import Common.Exceptions.ExceptionCode;

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

}
