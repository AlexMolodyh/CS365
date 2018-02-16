import java.util.Date;

class Producer implements Runnable
{
    private Channel<Date> queue;
    public Producer(Channel<Date> queue)
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

            // produce an item and enter it into the buffer
            message = new Date();
            System.out.println("Producer produced " + message);
            queue.send(message);
        }
    }
}