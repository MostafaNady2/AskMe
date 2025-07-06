package models;

public class Answer {
    private int id;
    private String content;
    public Answer(int id, String content) {
        this.id = id;
        this.content = content;
    }
    public void showAnswer() {
        System.out.println("Answer : "+this.content);
    }
    public int getId() {
        return id;
    }
    public String toString(){
        return id+";"+content;
    }
}
