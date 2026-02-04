package Data;

import java.time.Instant;
import java.util.*;
import java.io.*;
import Model.QuizParticipation;
import Model.Option;
import Model.ParticipantAnswer;
import Model.Question;
import Model.Quiz;
import Color.Color;
import Util.ScannerUtil;

public class QuizManager {
	private List<Quiz> quizzes = new ArrayList<>();
	private List<User> users = new ArrayList<>();
	private List<QuizParticipation> QPList = new ArrayList<>();
	private Scanner sc = new Scanner(System.in);
	private static int quizId = 0;
	private static int quizCount = 0;
	private static int questionId = 0;
	private static int questionCount = 0;
	private int correctAnswered, wrongAnswered;

	
	public List<Quiz> getQuizzes() {
		return quizzes;
	}

	public List<User> getUsers() {
		return users;
	}

	public void creatorMenu(User u) {
		while (true) {
			System.out.println(Color.CYAN + "\n=== Creator ===\n" + Color.RESET);
			System.out.println(Color.BOLD + "1. Add Quiz\n2. Participation details\n3. View created quizzes\n4. Exit"
					+ Color.RESET);
			System.out.print(Color.PURPLE + "\nEnter your choice:" + Color.RESET);
			int ch = ScannerUtil.nextInt(sc, "");
			if (ch == 1) {
				addQuiz();
			} else if (ch == 2)
				attendedParticpantDetails();
			else if (ch == 3)
				viewCreatedQuizzes(u);
			else
				break;
		}
	}

	public void attendorMenu(User u) {
		while (true) {
			System.out.println(Color.CYAN + "\n=== Attendor ===\n" + Color.RESET);
			System.out.println(Color.BOLD + "1. View Available Quizzes\n2. View History\n3. Exit" + Color.RESET);
			System.out.print(Color.PURPLE + "Enter your choice:" + Color.RESET);
			int ch = ScannerUtil.nextInt(sc, "");
			if (ch == 1) {
				takeQuiz(u);
			} else if (ch == 2)
				participantHistory(u);
			else
				break;
		}
	}

	void addQuiz() {
		quizId = quizCount++;
		User us = new User(0, "", "", "", "");
		int userId = us.getUserId();
		int optionId = 0;
		String name = us.getName();
		System.out.print(Color.GREEN + "\nEnter quiz title: " + Color.RESET);
		String title = sc.nextLine();
		System.out.print(Color.GREEN + "Enter description: " + Color.RESET);
		String desc = sc.nextLine();
		Quiz q = new Quiz(quizId, userId, title, desc, name, 0);
		while (true) {
			questionId = questionCount++;
			System.out.print("\nEnter question type (1. MCQ / 2. TrueFalse / 3. Text): ");
			int typeop = ScannerUtil.nextInt(sc, "");
			System.out.print(Color.CYAN + "\nEnter question: " + Color.RESET);
			String text = ScannerUtil.nextLine(sc, "");
			sc.nextLine();
			String type = (typeop == 1) ? "MCQ" : (typeop == 2) ? "TrueFalse" : "Text";
			System.out.print(Color.BOLD + "\nEnter marks: ");
			int mark = ScannerUtil.nextInt(sc, "");
			Question ques = new Question(questionId, quizId, text, type, mark);
			if (type.equalsIgnoreCase("mcq")) {
				boolean hasCorrect = false;
				while (!hasCorrect) {
					ques.getOptions().clear();
					for (int i = 1; i <= 4; i++) {
						System.out.print(Color.YELLOW + "\nEnter option " + i + ": " + Color.RESET);
						String optText = sc.nextLine();
						int op = ScannerUtil.nextInt(sc, "Is this option correct? ( 1." + Color.GREEN + " YES "
								+ Color.RESET + "/ 2. " + Color.RED + "NO " + Color.RESET + "):");
						boolean isCorrect = (op == 1);
						optionId++;
						if (isCorrect)
							hasCorrect = true;
						ques.addOption(new Option(questionId, optText, isCorrect, optionId));
					}
					if (!hasCorrect)
						System.out.println(Color.RED + "\nAt least one option must be correct!\n" + Color.RESET);
				}
			} else if (type.equalsIgnoreCase("truefalse")) {
				System.out.println(Color.YELLOW + "Enter correct answer: " + Color.RESET + "(1. " + Color.GREEN + "True"
						+ Color.RESET + "/ 2." + Color.RED + " False" + Color.RESET + "): ");
				int cor = sc.nextInt();
				optionId++;
				boolean ans = (cor == 1);
				ques.addOption(new Option(questionId, "true", ans, optionId));
				ques.addOption(new Option(questionId, "false", !ans, optionId));
			} else {
				System.out.println(Color.YELLOW + "Enter correct answer text: " + Color.RESET);
				String num = sc.nextLine();
				optionId++;
				ques.addOption(new Option(questionId, num, true, optionId));
			}
			q.addQuestion(ques);
			System.out.print(Color.BG_BLUE + "\nWant to Add another question? (1. yes / 2. no):" + Color.RESET + "   ");
			int cont = sc.nextInt();
			if (cont != 1)
				break;
		}
		quizzes.add(q);
		System.out.println("\nQuiz saved.\n");
	}

