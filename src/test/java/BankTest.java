import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @version : 1.0
 * @author: momoshenchi
 * @date: 2020/11/19 - 20:24
 */
public class BankTest
{

    @BeforeClass
    public static  void init()
    {


    }
    @Test
    public void  testRun()
    {
        Bank b=new Bank();
        b.run();
    }
    @Test
    public  void  testSimulation()
    {
        Bank b=new Bank();
        b.simulation(13.00,18.00);
    }

}