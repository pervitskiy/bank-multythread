import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class Client {
    /**
     * Amount of money
     */
    private double money;
    /**
     * Service Time
     */
    private long serviceTime;
    private TypeOperation typeOperation;
}
