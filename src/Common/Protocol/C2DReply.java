package Common.Protocol;

import Common.Exceptions.ExceptionCode;
import Common.Exceptions.ProtocolParseError;
import Common.Model.Genre;

public class C2DReply
{

    public static class Login implements Reply
    {
        private ExceptionCode status;
        private String auth;

        public Login(String auth)
        {
            this.status = null;
            this.auth = auth;
        }

        public Login(ExceptionCode code)
        {
            this.status = code;
        }


        public ExceptionCode getStatus()
        {
            return this.status;
        }
        public String getAuth()
        {
            return this.auth;
        }

        public String write()
        {
            return (this.status == null) ? "logged:"+this.auth : "l_err:"+status.ordinal();
        }
    }

    public static class Register implements Reply
    {
        private ExceptionCode status;

        public Register()
        {
            this.status = null;
        }

        public Register(ExceptionCode code)
        {
            this.status = code;
        }

        public ExceptionCode getStatus()
        {
            return status;
        }

        @Override
        public String write()
        {
            return (this.status == null) ? "registered" : "r_err:"+status.ordinal();
        }
    }

    public static class Download implements Reply
    {
        private String name;
        private String author;
        private Genre genre;
        private String artist;
        public long file_length;

        private ExceptionCode status;

        public Download(String name, String author, Genre genre, String artist, long file_length)
        {
            this.name = name;
            this.author = author;
            this.genre = genre;
            this.artist = artist;
            this.file_length = file_length;
        }

        public Download(ExceptionCode code)
        {
            this.status = code;
        }


        public String getName()
        {
            return name;
        }
        public String getAuthor()
        {
            return author;
        }
        public Genre getGenre()
        {
            return genre;
        }
        public String getArtist()
        {
            return artist;
        }
        public long getFileLength()
        {
            return file_length;
        }
        public ExceptionCode getStatus()
        {
            return status;
        }


        //TODO: ISTO NAO TA BEM
        @Override
        public String write()
        {
            return (this.status == null) ? "music:"+this.name+
                    ":"+this.author+":"+this.genre.ordinal()+":"+this.artist+
                    ":"+this.file_length : "d_err:"+status.ordinal();
        }
    }


    public static Reply parse(String str) throws ProtocolParseError
    {
        try
        {
            String[] args = str.split(":");
            switch (args[0])
            {
                case "l_err":
                    return new C2DReply.Login(ExceptionCode.values()[Integer.parseInt(args[1])]);
                case "r_err":
                    return new C2DReply.Register(ExceptionCode.values()[Integer.parseInt(args[1])]);
                case "d_err":
                    return new C2DReply.Download(ExceptionCode.values()[Integer.parseInt(args[1])]);
                case "logged":
                    return new C2DReply.Login(args[1]);
                case "registered":
                    return new C2DReply.Register();
                case "music":
                    return new C2DReply.Download(args[1],args[2],Genre.values()[Integer.parseInt(args[3])],
                            args[4],Long.parseLong(args[5]));
            }
        }
        catch (Exception e) { }

        throw new ProtocolParseError("Protocol Parse Error");
    }
}
