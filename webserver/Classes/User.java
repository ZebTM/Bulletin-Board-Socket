package webserver.Classes;

import java.net.Socket;
import java.util.List;

public class User {
    public String UserID;
    public List<Groups> Groups;
    public Socket connectionSocket;

    public void PublishMessage(String message) {
        // User will write message 
    }
}
