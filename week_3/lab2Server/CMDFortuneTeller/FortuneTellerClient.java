import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class FortuneTellerClient
{
    private static String currentInput = "";
    private static Date currentDate;

    private static final String IP = "127.0.0.1";
    private static final int PORT = 1234;
    private static Scanner scan;

    public static void start()
    {
        scan = new Scanner(System.in);
        boolean keepGoing = true;
        System.out.println("Connecting to server...");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        while (keepGoing)
        {
            System.out.print("Please enter a currentDate in format MM/dd/yyyy: ");
            currentInput = scan.nextLine();

            try
            {
                currentDate = df.parse(currentInput);
            } catch (Exception e)
            {
                if (currentInput.equalsIgnoreCase("q"))
				{
                    keepGoing = false;
					currentInput = "00/00/0000";
				}
                else
                    System.out.println("Date doesn't match pattern.");
            }

            if(callServer(currentInput).equalsIgnoreCase("quit"))
                keepGoing = false;

        }
        System.out.println("Exiting... \nGoodbye...");
    }

    public static void main(String[] args)
    {
        start();
    }

    private static String callServer(String date)
    {
        String serverResponse = "";
        try
        {
            Socket socket = new Socket(IP, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(date); // send to server
            out.flush();

            serverResponse = in.readLine();
            if (serverResponse.equalsIgnoreCase("quit"))
                return "quit";

            System.out.println("Your fortune is: " + serverResponse);

            out.close();
            in.close();
            socket.close();
        } catch (IOException e)
        {
            System.out.println("Server is not started. Please start the server.");
        }

        return serverResponse;
    }
}
