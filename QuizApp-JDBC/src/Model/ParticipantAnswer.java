package Model;

public class ParticipantAnswer {
	private int quizId;
	private int selectedOptionId;
	private String text;
	private String[] selectedOptionIdArr;
	private int questionId;
	private int userId;

	public ParticipantAnswer(int userId,int quizId, int questionId, String[] selectedOptionIdArr, int selectedOptionId,
			String text) {
		this.userId= userId;
		this.selectedOptionId = selectedOptionId;
		this.quizId = quizId;
		this.selectedOptionIdArr = selectedOptionIdArr;
		this.text = text;
		this.questionId = questionId;

	}

	

	public String[] getSelectedOptionIdArr() {
		return selectedOptionIdArr;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setSelectedOptionIdArr(String[] selectedOptionIdArr) {
		this.selectedOptionIdArr = selectedOptionIdArr;
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public int getSelectedOptionId() {
		return selectedOptionId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setSelectedOptionId(int selectedOptionId) {
		this.selectedOptionId = selectedOptionId;
	}

}
