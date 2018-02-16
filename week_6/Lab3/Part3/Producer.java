import java.util.Date;

public class Producer implements Runnable
{
    private Buffer<Date> buffer;
    private boolean keepWriting = true;

    public Producer(Buffer<Date> buffer)
    {
        this.buffer = buffer;
    }
    public void run()
    {
        Date message;
        while (keepWriting)
        {
            // nap for awhile
            SleepUtilities.nap(((BoundedBuffer<Date>)buffer).getProducerSleep());
            // produce an item & enter it into the buffer
            message = new Date();
            buffer.insert(message);
        }
    }

    public void setKeepWriting(boolean keepWriting)
    {
        this.keepWriting = keepWriting;
    }
}