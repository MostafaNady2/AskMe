package models;

public class User {
    private int id;
    private String name;
    private String email;
    private String username;
    private String password;
    public User(int id, String name, String email, String username, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String toString(){
        return  id+";"+name+";"+email+";"+username+";"+password;
    }
}
