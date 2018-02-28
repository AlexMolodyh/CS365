package nostarve;

public class Sleeper
{
    public static void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        } catch (Exception e) {}
    }
}
