import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

/**
 * @version : 1.0
 * @author: momoshenchi
 * @date: 2020/11/17 - 18:19
 */
public class Producer implements Runnable
{

    private final double VIPPOSSIBILITY = 0.05;
//    private final int BUSINESSNUM = 8;
    private final int GENERATESPEED = 10;
    private static List<String> nameList;
    private  final  int[] DAYA={20,20,10,10,5,15,10,10};
    private  final  int[] DAYB={10,10,5,5,5,40,5,20};
    static
    {
        nameList = new ArrayList<>();
        FileReader fr = null;
        BufferedReader br;
        try
        {
            fr = new FileReader("src/main/resources/name.txt");
            br = new BufferedReader(fr);
            String str;
            while ((str = br.readLine()) != null)
            {
                nameList.add(str);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fr.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void generateCustomer(SecureRandom sr)
    {
        Customer c = new Customer();
        double is = sr.nextDouble();
        if (is <= VIPPOSSIBILITY)
        {
            c.setVIP(true);

        }
        else
        {
            c.setVIP(false);
        }
        BusinessEnum[] enums = BusinessEnum.values();
        int tem=sr.nextInt(100);

        //在这里修改基准日.
        for (int i = 0; i < DAYA.length; i++)
        {
            if(tem<DAYA[i])
            {
                c.setBusinessEnum(enums[i]);
                break;
            }
            else
            {
                tem-=DAYA[i];
            }
        }
//        c.setBusinessEnum(enums[tem]);
        c.setStartTime(new Date());
        int tem2 = sr.nextInt( 380190) % nameList.size();
        c.setName(nameList.get(tem2));
        synchronized (Bank.waitList)
        {
            Bank.waitList.add(c);
        }
    }

    public void run()
    {
        SecureRandom sr=new SecureRandom();
        while (Bank.isDoorOpen)
        {
            generateCustomer(sr);
            try
            {
                int time=sr.nextInt(GENERATESPEED);
                Thread.sleep(time);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

}
