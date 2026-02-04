package Model;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int id;
    private String text;
    private String type;
    private int mark;
    private int quizID;
    private List<Option> optionList = new ArrayList<>();

    public Question(int id, int quizId, String text, String type, int mark) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.mark = mark;
    }

    public void addOption(Option option) {
        optionList.add(option);
    }

    public List<Option> getOptions() {
        return optionList;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }
    
    public int getMark() {
        return mark;
    }

    public int getId() {
        return id;
    }

    public int getquizId() {
        return quizID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }
  
}
