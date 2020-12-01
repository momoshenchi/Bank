import java.util.ArrayList;
import java.util.Date;

/**
 * @version : 1.0
 * @author: momoshenchi
 * @date: 2020/11/18 - 11:10
 */
public class WindowB extends AbstractWindow
{

    public WindowB()
    {
        finishList = new ArrayList<>();
        on = false;
        customer = null;
    }

    static
    {

        description = "所有客户可以办理";
        businessType = new ArrayList<>();
        businessType.add(1);
        businessType.add(2);
        businessType.add(4);
        businessType.add(5);
        businessType.add(7);
    }


    @Override
    public void  process()
    {
        for (int i = 0; i < Bank.waitList.size(); ++i)
        {
            Customer tem = Bank.waitList.get(i);
            int item = tem.getBusinessEnum().itemNum;
            if (businessType.contains(item))
            {
                customer = tem;
                Bank.waitList.remove(i);
                on = true;
                break;
            }
        }
    }
}
