import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Teller implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(Teller.class);
    /**
     * The indexer teller
     */
    private int id;

    /**
     * The queue of clients to be served
     */
    private BlockingQueue<Client> clients = new LinkedBlockingQueue<>();

    public Teller() {}

    public Teller(int id) {
        this.id = id;
    }

    /**
     * Put money in the cashBox
     *
     * @param sum - the amount of money
     * @param client - the client who wants to perform the operation
     */
    private void putMoneyInCashBox(double sum, Client client){
        Cashbox.getCashbox().put(sum);
        LOGGER.info("Оператор положил в кассу: " + sum + " клиента " + client);
    }

    /** Withdraw money from the cashBox
     *
     * @param sum - the amount of money
     * @param client - the client who wants to perform the operation
     */
    private void withDrawMoneyInCashBox(double sum, Client client){
        if (Cashbox.getCashbox().withdraw(sum)){
            LOGGER.info("Оператор взял из кассы: " + sum + " для клиента " + client);
        }
        else {
            LOGGER.warn("Оператору неудалось взять из кассы: " + sum + " для клиента " + client + " так как недостаточно средств в кассе");
        }
    }

    /** Add client to the queue
     *
     * @param newClient - client to add to the queue
     */
    public void addClient(Client newClient){
        synchronized (clients) {
            clients.add(newClient);
            LOGGER.info("Клиент " + newClient + " добавился в очередь к оператору teller " + this.id);
            clients.notify(); //снимаем блокировку на поток, так как теперь у нас есть клиенты в очереди
        }
    }

    public int size(){
        return clients.size();
    }

    /**
     * The cashier's method of operation.
     * If the client queue is empty, then the thread is waiting for new clients.
     * If the queue of clients is not empty, then the first client is selected and the work occurs(the thread falls asleep)
     * and the execution of the type of client operation
     */
    @Override
    public void run() {
        Client client;
        while (true){
            try {
                synchronized (clients) {
                    if (clients.isEmpty()) {
                        clients.wait();
                    }
                }
                LOGGER.info("Клиентов в очереди: " + clients.size());
                client = clients.poll();
                Thread.sleep(client.getServiceTime());
                if (client.getTypeOperation() == TypeOperation.PUT_MONEY) {
                    this.putMoneyInCashBox(client.getMoney(), client);
                } else {
                    this.withDrawMoneyInCashBox(client.getMoney(), client);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
