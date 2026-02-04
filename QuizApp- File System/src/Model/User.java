package Model;

import java.util.*;
import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;
import java.io.*;
import Color.Color;
import Util.ScannerUtil;

public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private String role;
    private int score;
    private static boolean eemail;
    private static int userCount;

    private List<QuizParticipation> history = new ArrayList<>();
    private List<ParticipantAnswer> particpantAnswerList = new ArrayList<>();

 

    public static boolean isEemail() {
        return eemail;
    }

    public static void setEemail(boolean eemail) {
        User.eemail = eemail;
    }

    public static int getUserCount() {
        return userCount;
    }

    public static void setUserCount(int userCount) {
        User.userCount = userCount;
    }

    public List<ParticipantAnswer> getPartcipantAnswerList() {
        return particpantAnswerList;
    }

    public void setPartcipantAnswerList(ParticipantAnswer pa) {
        particpantAnswerList.add(pa);
    }

    public User(int userId, String name, String email, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public void addHistory(QuizParticipation record) {
        history.add(record);
    }

    public List<QuizParticipation> getHistory() {
        return history;
    }

    public void setScore(int total) {
        score = total;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setHistory(List<QuizParticipation> history) {
        this.history = history;
    }

    public int getScore() {
        return score;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void signup(List<User> users) {
        userCount = users.size();

        Scanner sc = new Scanner(System.in);
        eemail = false;

        String name = ScannerUtil.nextLine(sc, "Enter name: ");
        String email = null;

        while (!eemail) {
            email = ScannerUtil.nextLine(sc, Color.BRIGHT_CYAN + "\nEnter email: ");
            if (!(email.contains("@") && email.endsWith(".com"))) {
                System.out.println("Please enter correct email format: (example123@mail.com)");
                continue;
            }

            boolean exists = false;
            for (User u : users) {
                if (u.getEmail().equalsIgnoreCase(email)) {
                    exists = true;
                    break;
                }
            }

            if (exists) {
                System.out.println("Email already exists enter new email");
            } else {
                eemail = true;
            }
        }

        String pass = ScannerUtil.nextLine(sc, "Enter password: ");
        int roleOp = ScannerUtil.nextInt(sc, Color.RESET + "\nEnter role (1.creator / 2.attendor): ");
        String role = (roleOp == 1) ? "creator" : "attendor";

        userCount++;
        User newUser = new User(userCount, name, email, pass, role);
        users.add(newUser);

      
        

        System.out.println(Color.GREEN + "Signup successful as a " + role + "." + Color.RESET + "\n");
    }

    public static User login(List<User> users) {
        Scanner sc = new Scanner(System.in);
        String email = ScannerUtil.nextLine(sc, Color.CYAN + "\nEnter email: ");
        String pass = ScannerUtil.nextLine(sc, "Enter password: ");

        for (User u : users) {
            if (u.email.equalsIgnoreCase(email) && u.password.equals(pass)) {
                System.out.println(
                    Color.GREEN + "\nLogin successful. Welcome " + Color.BOLD + u.getName() + "!\n" + Color.RESET);
                return u;
            }
        }
        System.out.println(Color.RED + "Invalid email or password.\n" + Color.RESET);
        return null;
    }

    public void showHistory() {
        if (history.isEmpty()) {
            System.out.println("No quiz history.\n");
            return;
        }
        System.out.println("Quiz History for " + name + ":");
        for (QuizParticipation qp : history) {
            System.out.println(qp);
        }
        System.out.println();
    }

   
}
    
