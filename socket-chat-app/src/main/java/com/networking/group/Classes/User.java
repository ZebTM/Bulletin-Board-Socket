package com.networking.group.Classes;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class User {
    public String UserID;
    public Socket connectionSocket;
    final static String CRLF = "\r\n";

    public void PublishMessageToUser(Message message) {
        // User will write message 
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;


        statusLine = "HTTP/1.1 200 OK" + CRLF;
        contentTypeLine = "Content-type: " + 
            "application/json" + CRLF;
        
        entityBody = "{}";

        
        try {
            DataOutputStream os = new DataOutputStream( connectionSocket.getOutputStream() );
            os.writeBytes(statusLine);
            os.writeBytes(contentTypeLine);
            os.writeBytes(CRLF);
            os.writeBytes(entityBody);
        } catch ( IOException exception) {
            System.out.println(exception);
        }
    }
}
