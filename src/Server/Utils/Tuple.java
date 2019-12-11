package Server.Utils;

import java.util.Objects;

public class Tuple<A,B>
{
    private A fst;
    private B snd;


    public Tuple()
    {
        this.fst = null;
        this.snd = null;
    }

    public Tuple(A fst , B snd)
    {
        this.fst = fst;
        this.snd = snd;
    }

    public A getFst()
    {
        return this.fst;
    }
    public B getSnd()
    {
        return this.snd;
    }

    public void setFst(A fst)
    {
        this.fst = fst;
    }
    public void setSnd(B snd)
    {
        this.snd = snd;
    }


    @Override
    public String toString()
    {
        return "Tuple (" + fst + "," + snd + ')';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(fst, tuple.fst) &&
                Objects.equals(snd, tuple.snd);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(fst, snd);
    }
}
