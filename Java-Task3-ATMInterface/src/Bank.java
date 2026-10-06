
import java.util.HashMap;

public class Bank {
    private final HashMap<String, Account> accounts = new HashMap<>();

    public void addAccount(Account account) {
        accounts.put(account.getUserId(), account);
    }

    public Account findAccount(String userId) {
        return accounts.get(userId);
    }

    public Account login(String userId, String pin) {
        Account account = accounts.get(userId);
        if(account != null && account.checkPin(pin)) {
            return account;
        }
        return null;
    }

    public boolean transfer(Account from, String toId, double amount) {
        Account to = accounts.get(toId);
        if(to == null || to == from) {
            return false;
        }
        if(!from.transferOut(amount, toId)) {
            return false;
        }
        to.transferIn(amount, from.getUserId());
        return true;
    }
}
