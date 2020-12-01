import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * @version : 1.0
 * @author: momoshenchi
 * @date: 2020/11/25 - 16:24
 */
public class Test {
    public static void main(String[] args) {
       int []arr=new int[100];
        Random r=new Random();
        for (int i = 0; i < 100; i++)
        {
            arr[i]=r.nextInt();
        }
        System.out.println("Enter an index:");
        Scanner sc=new Scanner(System.in);
        int tem = sc.nextInt();
        try{
            System.out.println(arr[tem]);
        }
        catch (Exception e)
        {
            System.out.println("Index out of bound");
        }
    }
}