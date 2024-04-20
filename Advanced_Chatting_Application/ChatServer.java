package Advanced_Chatting_Application;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.List;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

class Group {
    private String name;
    private List<User> members;

    public Group(String name) {
        this.name = name;
        members = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<User> getMembers() {
        return members;
    }

    public void addMember(User user) {
        members.add(user);
    }
}

public class ChatServer{
    private Map<String, User> users;
    private Map<String, Group> groups;
    private JTextArea messageArea;

    public ChatServer(JTextArea messageArea) {
        users = new HashMap<>();
        groups = new HashMap<>();
        this.messageArea = messageArea;
        
        users.put("user1", new User("user1", "password1"));
        users.put("user2", new User("user2", "password2"));
    }

    public boolean createUser(String username, String password) {
        if (!users.containsKey(username)) {
            User user = new User(username, password);
            users.put(username, user);
            return true;
        }
        return false;
    }

    public boolean authenticateUser(String username, String password) {
        if (users.containsKey(username)) {
            User user = users.get(username);
            return user.getPassword().equals(password);
        }
        return false;
    }

    public boolean createGroup(String groupName) {
        if (!groups.containsKey(groupName)) {
            Group group = new Group(groupName);
            groups.put(groupName, group);
            return true;
        }
        return false;
    }

    public boolean joinGroup(String username, String groupName) {
        if (users.containsKey(username) && groups.containsKey(groupName)) {
            User user = users.get(username);
            Group group = groups.get(groupName);
            group.addMember(user);
            return true;
        }
        return false;
    }

    public void sendMessage(String sender, String groupName, String message) {
        if (groups.containsKey(groupName)) {
            Group group = groups.get(groupName);
            List<User> members = group.getMembers();
            for (User member : members) {
                messageArea.append(sender + " -> " + member.getUsername() + " (" + groupName + "): " + message + "\n");
            }
        }
    }

    public void start() {
        try {
           @SuppressWarnings("resource")
            ServerSocket serverSocket = new ServerSocket(95);
            messageArea.append("Server started. Waiting for clients...\n");
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClientHandler implements Runnable {
        @SuppressWarnings("unused")
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
    
            public ClientHandler(Socket socket) {
                this.socket = socket;
                try {
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void run() {
                try {
                    String username = reader.readLine();
                    String password = reader.readLine();
                    System.out.println("Received username: " + username);
                    System.out.println("Received password: " + password);
                    
                    if (authenticateUser(username, password)) {
                        writer.println("Authenticated");
                        System.out.println("Authenticated user: " + username);
                        while (true) {
                            String groupName = reader.readLine();
                            String message = reader.readLine();
                            System.out.println("Received message from " + username + " in group " + groupName + ": " + message);
                            sendMessage(username, groupName, message);
                        }
                    } else {
                        writer.println("Invalid credentials");
                        System.out.println("Invalid credentials for user: " + username);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        JTextArea messageArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(messageArea);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);

        ChatServer chatServer = new ChatServer(messageArea);
        chatServer.start();
    }
}

