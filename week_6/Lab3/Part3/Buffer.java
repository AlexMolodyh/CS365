public interface Buffer<E>
{
    // Send a message to the channel
    public void insert(E item);
    // Receive a message from the channel
    public E remove();
}
