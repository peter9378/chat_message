package message;

import java.sql.Timestamp;

public class Message {
    int index = -1;
    String text = "";
    Timestamp timestamp;

    public Message(){

    }

    public Message(int index, String text, Timestamp timeStamp){
        this.index = index;
        this.text = text;
        this.timestamp = timeStamp;
    }

    public String getText() {
        return text;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
