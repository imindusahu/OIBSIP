import java.util.Scanner;

public class ATM {
    private final Bank bank;
    private final Scanner sc = new Scanner(System.in);

    public ATM(Bank bank) {
        this.bank = bank;
    }

    public void start() {
        System.out.println("======= WELCOME TO OASIS ATM =======");
        boolean running = true;

        while (running) {
            Account user = login();
            if (user == null) {
                System.out.println("Too many wrong attempts. Access denied.");
                return;
            }
            System.out.println("Login successful!");
            running = showMenu(user);  // true = logout, false = quit
        }
    }

    private Account login() {
        Account user = null;
        for(int attempt = 1; attempt <= 3 && user == null; attempt++) {
            System.out.print("User ID: ");
            String id = sc.nextLine().trim();
            System.out.print("PIN: ");
            String pin = sc.nextLine().trim();

            user = bank.login(id, pin);
            if(user == null) {
                System.out.println("Wrong ID or PIN. Attempt " + attempt + " of 3.");
            }
        }
        return user;
    }

    private  boolean showMenu(Account user) {
        while(true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.println("6. Logout");
            System.out.print("Choose option: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> showHistory(user);
                case "2" -> withdraw(user);
                case "3" -> deposit(user);
                case "4" -> transfer(user);
                case "5" -> {
                    System.out.println("Thank you for using our ATM. Goodbye!");
                    return false;
                }
                case "6" -> {
                    System.out.println("Logged out successfully.\n");
                    return true;
                }
                default -> System.out.println("Invalid option. Please choose 1-6.");
            }
        }
    }

    private void showHistory(Account user) {
        if(user.getHistory().isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        for(Transaction t : user.getHistory()) {
            System.out.println(t);
        }
    }

    private void withdraw(Account user) {
        double amount = readAmount();
        if(amount <= 0) return;

        if(user.withdraw(amount)) {
            System.out.println("Withdrawn Rs." + amount + ". New balance: Rs." + user.getBalance());
        } else {
            System.out.println("Insufficient Funds");
        }
    }

    private void deposit(Account user) {
        double amount = readAmount();
        if(amount <= 0) return;

        user.deposit(amount);
        System.out.println("Deposited Rs." + amount + ". New balance: Rs." + user.getBalance());
    }

    private void transfer(Account user) {
        System.out.print("Recipient Account ID: ");
        String toId = sc.nextLine().trim();

        if(bank.findAccount(toId) == null) {
            System.out.println("Recipient account not found.");
            return;
        }
        if(toId.equals(user.getUserId())) {
            System.out.println("You cannot transfer to your own account.");
            return;
        }

        double amount = readAmount();
        if(amount <= 0) return;

        if(bank.transfer(user, toId, amount)) {
            System.out.println("Transferred Rs." + amount + " to " + toId + ". New balance: Rs." + user.getBalance());
        } else {
            System.out.println("Insufficient Funds");
        }
    }

    private double readAmount() {
        System.out.print("Enter amount: ");
        try {
            double amount = Double.parseDouble(sc.nextLine().trim());
            if(amount <= 0) {
                System.out.println("Amount must be greater than 0.");
                return -1;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return -1;
        }
    }
}
