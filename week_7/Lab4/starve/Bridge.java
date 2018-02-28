import java.util.LinkedList;

/*The Bridge only allows 3 cars at a time. If less than 3 cars are on the bridge and 
the same type of car requests the bridge then it will be allowed to drive on the bridge. 
If no cars are on the bridge then the first car in the waiting list will be woken up and allowed 
to drive on the bridge.*/
public class Bridge
{
    private final int MAX_CARS_ON_ROAD = 3;
    private int currCarVal = 0;

    private boolean bridgeAvailable = true;
    private Car lastCarOnBridge;
    private LinkedList<Car> cars;

    public Bridge()
    {
        cars = new LinkedList<>();
    }

    public boolean requestBridge(Car car)
    {
        if(car != null)
        {
            if (currCarVal < MAX_CARS_ON_ROAD)
            {
                if (lastCarOnBridge == null)//If there are no cars on bridge then let car on to bridge
                    return setCarToBridge(car);
                else if (car.equalsType(lastCarOnBridge))//if last car on bridge is the same as current car then let it onto the bridge
                    return setCarToBridge(car);
                else//Otherwise add the car to the waiting list
                    return addCarToList(car);
            }
            else
            {
                return addCarToList(car);
            }
        }
        return false;
    }

    private boolean addCarToList(Car car)
    {
        cars.offer(car);
        car.interrupt();
        return false;
    }

    private boolean setCarToBridge(Car car)
    {
        lastCarOnBridge = car;
		//If the maximum cars allowed has been reached, then add the car to the waiting list
        if (currCarVal >= MAX_CARS_ON_ROAD)
        {
            bridgeAvailable = false;
            cars.offer(car);
            car.interrupt();
            return false;
        }
        if(car.isInterrupted())
            car.run();
        currCarVal++;
        return true;
    }

    public void doneWithBridge()
    {
        if (currCarVal > 0)
        {
            if(currCarVal < MAX_CARS_ON_ROAD)
                bridgeAvailable = true;

            currCarVal--;
            while (currCarVal < MAX_CARS_ON_ROAD && bridgeAvailable)
                if(cars.peekFirst() != null)
                    requestBridge(cars.removeFirst());
        }
        else if (currCarVal <= 0)
        {
            currCarVal--;
            bridgeAvailable = true;
            lastCarOnBridge = null;
            while (currCarVal < MAX_CARS_ON_ROAD && bridgeAvailable)
                requestBridge(cars.removeFirst());
        }
    }

    public void driveThroughBridge(int number, String carType)
    {
        System.out.println(carType + " number " + number + " is driving through bridge...");
    }
}
