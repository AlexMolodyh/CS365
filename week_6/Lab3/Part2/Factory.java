import java.util.Date;

public class Factory
{
    public static void main(String args[])
    {
        Buffer<Date> buffer = new BoundedBuffer<Date>();
        // Create the producer and consumer threads
        Thread producer = new Thread(new Producer(buffer));
        Thread consumer = new Thread(new Consumer(buffer));
        producer.start();
        consumer.start();
    }
}