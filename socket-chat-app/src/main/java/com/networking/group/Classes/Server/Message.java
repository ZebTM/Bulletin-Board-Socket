package com.networking.group.Classes.Server;

import java.util.Date;
import java.util.UUID;

public class Message {
    public UUID Id;
    public String Subject;
    public String Message;
    public Date Time; 
    public User Sender;

    public Message(UUID id, String subject, String message, User sender) {
        Id = id;
        Subject = subject;
        Message = message;
        Time = new Date();
        Sender = sender;
    }

    public String ConvertMessageToChatMessage() {
       
        return "ID: " + this.Id +  "\nSender: " + this.Sender.UserID + "\n" + "Subject: " + this.Subject + "\n" + "Sent Time: " + Time.toString() + "\n";
    }

    public String ConvertMessageToRequestMessage() {
        return "ID: " + this.Id + "\nSubject: " + this.Subject + "\nSender: " + this.Sender.UserID + "\n" + "Sent Time: " + Time.toString() + "\n" + "Message: \n" + this.Message;
    }
}
