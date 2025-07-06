package models;

public class Question {
    private int id;
    private String content;
    private boolean isAnonymous;
    private int isAnswered;
    public Question(int id, String content, boolean isAnonymous, int isAnswered) {
        this.id = id;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.isAnswered = isAnswered;
    }
    public int getId() {
        return id;
    }
    public void setIsAnswered(int isAnswered) {
        this.isAnswered = isAnswered;
    }
    @Override
    public String toString(){
        return  id+";"+content+";"+isAnonymous+";"+isAnswered;
    }

}
