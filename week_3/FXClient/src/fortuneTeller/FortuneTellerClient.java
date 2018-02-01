package fortuneTeller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FortuneTellerClient extends Application
{

    private String currentDate = "";
    private TextArea outputArea = null;

    private String fortune = "";
    private String ip = null;
    private int port = 0;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

    private Button connectButton;
    private Button disconnectButton;
    private Button fortuneButton;
    private Button clearButton;
    private DatePicker datePicker;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("fortuneTellerLayout.fxml"));
        root.minWidth(800);
        Scene scene = new Scene(root, 1100, 600);

        TextField ipText = (TextField) scene.lookup("#ipText");
        TextField portText = (TextField) scene.lookup("#portText");
        datePicker = (DatePicker) scene.lookup("#datePicker");
        connectButton = (Button) scene.lookup("#connectButton");
        disconnectButton = (Button) scene.lookup("#disconnectButton");
        fortuneButton = (Button) scene.lookup("#fortuneButton");
        clearButton = (Button) scene.lookup("#clearButton");
        outputArea = (TextArea) scene.lookup("#messageOutput");

        disconnectButton.setDisable(true);
        fortuneButton.setDisable(true);

        disconnectButton.setOnAction((event) -> {
            if (socket.isClosed())
            {
                if (connect())
                {
                    disconnect();
                }
                else
                {
                    setOutput("Something went wrong and we couldn't disconnect");
                    setUpConnectButtons();
                }
            }
            else
            {
                disconnect();
            }
        });

        clearButton.setOnAction(event -> {
            if(outputArea != null)
                outputArea.setText("");
        });

        connectButton.setOnMouseClicked((event) ->
        {
            boolean goodToGo = true;
            String output = "";
            this.ip = ipText.getText();

            if (!this.ip.matches("([0-9]{2,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3})"))
            {
                output = "Not a valid IP address!!\n";
                goodToGo = false;
            }

            try
            {
                this.port = Integer.parseInt(portText.getText());
            } catch (NumberFormatException e)
            {
                e.printStackTrace();
            }

            if (this.port > 49151 || this.port < 1000)
            {
                output += "Not a valid port number, must be between 1000 and 49151\n";
                goodToGo = false;
            }

            if (goodToGo)
            {
                setOutput("Connecting...");
                connect();
                if(!socket.isClosed())
                    setOutput("Connected....");
            }
            else
            {
                setOutput("Could't connect...");
                setOutput(output);
            }
        });

        fortuneButton.setOnAction((event ->
        {
            if(socket.isClosed())
            {
                if (connect())
                {
                    callServer();
                }
                else
                {
                    setOutput("Something went wrong and we disconnected from the server");
                    setUpConnectButtons();
                }
            }
            else
            {
                callServer();
            }
        }));


        primaryStage.setTitle("Fortune Teller");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void disconnect()
    {
        String disc = callServer("00/00/0000");
        if (disc.equalsIgnoreCase("quit"))
        {
            connectButton.setDisable(false);
            disconnectButton.setDisable(true);
            fortuneButton.setDisable(true);
            setOutput("Disconnecting...");
        }
        else
        {
            setOutput("Can't disconnect");
        }
    }

    private void callServer()
    {
        //get the selected date
        LocalDate localDate = datePicker.getValue();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        setCurrentDate(localDate.format(dateTimeFormatter));

        this.fortune = callServer(getCurrentDate());
        setOutput("Your fortune is: " + this.fortune);
    }

    private void setUpConnectButtons()
    {
        connectButton.setDisable(false);
        fortuneButton.setDisable(true);
        disconnectButton.setDisable(true);
    }

    private void setUpDisconnectButtons()
    {
        connectButton.setDisable(true);
        fortuneButton.setDisable(false);
        disconnectButton.setDisable(false);
    }

    private boolean connect()
    {
        try
        {
            socket = new Socket(this.ip, this.port);
            setUpDisconnectButtons();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private void setCurrentDate(String date)
    {
        this.currentDate = date;
    }

    private String getCurrentDate()
    {
        return this.currentDate;
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    private void setOutput(String data)
    {
        outputArea.setText(outputArea.getText() + data + "\n");
    }

    private String callServer(String date)
    {
        String serverResponse = "";
        try
        {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(date); // send to server
            out.flush();

            serverResponse = in.readLine();
            if(serverResponse.equalsIgnoreCase("quit"))
                return "quit";

            out.close();
            in.close();
            read.close();
            socket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return serverResponse;
    }
}
