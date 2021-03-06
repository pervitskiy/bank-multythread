import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Teller> tellers = new ArrayList<>();
    private int countTeller;
    private Cashbox cashbox;

    public Bank(int countTeller, double cash) {
        this.countTeller = countTeller;
        this.cashbox = Cashbox.getCashbox();
        this.cashbox.put(cash);
    }

    /**
     * Launch of all cashier flows and customer generation flow
     */
    public void start(){
        for (int i = 0; i < this.countTeller; i++) {
            Teller teller = new Teller(i+1);
            tellers.add(teller);
            new Thread(teller).start();
        }
        Thread generator = new SpawnClient(this);
        generator.start();
    }

    /**
     * Find a free cashier and add a customer to it
     *
     * @param client - new Client
     */
    public void findFreeTellerAndAddClient(Client client){
        Teller tr = tellers.get(0);
        int minClient = tellers.get(0).size();
        for (Teller teller : tellers) {
            if(teller.size() < minClient){
                minClient = teller.size();
                tr = teller;
            }
        }
        tr.addClient(client);
    }
}
