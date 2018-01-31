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

public class FortuneTellerClient extends Application {

    String currentDate = "";
    Thread serverThread = null;
    TextArea outputArea = null;

    private String fortune = "";
    private String ip = null;
    private int port = 0;
    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("fortuneTellerLayout.fxml"));
        root.minWidth(800);
        Scene scene = new Scene(root, 1100, 600);

        TextField ipText = (TextField) scene.lookup("#ipText");
        TextField portText = (TextField) scene.lookup("#portText");
        DatePicker datePicker = (DatePicker) scene.lookup("#datePicker");
        Button connectButton = (Button) scene.lookup("#connectButton");
        Button fortuneButton = (Button) scene.lookup("#fortuneButton");
        fortuneButton.setDisable(true);
        this.outputArea = (TextArea) scene.lookup("#messageOutput");


        connectButton.setOnMouseClicked((event) ->
        {
            boolean goodToGo = true;
            String output = "";
            outputArea.setText("Starting Server...\n");
            this.ip = ipText.getText();

            if(!this.ip.matches("([0-9]{2,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3})"))
            {
                output = "Not a valid IP address!!\n";
                goodToGo = false;
            }

            try
            {
                this.port = Integer.parseInt(portText.getText().toString());
            }catch (NumberFormatException e){e.printStackTrace();}

            if(this.port > 49151 || this.port < 1000)
            {
                output += "Not a valid port number, must be between 1000 and 49151\n";
                goodToGo = false;
            }

            if (goodToGo)
            {
                connectButton.setDisable(true);
                serverThread = new Thread(new FortuneTellerServer(port));
                serverThread.start();
                fortuneButton.setDisable(false);
            }
            else
            {
                setOutput(output);
            }
        });

        fortuneButton.setOnAction((event ->
        {
            //get the selected date
            LocalDate localDate = datePicker.getValue();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            setCurrentDate(localDate.format(dateTimeFormatter));

            if(!serverThread.isAlive())
            {
                serverThread = new Thread(new FortuneTellerServer(this.port));
                serverThread.start();
                if(!connectButton.isDisable())
                    connectButton.setDisable(true);
            }

            this.fortune = callServer(getCurrentDate());
            setOutput("Your fortune is: " + this.fortune);
        }));


        primaryStage.setTitle("Fortune Teller");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void setCurrentDate(String date) { this.currentDate = date; }
    private String getCurrentDate(){return this.currentDate; }

    public static void main(String[] args) {
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
            socket = new Socket(this.ip, this.port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(date); // send to server
            out.flush();

            serverResponse = in.readLine();

            out.close();
            in.close();
            read.close();
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return serverResponse;
    }
}
