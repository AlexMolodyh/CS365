public class DekkerTest
{
    public static void main(String[] args)
    {
        boolean flag[] = {false, false};
        int i = 0, j = 1;
        int turn = 0;
        int counter = 100;

        do
        {
            flag[i] = true;
            while (flag[j])
            {
                if (turn == j)
                {
                    flag[i] = false;
                    while (turn == j)
                        System.out.println("Process " + turn + " executing in do nothing....");
                    flag[i] = true;
                }
            }
            System.out.println("Process " + turn + " executing in Critical Section....");
            turn = j;
            flag[i] = false;
            System.out.println("Process " + turn + " executing in Remainder Section....");
            --counter;
        }
        while (counter > 0);
    }
}
