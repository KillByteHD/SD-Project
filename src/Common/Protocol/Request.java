package Common.Protocol;

import Common.Exceptions.ProtocolParseError;

public interface Request
{
    String write();

    /*static Request parse(String str) throws ProtocolParseError
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
    }*/
}

