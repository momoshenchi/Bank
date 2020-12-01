import java.util.ArrayList;
import java.util.Date;

/**
 * @version : 1.0
 * @author: momoshenchi
 * @date: 2020/11/18 - 11:12
 */
public class WindowV extends AbstractWindow
{
    static
    {
        description = "VIP客户优先";
        businessType = new ArrayList<>();
        for (int i = 1; i <= 8; i++)
        {
            businessType.add(i);
        }
    }

    public WindowV()
    {
        finishList = new ArrayList<>();
        on = false;
        customer = null;
    }
    @Override
    public void  process()
    {
        for (int i = 0; i < Bank.waitList.size(); ++i)
        {
            Customer tem = Bank.waitList.get(i);
            if (tem.isVIP())
            {
                customer = tem;
                on = true;
                Bank.waitList.remove(i);
                break;
            }
        }
        if (!on)
        {
            customer = Bank.waitList.get(0);
            on = true;
            Bank.waitList.remove(0);
        }
    }
}
