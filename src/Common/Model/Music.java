package Common.Model;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Music
{
    private String name;
    private String author;
    private Genre genre;
    private String artist;
    private String file_path;
    private int downloads;

    private Set<String> tags;


    public Music(String name, String author, Genre genre, String artist, String file_path)
    {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.artist = artist;
        this.file_path = file_path;
        this.downloads = 0;

        this.tags = new TreeSet<>();
    }

    public Music(String name, String author, Genre genre, String artist, String file_path, int downloads)
    {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.artist = artist;
        this.file_path = file_path;
        this.downloads = downloads;

        this.tags = new TreeSet<>();
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
    public String getFilePath()
    {
        return file_path;
    }
    public String getFileName()
    {
        String[] tmp = this.file_path.split("/");
        return tmp[tmp.length-1];
    }
    public int getDownloads()
    {
        return downloads;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return Objects.equals(name, music.name) &&
                Objects.equals(author, music.author) &&
                genre == music.genre &&
                Objects.equals(artist, music.artist) &&
                Objects.equals(file_path, music.file_path);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, author, genre, artist, file_path);
    }


    // name artist and are imutable so no need for synchronized or locks
    public String getID()
    {
        return Utils.sha256String(this.name + this.artist);
    }

    public synchronized void incrementDownloads()
    {
        ++this.downloads;
    }

    public String authorAndName()
    {
        return this.author + " - " + this.name;
    }

    public synchronized void addTag(String tag)
    {
        this.tags.add(tag);
    }

    public synchronized boolean hasTag(String tag)
    {
        return this.tags.contains(tag);
    }
}
