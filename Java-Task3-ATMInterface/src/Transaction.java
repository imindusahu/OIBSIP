import java.time.LocalDateTime;

public class Transaction {
    private final String type;
    private final double amount;
    private final double balanceAfter;
    private final LocalDateTime time;

    public Transaction(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.time = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return time.withNano(0) + " | " + type + " | Rs." + amount + " | Balance: Rs." + balanceAfter;
    }
}