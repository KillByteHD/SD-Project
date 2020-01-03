package Server.Controller;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer<T>
{
    private Object[] values;
    private int pos_write;
    private Lock lock;
    private Condition isEmpty;
    private Condition isFull;

    public BoundedBuffer(int size)
    {
        this.values =  new Object[size];
        this.pos_write = 0;
        this.lock = new ReentrantLock();
        this.isEmpty = lock.newCondition();
        this.isFull = lock.newCondition();
    }

    public void put(T v)
    {
        this.lock.lock();

        while(this.pos_write == this.values.length)
        {
            try
            {
                this.isFull.await();
            } catch (InterruptedException e) { }
        }

        this.values[this.pos_write++] = v;

        this.isEmpty.signal();

        this.lock.unlock();
    }

    public T get()
    {
        this.lock.lock();

        while(this.pos_write == 0)
        {
            try
            {
                this.isEmpty.await();
            } catch (InterruptedException e) { }
        }

        this.isFull.signal();

        @SuppressWarnings("unchecked")
        T tmp = (T) this.values[--this.pos_write];

        this.lock.unlock();

        return tmp;
    }

}
