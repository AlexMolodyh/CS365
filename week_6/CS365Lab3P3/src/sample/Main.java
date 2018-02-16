package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Date;


public class Main extends Application
{
    private Thread producer = null;
    private Thread consumer = null;
    private static Producer producerObj = null;
    private static Consumer consumerObj = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root, 700, 400);

        Button button = (Button) scene.lookup("#button");
        TextArea textArea = (TextArea) scene.lookup("#messageOutput");
        TextField pSleep = (TextField) scene.lookup("#pSleep");
        pSleep.setPromptText("Producer Sleep");
        TextField cSleep = (TextField) scene.lookup("#cSleep");
        cSleep.setPromptText("Consumer Sleep");

        button.setOnAction(e -> {
            textArea.setText("Starting Semaphore...");

            int pTime = Integer.parseInt(pSleep.getText().equalsIgnoreCase("") ? "10" : pSleep.getText());
            int cTime = Integer.parseInt(cSleep.getText().equalsIgnoreCase("") ? "30" : cSleep.getText());

            Buffer<Date> buffer = new BoundedBuffer<Date>(textArea);
            ((BoundedBuffer<Date>)buffer).setProducerSleep(pTime);
            ((BoundedBuffer<Date>)buffer).setConsumerSleep(cTime);

            producerObj = new Producer(buffer);
            consumerObj = new Consumer(buffer);

            // Create the producer and consumer threads
            producer = new Thread(producerObj);
            consumer = new Thread(consumerObj);
            producer.start();
            consumer.start();
        });

        primaryStage.setOnCloseRequest(e -> {
            textArea.setText(textArea.getText() + "\nClosing...");

            try
            {
                producerObj.setKeepWriting(false);
                consumerObj.setKeepReading(false);
                producer.interrupt();
                consumer.interrupt();
            } catch (Exception ex) {}
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
