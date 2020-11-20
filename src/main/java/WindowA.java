import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @version : 1.0
 * @author: momoshenchi
 * @date: 2020/11/18 - 11:06
 */
public class WindowA extends AbstractWindow
{
    //every day start
    static
    {
        description = "所有客户可以办理";
        businessType = new ArrayList<>();
        for (int i = 1; i <= 8; i++)
        {
            businessType.add(i);
        }
    }

    public WindowA()
    {
        finishList = new ArrayList<>();
        on=false;
        customer=null;
    }

    @Override
    public void run()
    {
        while (Bank.isDayEnd || !Bank.waitList.isEmpty())
        {
            while (!on)
            {
                synchronized (Bank.waitList)
                {
                    if (!Bank.waitList.isEmpty())
                    {
                        customer = Bank.waitList.get(0);
                        Bank.waitList.remove(0);
                        on = true;
                    }
                }
            }
            sleep();
        }
    }
}
