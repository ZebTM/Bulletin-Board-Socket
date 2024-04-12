package com.networking.group.Classes;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Group {
    public String Name;
    private HashMap<String, User> Users;
    private HashMap<UUID, Message> SentMessages;

    public Group(String name) {
        Name = name;
        SentMessages = new HashMap<UUID, Message>();
    }

    public void AddUser(User user) {
        Users.put(user.UserID, user);
    }

    public void RemoveUser(String userId)
    {
        Users.remove(userId);
    }
    
    public void AddMessage(Message message) {
        // Add Message to stack
        SentMessages.put(message.Id, message);

        // Send Message to Users
        SendMessageToUsers(message);
    }

    public Message GetMessage(UUID id) {
        return SentMessages.get(id);
    }

    public void SendMessageToUsers(Message message) {
        for (User user : Users.values()) {
            user.PublishMessageToUser(message);
        }
    }
}
