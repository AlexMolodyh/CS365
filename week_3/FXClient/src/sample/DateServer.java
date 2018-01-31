package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DateServer implements Runnable
{
    private int port = 0;
    private ServerListener serverListener;
    private String[] beforeDate = {"You will be rich"
                                    ,"You will not be rich"};
    private String[] afterDate = {"You will find a job soon"
                                    ,"You will never find a job"};

    public DateServer(int port, ServerListener serverListener)
    {
        this.port = port;
        this.serverListener = serverListener;
    }

    @Override
    public void run()
    {
        ServerSocket serverSocket = null;
        System.out.println("Starting Server.....");
        try
        {
            while (true)
            {
                serverSocket = new ServerSocket(1234);
                System.out.println("Server started....");

                Socket clientSocket = null;
                clientSocket = serverSocket.accept();
                System.out.println("Connected");

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String textFromClient = null;
                String textToClient = null;

                textFromClient = in.readLine(); // read the text from client

                System.out.println("Read '" + textFromClient + "'");

                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date startDate;

                try
                {
                    startDate = df.parse(textFromClient);
                    if(startDate.before(new java.util.Date()))
                        textToClient = beforeDate[new Random().nextInt(1)];
                    else
                        textToClient = afterDate[new Random().nextInt(1)];
                } catch (Exception e) {e.printStackTrace();}

                System.out.println("Writing '" + textToClient + "'");
                out.println(textToClient); // send the response to client
                out.flush();
                out.close();
                in.close();
                clientSocket.close();
                serverSocket.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}