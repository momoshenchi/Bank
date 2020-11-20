import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version : 1.0
 * @author: momoshenchi
 * @date: 2020/11/18 - 21:39
 */
public class Time
{
    public static void main(String[] args)
    {
        Date d = new Date();
        List<String> nameList;
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
        Date d2 = new Date();
        System.out.println(d2.getTime() - d.getTime());
        System.out.println(nameList.size());
    }
}
