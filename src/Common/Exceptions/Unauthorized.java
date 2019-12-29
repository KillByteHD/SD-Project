package Common.Exceptions;

public class Unauthorized extends AbstractException
{
    public Unauthorized()
    {
        super(ExceptionCode.Unauthorized);
    }
}
