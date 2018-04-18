package user;

enum Status {online, offline, busy};

public class User {

    String id = "";
    String name = "";
    String password = "";
    Status status = Status.online;
    int index = -1;

    public User(){

    }

    public User(String id, String name, int index){
        new User(id, name, "", index);
    }

    public User(String id, String name, String password, int index){
        this.id = id;
        this.name = name;
        this.password = password;
        this.index = index;
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public Status getStatus(){return this.status;};

    public int getIndex(){
        return this.index;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setStatus(Status status) {this.status = status;}

    public void setIndex(int index){
        this.index = index;
    }
}
