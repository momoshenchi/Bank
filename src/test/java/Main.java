import java.util.Scanner;

/**
 * @version : 1.0
 * @author: momoshenchi
 * @date: 2020/11/25 - 8:57
 */
public  class  Main
{
    public static void main(String[] args)
    {
        Scanner sc=new Scanner(System.in);
        int a=sc.nextInt();
        int b=sc.nextInt();
        int c=sc.nextInt();
        BankAccount bankAccount=new BankAccount(a);
        System.out.println(bankAccount.getBalance());
        bankAccount.withdraw(b);
        System.out.println(bankAccount.getBalance());
         bankAccount.deposit(c);
        System.out.println(bankAccount.getBalance());

    }
}
 class BankAccount
{
    private int balance;

    public BankAccount(int balance)
    {
        this.balance = balance;
    }

    public BankAccount()
    {
        balance=0;
    }

    public int getBalance()
    {
        return balance;
    }
    public  void  withdraw(int amount)
    {
        balance-=amount;
    }
    public  void  deposit(int amount)
    {
        balance+=amount;
    }
}
