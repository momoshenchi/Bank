import java.security.SecureRandom;
import java.util.*;

/**
 * @version : 1.0
 * @author: momoshenchi
 * @date: 2020/11/17 - 16:39
 */
public class Bank
{
    public static List<Customer> waitList ;  //等待队列,每天清空
    public static List<Customer> myList[] = new LinkedList[100];//保留每天的数据
    public static boolean isDoorOpen = false;
    public static boolean isDayEnd = false;
    public static Date today;
    public static Date eight;
    public static  int cnt;
    public static void main(String[] args)
    {
        //设置运行的天数
        while (cnt<10)
        {

            Producer p = new Producer();
            Thread thread = new Thread(p);
            WindowA A = new WindowA();
            WindowB B1 = new WindowB();
            WindowB B2 = new WindowB();
            WindowV V = new WindowV();
            reset();
            thread.start();
            try
            {
                Thread.sleep(100);
                A.start();
                B1.start();
                B2.start();
                V.start();
                Thread.sleep(1080);
                isDoorOpen = false;
                Thread.sleep(20);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            System.out.println("第" + (cnt+1) + "天");
            System.out.println(A.finishList);
            System.out.println(B1.finishList);
            System.out.println(B2.finishList);
            System.out.println(V.finishList);
            myList[cnt].addAll(A.finishList);
            myList[cnt].addAll(B1.finishList);
            myList[cnt].addAll(B2.finishList);
            myList[cnt].addAll(V.finishList);
            System.out.println("服务用户数量 :"+myList[cnt].size());
            System.out.println("平均等待与处理时间 :" + String.format("%.2f",getBussinessProportion())+"分钟");
            printRadio();
            cnt++;
            isDayEnd=false;
        }
    }

    public static double getBussinessProportion()
    {
        int sum = 0;
        for (Customer customer : myList[cnt])
        {
            sum += (customer.getEndTime().getTime() - customer.getStartTime().getTime());
        }
        return sum * 0.5 / myList[cnt].size();
    }

    public static void printRadio()
    {
        int []arr=new int[8];
        int count=0;
        for (Customer customer : myList[cnt])
        {
            switch (customer.getBusinessEnum())
            {
                case DEPOSIT:arr[0]++;count++;break;
                case WITHDRAWAL:arr[1]++;count++;break;
                case FINE:arr[2]++;count++;break;
                case E_BANK:arr[3]++;count++;break;
                case UTILIES:arr[4]++;count++;break;
                case FUND:arr[5]++;count++;break;
                case TRANSFER:arr[6]++;count++;break;
                case LOAN:arr[7]++;count++;break;
            }
        }
        BusinessEnum[] values = BusinessEnum.values();
        int i=0;
        for (BusinessEnum value : values)
        {
            System.out.println(value.chinese+"所占比例 :"+String.format("%.2f",arr[i]*100.0/count)+"%");
            i++;
        }

    }
    public static  void  reset()
    {
        waitList=new LinkedList<>();
        myList[cnt]=new LinkedList<>();
        isDoorOpen = true;
        isDayEnd=true;
        eight = getToday(cnt);
        today = new Date();

    }
    public static Date getToday(int cnt)
    {
        Calendar c = Calendar.getInstance();
        c.set(2020, 3, 1 + cnt,8,0,0);

        return c.getTime();
    }

}
