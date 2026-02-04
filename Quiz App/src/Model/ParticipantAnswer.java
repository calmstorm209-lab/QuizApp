package Model;

public class ParticipantAnswer {
	private int quizId;
	private int selectedOptionId;
	public ParticipantAnswer( int quizId, int selectedOptionId) {
		super();
		this.selectedOptionId = selectedOptionId;
				this.quizId= quizId;
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

	


	public void setSelectedOptionId(int selectedOptionId) {
		this.selectedOptionId = selectedOptionId;
	}




}
