import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Teller implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(Teller.class);
    private int id;
    private BlockingQueue<Client> clients = new LinkedBlockingQueue<>();

    public Teller() {}

    public Teller(int id) {
        this.id = id;
    }

    private void putMoneyInCashBox(double sum, Client client){
        Cashbox.getCashbox().put(sum);
        LOGGER.info("Оператор положил в кассу: " + sum + " клиента " + client);
    }

    private void withDrawMoneyInCashBox(double sum, Client client){
        if (Cashbox.getCashbox().withdraw(sum)){
            LOGGER.info("Оператор взял из кассы: " + sum + " для клиента " + client);
        }
        else {
            LOGGER.warn("Оператору неудалось взять из кассы: " + sum + " для клиента " + client + " так как недостаточно средств в кассе");
        }
    }

    public void addClient(Client newClient){
        synchronized (clients) {
            clients.add(newClient);
            LOGGER.info("Клиент " + newClient + " добавился в очередь к оператору teller " + this.id);
            clients.notify();
        }
    }

    public int size(){
        return clients.size();
    }

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
