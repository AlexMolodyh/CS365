package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import javax.lang.model.util.ElementScanner6;
import java.awt.event.ActionEvent;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Main extends Application implements ServerListener {

    String currentDate = "";
    Thread serverThread = null;
    Thread clientThread = null;
    static Label outputLabel = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root, 1100, 600);

        TextField ipText = (TextField) scene.lookup("#ipText");
        TextField portText = (TextField) scene.lookup("#portText");
        DatePicker datePicker = (DatePicker) scene.lookup("#datePicker");
        Button connectButton = (Button) scene.lookup("#connectButton");
        outputLabel = (Label) scene.lookup("#messageOutput");


        connectButton.setOnMouseClicked((event) ->
        {
            boolean goodToGo = true;
            String output = "";
            int port = 0;
            outputLabel.setText("Starting Server...\n");

            if(!ipText.getText().matches("([0-9]{2,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3})"))
            {
                output = "Not a valid IP address!!\n";
                goodToGo = false;
            }

            try
            {
                port = Integer.parseInt(portText.getText().toString());
            }catch (NumberFormatException e){e.printStackTrace();}

            if(port > 49151 || port < 1000)
            {
                output += "Not a valid port number, must be between 1000 and 49151\n";
                goodToGo = false;
            }

            //get the selected date
            LocalDate localDate = datePicker.getValue();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            setCurrentDate(localDate.format(dateTimeFormatter));

            if (goodToGo)
            {
                serverThread = new Thread(new DateServer(port, this));
                serverThread.start();
                clientThread = new Thread(new DateClient(ipText.getText().toString(), port, this, getCurrentDate()));
                clientThread.start();
            }
            else
            {
                Label label = (Label) scene.lookup("#errorOutput");
                label.setText(output);
            }
        });


        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setCurrentDate(String date) { this.currentDate = date; }
    private String getCurrentDate(){return this.currentDate; }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void onDataPass(Object o)
    {
        String output = "";
        try
        {
            output = (String) o;
        } catch (Exception e) {e.printStackTrace();}
        setLabel(output);
    }

    private void setLabel(String data)
    {
        outputLabel.setText(outputLabel.getText() + "\n" + data);
    }
}
