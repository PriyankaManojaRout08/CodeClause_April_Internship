package Advanced_Chatting_Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ChatClient extends JFrame {
    
    private PrintWriter writer;
    private BufferedReader reader;
    private JTextArea messageArea;
    private JTextField groupField;
    private JTextField messageField;
    private JTextField serverAddressField;
    private JTextField portNumberField;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public ChatClient() {
        
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        JPanel connectionPanel = new JPanel(new GridLayout(4, 2));
        connectionPanel.add(new JLabel("Server Address:"));
        serverAddressField = new JTextField("localhost");
        connectionPanel.add(serverAddressField);
        connectionPanel.add(new JLabel("Port Number:"));
        portNumberField = new JTextField("95");
        connectionPanel.add(portNumberField);
        connectionPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        connectionPanel.add(usernameField);
        connectionPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        connectionPanel.add(passwordField);
        add(connectionPanel, BorderLayout.NORTH);

        messageArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Group Name:"), BorderLayout.WEST);
        groupField = new JTextField();
        inputPanel.add(groupField, BorderLayout.CENTER);
        messageField = new JTextField();
        inputPanel.add(messageField, BorderLayout.SOUTH);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);

        connectToServer();
    }

    @SuppressWarnings("resource")
    private void connectToServer() {
        try {
        
            Socket socket = new Socket("localhost",95);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             
            writer.println("user1");
            writer.println("password1");

            new Thread(new ServerListener()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
          
            String groupName = groupField.getText();
            String message = messageField.getText();
            System.out.println("Sending message to group " + groupName + ": " + message);
            writer.println(groupName);
            writer.println(message);
            messageField.setText(""); 
        }
    }

    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    String line = reader.readLine();
                    if (line != null) {
                        messageArea.append(line + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        
        new ChatClient();
    }
}


