package Common.Exceptions;

import Common.Model.Music;

public class MusicAlreadyExists extends AbstractException
{
    public MusicAlreadyExists()
    {
        super(ExceptionCode.MusicAlreadyExists);
    }
}
