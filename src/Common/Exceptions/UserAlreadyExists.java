package Common.Exceptions;

public class UserAlreadyExists extends AbstractException
{
    public UserAlreadyExists()
    {
        super(ExceptionCode.UserAlreadyExists);
    }
}
