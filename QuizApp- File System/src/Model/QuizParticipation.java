package Model;

public class QuizParticipation {
	private int userId;
	private String name;
	private String email;
	private int correctAnswered;
	private int wrongAnswered;
	private int score;
	private String date;
	private int quizId;
//    private List<ParticipantAnswer> = new ArrayList<>();

	public QuizParticipation(int quizId, int userId, String name, String email, int score,  int correctAnswered, int wrongAnswered, String date) {
		this.quizId= quizId;
		this.userId= userId;
		this.name = name;
		this.email = email;
		this.correctAnswered = correctAnswered;
		this.wrongAnswered = wrongAnswered;
		this.date = date;
		this.score= score;
	}

	public int getQuizId() {
		return quizId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setQuizId(int quizId) {
		this.quizId =quizId;
	}

	
	public int getUserId() {
		return userId;
	}

	public void setUSerId(int userId) {
		this.userId =userId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getCorrectAnswered() {
		return correctAnswered;
	}

	public void setCorrectAnswered(int correctAnswered) {
		this.correctAnswered = correctAnswered;
	}

	public int getWrongAnswered() {
		return wrongAnswered;
	}

	public void setWrongAnswered(int wrongAnswered) {
		this.wrongAnswered = wrongAnswered;
	}
	public void setDate(String date) {
		this.date= date;
	}
	public String getDate() {
		return date;
	}
	


}
