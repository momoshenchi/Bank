import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * @version : 1.0
 * @author: momoshenchi
 * @date: 2020/11/17 - 16:39
 */
public class Bank
{
    public volatile static List<BeanCustomer> waitList;  //等待队列,每天清空
    public volatile static boolean isDoorOpen;   //关门后,生产者不再生产

    public volatile static Set<String> isDayEnd;    //是否每天业务全部结束
    public static Date today;
    public static Date eight;
    public static int cnt;                       //记录运行天数
    public static List<BeanCustomer>[] myList;    //保留每天的数据,用来进行分析
    private final static int SLEEPTIME = 1080;               //每天的睡眠时间,由实际运行9小时/30000倍
    private final static int DAYNUM = 10;                    //一个运行周期的天数
    private final int[] DAYA = {20, 20, 10, 10, 5, 15, 10, 10}; //基准日
    private final int[] DAYB = {10, 10, 5, 5, 5, 40, 5, 20};    //模拟日
    private BlockingQueue<Runnable> bqueue = new ArrayBlockingQueue<>(15);   //阻塞队列
    //线程池,设置10个核心线程,最大线程数15
    //10个线程用来模拟营业日的窗口数量搜索
    //一定要等当天所有任务处理完毕才能进入第二天
    private  ThreadPoolExecutor tpl = new ThreadPoolExecutor(10, 15, 20, TimeUnit.MILLISECONDS, bqueue);

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
            Window[]w= init(1, 2, 1);
            process(p, w ,4);
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

    private void process(Producer p,Window[] w,int num)
    {
        System.out.println("第" + (cnt + 1) + "天");
        Thread thread = new Thread(p);
        try
        {
            thread.start();
            Thread.sleep(200); //给定一定时间让顾客先来,使程序比较平滑


            for (Window window : w)
            {
                tpl.execute(window);
            }
            Thread.sleep(SLEEPTIME);
            isDoorOpen = false;
            while (isDayEnd.size()!=num)
            {

            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
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
        System.out.println("测试数量 :"+"A类窗口 : " + numA + "   B类窗口 : " + numB + "   V类窗口 : " + numV);
        myList = new LinkedList[100];
        cnt = 0;
        while (cnt < DAYNUM)
        {
            Window[]w= init(numA,numB,numV);
            process( p,w,numA+numB+numV);
            cnt++;
        }
        double average = printTotal();
        if (average < L)
        {
            return;
        }
        if ((average >= L && average <= R))
        {
            System.out.println("满足要求,加入队列");
            res.add(new int[]{numA, numB, numV});
            return;
        }
        System.out.println("不满足要求,进入下一轮循环---");
        backtrace(p, L, R, numA + 1, numB, numV, res);
        backtrace(p, L, R, numA, numB + 1, numV, res);
        backtrace(p, L, R, numA, numB, numV + 1, res);
        return;
    }


    private Window[] init(int numA, int numB, int numV)
    {
        waitList = new LinkedList<>();
        myList[cnt] = new LinkedList<>();
        isDoorOpen = true;
        isDayEnd = new HashSet<>();
        eight = getToday(cnt);
        today = new Date();
        List<Integer> a = new ArrayList<>();
        for (int i = 1; i <= 8; i++)
        {
            a.add(i);
        }
        List<Integer> b = new ArrayList<>();
        b.add(1);
        b.add(2);
        b.add(4);
        b.add(5);
        b.add(7);

        Window[] windows = new Window[numA + numB + numV];

        for (int i = 0; i < numA; i++)
        {
            windows[i] = new Window(a, "所有客户可以办理", false);
        }
        for (int i = numA; i < numA + numB; i++)
        {
            windows[i] = new Window(b, "所有客户可以办理", false);
        }
        for (int i = numA + numB; i < numA + numB + numV; i++)
        {
            windows[i] = new Window(a, "VIP客户优先", true);
        }
        return windows;
    }
    private double getAverageTime(List<BeanCustomer> list)
    {
        int sum = 0;
        for (BeanCustomer beanCustomer : list)
        {
            sum += (beanCustomer.getEndTime().getTime() - beanCustomer.getStartTime().getTime());
        }
        return sum * 0.5 / list.size();
    }

    private void getRate(List<BeanCustomer> list, int[] arr)
    {
        for (BeanCustomer beanCustomer : list)
        {
            switch (beanCustomer.getBusinessEnum())
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
        System.out.println("-------------");
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
            List<BeanCustomer> beanCustomers = myList[i];
            sum += beanCustomers.size();
            getRate(beanCustomers, arr);
            time += getAverageTime(beanCustomers);
        }
        int day = cnt - 1;
        double averageTime = time / day;
        System.out.println("-------------");
        System.out.println(day + "天共服务用户数量 :" + sum);
        System.out.println("平均等待与处理时间 :" + String.format("%.2f", averageTime) + "分钟");
        printRate(arr);
        return averageTime;
    }

}
