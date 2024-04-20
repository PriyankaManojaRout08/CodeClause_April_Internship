import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

class Account {
    private double balance;
    private List<Double> transactions;

    public Account(double initialBalance) {
        balance = initialBalance;
        transactions = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add(amount);
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            transactions.add(-amount);
        } else {
            JOptionPane.showMessageDialog(null, "Insufficient funds");
        }
    }

    public List<Double> getTransactions() {
        return transactions;
    }
}

class MainFrame extends JFrame {
    private Account account;
    private String username;

    public MainFrame(Account account, String username) {
        super("Banking Management System");

        this.account = account;
        this.username = username;

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JButton balanceButton = new JButton("Balance");
        balanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBalanceDialog();
            }
        });
        panel.add(balanceButton);

        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDepositDialog();
            }
        });
        panel.add(depositButton);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showWithdrawDialog();
            }
        });
        panel.add(withdrawButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
        panel.add(logoutButton);

        add(panel);
    }

    private void showBalanceDialog() {
        JOptionPane.showMessageDialog(this, "Current Balance for " + username + ": " + account.getBalance());
    }

    private void showDepositDialog() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter deposit amount:");
        if (amountStr != null && !amountStr.isEmpty()) {
            double amount = Double.parseDouble(amountStr);
            account.deposit(amount);
            JOptionPane.showMessageDialog(this, "Deposited: " + amount);
        }
    }

    private void showWithdrawDialog() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter withdrawal amount:");
        if (amountStr != null && !amountStr.isEmpty()) {
            double amount = Double.parseDouble(amountStr);
            account.withdraw(amount);
            JOptionPane.showMessageDialog(this, "Withdrawn: " + amount);
        }
    }
}

class LoginFrame extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Map<String, String> users;

    public LoginFrame() {
        super("Login");

        // Initialize users map with usernames and passwords
        users = new HashMap<>();
        users.put("Priyanka", "password1");
        users.put("Armistha", "password2");
        users.put("Mukesh", "password3");
        users.put("Manoja", "password4");
        users.put("Anusuya", "password5");

        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        panel.add(loginButton);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (authenticate(username, password)) {
            openMainFrame(username);
            JOptionPane.showMessageDialog(this, "User Authenticated");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid user");
        }
    }

    private boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    private void openMainFrame(String username) {
        new MainFrame(new Account(1000.0), username).setVisible(true);
        dispose();
    }
}

public class BankingManagementSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}
