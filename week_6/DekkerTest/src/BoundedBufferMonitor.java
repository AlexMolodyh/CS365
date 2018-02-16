public class BoundedBufferMonitor<E>
{
    private final int BUFFER_SIZE = 20;
    private E buffer[] = (E[]) new Object[BUFFER_SIZE];

    private BoundedBuffer producer;
    private BoundedBuffer consumer;
    private int in, out, count;

    public BoundedBufferMonitor()
    {
        in = 0;
        out = 0;
        count = 0;
    }

    public void insert(E e)
    {
        //if count is full, then wait
        if (count == BUFFER_SIZE)
            producer.wait();

        buffer[in] = e;
        ++count;

        in = (in + 1) % BUFFER_SIZE;
        consumer.signal();
    }

    public E remove(E e)
    {
        //if count has no items then wait
        if (count == 0)
            consumer.wait();

        e = buffer[out];
        --count;

        out = (out + 1) % BUFFER_SIZE;
        producer.signal();
    }
}