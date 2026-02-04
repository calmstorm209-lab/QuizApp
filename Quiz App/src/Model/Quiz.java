package Model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private int id;
    private String title;
    private String description;
    private List<Question> questionList = new ArrayList<>();
    private int creatorId;
    private String author;
    private int quizCode;
    private String CreatorName;
    private boolean attended;

    public Quiz(int id, int creatord, String title, String description, String creatorName, int quizCode) {
        this.id = id;
        this.title = title;
        this.description = description;
       this.CreatorName= creatorName;
       this.quizCode= quizCode;
//       this.creatorId= creatorId;
    }

    public void addQuestion(Question question) {
        questionList.add(question);
    }
    

    public void setAttended(boolean el) {
        attended= el;
    }
    public boolean getAttended() {
    	return attended;
    }

    public List<Question> getQuestions() {
        return questionList;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getdescription() {
        return description;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public int getquizCode() {
        return quizCode;
    }

    public String getAuthor() {
        return author;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public void setCreatorId(int userId) {
        this.creatorId = userId;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setQuizCode(int quizCode) {
        this.quizCode = quizCode;
    }
    public void setcreatorName(String Creator) {
    	this.CreatorName=Creator;
    }
    public String getcreatorName() {
    	return CreatorName;
    }
}
