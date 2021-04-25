import java.util.Random;

public class SpawnClient extends Thread{

    private static final long SERVICE_TIME = 3500;
    /**
     * The average amount of money that a client has when it is created
     */
    private static final double AVERAGE_CASH_AMOUNT = 20000;
    /**
     * Number of clients generated per minute
     */
    private static final int CLIENTS_PER_MINUTE = 20;

    /**
     * The time interval between the creation of clients. Calculated based on the number of clients per minute.
     */
    private int generationDelay;
    /**
     * The bank to which the created clients will be directed
     */
    private Bank bank;
    private Random random;

    /**
     * Constructor with parameters
     *
     * @param bank the bank to which the created clients will be directed
     */
    public SpawnClient(Bank bank) {
        this.generationDelay = 60 / CLIENTS_PER_MINUTE;
        this.random = new Random();
        this.bank = bank;
    }

    /**
     * The method creates a new client at a specified time interval and redirects him to the queue to the bank cashier
     */
    @Override
    public void run() {
        double rand;
        while (true) {
            try {
                Thread.sleep(Math.abs((long) ((random.nextGaussian() * (generationDelay / 2) + generationDelay) * 1000)));
                long clientServiceTime = Math.abs(Math.round(random.nextGaussian() * (SERVICE_TIME / 2) + SERVICE_TIME));
                double clientCash = Math.abs(Math.round(random.nextGaussian() * (AVERAGE_CASH_AMOUNT / 2) + AVERAGE_CASH_AMOUNT));
                rand = Math.random();
                Client client;
                if (rand > 0.5){
                    client = new Client(clientCash, clientServiceTime, TypeOperation.PUT_MONEY);
                }
                else
                    client = new Client(clientCash, clientServiceTime,TypeOperation.WITHDRAW_MONEY);

                bank.findFreeTeller(client);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
