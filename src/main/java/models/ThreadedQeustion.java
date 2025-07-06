package models;

public class ThreadedQeustion extends Question{
    private int parentId;
    public ThreadedQeustion(int id, String content, boolean isAnonymous, int isAnswered , int parentId){
        super(id,content,isAnonymous,isAnswered);
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return super.toString()+";"+parentId;
    }

}
