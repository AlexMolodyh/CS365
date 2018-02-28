package nostarve;

import java.util.LinkedList;

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
                if (lastCarOnBridge == null)
                    return setCarToBridge(car);
                else if (car.equalsType(lastCarOnBridge))
                    return setCarToBridge(car);
                else
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
