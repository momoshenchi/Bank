/**
 * @version : 1.0
 * @author: momoshenchi
 * @date: 2020/11/17 - 16:41
 */
public enum BusinessEnum
{
    DEPOSIT(1,"存款",0.1,0.3),
    WITHDRAWAL(2,"取款",0.1,0.3),
    FINE(3,"罚款",0.3,0.4),
    E_BANK(4,"网银",1,2),
    UTILIES(5,"水电费",0.3,4),
    FUND(6,"基金",0.4,0.6),
    TRANSFER(7,"转账汇款",0.6,0.8),
    LOAN(8,"贷款",0.4,0.8);

    public final int itemNum;
    public final String chinese;
    public final double start;
    public final double end;

    private BusinessEnum(int itemNum, String chinese,double start,double end) {
        this.itemNum = itemNum;
        this.chinese = chinese;
        this.start=start;
        this.end=end;
    }
}
