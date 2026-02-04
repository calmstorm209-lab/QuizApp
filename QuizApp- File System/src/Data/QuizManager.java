package Data;

import java.time.Instant;
import java.util.*;
import java.io.*;
import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;
import Model.QuizParticipation;
import Model.User;
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

	private int quizId = 1;
	private int questionId = 1;
	private int optionId = 1;

	File quizFile = new File(
			"/home/balaji-zstth004/eclipse-workspace-new/QuizApp- File System/src/FileManager/quizzes.csv");
	File questionFile = new File(
			"/home/balaji-zstth004/eclipse-workspace-new/QuizApp- File System/src/FileManager/questions.csv");
	File optionFile = new File(
			"/home/balaji-zstth004/eclipse-workspace-new/QuizApp- File System/src/FileManager/options.csv");
	File quizparFile = new File(
			"/home/balaji-zstth004/eclipse-workspace-new/QuizApp- File System/src/FileManager/quizParticipation.csv");
	File userFile = new File(
			"/home/balaji-zstth004/eclipse-workspace-new/QuizApp- File System/src/FileManager/users.csv");
	File participantAnswerFile = new File(
			"/home/balaji-zstth004/eclipse-workspace-new/QuizApp- File System/src/FileManager/participantAnswers.csv");
	{
		try {
			quizFile.createNewFile();
			questionFile.createNewFile();
			optionFile.createNewFile();
			quizparFile.createNewFile();
			userFile.createNewFile();
			participantAnswerFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadAllData();
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public void creatorMenu(User u) {
		while (true) {
			System.out.println(Color.CYAN + "\n=== Creator ===\n" + Color.RESET);
			System.out.println(Color.BOLD + "1. Add Quiz\n2. Participation details\n3. View created quizzes\n4. Exit"
					+ Color.RESET);
			System.out.print(Color.PURPLE + "\nEnter your choice:" + Color.RESET);
			int ch = ScannerUtil.nextInt(sc, "");
			if (ch == 1) {
				addQuiz(u);
				saveAllData();
			} else if (ch == 2)
				attendedParticpantDetails();
			else if (ch == 3)
				viewCreatedQuizzes(u);
			else
				break;
		}
		saveAllData();
	}

	public void attendorMenu(User u) {
		while (true) {
			System.out.println(Color.CYAN + "\n=== Attendor ===\n" + Color.RESET);
			System.out.println(Color.BOLD + "1. View Available Quizzes\n2. View attended quiz\n3. Exit" + Color.RESET);
			System.out.print(Color.PURPLE + "Enter your choice:" + Color.RESET);
			int ch = ScannerUtil.nextInt(sc, "");
			if (ch == 1)
				takeQuiz(u);
			else if (ch == 2)
				participantHistory(u);
			else
				break;
		}
		saveAllData();
	}

	private void updateMaxIds() {
		quizId = 0;
		questionId = 0;
		optionId = 0;

		for (Quiz q : quizzes) {
			if (q == null)
				continue;
			quizId = Math.max(quizId, q.getId());
			if (q.getQuestions() == null)
				continue;
			for (Question qs : q.getQuestions()) {
				if (qs == null)
					continue;
				questionId = Math.max(questionId, qs.getId());
				if (qs.getOptions() == null)
					continue;
				for (Option op : qs.getOptions()) {
					if (op == null)
						continue;
					optionId = Math.max(optionId, op.getOptionId());
				}
			}
		}
	}

	public void addQuiz(User us) {
		updateMaxIds();

		System.out.print(Color.GREEN + "\nEnter quiz title: " + Color.RESET);
		String title = sc.nextLine();

		System.out.print(Color.GREEN + "Enter description: " + Color.RESET);
		String desc = sc.nextLine();

		quizId++;
		int quizCode = (int) (Math.random() * 900000) + 100000;

		Quiz q = new Quiz(quizId, us.getUserId(), title, desc, us.getName(), quizCode);

		while (true) {

			System.out.print("\nEnter question type (1.MCQ / 2.TrueFalse / 3.Text): ");
			int typeop = ScannerUtil.nextInt(sc, "");

			System.out.print(Color.CYAN + "\nEnter question: " + Color.RESET);
			String text = ScannerUtil.nextLine(sc, "");

			String type = (typeop == 1) ? "MCQ" : (typeop == 2) ? "TrueFalse" : "Text";

			System.out.print(Color.BOLD + "\nEnter marks: ");
			int mark = ScannerUtil.nextInt(sc, "");

			questionId++;
			Question ques = new Question(questionId, quizId, text, type, mark);

			if (type.equalsIgnoreCase("MCQ")) {

				boolean hasCorrect = false;

				while (!hasCorrect) {
					if (ques.getOptions() != null)
						ques.getOptions().clear();

					for (int i = 1; i <= 4; i++) {
						System.out.print(Color.YELLOW + "\nEnter option " + i + ": " + Color.RESET);
						String optText = sc.nextLine();

						int op = ScannerUtil.nextInt(sc, "Is this correct? (1.YES / 2.NO): ");
						boolean isCorrect = (op == 1);

						if (isCorrect)
							hasCorrect = true;

						optionId++;
						ques.addOption(new Option(questionId, optText, isCorrect, optionId));
					}
				}
			}

			else if (type.equalsIgnoreCase("TrueFalse")) {

				System.out.print(Color.YELLOW + "Enter correct answer (1.True / 2.False): " + Color.RESET);
				int cor = ScannerUtil.nextInt(sc, "");
				boolean ans = (cor == 1);

				optionId++;
				ques.addOption(new Option(questionId, "true", ans, optionId));

				optionId++;
				ques.addOption(new Option(questionId, "false", !ans, optionId));
			}

			else {

				System.out.print(Color.YELLOW + "Enter correct answer text: " + Color.RESET);
				String num = sc.nextLine();

				optionId++;
				ques.addOption(new Option(questionId, num, true, optionId));
			}

			q.addQuestion(ques);

			System.out.print(Color.BG_BLUE + "\nAdd another question? (1.yes / 2.no): " + Color.RESET);
			int cont = sc.nextInt();
			sc.nextLine();
			if (cont != 1)
				break;
		}

		quizzes.add(q);
		System.out.println("Your Quiz Code is " + quizCode);
		System.out.println("\nQuiz saved.\n");

		saveAllData();
	}

	public void viewCreatedQuizzes(User u) {
		List<Quiz> userQuizzes = new ArrayList<>();
		for (Quiz q : quizzes)
			if (q.getCreatorId() == u.getUserId())
				userQuizzes.add(q);

		System.out.println(Color.BRIGHT_GREEN + "=== Created Quizzes ===" + Color.RESET);
		for (int i = 0; i < userQuizzes.size(); i++)
			System.out.println(
					(i + 1) + ". " + userQuizzes.get(i).getTitle() + "     " + userQuizzes.get(i).getquizCode());

		System.out.print("\nEnter quiz number for full details: ");
		int selectedQuiz = ScannerUtil.nextInt(sc, "");
		if (selectedQuiz < 1 || selectedQuiz > userQuizzes.size()) {
			System.out.println(Color.RED + "Invalid choice" + Color.RESET);
			return;
		}

		Quiz q = userQuizzes.get(selectedQuiz - 1);
		System.out.println(Color.BRIGHT_GREEN + "\n=== Questions ===" + Color.RESET);
		for (Question qn : q.getQuestions()) {
			System.out.println(Color.CYAN + qn.getText() + Color.RESET);
			for (Option op : qn.getOptions())
				System.out.println("  " + Color.YELLOW + op.getOptionId() + "   " + op.getText()
						+ (op.getCorrect() ? " ✅" : "") + Color.RESET);
			System.out.println();
		}
		saveAllData();
	}

	public void participantHistory(User u) {
		List<ParticipantAnswer> palist = u.getPartcipantAnswerList();
		if (palist == null || palist.isEmpty()) {
			System.out.println(Color.RED + "No quizzes participated." + Color.RESET);
			return;
		}

		System.out.println(Color.GREEN + "=== Attended Quizzes ===" + Color.RESET);

		List<Quiz> attendedQuiz = new ArrayList<>();

		for (ParticipantAnswer pa : palist) {
			for (Quiz quiz : quizzes) {
				if (quiz.getId() == pa.getQuizId()) {
					if (!attendedQuiz.contains(quiz)) {
						attendedQuiz.add(quiz);
					}
				}
			}
		}

		int index = 1;
		for (Quiz aq : attendedQuiz) {
			System.out.println(index++ + ". " + aq.getTitle());
		}

		System.out.print("\nChoose quiz for full details: ");
		int quizChoice = ScannerUtil.nextInt(sc, "");
		if (quizChoice < 1 || quizChoice > attendedQuiz.size()) {
			System.out.println(Color.RED + "Invalid choice" + Color.RESET);
			return;
		}

		Quiz selectedQuizForView = attendedQuiz.get(quizChoice - 1);
		System.out.println(Color.PURPLE + "\nYou selected : " + Color.RESET + selectedQuizForView.getTitle() + "\n");

		System.out.println(Color.GREEN + "=== Questions ===" + Color.RESET);

		for (Question qn : selectedQuizForView.getQuestions()) {
			System.out.println(Color.CYAN + qn.getText() + Color.RESET);

			ParticipantAnswer answerForQuestion = null;
			for (ParticipantAnswer pa : palist) {
				if (pa.getQuizId() == selectedQuizForView.getId() && pa.getQuestionId() == qn.getId()) {
					answerForQuestion = pa;
					break;
				}
			}

			if (answerForQuestion == null) {
				System.out.println(Color.YELLOW + "  No answer provided." + Color.RESET);
				continue;
			}

			String qType = qn.getType() != null ? qn.getType().trim().toLowerCase() : "";

			if (qType.equals("mcq")) {
				String[] selArr = answerForQuestion.getSelectedOptionIdArr();
				if (selArr == null || selArr.length == 0) {
					System.out.println(Color.YELLOW + "  No option selected." + Color.RESET);
				} else {
					List<String> correctOptions = new ArrayList<>();
					for (Option opt : qn.getOptions()) {
						if (opt.getCorrect())
							correctOptions.add(opt.getText());
					}

					for (String s : selArr) {
						if (s == null || s.trim().isEmpty())
							continue;
						try {
							int selId = Integer.parseInt(s.trim());
							Option matched = null;
							for (Option o : qn.getOptions()) {
								if (o.getOptionId() == selId) {
									matched = o;
									break;
								}
							}
							if (matched != null) {
								String mark = matched.getCorrect() ? "✅ CORRECT" : "❌ WRONG";
								System.out.println("  Your answer: " + matched.getText() + "  " + mark);
							}
						} catch (NumberFormatException e) {
							System.out.println("  Your answer: (invalid option format: " + s + ")");
						}
					}

					if (!correctOptions.isEmpty()) {
						System.out.println("  Correct option(s): " + String.join(", ", correctOptions));
					}
				}
			} else if (qType.equals("truefalse") || qType.equals("truefalse") || qType.equals("true")
					|| qType.equals("false")) {
				int selId = answerForQuestion.getSelectedOptionId();
				Option matched = null;
				for (Option o : qn.getOptions()) {
					if (o.getOptionId() == selId) {
						matched = o;
						break;
					}
				}
				if (matched != null) {
					String mark = matched.getCorrect() ? "✅ CORRECT" : "❌ WRONG";
					System.out.println("  Your answer: " + matched.getText() + "  " + mark);
				}
			} else {
				String given = answerForQuestion.getText() != null ? answerForQuestion.getText().trim() : "";
				System.out.println("  Your answer: " + (given.isEmpty() ? "(empty)" : given));
				if (qn.getOptions() != null && !qn.getOptions().isEmpty()) {
					String correctText = qn.getOptions().get(0).getText();
					boolean correct = correctText != null && correctText.equalsIgnoreCase(given);
					System.out.println(
							"  " + (correct ? "✅ CORRECT" : "❌ WRONG") + "  (Correct Answer: " + correctText + ")");
				}
			}
		}

		saveAllData();
	}

	void attendedParticpantDetails() {
		if (quizzes.isEmpty()) {
			System.out.println(Color.RED + "No quizzes available yet." + Color.RESET);
			return;
		}

		for (Quiz q : quizzes) {
			System.out.println(Color.BRIGHT_CYAN
					+ "\n══════════════════════════════════════════════════════════════════════════" + Color.RESET);
			System.out.println(Color.BRIGHT_YELLOW + " Quiz: " + q.getTitle() + Color.RESET);
			System.out.println(Color.BRIGHT_BLACK + q.getdescription() + Color.RESET);
			System.out.println(Color.BRIGHT_CYAN
					+ "══════════════════════════════════════════════════════════════════════════" + Color.RESET);

			String rowFormat = Color.BRIGHT_GREEN + "%-20s %-30s %7s %7s %-45s" + Color.RESET + "%n";
			System.out.printf(rowFormat, "Name", "Email", "Correct", "Wrong", "Date");

			boolean found = false;

			for (QuizParticipation qp : QPList) {
				if (qp.getQuizId() != q.getId())
					continue;
				found = true;

				String name = qp.getName() != null ? qp.getName() : "";

				String email = qp.getEmail() != null ? qp.getEmail() : "";

				String date = qp.getDate() != null ? qp.getDate() : "";

				System.out.printf(
						Color.BRIGHT_PURPLE + "%-20s " + Color.BRIGHT_CYAN + "%-30s " + Color.RESET + "%7d %7d "
								+ Color.BRIGHT_BLACK + "%-25s" + Color.RESET + "%n",
						name, email, qp.getCorrectAnswered(), qp.getWrongAnswered(), date);
			}

			if (!found)
				System.out.println(Color.RED + "No participation records yet." + Color.RESET);

			System.out.println(Color.BRIGHT_CYAN
					+ "══════════════════════════════════════════════════════════════════════════" + Color.RESET);
		}
		saveAllData();
	}

	void takeQuiz(User u) {
		String answerText = "";
		if (quizzes.isEmpty()) {
			System.out.println("No quizzes available.\n");
			return;
		}

		System.out.println("Enter your quiz code to join!");
		int enteredQuizCode = ScannerUtil.nextInt(sc, "");

		Quiz quizObj = null;
		for (Quiz q : quizzes)
			if (q.getquizCode() == enteredQuizCode) {
				quizObj = q;
				break;
			}

		if (quizObj == null) {
			System.out.println("Invalid quiz code!\n");
			return;
		}

		int quizIdForParticipation = quizObj.getId();
		int correctAnswered = 0, wrongAnswered = 0, total = 0, questionCountLocal = 0;

		List<ParticipantAnswer> answersForThisQuiz = new ArrayList<>();

		for (Question question : quizObj.getQuestions()) {
			questionCountLocal++;
			System.out.println(Color.YELLOW + "\n" + questionCountLocal + ". " + question.getText() + Color.RESET);
			int selectedOptionId = 0;
			String[] selectedArr = {};

			if (question.getType().equalsIgnoreCase("MCQ")) {
				List<Option> opts = question.getOptions();
				for (int idx = 0; idx < opts.size(); idx++) {
					Option opt = opts.get(idx);
					System.out.println("   " + (idx + 1) + ". " + opt.getText());
				}
				System.out.print(Color.BLUE + "\nEnter choice(s) (comma-separated): " + Color.RESET);
				String ans = sc.nextLine();
				if (ans == null)
					ans = "";
				selectedArr = ans.split(",");
				boolean fullCorrect = true;
				for (String s : selectedArr) {
					s = s.trim();
					if (!s.isEmpty()) {
						try {
							int idx = Integer.parseInt(s) - 1;
							if (idx >= 0 && idx < opts.size()) {
								Option selectedoption = opts.get(idx);
								if (!selectedoption.getCorrect())
									fullCorrect = false;
							} else {
								fullCorrect = false;
							}
						} catch (NumberFormatException e) {
							fullCorrect = false;
						}
					}
				}
				if (fullCorrect) {
					correctAnswered++;
					total += question.getMark();
				} else
					wrongAnswered++;
			} else if (question.getType().equalsIgnoreCase("TrueFalse")) {
				System.out.print(Color.UNDERLINE + "Enter answer:" + Color.RESET + " (1. " + Color.GREEN + "True"
						+ Color.RESET + " / 2. " + Color.RED + "False" + Color.RESET + "): ");
				int ans = ScannerUtil.nextInt(sc, "");
				sc.nextLine();
				boolean correct = (ans == 1 && question.getOptions().get(0).getCorrect())
						|| (ans == 2 && question.getOptions().get(1).getCorrect());
				if (correct) {
					correctAnswered++;
					total += question.getMark();
				} else
					wrongAnswered++;
				selectedOptionId = (ans == 1) ? question.getOptions().get(0).getOptionId()
						: question.getOptions().get(1).getOptionId();
			} else {
				System.out.print(Color.BLUE + "Enter your answer: " + Color.RESET);
				answerText = sc.nextLine();
				Option correctOp = question.getOptions().get(0);
				if (correctOp.getText().equalsIgnoreCase(answerText)) {
					correctAnswered++;
					total += question.getMark();
				} else
					wrongAnswered++;
			}

			ParticipantAnswer pa = new ParticipantAnswer(quizIdForParticipation, question.getId(), selectedArr,
					selectedOptionId, answerText);
			answersForThisQuiz.add(pa);
			u.setPartcipantAnswerList(pa);
		}

		u.setScore(total);
		System.out.println(Color.BG_GREEN + Color.BOLD + "\n===You scored: " + total + "!===" + Color.RESET);

		QuizParticipation QP = new QuizParticipation(quizIdForParticipation, u.getUserId(), u.getName(), u.getEmail(),
				total, correctAnswered, wrongAnswered, Instant.now().toString());
		QPList.add(QP);

		saveAllData();
	}

	public void saveAllData() {
		saveQuizzes();
		saveQuestions();
		saveOptions();
		saveParticipation();
		saveUsers();
		saveParticipantAnswers();
	}

	public void saveQuizzes() {
		try (CSVWriter writer = new CSVWriter(new FileWriter(quizFile))) {
			writer.writeNext(new String[] { "QuizId", "CreatorId", "Title", "Description", "Quiz code" });
			for (Quiz q : quizzes)
				writer.writeNext(new String[] { String.valueOf(q.getId()), String.valueOf(q.getCreatorId()),
						q.getTitle(), q.getdescription(), Integer.toString(q.getquizCode()) });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveQuestions() {
		try (CSVWriter writer = new CSVWriter(new FileWriter(questionFile))) {
			writer.writeNext(new String[] { "QuestionId", "QuizId", "QuestionText", "Type", "Marks" });
			for (Quiz q : quizzes)
				for (Question qs : q.getQuestions())
					writer.writeNext(new String[] { String.valueOf(qs.getId()), String.valueOf(qs.getquizId()),
							qs.getText(), qs.getType(), String.valueOf(qs.getMark()) });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveOptions() {
		try (CSVWriter writer = new CSVWriter(new FileWriter(optionFile))) {
			writer.writeNext(new String[] { "OptionId", "QuestionId", "OptionText", "IsCorrect" });
			for (Quiz q : quizzes)
				for (Question qs : q.getQuestions())
					for (Option op : qs.getOptions())
						writer.writeNext(new String[] { String.valueOf(op.getOptionId()), String.valueOf(qs.getId()),
								op.getText(), String.valueOf(op.getCorrect()) });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveParticipation() {
		try (CSVWriter writer = new CSVWriter(new FileWriter(quizparFile))) {
			writer.writeNext(new String[] { "QuizId", "UserId", "Name", "Email", "Score", "Correct", "Wrong", "Date" });
			for (QuizParticipation qp : QPList)
				writer.writeNext(new String[] { String.valueOf(qp.getQuizId()), String.valueOf(qp.getUserId()),
						qp.getName(), qp.getEmail(), String.valueOf(qp.getScore()),
						String.valueOf(qp.getCorrectAnswered()), String.valueOf(qp.getWrongAnswered()), qp.getDate() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveUsers() {
		try (CSVWriter writer = new CSVWriter(new FileWriter(userFile))) {
			writer.writeNext(new String[] { "UserId", "Role", "Name", "Email", "Password" });
			for (User u : users)
				writer.writeNext(new String[] { String.valueOf(u.getUserId()), u.getRole(), u.getName(), u.getEmail(),
						u.getPassword() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveParticipantAnswers() {
		try (CSVWriter writer = new CSVWriter(new FileWriter(participantAnswerFile))) {
			writer.writeNext(new String[] { "UserId", "QuizId", "QuestionId", "SelectedOptions", "SelectedOptionSingle",
					"AnswerText" });
			for (User u : users) {
				List<ParticipantAnswer> list = u.getPartcipantAnswerList();
				if (list == null)
					continue;
				for (ParticipantAnswer pa : list) {
					String joined = "";
					String[] arr = pa.getSelectedOptionIdArr();
					if (arr != null && arr.length > 0)
						joined = String.join("|", arr);
					writer.writeNext(new String[] { String.valueOf(u.getUserId()), String.valueOf(pa.getQuizId()),
							String.valueOf(pa.getQuestionId()), joined, String.valueOf(pa.getSelectedOptionId()),
							pa.getText() != null ? pa.getText() : "" });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadAllData() {
		loadUsers();
		loadQuizzes();
		loadQuestions();
		loadOptions();
		loadParticipation();
		loadParticipantAnswers();
	}

	public void loadUsers() {
		if (!userFile.exists())
			return;
		try (CSVReader reader = new CSVReader(new FileReader(userFile))) {
			String[] line;
			reader.readNext();
			users.clear();
			while ((line = reader.readNext()) != null) {
				if (line.length < 5)
					continue;
				User u = new User(Integer.parseInt(line[0]), line[2], line[3], line[4], line[1]);
				users.add(u);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadQuizzes() {
		if (!quizFile.exists())
			return;
		try (CSVReader reader = new CSVReader(new FileReader(quizFile))) {
			String[] line;
			reader.readNext();
			quizzes.clear();
			while ((line = reader.readNext()) != null) {
				if (line.length < 5)
					continue;
				Quiz q = new Quiz(Integer.parseInt(line[0]), Integer.parseInt(line[1]), line[2], line[3], "",
						Integer.parseInt(line[4]));
				quizzes.add(q);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadQuestions() {
		if (!questionFile.exists())
			return;
		try (CSVReader reader = new CSVReader(new FileReader(questionFile))) {
			String[] line;
			reader.readNext();
			while ((line = reader.readNext()) != null) {
				if (line.length < 5)
					continue;
				int qid = Integer.parseInt(line[0]);
				int quizId = Integer.parseInt(line[1]);
				String text = line[2];
				String type = line[3];
				int mark = Integer.parseInt(line[4]);
				Question q = new Question(qid, quizId, text, type, mark);
				for (Quiz quiz : quizzes)
					if (quiz.getId() == quizId)
						quiz.addQuestion(q);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadOptions() {
		if (!optionFile.exists())
			return;
		try (CSVReader reader = new CSVReader(new FileReader(optionFile))) {
			String[] line;
			reader.readNext();
			while ((line = reader.readNext()) != null) {
				if (line.length < 4)
					continue;
				int oid = Integer.parseInt(line[0]);
				int qid = Integer.parseInt(line[1]);
				String text = line[2];
				boolean correct = Boolean.parseBoolean(line[3]);
				for (Quiz quiz : quizzes) {
					if (quiz.getQuestions() == null)
						continue;
					for (Question q : quiz.getQuestions()) {
						if (q.getId() == qid) {
							if (q.getOptions() == null) {
								q.setOptionList(new ArrayList<Option>());
							}
							boolean exists = false;
							for (Option existing : q.getOptions()) {
								if (existing.getOptionId() == oid) {
									exists = true;
									break;
								}
							}
							if (!exists) {
								q.addOption(new Option(qid, text, correct, oid));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadParticipation() {
		if (!quizparFile.exists())
			return;
		try (CSVReader reader = new CSVReader(new FileReader(quizparFile))) {
			String[] line;
			reader.readNext();
			QPList.clear();
			while ((line = reader.readNext()) != null) {
				if (line.length < 8)
					continue;
				QuizParticipation qp = new QuizParticipation(Integer.parseInt(line[0]), Integer.parseInt(line[1]),
						line[2], line[3], Integer.parseInt(line[4]), Integer.parseInt(line[5]),
						Integer.parseInt(line[6]), line[7]);
				QPList.add(qp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadParticipantAnswers() {
		if (!participantAnswerFile.exists())
			return;
		try (CSVReader reader = new CSVReader(new FileReader(participantAnswerFile))) {
			String[] line;
			reader.readNext();
			while ((line = reader.readNext()) != null) {
				if (line.length < 6)
					continue;
				int userId = Integer.parseInt(line[0]);
				int quizId = Integer.parseInt(line[1]);
				int questionId = Integer.parseInt(line[2]);
				String joined = line[3];
				String singleStr = line[4];
				String ansText = line[5];

				String[] selectedArr = joined == null || joined.isEmpty() ? new String[0] : joined.split("\\|");
				int singleSelected = 0;
				try {
					singleSelected = Integer.parseInt(singleStr);
				} catch (Exception e) {
					singleSelected = 0;
				}

				ParticipantAnswer pa = new ParticipantAnswer(quizId, questionId, selectedArr, singleSelected,
						ansText != null ? ansText : "");
				User target = null;
				for (User u : users)
					if (u.getUserId() == userId) {
						target = u;
						break;
					}
				if (target != null) {
					target.setPartcipantAnswerList(pa);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
