package com.networking.group.Classes.Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class User {
    public String UserID;
    public Socket connectionSocket;
    public DataOutputStream clientWriter;
    final static String CRLF = "\r\n";

    public void PublishMessageToUser(Message message) {
        String convertedMessage = message.ConvertMessageToChatMessage();
        try {
            clientWriter.writeBytes(convertedMessage + CRLF);
        } catch ( IOException exception) {
            System.out.println(exception);
        }
    }

    public void PublishStatusToUser(String message) {
        try {
            clientWriter.writeBytes(message + CRLF);
        } catch ( IOException exception) {
            System.out.println(exception);
        }
    }
}
