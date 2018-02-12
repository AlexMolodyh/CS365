import java.util.Date;

public class Producer implements Runnable
{
    private Buffer<Date> buffer;
    public Producer(Buffer<Date> buffer)
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
            // produce an item & enter it into the buffer
            message = new Date();
            buffer.insert(message);
        }
    }
}