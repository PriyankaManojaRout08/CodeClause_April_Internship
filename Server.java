import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

@SuppressWarnings("unused")
public class Server extends JFrame {
    private static final int PORT = 95;
    private static final List<String> SERVER_ADDRESSES = Arrays.asList("server1", "server2", "server3");
    private static JTextArea logArea;

    public Server() {
        setTitle("Distributed File System Server");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        logArea = new JTextArea(10, 30);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        add(scrollPane);

        startServer();
    }

    private void startServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                appendToLog("Server started. Listening on port " + PORT);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    appendToLog("Client connected: " + clientSocket);

                    // Start a new thread to handle client requests
                    Thread t = new Thread(new ClientHandler(clientSocket));
                    t.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void appendToLog(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                // Receive request from client
                String request = (String) in.readObject();
                appendToLog("Received request: " + request);

                // Simulate load balancing by selecting a random server
                String selectedServer = SERVER_ADDRESSES.get(new Random().nextInt(SERVER_ADDRESSES.size()));

                // Simulate server availability check
                if (isServerAvailable(selectedServer)) {
                    // Send selected server address to client
                    out.writeObject(selectedServer);
                    out.flush();
                } else {
                    appendToLog("Server " + selectedServer + " not available. Retrying...");
                    // Retry with another server
                    selectedServer = retryWithAnotherServer();
                    if (selectedServer != null) {
                        out.writeObject(selectedServer);
                        out.flush();
                    } else {
                        out.writeObject("Server not available");
                        out.flush();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private boolean isServerAvailable(String serverAddress) {
            // Simulate server availability check
            return true;
        }

        private String retryWithAnotherServer() {
            for (String server : SERVER_ADDRESSES) {
                if (isServerAvailable(server)) {
                    return server;
                }
            }
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Server server = new Server();
            server.setVisible(true);
        });
    }
}

