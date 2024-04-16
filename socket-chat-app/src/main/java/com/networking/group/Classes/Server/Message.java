package com.networking.group.Classes.Server;

import java.util.Date;
import java.util.UUID;
import com.google.gson.*;

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
       
        return "ID: " + this.Id + "\nSubject: " + this.Subject + "\nSender: " + this.Sender.UserID + "\n";
    }
}