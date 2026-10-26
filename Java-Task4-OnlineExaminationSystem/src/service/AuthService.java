package service;

import model.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private static final File FILE = new File("users.txt");
    private static final Map<String, User> users = new HashMap<>();

    static {
        load();
        if(users.isEmpty()) {
            users.put("student", new User("student", "1234", "Student"));
            save();
        }
    }

    public static User login(String username, String password) {
        User u = users.get(username);
        if(u != null && u.getPassword().equals(password)) return u;
        return null;
    }

    public static void save() {
        try(PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for(User u : users.values()) {
                pw.println(u.getUsername() + ";" + u.getPassword() + ";" + u.getDisplayName());
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void load() {
        if(!FILE.exists()) return;
        try(BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] p = line.split(";", 3);
                if(p.length == 3) users.put(p[0], new User(p[0], p[1], p[2]));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
