package com.networking.group.Classes;

import java.util.Date;
import java.util.UUID;
import com.google.gson.*;

public class Message {
    public UUID Id;
    public String Message;
    public Date Time; 

    public Message(UUID id, String message) {
        Id = id;
        Message = message;
        Time = new Date();
    }

    public String ConvertMessageToJson(Message message) {
        Gson gson = new Gson();
        return gson.toJson(message);
    }
}
