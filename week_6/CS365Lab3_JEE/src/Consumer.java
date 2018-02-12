import java.util.Date;
class Consumer implements Runnable
{
    private Channel<Date> queue;
    public Consumer(Channel<Date> queue)
    {
        this.queue = queue;
    }
    public void run()
    {
        Date message;
        while (true)
        {
            // nap for awhile
            SleepUtilities.nap();
            // consume an item from the buffer
            message = queue.receive();
            if (message != null)
                System.out.println("Consumer consumed " + message);
        }
    }
}