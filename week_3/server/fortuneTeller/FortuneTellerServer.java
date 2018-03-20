package fortuneTeller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FortuneTellerServer implements Runnable
{
    private int port = 0;
    private long startTime = 0;
    private String[] beforeDate = {"You will be rich", "You will not be rich", "You will find someone soon"
    ,"Be kind, rewind", "This isn't an actual fortune teller...."};
    private String[] afterDate = {"You will find a job soon", "You will never find a job", "You will be successful after finishing school"
    ,"You need to try harder!", "He's around the corner!!!"};

    public FortuneTellerServer(int port)
    {
        this.port = port;
    }

    @Override
    public void run()
    {
        boolean serverRun = true;
        this.startTime = System.currentTimeMillis();
        try
        {
            while (serverRun)
            {
                ServerSocket serverSocket = new ServerSocket(this.port);
                Socket clientSocket = serverSocket.accept();

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String textFromClient = null;
                String textToClient = null;

                textFromClient = in.readLine(); // read the text from client

                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date startDate;

                try
                {
                    Date targetDate = df.parse("01/01/1990");

                    startDate = df.parse(textFromClient);
                    if(startDate.before(targetDate))
                        textToClient = beforeDate[new Random().nextInt(beforeDate.length - 1)];
                    else
                        textToClient = afterDate[new Random().nextInt(afterDate.length - 1)];
                } catch (Exception e) {e.printStackTrace();}

                out.println(textToClient); // send the response to client
                out.flush();
                out.close();
                in.close();
                clientSocket.close();
                serverSocket.close();

                if(System.currentTimeMillis() - this.startTime > 45000)
                    serverRun = false;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}