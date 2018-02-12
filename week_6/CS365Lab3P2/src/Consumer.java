import java.util.Date;

public class Consumer implements Runnable
{
    private Buffer<Date> buffer;
    public Consumer(Buffer<Date> buffer)
    {
        this.buffer = buffer;
    }
    public void run()
    {
        Date message;
        while (true)
        {
            // nap for awhile
            SleepUtilities.nap();
            // consume an item from the buffer
            message = (Date)buffer.remove();
        }
    }
}