package Server.Controller;

import Common.Model.Music;
import Server.Utils.Tuple;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Notifier implements Runnable
{
    private List<Tuple<Socket, PrintWriter>> notifier_list;
    private BoundedBuffer<Music> buffer;

    public Notifier()
    {
        this.notifier_list = new ArrayList<>();
        this.buffer = new BoundedBuffer<>(5);
    }

    @Override
    public void run()
    {
        while(true)
        {
            Music new_music = this.buffer.get();

            for(Tuple<Socket, PrintWriter> m : this.notifier_list)
            {
                if(m.fst().isClosed())
                    this.notifier_list.remove(m);
                else
                {
                    PrintWriter tmp = m.snd();
                    tmp.println("New Music : " + new_music.authorAndName());
                    tmp.flush();
                }
            }
        }
    }

    public synchronized void add(Socket s) throws IOException
    {
        this.notifier_list.add(new Tuple<>(s,new PrintWriter(s.getOutputStream())));
    }

    public synchronized void put(Music m)
    {
        this.buffer.put(m);
    }
}
