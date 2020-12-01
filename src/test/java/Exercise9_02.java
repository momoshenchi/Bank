import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Exercise9_02 {

    public static void main(String[] args)throws Exception {

        try{
            System.out.println("statement 1");
            m1(4);//修改参数
            System.out.println("statment 3");
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println(e);
        }
        catch (RuntimeException e) {
            System.out.println(e);
        }
        System.out.println("statement 4");

    }
    static void m1(int i) throws RuntimeException,IndexOutOfBoundsException,FileNotFoundException{
        if(i==1)
            throw new RuntimeException("RuntimeException!");
        else if(i==2)
            throw new IndexOutOfBoundsException("IndexOutofBoundsException!");
        else if(i==3)
            ;
        else {
            throw new FileNotFoundException("FileNotFoundException!");
        }
    }

}
