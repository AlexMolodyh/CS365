import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FortuneTellerServer
{
    private static final int PORT = 1234;
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    private static String[] beforeDate = {
            "You will be rich"
            , "You will not be rich"
            , "You will find someone soon"
            , "Be kind, rewind"
            , "This isn't an actual fortune teller...."};

    private static String[] afterDate = {
            "You will find a job soon"
            , "You will never find a job"
            , "You will be successful after finishing school"
            , "You need to try harder!"
            , "He's around the corner!!!"};

    public static void main(String[] args)
    {
        boolean serverRun = true;
        long startTime = System.currentTimeMillis();
        System.out.println("Server started...");
        try
        {
            while (serverRun)
            {
                //Connect to client
                serverSocket = new ServerSocket(PORT);
                clientSocket = serverSocket.accept();

                //Create reader and writer
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String textFromClient = null;
                String textToClient = null;
                textFromClient = in.readLine(); // read the text from client

                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date startDate;

                try
                {
                    Date targetDate = df.parse("01/01/1990");

                    //Check if date from client is before or after target date
                    startDate = df.parse(textFromClient);

                    if (startDate.before(df.parse("00/01/0001")))
                    {
                        serverRun = false;
                        textToClient = "quit";
                    }
                    else
                    {
                        if (startDate.before(targetDate))
                            textToClient = beforeDate[new Random().nextInt(beforeDate.length - 1)];
                        else
                            textToClient = afterDate[new Random().nextInt(afterDate.length - 1)];
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                out.println(textToClient); // send the response to client
                closeObjects();

                if (System.currentTimeMillis() - startTime > 120000)
                    serverRun = false;
            }
        } catch (
                Exception e)

        {
            e.printStackTrace();
        }
		System.out.println("Server has stopped...");
    }

    private static boolean closeObjects()
    {
        try
        {
            out.flush();
            out.close();
            in.close();
            clientSocket.close();
            serverSocket.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}