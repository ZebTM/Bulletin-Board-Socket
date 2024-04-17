package com.networking.group.Classes.Server;

import java.io.DataOutputStream;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.networking.group.Server.WebServer;

public class Group {
    final static String CRLF = "\r\n";
    public String Name;
    private ConcurrentHashMap<String, User> Users;
    private ConcurrentHashMap<UUID, Message> SentMessages;
    private Message secondToLastMessage;
    private Message lastMessage;
    public Integer Id;

    public Group(String name, Integer id) {
        Name = name;
        Id = id;
        SentMessages = new ConcurrentHashMap<UUID, Message>();
        Users = new ConcurrentHashMap<String, User>();
    }

    public void AddUser(User user) {
        Users.put(user.UserID, user);
    }

    public Boolean IsUserInGroup(String userId) {
        return Users.get(userId) != null;
    }

    public void RemoveUser(String userId)
    {
        if (IsUserInGroup(userId)) {
            Users.remove(userId);

            SendStatusMessageToUsers("User: " + userId + " has left the group " + this.Name + "\n" + CRLF);
        }   
    }
    
    public void AddMessage(Message message) {
        // Add Message to stack
        secondToLastMessage = lastMessage;
        lastMessage = message;
        SentMessages.put(message.Id, message);
        WebServer.messagesToGroupId.put(message.Id, this.Id);
        // Send Message to Users
        SendMessageToUsers(message);
    }

    public Message GetMessage(UUID id) {
        return SentMessages.get(id);
    }

    public void SendMessageToUsers(Message message) {
        System.out.println(message);
        for (User user : Users.values()) {
            user.PublishMessageToUser(message);
            
        }
    }

    public void SendStatusMessageToUsers(String status) {
        System.out.println(status);
        for (User user : Users.values()) {
            user.PublishStatusToUser(status);
            
        }
    }
    
    public Collection<User> GetAllUsers() {
        return Users.values();
    }

    public void SendLastTwoMessagesToUser(String userId) {
        if (secondToLastMessage != null) {
            Users.get(userId).PublishMessageToUser(secondToLastMessage);
        }
        if (lastMessage != null) {
            Users.get(userId).PublishMessageToUser(lastMessage);
        }
        
       
    }

    public Collection<Message> GetAllMessages() {
        return SentMessages.values();
    }
}
