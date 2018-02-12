import java.util.Vector;

public class MessageQueue<E> implements Channel<E>
{
    private Vector<E> queue;
    public MessageQueue()
    {
        queue = new Vector<E>();
    }

    // This implements a nonblocking send
    public void send(E item)
    {
        queue.addElement(item);
    }

    // This implements a nonblocking receive
    public E receive()
    {
        if (queue.size() == 0)
            return null;
        else
            return queue.remove(0);
    }
}