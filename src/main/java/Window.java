import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version : 1.0
 * @author: momoshenchi
 * @date: 2020/12/1 - 20:43
 */
public class Window implements Runnable
{
    private static final int RADIO = 10;


    public String description;
    public List<Integer> businessType;
    public boolean on;
    public BeanCustomer beanCustomer;
    public List<BeanCustomer> finishList;
    public boolean isVIP;

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
            int time = getSleepTime(beanCustomer.getBusinessEnum().start, beanCustomer.getBusinessEnum().end);
            Thread.sleep(time);
            on = false;
            beanCustomer.setEndTime(new Date());
            finishList.add(beanCustomer);
            beanCustomer = null;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }

    public Window(List<Integer> businessType, String description, boolean isVIP)
    {
        this.description = description;
        this.businessType = businessType;
        this.isVIP = isVIP;
        finishList = new ArrayList<>();
        on = false;
        beanCustomer = null;
    }

    @Override
    public void run()
    {
        while (Bank.isDoorOpen || !Bank.waitList.isEmpty())
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
        }
        System.out.println(finishList);
        Bank.myList[Bank.cnt].addAll(finishList);
        Bank.isDayEnd.add(Thread.currentThread().getName());
    }

    public void process()
    {
        if (isVIP)
        {
            for (int i = 0; i < Bank.waitList.size(); ++i)
            {
                BeanCustomer tem = Bank.waitList.get(i);
                if (tem.isVIP())
                {
                    beanCustomer = tem;
                    on = true;
                    Bank.waitList.remove(i);
                    break;
                }
            }
        }
        if (!on)
        {
            for (int i = 0; i < Bank.waitList.size(); ++i)
            {
                BeanCustomer tem = Bank.waitList.get(i);
                int item = tem.getBusinessEnum().itemNum;
                if (businessType.contains(item))
                {
                    beanCustomer = tem;
                    Bank.waitList.remove(i);
                    on = true;
                    break;
                }
            }
        }

    }

}
