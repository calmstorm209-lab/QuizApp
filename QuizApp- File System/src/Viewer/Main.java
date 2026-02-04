package Viewer;
import Model.Question;

import java.util.Scanner;
import Util.ScannerUtil;
import Data.QuizManager;
import Model.User;
import Model.Quiz;
import Color.Color;

public class Main {


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		QuizManager qm = new QuizManager();
		
		qm.loadAllData();
		
	

		while (true) {
			
		
			System.out.println(Color.CYAN + "\n=== Quiz System ===\n" + Color.RESET);
			System.out.println(Color.YELLOW +Color.BOLD+ "    1. Signup");
			System.out.println("    2. Login");
			System.out.println("    3. Exit\n" +Color.RESET);
			
			

			int ch = ScannerUtil.nextInt(sc, "Enter choice: ");

			if (ch == 1) {
				User user = new User(0, null, null, null, null);
				user.signup(qm.getUsers());
				qm.saveQuizzes();
			}

			else if (ch == 2) {
				User u = User.login(qm.getUsers());
				if (u != null) {
					if (u.getRole().equalsIgnoreCase("creator"))
						qm.creatorMenu(u);
					else
						{
						qm.attendorMenu(u);
						
						}
					
					
				}
			
			} else if (ch == 3) {
				System.out.println(Color.YELLOW + "Exiting..." + Color.RESET);
				break;
			} else {
				System.out.println("Invalid choice. Try again.");
			}
		}
		sc.close();

	}
}
