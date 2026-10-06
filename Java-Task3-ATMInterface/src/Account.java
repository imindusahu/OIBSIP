import java.util.ArrayList;

public class Account {
    private final String userId;
    private final String pin;
    private double balance;
    private final ArrayList<Transaction> history = new ArrayList<>();

    public Account (String userId, String pin, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
    }

    public boolean checkPin(String input) {
        return pin.equals(input);
    }

    public String getUserId() { return userId; }
    public double getBalance() { return balance; }
    public ArrayList<Transaction> getHistory() { return history; }

    public void deposit(double amount) {
        balance += amount;
        history.add(new Transaction("DEPOSIT", amount, balance));
    }

    public boolean withdraw(double amount) {
        if(amount > balance) {
            return false;
        }
        balance -= amount;
        history.add(new Transaction("WITHDRAW", amount, balance));
        return true;
    }

    public boolean transferOut(double amount, String toId) {
        if(amount > balance) {
            return false;
        }
        balance -= amount;
        history.add(new Transaction("TRANSFER TO " + toId, amount, balance));
        return true;
    }

    public void transferIn(double amount, String fromId) {
        balance += amount;
        history.add(new Transaction("TRANSFER FROM " + fromId, amount, balance));
    }
}
