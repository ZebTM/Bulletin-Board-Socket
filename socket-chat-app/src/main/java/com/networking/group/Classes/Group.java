package com.networking.group.Classes;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Group {
    public String Name;
    private ConcurrentHashMap<String, User> Users;
    private ConcurrentHashMap<UUID, Message> SentMessages;

    public Group(String name) {
        Name = name;
        SentMessages = new ConcurrentHashMap<UUID, Message>();
        Users = new ConcurrentHashMap<String, User>();
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
