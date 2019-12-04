package Common.Protocol;

import Common.Exceptions.ExceptionCode;
import Common.Exceptions.ProtocolParseError;

public interface Reply
{
    String write();

    static Reply parse(String str) throws ProtocolParseError
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
