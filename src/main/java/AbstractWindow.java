import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @version : 1.0
 * @author: momoshenchi
 * @date: 2020/11/18 - 11:03
 */
public abstract class AbstractWindow extends Thread
{
    public static String description;
    public static List<Integer> businessType;
    public boolean on;
    public Customer customer;
    public List<Customer> finishList;
    private static final int RADIO = 10;

    public int getSleepTime(double L, double R)
    {
        int start = (int) (L * RADIO);
        int end = (int) (R * RADIO);
        SecureRandom sr = new SecureRandom();
        return sr.nextInt(end - start + 1) + start;
    }

    public void sleep()
    {
        try
        {
            int time = getSleepTime(customer.getBusinessEnum().start, customer.getBusinessEnum().end);
            Thread.sleep(time);
            on = false;
            customer.setEndTime(new Date());
            finishList.add(customer);
            customer = null;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            synchronized (Bank.waitList)
            {
                if (!Bank.waitList.isEmpty())
                {
                    process();
                }
            }
            if (on)
            {
                sleep();
            }

            synchronized (Bank.isDayEnd)
            {
                if (Bank.waitList.isEmpty() && !Bank.isDoorOpen)
                {
                    Bank.isDayEnd.add(Thread.currentThread().getName());
                }
            }
        }
    }
    public void  process()
    {

    }
}
