import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class Client {
    private double money;
    private long serviceTime;
    private TypeOperation typeOperation;
}
