package sample;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class DateClient implements Runnable, ServerListener
{
    private String ip = null;
    private int port = 0;
    private ServerListener serverListener;
    private String date = "";
    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

    public DateClient(String ip, int port, ServerListener serverListener, String date)
    {
        this.ip = ip;
        this.port = port;
        this.serverListener = serverListener;
        this.date = date;
    }

    public void run()
    {
        System.out.println("Starting Client.....");
        callServer(this.date);
    }

    @Override
    public void onDataPass(Object o)
    {
        callServer((String) o);
    }

    private void callServer(String date)
    {
        this.date = date;
        try
        {
            socket = new Socket(this.ip, this.port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected");

            out.println(this.date); // send to server
            out.flush();

            String serverResponse = in.readLine();
            this.serverListener.onDataPass(serverResponse);

            out.close();
            in.close();
            read.close();
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
