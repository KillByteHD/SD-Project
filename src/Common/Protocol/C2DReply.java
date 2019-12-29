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
        private String file_name;
        private long file_length;

        private ExceptionCode status;

        public Download(String name, String author, Genre genre, String artist, String file_name, long file_length)
        {
            this.name = name;
            this.author = author;
            this.genre = genre;
            this.artist = artist;
            this.file_name = file_name;
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
        public String getFileName()
        {
            return file_name;
        }
        public long getFileLength()
        {
            return file_length;
        }
        public ExceptionCode getStatus()
        {
            return status;
        }


        @Override
        public String write()
        {
            return (this.status == null) ? "music:"+this.name+
                    ":"+this.author+":"+this.genre.ordinal()+":"+this.artist+
                    ":"+this.file_name+":"+this.file_length : "d_err:"+this.status.ordinal();
        }
    }

    public static class Upload implements Reply
    {
        private ExceptionCode status;
        private int port;

        public Upload(int port)
        {
            this.status = null;
            this.port = port;
        }
        public Upload(ExceptionCode code)
        {
            this.status = code;
        }

        public ExceptionCode getStatus()
        {
            return status;
        }

        public int getPort()
        {
            return port;
        }

        @Override
        public String write()
        {
            return (this.status == null) ? "uploaded:" + this.port : "u_err:"+this.status.ordinal();
        }
    }


    public static Reply parse(String str) throws ProtocolParseError
    {
        try
        {
            String[] args = str.split(":");
            switch (args[0])
            {
                // Match an exception
                case "l_err":
                    return new C2DReply.Login(ExceptionCode.values()[Integer.parseInt(args[1])]);
                case "r_err":
                    return new C2DReply.Register(ExceptionCode.values()[Integer.parseInt(args[1])]);
                case "d_err":
                    return new C2DReply.Download(ExceptionCode.values()[Integer.parseInt(args[1])]);
                case "u_err":
                    return new C2DReply.Upload(ExceptionCode.values()[Integer.parseInt(args[1])]);
                // Match a success
                case "logged":
                    return new C2DReply.Login(args[1]);
                case "registered":
                    return new C2DReply.Register();
                case "music":
                    return new C2DReply.Download(args[1],args[2],Genre.values()[Integer.parseInt(args[3])],
                            args[4],args[5],Long.parseLong(args[6]));
                case "uploaded":
                    return new C2DReply.Upload(Integer.parseInt(args[1]));
            }
        }
        catch (Exception e) { }

        throw new ProtocolParseError("Protocol Parse Error");
    }
}