	public void viewCreatedQuizzes(User u) {

	    int userId = u.getUserId();
	    List<Quiz> ownQuizzes = new ArrayList<>();

	    for (Quiz q : quizzes) {
	        if (q.getCreatorId() == userId) {
	            if (ownQuizzes.isEmpty())
	                System.out.println(Color.BRIGHT_GREEN + "=== Created Quizzes ===" + Color.RESET);
	            ownQuizzes.add(q);
	            System.out.println(ownQuizzes.size() + ". " + q.getTitle());
	        }
	    }

	    if (ownQuizzes.isEmpty()) {
	        System.out.println(Color.RED + "No quizzes created yet." + Color.RESET);
	        return;
	    }

	    System.out.print("\nEnter quiz number for full details: ");
	    int selectedQuiz = ScannerUtil.nextInt(sc, "");

	    if (selectedQuiz < 1 || selectedQuiz > ownQuizzes.size()) {
	        System.out.println(Color.RED + "Invalid choice" + Color.RESET);
	        return;
	    }

	    Quiz q = ownQuizzes.get(selectedQuiz - 1);
	    System.out.println(Color.BRIGHT_GREEN + "\n=== Questions ===" + Color.RESET);

	    for (Question qn : q.getQuestions()) {
	        System.out.println(Color.CYAN + qn.getText() + Color.RESET);
	        for (Option op : qn.getOptions())
	            System.out.println("  " + Color.YELLOW + op.getText() + (op.getCorrect() ? "  ✅" : "") + Color.RESET);
	        System.out.println();
	    }
	}


	public void participantHistory(User ur) {
		List<ParticipantAnswer> list = ur.getPartcipantAnswerList();
		System.out.println(Color.BOLD + Color.BG_BRIGHT_YELLOW  + "\n=== Attended Quiz ===" + Color.RESET);
		for (ParticipantAnswer pa : list) {
			int quizId = pa.getQuizId();
			int selectedOption = pa.getSelectedOptionId();
			for (Quiz q : quizzes) {
				if (quizId == q.getId()) {
					System.out.println(Color.BOLD + "\nQuiz: " + q.getTitle() + Color.RESET);
					System.out.printf(
							Color.BG_BRIGHT_CYAN + Color.BOLD + Color.BLACK + "%-40s %-30s %-30s%n" + Color.RESET,
							"Question", "Selected Option", "Correct Option");
					for (Question qn : q.getQuestions()) {
						String selected = "";
						String correct = "";
						for (Option op : qn.getOptions()) {
							if (op.getOptionId() == selectedOption) {
								selected = op.getText();
							}
							if (op.getCorrect()) {
								correct = op.getText();
							}
						}
						System.out.printf("%-40s %-30s %-30s%n", qn.getText(), selected, correct);
					}
				}
			}
		}
	}

	void attendedParticpantDetails() {

	    if (quizzes.isEmpty()) {
	        System.out.println(Color.RED + "No quizzes available yet." + Color.RESET);
	        return;
	    }

	    final int COL_NAME = 20;
	    final int COL_EMAIL = 30;
	    final int COL_CORRECT = 10;
	    final int COL_WRONG = 10;
	    final int COL_DATE = 25;

	    for (Quiz q : quizzes) {
	        System.out.println(Color.BRIGHT_CYAN
	                + "\n══════════════════════════════════════════════════════════════════════════" + Color.RESET);
	        System.out.println(Color.BRIGHT_YELLOW + " Quiz: " + q.getTitle() + Color.RESET);
	        System.out.println(Color.BRIGHT_BLACK + q.getdescription() + Color.RESET);
	        System.out.println(Color.BRIGHT_CYAN
	                + "══════════════════════════════════════════════════════════════════════════" + Color.RESET);

	        System.out.printf(
	                Color.BRIGHT_GREEN + "%-" + COL_NAME + "s %-" + COL_EMAIL + "s %-" + COL_CORRECT + "s %-"
	                        + COL_WRONG + "s %-" + COL_DATE + "s" + Color.RESET + "%n",
	                "Name", "Email", "Correct", "Wrong", "Date");

	        System.out.println(Color.BRIGHT_CYAN
	                + "──────────────────────────────────────────────────────────────────────────" + Color.RESET);

	        boolean found = false;

	        for (QuizParticipation qp : QPList) {
	            if (qp.getQuizId() == q.getId()) {
	                found = true;

	                String rawName = qp.getName();
	                String rawEmail = qp.getEmail();
	                String rawCorrect = String.valueOf(qp.getCorrectAnswered());
	                String rawWrong = String.valueOf(qp.getWrongAnswered());
	                String rawDate = qp.getDate();

	                String fName = String.format("%-" + COL_NAME + "s", rawName);
	                String fEmail = String.format("%-" + COL_EMAIL + "s", rawEmail);
	                String fCorrect = String.format("%-" + COL_CORRECT + "s", rawCorrect);
	                String fWrong = String.format("%-" + COL_WRONG + "s", rawWrong);
	                String fDate = String.format("%-" + COL_DATE + "s", rawDate);

	                System.out.println(
	                        Color.BRIGHT_PURPLE + fName + Color.RESET + " " +
	                        Color.BRIGHT_CYAN + fEmail + Color.RESET + " " +
	                        fCorrect + " " +
	                        fWrong + " " +
	                        fDate
	                );
	            }
	        }

	        if (!found) {
	            System.out.println(Color.RED + "No participation records yet." + Color.RESET);
	        }

	        System.out.println(Color.BRIGHT_CYAN
	                + "══════════════════════════════════════════════════════════════════════════" + Color.RESET);
	    }
	}

