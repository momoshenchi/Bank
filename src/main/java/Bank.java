import java.util.*;

/**
 * @version : 1.0
 * @author: momoshenchi
 * @date: 2020/11/17 - 16:39
 */
public class Bank
{
    public volatile static List<Customer> waitList;  //等待队列,每天清空
    public volatile static boolean isDoorOpen;   //关门后,生产者不再生产

    public volatile static Set<String> isDayEnd;    //是否每天业务全部结束
    public static Date today;
    public static Date eight;
    private int cnt;                       //记录运行天数
    private List<Customer> myList[];    //保留每天的数据,用来进行分析
    private final static int DAYNUM = 10;
    private final int[] DAYA = {20, 20, 10, 10, 5, 15, 10, 10}; //基准日
    private final int[] DAYB = {10, 10, 5, 5, 5, 40, 5, 20};    //模拟日

    public void run()
    {
        myList = new LinkedList[100];
        cnt = 0;
        Producer p = new Producer(DAYA);
        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        //设置运行的天数
        while (cnt < DAYNUM)
        {
            process(p, 1, 2, 1);
            System.out.println(isDayEnd);
            System.out.println("服务用户数量 :" + myList[cnt].size());
            System.out.println("平均等待与处理时间 :" + String.format("%.2f", getAverageTime(myList[cnt])) + "分钟");
            int[] arr = new int[9];
            getRate(myList[cnt], arr);
            printRate(arr);
            cnt++;
        }
        printTotal();
    }

        private void process(Producer p, int numA, int numB, int numV)
        {
            Thread thread = new Thread(p);
            WindowA[] A = new WindowA[numA];
            WindowB[] B = new WindowB[numB];
            WindowV[] V = new WindowV[numV];
            for (int i = 0; i < numA; i++)
        {
            A[i] = new WindowA();
        }
        for (int i = 0; i < numB; i++)
        {
            B[i] = new WindowB();
        }
        for (int i = 0; i < numV; i++)
        {
            V[i] = new WindowV();
        }
        reset();
        try
        {
            Thread.sleep(5);
            thread.start();
            Thread.sleep(200);
            for (int i = 0; i < numA; i++)
            {
                A[i].start();
            }
            for (int i = 0; i < numB; i++)
            {
                B[i].start();
            }
            for (int i = 0; i < numV; i++)
            {
                V[i].start();
            }
            Thread.sleep(1080);
            isDoorOpen = false;
            while (isDayEnd.size() != numA + numB + numV)
            {

            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println("第" + (cnt + 1) + "天");
        for (int i = 0; i < numA; i++)
        {

            System.out.println(A[i].finishList);
            myList[cnt].addAll(A[i].finishList);
            A[i].interrupt();
        }
        for (int i = 0; i < numB; i++)
        {

            System.out.println(B[i].finishList);
            myList[cnt].addAll(B[i].finishList);
            B[i].interrupt();
        }
        for (int i = 0; i < numV; i++)
        {

            System.out.println(V[i].finishList);
            myList[cnt].addAll(V[i].finishList);
            V[i].interrupt();
        }
    }

    public void simulation(double L, double R)
    {

        Producer p = new Producer(DAYB);
        int numA = 1;
        int numB = 1;
        int numV = 1;
        myList = new LinkedList[100];
        cnt = 0;
        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        List<int[]> res = new ArrayList<>();
        backtrace(p, L, R, numA, numB, numV, res);
        for (int[] re : res)
        {
            System.out.println("A类窗口 : " + re[0] + "   B类窗口 : " + re[1] + "   V类窗口 : " + re[2]);
        }

    }

    private void backtrace(Producer p, double L, double R, int numA, int numB, int numV, List<int[]> res)
    {
        myList = new LinkedList[100];
        cnt = 0;
        while (cnt < DAYNUM)
        {
            process(p, numA, numB, numV);
            cnt++;
        }
        double average = printTotal();
        if (average < L)
        {
            return;
        }
        if ((average >= L && average <= R))
        {
            res.add(new int[]{numA, numB, numV});
            return;
        }
        backtrace(p, L, R, numA + 1, numB, numV, res);
        backtrace(p, L, R, numA, numB + 1, numV, res);
        backtrace(p, L, R, numA, numB, numV + 1, res);
        return;
    }

    private double getAverageTime(List<Customer> list)
    {
        int sum = 0;
        for (Customer customer : list)
        {
            sum += (customer.getEndTime().getTime() - customer.getStartTime().getTime());
        }
        return sum * 0.5 / list.size();
    }

    private void getRate(List<Customer> list, int[] arr)
    {
        for (Customer customer : list)
        {
            switch (customer.getBusinessEnum())
            {
                case DEPOSIT:
                    arr[0]++;
                    arr[8]++;
                    break;
                case WITHDRAWAL:
                    arr[1]++;
                    arr[8]++;
                    break;
                case FINE:
                    arr[2]++;
                    arr[8]++;
                    break;
                case E_BANK:
                    arr[3]++;
                    arr[8]++;
                    break;
                case UTILIES:
                    arr[4]++;
                    arr[8]++;
                    break;
                case FUND:
                    arr[5]++;
                    arr[8]++;
                    break;
                case TRANSFER:
                    arr[6]++;
                    arr[8]++;
                    break;
                case LOAN:
                    arr[7]++;
                    arr[8]++;
                    break;
            }
        }
    }

    private void printRate(int[] arr)
    {
        BusinessEnum[] values = BusinessEnum.values();
        int i = 0;
        for (BusinessEnum value : values)
        {
            System.out.println(value.chinese + "所占比例 :" + String.format("%.2f", arr[i] * 100.0 / arr[8]) + "%");
            i++;
        }
    }

    private void reset()
    {
        waitList = new LinkedList<>();
        myList[cnt] = new LinkedList<>();
        isDoorOpen = true;
        isDayEnd = new HashSet<>();
        eight = getToday(cnt);
        today = new Date();

    }

    public Date getToday(int cnt)
    {
        Calendar c = Calendar.getInstance();
        c.set(2020, 3, 1 + cnt, 8, 0, 0);
        return c.getTime();
    }

    private double printTotal()
    {
        int sum = 0;
        double time = 0;
        int[] arr = new int[9];
        for (int i = 1; i < 10; ++i)
        {
            List<Customer> customers = myList[i];
            sum += customers.size();
            getRate(customers, arr);
            time += getAverageTime(customers);
        }
        int day = cnt - 1;
        double averageTime = time / day;
        System.out.println("-------");
        System.out.println(day + "天共服务用户数量 :" + sum);
        System.out.println("平均等待与处理时间 :" + String.format("%.2f", averageTime) + "分钟");
        printRate(arr);
        return averageTime;
    }

}
