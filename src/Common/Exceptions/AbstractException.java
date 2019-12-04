package Common.Exceptions;

public abstract class AbstractException extends Exception
{
    private ExceptionCode code;

    public AbstractException(ExceptionCode code)
    {
        this.code = code;
    }

    public ExceptionCode getCode()
    {
        return code;
    }
}
