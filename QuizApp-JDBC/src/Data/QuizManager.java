package Data;

import java.time.Instant;
import java.util.*;

import Model.*;
import Color.Color;
import Util.ScannerUtil;
import Service.*;

public class QuizManager {
	private List<Quiz> quizzes = new ArrayList<>();
	private List<User> users = new ArrayList<>();
	private List<QuizParticipation> QPList = new ArrayList<>();
	private List<ParticipantAnswer> paList = new ArrayList<>();
	private Scanner sc = new Scanner(System.in);

	private QuizService quizService = new QuizService();
	private QuestionService questionService = new QuestionService();
	private OptionService optionService = new OptionService();
	private ParticipantAnswerService participantAnswerService = new ParticipantAnswerService();
	private QuizParticipationservice quizParticipationService = new QuizParticipationservice();
	private UserService uservice = new UserService();

	public QuizManager() {
		quizzes = quizService.getAll();
		users = uservice.getAll();
		QPList = quizParticipationService.getAll();
		paList = participantAnswerService.getAll();

		List<Question> allQuestions = questionService.getAll();
		List<Option> allOptions = optionService.getAll();

		for (Quiz q : quizzes) {
			List<Question> qList = new ArrayList<>();
			for (Question qu : allQuestions) {
				if (qu.getquizId() == q.getId()) {
					List<Option> opList = new ArrayList<>();
					for (Option op : allOptions) {
						if (op.getQuestionId() == qu.getId()) {
							opList.add(op);
						}
					}
					qu.setOptionList(opList);
					qList.add(qu);
				}
			}
			q.setQuestionList(qList);
		}

		for (User u : users) {
			for (ParticipantAnswer pa : paList) {
				if (pa.getUserId() == u.getUserId()) {
					u.setPartcipantAnswerList(pa);
				}
			}
		}
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
			System.out.println(Color.BOLD + "1. Add Quiz\n2. Participation details\n3. View created quizzes\n4. Exit" + Color.RESET);
			System.out.print(Color.PURPLE + "\nEnter your choice:" + Color.RESET);
			int ch = ScannerUtil.nextInt(sc, "");
			if (ch == 1)
				addQuiz(u);
			else if (ch == 2)
				attendedParticipantDetails();
			else if (ch == 3)
				viewCreatedQuizzes(u);
			else
				break;
		}
	}

	public void attendorMenu(User u) {
		while (true) {
			System.out.println(Color.CYAN + "\n=== Attendor ===\n" + Color.RESET);
			System.out.println(Color.BOLD + "1.Join quiz\n2. View History\n3. Exit" + Color.RESET);
			System.out.print(Color.PURPLE + "Enter your choice:" + Color.RESET);
			int ch = ScannerUtil.nextInt(sc, "");
			if (ch == 1)
				takeQuiz(u);
			else if (ch == 2)
				participantHistory(u);
			else
				break;
		}
	}

	public void addQuiz(User us) {
		System.out.print("\nEnter quiz title: ");
		String title = sc.nextLine();
		System.out.print("Enter description: ");
		String desc = sc.nextLine();

		int quizCode = (int) (Math.random() * (999999 - 100000 + 1) + 100000);
		Quiz q = new Quiz(0, us.getUserId(), title, desc, us.getName(), quizCode);
		int quizId = quizService.save(q);
		System.out.println(quizId);
		q.setId(quizId);

		while (true) {
			System.out.print("\nEnter question type (1. MCQ / 2. TrueFalse / 3. Text): ");
			int typeop = ScannerUtil.nextInt(sc, "");
			String questionType = (typeop == 1) ? "MCQ" : (typeop == 2) ? "TrueFalse" : "Text";
			System.out.print("\nEnter question: ");
			String questionText = ScannerUtil.nextLine(sc, "");
			System.out.print("\nEnter marks: ");
			int questionMark = ScannerUtil.nextInt(sc, "");

			Question ques = new Question(0, quizId, questionText, questionType, questionMark);
			System.out.println(ques.getquizId());
			int questionId = questionService.save(ques);
			ques.setId(questionId);

			if (questionType.equalsIgnoreCase("MCQ")) {
				boolean hasCorrect = false;
				while (!hasCorrect) {
					ques.getOptions().clear();
					for (int i = 1; i <= 4; i++) {
						System.out.print("\nEnter option " + i + ": ");
						String optText = sc.nextLine();
						int op = ScannerUtil.nextInt(sc, "Is this option correct? (1.YES / 2.NO): ");
						boolean isCorrect = (op == 1);
						if (isCorrect)
							hasCorrect = true;

						Option option = new Option(questionId, optText, isCorrect, 0);
						int optionId = optionService.save(option);
						option.setOptionId(optionId);
						ques.addOption(option);
					}
					if (!hasCorrect)
						System.out.println("\nAt least one option must be correct!\n");
				}
			} else if (questionType.equalsIgnoreCase("TrueFalse")) {
				System.out.print("Enter correct answer (1.True / 2.False): ");
				int cor = ScannerUtil.nextInt(sc, "");
				boolean ans = (cor == 1);

				Option trueOption = new Option(questionId, "true", ans, 0);
				trueOption.setOptionId(optionService.save(trueOption));
				Option falseOption = new Option(questionId, "false", !ans, 0);
				falseOption.setOptionId(optionService.save(falseOption));
				ques.addOption(trueOption);
				ques.addOption(falseOption);
				sc.nextLine();
			} else {
				System.out.print("Enter correct answer text: ");
				String text = sc.nextLine();
				Option textOption = new Option(questionId, text, true, 0);
				textOption.setOptionId(optionService.save(textOption));
				ques.addOption(textOption);
			}

			q.addQuestion(ques);
			System.out.print("\nWant to add another question? (1. yes / 2. no): ");
			int cont = sc.nextInt();
			sc.nextLine();
			if (cont != 1)
				break;
		}

		quizzes.add(q);
		System.out.println("Your Quiz Code is " + quizCode);
		System.out.println("\nQuiz saved.\n");
	}

	public void viewCreatedQuizzes(User u) {
		List<Quiz> userQuizzes = new ArrayList<>();
		for (Quiz qz : quizzes)
			if (qz.getCreatorId() == u.getUserId())
				userQuizzes.add(qz);

		if (userQuizzes.isEmpty()) {
			System.out.println(Color.RED + "You have not created any quizzes yet." + Color.RESET);
			return;
		}

		System.out.println(Color.CYAN + "Your Created Quizzes:" + Color.RESET);
		for (int i = 0; i < userQuizzes.size(); i++)
			System.out.println((i + 1) + ". " + userQuizzes.get(i).getTitle());

		int selectedQuiz = ScannerUtil.nextInt(sc, "Select quiz number: ");
		if (selectedQuiz < 1 || selectedQuiz > userQuizzes.size()) {
			System.out.println("Invalid choice");
			return;
		}

		Quiz q = userQuizzes.get(selectedQuiz - 1);

		while (true) {
			int opt = ScannerUtil.nextInt(sc, "\n1.View Quiz\n2.Update Quiz\n3.Delete Quiz\n4.Back\nChoose option: ");

			if (opt == 1) {
				while (true) {
					if (q.getQuestions().isEmpty()) {
						System.out.println(Color.YELLOW + "No questions available in this quiz." + Color.RESET);
					} else {
						System.out.println(Color.CYAN + "Questions:" + Color.RESET);
						for (int i = 0; i < q.getQuestions().size(); i++)
							System.out.println((i + 1) + ". " + q.getQuestions().get(i).getText());
					}

					int qOpt = ScannerUtil.nextInt(sc, "\n1.View Questions\n2.Update Question\n3.Delete Question\n4.Add extra question5.Back\nChoose option: ");

					if (qOpt == 1) {
						for (Question qu : q.getQuestions()) {
							System.out.println(Color.GREEN + qu.getText() + Color.RESET);
							int i = 1;
							for (Option op : qu.getOptions()) {
								if (op.isCorrect())
									System.out.println("   " + i++ + ". " + op.getText() + " ✅");
								else
									System.out.println("   " + i++ + ". " + op.getText());
							}
						}
					} else if (qOpt == 2) {
						if (q.getQuestions().isEmpty())
							continue;

						int qnNum = ScannerUtil.nextInt(sc, "Select question number to update: ");
						if (qnNum < 1 || qnNum > q.getQuestions().size())
							continue;

						Question qu = q.getQuestions().get(qnNum - 1);

						sc.nextLine();
						System.out.print("Enter new question text: ");
						qu.setText(sc.nextLine());

						System.out.print("Enter question type (1.MCQ / 2.TrueFalse / 3.Text): ");
						int typeop = ScannerUtil.nextInt(sc, "");
						String questionType = (typeop == 1) ? "MCQ" : (typeop == 2) ? "TrueFalse" : "Text";
						qu.setType(questionType);

						System.out.print("Enter marks: ");
						qu.setMark(ScannerUtil.nextInt(sc, ""));

						questionService.update(qu);

						for (Option oldOp : qu.getOptions())
							optionService.delete(oldOp);
						qu.getOptions().clear();

						if (questionType.equalsIgnoreCase("MCQ")) {
							boolean hasCorrect = false;
							while (!hasCorrect) {
								qu.getOptions().clear();

								for (int i = 1; i <= 4; i++) {
									System.out.print("Enter option " + i + ": ");
									String optText = sc.nextLine();

									int op = ScannerUtil.nextInt(sc, "Is this option correct? (1.YES / 2.NO): ");
									boolean isCorrect = (op == 1);
									if (isCorrect)
										hasCorrect = true;

									Option option = new Option(qu.getId(), optText, isCorrect, 0);
									option.setOptionId(optionService.save(option));
									qu.addOption(option);
								}

								if (!hasCorrect)
									System.out.println("At least one option must be correct!");
							}
						} else if (questionType.equalsIgnoreCase("TrueFalse")) {
							System.out.print("Enter correct answer (1.True / 2.False): ");
							int cor = ScannerUtil.nextInt(sc, "");
							boolean ans = (cor == 1);

							Option trueOp = new Option(qu.getId(), "true", ans, 0);
							trueOp.setOptionId(optionService.save(trueOp));

							Option falseOp = new Option(qu.getId(), "false", !ans, 0);
							falseOp.setOptionId(optionService.save(falseOp));

							qu.addOption(trueOp);
							qu.addOption(falseOp);
						} else {
							sc.nextLine();
							System.out.print("Enter correct answer text: ");
							String text = sc.nextLine();

							Option textOp = new Option(qu.getId(), text, true, 0);
							textOp.setOptionId(optionService.save(textOp));
							qu.addOption(textOp);
						}

						System.out.println(Color.GREEN + "Question updated successfully." + Color.RESET);
					} else {
						break;
					}
				}
			} else if (opt == 2) {
				sc.nextLine();
				System.out.print("Enter new quiz title: ");
				q.setTitle(sc.nextLine());
				System.out.print("Enter new quiz description: ");
				q.setDescription(sc.nextLine());
				quizService.update(q);
				System.out.println(Color.GREEN + "Quiz updated successfully." + Color.RESET);
			} else if (opt == 3) {
				sc.nextLine();
				System.out.print(Color.RED + "Are you sure you want to delete this quiz? (yes/no): " + Color.RESET);
				String confirm = sc.nextLine();

				if (!confirm.equalsIgnoreCase("yes"))
					continue;

				for (Question qu : q.getQuestions()) {
					for (Option op : qu.getOptions())
						optionService.delete(op);
					questionService.delete(qu);
				}

				quizService.delete(q);
				quizzes.remove(q);
				System.out.println(Color.GREEN + "Quiz deleted successfully." + Color.RESET);
				break;
			} else {
				break;
			}
		}
	}

	public void participantHistory(User u) {
		List<ParticipantAnswer> palist = u.getPartcipantAnswerList();
		if (palist == null || palist.isEmpty()) {
			System.out.println(Color.RED + "No quizzes participated." + Color.RESET);
			return;
		}

		Set<Integer> attendedQuizIds = new HashSet<>();
		for (ParticipantAnswer pa : palist) {
			attendedQuizIds.add(pa.getQuizId());
		}

		List<Quiz> attendedQuiz = new ArrayList<>();
		for (Quiz quiz : quizzes) {
			if (attendedQuizIds.contains(quiz.getId())) {
				attendedQuiz.add(quiz);
			}
		}

		System.out.println(Color.GREEN + "=== Attended Quizzes ===" + Color.RESET);
		for (int i = 0; i < attendedQuiz.size(); i++)
			System.out.println((i + 1) + ". " + attendedQuiz.get(i).getTitle());

		System.out.print("\nChoose quiz for full details: ");
		int quizChoice = ScannerUtil.nextInt(sc, "");
		if (quizChoice < 1 || quizChoice > attendedQuiz.size()) {
			System.out.println(Color.RED + "Invalid choice" + Color.RESET);
			return;
		}

		Quiz selectedQuiz = attendedQuiz.get(quizChoice - 1);
		System.out.println(Color.PURPLE + "\nYou selected : " + Color.RESET + selectedQuiz.getTitle() + "\n");

		for (Question qn : selectedQuiz.getQuestions()) {
			System.out.println(Color.CYAN + qn.getText() + Color.RESET);

			ParticipantAnswer ans = null;
			for (ParticipantAnswer pa : palist) {
				if (pa.getQuizId() == selectedQuiz.getId() && pa.getQuestionId() == qn.getId()) {
					ans = pa;
					break;
				}
			}

			if (ans == null) {
				System.out.println(Color.YELLOW + "  No answer provided." + Color.RESET);
				continue;
			}

			String type = qn.getType() == null ? "" : qn.getType().trim().toLowerCase();

			if (type.equals("mcq")) {
				String[] selArr = ans.getSelectedOptionIdArr();
				if (selArr == null || selArr.length == 0) {
					System.out.println(Color.YELLOW + "  No option selected." + Color.RESET);
				} else {
					List<String> correctOptions = new ArrayList<>();
					for (Option opt : qn.getOptions())
						if (opt.getCorrect())
							correctOptions.add(opt.getText());

					for (String s : selArr) {
						if (s == null || s.trim().isEmpty())
							continue;
						try {
							int selId = Integer.parseInt(s.trim());
							for (Option o : qn.getOptions()) {
								if (o.getOptionId() == selId) {
									System.out.println("  Your answer: " + o.getText() + "  " + (o.getCorrect() ? "✅ CORRECT" : "❌ WRONG"));
									break;
								}
							}
						} catch (Exception e) {
							System.out.println("  Your answer: invalid");
						}
					}

					if (!correctOptions.isEmpty())
						System.out.println("  Correct option(s): " + String.join(", ", correctOptions));
				}
			} else if (type.equals("truefalse") || type.equals("true") || type.equals("false")) {
				int selId = ans.getSelectedOptionId();
				for (Option o : qn.getOptions()) {
					if (o.getOptionId() == selId) {
						System.out.println("  Your answer: " + o.getText() + "  " + (o.getCorrect() ? "✅ CORRECT" : "❌ WRONG"));
						break;
					}
				}
			} else {
				String given = ans.getText() == null ? "" : ans.getText().trim();
				System.out.println("  Your answer: " + (given.isEmpty() ? "(empty)" : given));
				if (!qn.getOptions().isEmpty()) {
					String correctText = qn.getOptions().get(0).getText();
					System.out.println("  " + (correctText.equalsIgnoreCase(given) ? "✅ CORRECT" : "❌ WRONG") + "  (Correct Answer: " + correctText + ")");
				}
			}
		}
	}

	public void attendedParticipantDetails() {
		if (quizzes.isEmpty()) {
			System.out.println(Color.RED + "No quizzes available yet." + Color.RESET);
			return;
		}

		for (Quiz q : quizzes) {
			System.out.println(Color.BRIGHT_CYAN + "\n══════════════════════════════════════════════════════════════════════════" + Color.RESET);
			System.out.println(Color.BRIGHT_YELLOW + " Quiz: " + q.getTitle() + Color.RESET);
			System.out.println(Color.BRIGHT_BLACK + q.getdescription() + Color.RESET);
			System.out.println(Color.BRIGHT_CYAN + "══════════════════════════════════════════════════════════════════════════" + Color.RESET);

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

				System.out.printf(Color.BRIGHT_PURPLE + "%-20s " + Color.BRIGHT_CYAN + "%-30s " + Color.RESET + "%7d %7d " + Color.BRIGHT_BLACK + "%-25s" + Color.RESET + "%n",
						name, email, qp.getCorrectAnswered(), qp.getWrongAnswered(), date);
			}

			if (!found)
				System.out.println(Color.RED + "No participation records yet." + Color.RESET);

			System.out.println(Color.BRIGHT_CYAN + "══════════════════════════════════════════════════════════════════════════" + Color.RESET);
		}
	}

	public void takeQuiz(User u) {
		if (quizzes.isEmpty()) {
			System.out.println(Color.RED + "No quizzes available." + Color.RESET);
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
		int correctAnswered = 0, wrongAnswered = 0, total = 0;

		for (Question question : quizObj.getQuestions()) {
			System.out.println(Color.YELLOW + "\n" + question.getText() + Color.RESET);

			String[] selectedArr = {};
			String answerText = "";
			int selectedOptionId = 0;

			if (question.getType().equalsIgnoreCase("MCQ")) {
				List<Option> opts = question.getOptions();
				for (int idx = 0; idx < opts.size(); idx++)
					System.out.println("   " + (idx + 1) + ". " + opts.get(idx).getText());

				System.out.print(Color.BLUE + "\nEnter choice(s) (comma-separated): " + Color.RESET);
				String anss = sc.nextLine();
				if (anss == null)
					anss = "";
				selectedArr = anss.split(",");

				Set<Integer> correctOptionIds = new HashSet<>();
				Set<Integer> selectedOptionIds = new HashSet<>();

				for (Option opt : opts) {
					if (opt.getCorrect()) {
						correctOptionIds.add(opt.getOptionId());
					}
				}

				for (String s : selectedArr) {
					s = s.trim();
					if (!s.isEmpty()) {
						try {
							int idx = Integer.parseInt(s) - 1;
							if (idx >= 0 && idx < opts.size()) {
								selectedOptionIds.add(opts.get(idx).getOptionId());
							}
						} catch (NumberFormatException e) {
						}
					}
				}

				boolean fullCorrect = correctOptionIds.equals(selectedOptionIds);

				if (fullCorrect) {
					correctAnswered++;
					total += question.getMark();
				} else {
					wrongAnswered++;
				}

				for (String s : selectedArr) {
					if (s == null || s.trim().isEmpty())
						continue;
					try {
						int idx = Integer.parseInt(s.trim()) - 1;
						if (idx >= 0 && idx < opts.size()) {
							int realOptionId = opts.get(idx).getOptionId();
							ParticipantAnswer pa = new ParticipantAnswer(u.getUserId(), quizIdForParticipation, question.getId(), selectedArr, realOptionId, answerText);
							participantAnswerService.save(pa);
							u.setPartcipantAnswerList(pa);
							paList.add(pa);
						}
					} catch (NumberFormatException e) {
					}
				}

			} else if (question.getType().equalsIgnoreCase("TrueFalse")) {
				System.out.print(Color.UNDERLINE + "Enter answer:" + Color.RESET + " (1. " + Color.GREEN + "True" + Color.RESET + " / 2. " + Color.RED + "False" + Color.RESET + "): ");
				int ans = ScannerUtil.nextInt(sc, "");
				sc.nextLine();

				selectedOptionId = (ans == 1) ? question.getOptions().get(0).getOptionId() : question.getOptions().get(1).getOptionId();
				boolean correct = (ans == 1 && question.getOptions().get(0).getCorrect()) || (ans == 2 && question.getOptions().get(1).getCorrect());

				if (correct) {
					correctAnswered++;
					total += question.getMark();
				} else {
					wrongAnswered++;
				}

				ParticipantAnswer pa = new ParticipantAnswer(u.getUserId(), quizIdForParticipation, question.getId(), new String[]{String.valueOf(selectedOptionId)}, selectedOptionId, answerText);
				participantAnswerService.save(pa);
				u.setPartcipantAnswerList(pa);
				paList.add(pa);

			} else {
				System.out.print(Color.BLUE + "Enter your answer: " + Color.RESET);
				answerText = sc.nextLine();
				Option correctOp = question.getOptions().get(0);
				if (correctOp.getText().equalsIgnoreCase(answerText)) {
					correctAnswered++;
					total += question.getMark();
				} else {
					wrongAnswered++;
				}

				ParticipantAnswer pa = new ParticipantAnswer(u.getUserId(), quizIdForParticipation, question.getId(), new String[]{}, 0, answerText);
				participantAnswerService.save(pa);
				u.setPartcipantAnswerList(pa);
				paList.add(pa);
			}
		}

		u.setScore(total);
		System.out.println(Color.BG_GREEN + Color.BOLD + "\n===You scored: " + total + "!===" + Color.RESET);

		QuizParticipation QP = new QuizParticipation(quizIdForParticipation, u.getUserId(), u.getName(), u.getEmail(), total, correctAnswered, wrongAnswered, Instant.now().toString());
		QPList.add(QP);
		quizParticipationService.save(QP);
	}
}