	void takeQuiz(User u) {
		Date dateobj = new Date();
		Instant date = dateobj.toInstant();
		int userId = u.getUserId();
		String participationName = u.getName();
		String userEmail = u.getEmail();
		if (quizzes.isEmpty()) {
			System.out.println("No quizzes available.\n");
			return;
		}
		System.out.println(Color.GREEN + "\n=== Quizzes ===\n" + Color.RESET);
		for (int i = 0; i < quizzes.size(); i++)
			System.out.println(Color.CYAN + (i + 1) + ".  " + quizzes.get(i).getTitle() + Color.RESET + "\n");
		System.out.print("Choose quiz: ");
		int choice = sc.nextInt();
		if (choice < 1 || choice > quizzes.size()) {
			System.out.println(Color.RED + "Invalid choice.\n" + Color.RESET);
			return;
		}
		Quiz quizObj = quizzes.get(choice - 1);
		int quizIdForParticipation = quizObj.getId();
		correctAnswered = 0;
		wrongAnswered = 0;
		int total = 0;
		int questionCount = 0;
		for (Question question : quizObj.getQuestions()) {
			questionCount++;
			System.out.println(Color.YELLOW + "\n" + questionCount + ". " + question.getText() + Color.RESET);
			int selectedOptionId = -1;
			if (question.getType().equalsIgnoreCase("mcq")) {
				List<Option> opts = question.getOptions();
				for (int i = 0; i < opts.size(); i++)
					System.out.println("   " + (i + 1) + ". " + opts.get(i).getText());
				System.out.print(Color.BLUE + "\nEnter choice: " + Color.RESET);
				int ans = sc.nextInt();
				sc.nextLine();
				Option selected = opts.get(ans - 1);
				selectedOptionId = selected.getOptionId();
				if (selected.getCorrect()) {
					total += question.getMark();
					correctAnswered++;
				} else
					wrongAnswered++;
				ParticipantAnswer pa = new ParticipantAnswer(quizIdForParticipation, selectedOptionId);
				u.setPartcipantAnswerList(pa);
			} else if (question.getType().equalsIgnoreCase("truefalse")) {
				System.out.print(Color.BLUE + "Enter answer:" + Color.RESET + " (1. " + Color.GREEN + "True"
						+ Color.RESET + " / 2. " + Color.RED + "false)  " + Color.RESET + ": ");
				int ans = sc.nextInt();
				sc.nextLine();
				String choosedAns = (ans == 1) ? "true" : "false";
				boolean correct = false;
				int chosenOpId = -1;
				for (Option op : question.getOptions()) {
					if (op.getText().equalsIgnoreCase(choosedAns)) {
						chosenOpId = op.getOptionId();
						if (op.getCorrect())
							correct = true;
					}
				}
				selectedOptionId = chosenOpId;
				if (correct) {
					total += question.getMark();
					correctAnswered++;
				} else
					wrongAnswered++;
				ParticipantAnswer pa = new ParticipantAnswer(quizIdForParticipation, selectedOptionId);
				u.setPartcipantAnswerList(pa);
			} else {
				System.out.print(Color.BLUE + "Enter your answer: " + Color.RESET);
				String ans = sc.nextLine().trim();
				Option correctOp = question.getOptions().get(0);
				selectedOptionId = correctOp.getOptionId();
				if (correctOp.getText().equalsIgnoreCase(ans)) {
					total += question.getMark();
					correctAnswered++;
				} else
					wrongAnswered++;
				ParticipantAnswer pa = new ParticipantAnswer(quizIdForParticipation, selectedOptionId);
				u.setPartcipantAnswerList(pa);
			}
		}
		u.setScore(total);
		System.out.println(
				Color.BG_GREEN + Color.BOLD + "\n===You scored: " + total + " !===" + Color.RESET);
		QuizParticipation QP = new QuizParticipation(quizIdForParticipation, userId, participationName, userEmail,
				correctAnswered, wrongAnswered, date.toString());
		QPList.add(QP);
	}

}
