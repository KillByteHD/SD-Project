package Common.Exceptions;

public class ServerError extends AbstractException
{
    public ServerError()
    {
        super(ExceptionCode.ServerError);
    }
}
