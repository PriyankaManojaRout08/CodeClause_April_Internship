import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

@SuppressWarnings("unused")
public class Client extends JFrame {
    private static final List<String> SERVER_ADDRESSES = Arrays.asList("localhost", "127.0.0.1", "127.0.0.1");
    private static final int PORT = 95;

    private JTextField fileNameField;
    private JButton requestButton;
    private JTextArea logArea;

    public Client() {
        setTitle("Distributed File System Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fileNameField = new JTextField(20);
        requestButton = new JButton("Request File");
        logArea = new JTextArea(10, 30);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter file name: " ));
        inputPanel.add(fileNameField);
        inputPanel.add(requestButton);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        requestButton.addActionListener(e -> requestFile());
    }

    private void requestFile() {
        String fileName = fileNameField.getText().trim();
        if (!fileName.isEmpty()) {
            String selectedServer = selectServerRandomly();
            if (selectedServer != null) {
                try (Socket socket = new Socket(selectedServer, PORT);
                     ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                     ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                    // Send file request to server
                    out.writeObject(fileName);
                    out.flush();

                    // Receive server response
                    String serverResponse = (String) in.readObject();
                    appendToLog(serverResponse);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    appendToLog("Error occurred: " + ex.getMessage());
                }
            } else {
                appendToLog("All servers are currently unavailable. Please try again later.");
            }
        } else {
            appendToLog("Please enter a file name.");
        }
    }

    private String selectServerRandomly() {
        Collections.shuffle(SERVER_ADDRESSES);
        for (String server : SERVER_ADDRESSES) {
            if (isServerAvailable(server)) {
                return server;
            }
        }
        return null;
    }

    private boolean isServerAvailable(String serverAddress) {
        // Simulate server availability check
        return true;
    }

    private void appendToLog(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Client client = new Client();
            client.setVisible(true);
        });
    }
}
