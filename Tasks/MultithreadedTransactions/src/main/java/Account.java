import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Account {

    private long money;
    private String accNumber;
    private boolean isBlocked = false;

    public Account(String accNumber, long money) {
        this.money = money;
        this.accNumber = accNumber;
    }
}
