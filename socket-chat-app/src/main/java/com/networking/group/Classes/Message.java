package com.networking.group.Classes;

import java.util.Date;
import java.util.UUID;
import com.google.gson.*;

public class Message {
    public UUID Id;
    public String Subject;
    public String Message;
    public Date Time; 

    public Message(UUID id, String subject, String message) {
        Id = id;
        Subject = subject;
        Message = message;
        Time = new Date();
    }

    public String ConvertMessageToJson(Message message) {
        Gson gson = new Gson();
        return gson.toJson(message);
    }
}
