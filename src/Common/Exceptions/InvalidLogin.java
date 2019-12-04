package Common.Exceptions;

public class InvalidLogin extends AbstractException
{
    public InvalidLogin()
    {
        super(ExceptionCode.InvalidLogin);
    }
}
