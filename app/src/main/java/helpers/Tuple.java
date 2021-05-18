package helpers;

public class Tuple<E, F>
{
    private E e;
    private F f;

    public E getE()
    {
         return e;
    }

    public F getF()
    {
        return f;
    }


    public Tuple(E e, F f)
    {
        this.e = e;
        this.f = f;
    }
}
