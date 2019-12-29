package Server;

import java.util.HashSet;
import java.util.Set;

public class SessionCache
{
    private Set<String> cache;

    public SessionCache()
    {
        this.cache = new HashSet<>();
    }

    public boolean contains(String auth)
    {
        return this.cache.contains(auth);
    }

    public synchronized void add(String auth)
    {
        this.cache.add(auth);
    }

    public synchronized boolean close_session(String auth)
    {
        return this.cache.remove(auth);
    }
}