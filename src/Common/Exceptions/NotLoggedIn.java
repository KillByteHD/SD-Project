package Common.Exceptions;

public class NotLoggedIn extends AbstractException
{
    public NotLoggedIn()
    {
        super(ExceptionCode.NotLoggedIn);
    }
}
