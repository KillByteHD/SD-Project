package Common.Protocol;

import Common.Exceptions.ProtocolParseError;
import Common.Model.Genre;

public class C2DRequest
{
    public static class Login implements Request
    {
        private String username;
        private String password;

        public Login(String username, String password)
        {
            this.username = username;
            this.password = password;
        }

        public String getUsername()
        {
            return username;
        }
        public String getPassword()
        {
            return password;
        }

        @Override
        public String write()
        {
            return "login:" + username + ":" + password;
        }
    }

    public static class Register implements Request
    {
        private String username;
        private String password;

        public Register(String username, String password)
        {
            this.username = username;
            this.password = password;
        }


        public String getUsername()
        {
            return username;
        }
        public String getPassword()
        {
            return password;
        }

        @Override
        public String write()
        {
            return "register:" + username + ":" + password;
        }
    }

    public static class Download implements Request
    {
        private String id_music;

        public Download(String id_music)
        {
            this.id_music = id_music;
        }

        public String getIDmusic()
        {
            return id_music;
        }

        @Override
        public String write()
        {
            return "download:" + id_music;
        }
    }

    public static class Upload implements Request
    {
        private String name;
        private String author;
        private Genre genre;
        private String artist;
        private String file_name;
        private long file_length;

        public Upload(String name, String author, Genre genre, String artist, String file_name, long file_length)
        {
            this.name = name;
            this.author = author;
            this.genre = genre;
            this.artist = artist;
            this.file_name = file_name;
            this.file_length = file_length;
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

        @Override
        public String write()
        {
            return "upload:"+name+":"+author+":"+genre.ordinal()+":"+artist+":"+file_name+":"+file_length;
        }
    }

    public static Request parse(String str) throws ProtocolParseError
    {
        try
        {
            String[] args = str.split(":");
            switch (args[0])
            {
                case "login":
                    return new C2DRequest.Login(args[1],args[2]);
                case "register":
                    return new C2DRequest.Register(args[1],args[2]);
                case "download":
                    return new C2DRequest.Download(args[1]);
                case "upload":
                    return new C2DRequest.Upload(args[1],args[2],
                            Genre.values()[Integer.parseInt(args[3])],
                            args[4],args[5],Long.parseLong(args[6]));
            }
        }
        catch (Exception ignored) { }
        throw new ProtocolParseError("Protocol Parse Error");
    }
}



