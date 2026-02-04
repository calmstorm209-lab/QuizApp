package Model;

public class Option {
    private String text;
    private boolean isCorrect;
    private int optionId;
   
    

    public int getOptionId() {
		return optionId;
	}

	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public Option(int questionId, String text, boolean isCorrect,int optionId ) {
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public boolean getCorrect() {
        return isCorrect;
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
