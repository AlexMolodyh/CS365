public class Car implements Runnable
{
    private Bridge bridge;
    private int carNumber = 0;
    private long driveTime = 0;
    private long timeWaited = 0;
    private String carType;

    public Car(Bridge bridge, int carNumber, long driveTime, String carType)
    {
        this.carType = carType;
        this.driveTime = driveTime;
        this.carNumber = carNumber;
        this.bridge = bridge;
    }

    @Override
    public void run()
    {
        timeWaited = System.currentTimeMillis();

        for(int i = 0; i < 6; i++)
        {
            //Drive to one side
            driveThroughBridge();

            Sleeper.sleep((long) Math.random() * 1000);

            //Now drive back
            driveThroughBridge();
        }
        //Prints out the total time this Car had to wait for the bridge (starvation)
        timeWaited = System.currentTimeMillis() - timeWaited;
        System.out.println(carType + " number " + carNumber + " waited: " + timeWaited / 1000 + " seconds");
    }

    private void driveThroughBridge()
    {
        //If the bridge is occupied, then we wait to use it
        while (!bridge.requestBridge())
            System.out.println(carType +" number: " + carNumber + " is waiting for the bridge");

        //Once the bridge is free, we can then use it if no one cuts in front of us
        bridge.occupyBridge();
        for (int i = 0; i < driveTime; i++)
        {
            bridge.driveThroughBridge(carNumber, carType);
        }
        bridge.doneWithBridge();
    }
}
