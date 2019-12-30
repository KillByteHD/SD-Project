package Common.Protocol;

import Common.Exceptions.ExceptionCode;
import Common.Exceptions.ProtocolParseError;
import Common.Model.Genre;
import Common.Model.Music;

import java.util.ArrayList;
import java.util.List;

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
            return (this.status == null) ? "logged:"+this.auth : "li_err:"+status.ordinal();
        }
    }

    public static class Logout implements Reply
    {
        private ExceptionCode status;

        public Logout()
        {
            this.status = null;
        }

        public Logout(ExceptionCode status)
        {
            this.status = status;
        }

        public ExceptionCode getStatus()
        {
            return status;
        }

        @Override
        public String write()
        {
            return (this.status == null) ? "logged_out" : "lo_err:"+status.ordinal();
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

    public static class Search implements Reply
    {
        private ExceptionCode status;
        private List<Music> result;

        public Search(List<Music> result)
        {
            this.result = result;
            this.status = null;
        }
        public Search(ExceptionCode code)
        {
            this.status = code;
        }

        public ExceptionCode getStatus()
        {
            return status;
        }
        public List<Music> getResult()
        {
            return result;
        }

        @Override
        public String write()
        {
            if(this.status == null)
            {
                StringBuilder sb = new StringBuilder("found:");
                final int length = this.result.size();

                for(int i = 0 ; i < length ; i++)
                {
                    Music m = this.result.get(i);
                    sb.append(m.getName()).append('|');
                    sb.append(m.getAuthor()).append('|');
                    sb.append(m.getGenre().ordinal()).append('|');
                    sb.append(m.getArtist()).append('|');
                    sb.append(m.getDownloads());
                    if(i < length-1)
                        sb.append(':');
                }

                return sb.toString();
            }
            else
                return "s_err:"+this.status.ordinal();
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
                case "li_err":
                    return new C2DReply.Login(ExceptionCode.values()[Integer.parseInt(args[1])]);
                case "lo_err":
                    return new C2DReply.Logout(ExceptionCode.values()[Integer.parseInt(args[1])]);
                case "r_err":
                    return new C2DReply.Register(ExceptionCode.values()[Integer.parseInt(args[1])]);
                case "d_err":
                    return new C2DReply.Download(ExceptionCode.values()[Integer.parseInt(args[1])]);
                case "u_err":
                    return new C2DReply.Upload(ExceptionCode.values()[Integer.parseInt(args[1])]);
                case "s_err":
                    return new C2DReply.Search(ExceptionCode.values()[Integer.parseInt(args[1])]);
                // Match a success
                case "logged":
                    return new C2DReply.Login(args[1]);
                case "logged_out":
                    return new C2DReply.Logout();
                case "registered":
                    return new C2DReply.Register();
                case "music":
                    return new C2DReply.Download(args[1],args[2],Genre.values()[Integer.parseInt(args[3])],
                            args[4],args[5],Long.parseLong(args[6]));
                case "uploaded":
                    return new C2DReply.Upload(Integer.parseInt(args[1]));
                case "found":
                    List<Music> result = new ArrayList<>(args.length-1);
                    for(int i = 1 ; i < args.length ; i++)
                    {
                        String[] str_music = args[i].split("\\|");
                        result.add(new Music(str_music[0],
                                str_music[1],
                                Genre.values()[Integer.parseInt(str_music[2])],
                                str_music[3],
                                "(hidden)",
                                Integer.parseInt(str_music[4])));
                    }
                    //path (hidden)
                    return new C2DReply.Search(result);
            }
        }
        catch (Exception e) { }

        throw new ProtocolParseError("Protocol Parse Error");
    }
}
