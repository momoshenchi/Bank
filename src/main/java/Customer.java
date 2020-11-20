import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version : 1.0
 * @author: momoshenchi
 * @date: 2020/11/17 - 16:38
 */
public class Customer implements Serializable, Comparable
{

    private String name;
    private boolean isVIP;
    private BusinessEnum businessEnum;
    public static final int RADIO = 30000;
    private Date startTime;
    private Date endTime;

    public Customer(String name, boolean isVIP, BusinessEnum businessEnum, Date time)
    {
        this.name = name;
        this.isVIP = isVIP;
        this.businessEnum = businessEnum;
        this.startTime = time;
    }

    public Customer()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public boolean isVIP()
    {
        return isVIP;
    }

    public void setVIP(boolean VIP)
    {
        isVIP = VIP;
    }

    public BusinessEnum getBusinessEnum()
    {
        return businessEnum;
    }

    public void setBusinessEnum(BusinessEnum businessEnum)
    {
        this.businessEnum = businessEnum;
    }

    public int compareTo(Object o)
    {
        if (o == null)
        {
            return -1;
        }
        Customer c = (Customer) o;
        if (c.isVIP == isVIP())
            return startTime.compareTo(c.startTime);
        else
        {
            return c.isVIP ? -1 : 1;
        }
    }

    @Override
    public String toString()
    {

        return
                "{客户名称='" + name + '\'' +
                        ", 业务类型=" + businessEnum.chinese +
                        ", 到达时间=" + getTime() +
                        ", 所用时间(分钟)=" + (endTime.getTime() - startTime.getTime()) / 2+"}";

    }

    private String getTime()
    {
        long strap = (startTime.getTime() - Bank.today.getTime()) * RADIO;
        Date n = new Date(Bank.eight.getTime() + strap);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(n);
        return time;
    }
}
