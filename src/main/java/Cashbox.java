import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Cashbox{

    private volatile double cash = 0;
    private static Cashbox cashbox;
    private static final Logger LOGGER = LogManager.getLogger(Cashbox.class);


    private Cashbox(){}

    public static synchronized Cashbox getCashbox(){
        if(cashbox == null){
            cashbox = new Cashbox();
        }
        return cashbox;
    }

    public synchronized void put(double sum) {
        LOGGER.info("Было в кассе денег: " + this.cash);
        this.cash += sum;
        LOGGER.info("После внесения денег в кассу: " + this.cash);
    }

    public synchronized boolean withdraw(double sum){
        LOGGER.info("Было в кассе денег: " + this.cash);
        if (this.cash-sum >= 0) {
            this.cash-=sum;
            LOGGER.info("После снятия денег: " + this.cash);
            return true;
        }
        return false;
    }
}
