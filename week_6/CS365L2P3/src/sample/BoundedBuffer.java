package sample;

import javafx.scene.control.TextArea;

import java.util.concurrent.Semaphore;

public class BoundedBuffer<E> implements Buffer<E>
{
    private static final int BUFFER_SIZE = 5;
    private E[] buffer;
    private int in, out;
    private Semaphore mutex;
    private Semaphore empty;
    private Semaphore full;
    private int producerSleep = 0;
    private int consumerSleep = 0;
    private TextArea textArea = null;

    public BoundedBuffer(TextArea textArea)
    {
        this.textArea = textArea;

        // buffer is initially empty
        in = 0;
        out = 0;
        mutex = new Semaphore(1);
        empty = new Semaphore(BUFFER_SIZE);
        full = new Semaphore(0);
        buffer = (E[]) new Object[BUFFER_SIZE];
    }

    // Producers call this method
    public void insert(E item)
    {
        try
        {
            empty.acquire();
            mutex.acquire();

            // add an item to the buffer
            buffer[in] = item;
            in = (in + 1) % BUFFER_SIZE;
            mutex.release();
            full.release();

            textArea.setText(textArea.getText() + "\n" + "Inserting into buffer\n   " +
                    "Number of items in buffer is: " + (in));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public E remove()
    {
        E item = null;
        try
        {
            full.acquire();
            mutex.acquire();
            // remove an item from the buffer
            item = buffer[out];
            out = (out + 1) % BUFFER_SIZE;
            mutex.release();
            empty.release();

            textArea.setText(textArea.getText() + "\n" + "Removing from buffer\n    " +
                    "Number of items in buffer is: " + (out));
        }
        catch (Exception e)
        {

        }
        return item;
    }

    public int getProducerSleep()
    {
        return producerSleep;
    }

    public void setProducerSleep(int producerSleep)
    {
        this.producerSleep = producerSleep;
    }

    public int getConsumerSleep()
    {
        return consumerSleep;
    }

    public void setConsumerSleep(int consumerSleep)
    {
        this.consumerSleep = consumerSleep;
    }
}
