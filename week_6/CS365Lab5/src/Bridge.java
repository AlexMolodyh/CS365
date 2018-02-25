public class Bridge
{
    private boolean bridgeAvailable = true;

    public Bridge() {}

    public boolean requestBridge()
    {
        if(bridgeAvailable)
            return true;

        return false;
    }

    public void occupyBridge()
    {
        bridgeAvailable = false;
    }

    public void doneWithBridge()
    {
        bridgeAvailable = true;
    }

    public void driveThroughBridge(int number, String carType)
    {
         System.out.println(carType + " number " + number + " is driving through bridge...");
    }
}
