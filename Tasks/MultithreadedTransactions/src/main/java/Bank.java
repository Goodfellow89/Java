import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Random;

@AllArgsConstructor
public class Bank {
    @Getter
    @Setter
    private HashMap<String, Account> accounts;
    private final Random random = new Random();

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    public synchronized void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        if ((accounts.get(fromAccountNum).getMoney() >= amount) && !accounts.get(fromAccountNum).isBlocked() && !accounts.get(toAccountNum).isBlocked()) {
            accounts.get(fromAccountNum).setMoney(getBalance(fromAccountNum) - amount);
            accounts.get(toAccountNum).setMoney(getBalance(toAccountNum) + amount);
            if (amount > 50000 && isFraud(fromAccountNum, toAccountNum, amount)) {
                accounts.get(fromAccountNum).setBlocked(true);
                accounts.get(toAccountNum).setBlocked(true);
            }
        }
    }

    public long getBalance(String accountNum) {
        return accounts.get(accountNum).getMoney();
    }

    public long getBankBalance() {
        long balance = 0;
        for (Account account : accounts.values()) {
            balance += account.getMoney();
        }
        return balance;
    }
}