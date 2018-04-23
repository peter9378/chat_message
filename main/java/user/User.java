package user;

public class User {

    String id = "";
    String name = "";
    String password = "";
    String status = "online";
    int index = -1;

    public User(){ }

    // initialize constructor
    public User(String id, String name, String status, int index){
        new User(id, name, "", status, index);
    }

    // initialize constructor
    public User(String id, String name, String password, String status, int index){
        this.id = id;
        this.name = name;
        this.password = password;
        this.status = status;
        this.index = index;
    }

    // getter/setter

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public String getStatus(){return this.status;};

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

    public void setStatus(String status) {this.status = status;}

    public void setIndex(int index){
        this.index = index;
    }
}
