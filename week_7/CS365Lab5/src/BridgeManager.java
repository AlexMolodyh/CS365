import java.util.Random;

public class BridgeManager
{
    static Bridge bridge = new Bridge();
    static Thread northCars[];
    static Thread southCars[];

    public static void main(String[] args)
    {
        int min = 20;
        int max = 100;
        northCars = new Thread[10];
        southCars = new Thread[20];
        Random random = new Random();

        for(int i = 0; i < 10; i++)
            northCars[i] = new Thread(new Car(bridge, i, (long) random.nextInt(max - min) + min, "North car"));
        for(int i = 0; i < 20; i++)
            southCars[i] = new Thread(new Car(bridge, i + 10, (long) random.nextInt(max - min) + min, "South car"));

        for(int j = 0; j < 10; j++)
            northCars[j].start();
        for(int j = 0; j < 20; j++)
            southCars[j].start();

        System.out.println("Done......");
    }
}
