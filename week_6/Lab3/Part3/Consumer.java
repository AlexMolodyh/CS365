import java.util.Date;

public class Consumer implements Runnable
{
    private Buffer<Date> buffer;
    private boolean keepReading = true;

    public Consumer(Buffer<Date> buffer)
    {
        this.buffer = buffer;
    }
    public void run()
    {
        Date message;
        while (keepReading)
        {
            // nap for awhile
            SleepUtilities.nap(((BoundedBuffer<Date>)buffer).getConsumerSleep());
            // consume an item from the buffer
            message = (Date)buffer.remove();
        }
    }

    public void setKeepReading(boolean keepReading)
    {
        this.keepReading = keepReading;
    }
}