package nostarve;

import java.util.Random;

public class BridgeManager
{
    static Bridge bridge = new Bridge();
    private static final int NORTH_CARS = 20;
    private static final int SOUTH_CARS = 40;
    static Thread northCars[];
    static Thread southCars[];
    public static String northCarType = "North car";
    public static String southCarType = "South car";

    public static void main(String[] args)
    {
        int min = 20;
        int max = 100;
        northCars = new Thread[NORTH_CARS];
        southCars = new Thread[SOUTH_CARS];
        Random random = new Random();

        for(int i = 0; i < NORTH_CARS; i++)
            northCars[i] = new Car(northCarType, bridge, i, (long) random.nextInt(max - min) + min, northCarType);
        for(int j = 0; j < SOUTH_CARS; j++)
            southCars[j] = new Car(southCarType, bridge, j + NORTH_CARS, (long) random.nextInt(max - min) + min, southCarType);

        for(int k = 0; k < NORTH_CARS; k++)
            northCars[k].start();
        for(int l = 0; l < SOUTH_CARS; l++)
            southCars[l].start();
    }
}
