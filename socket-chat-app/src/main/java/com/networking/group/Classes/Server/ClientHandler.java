package com.networking.group.Classes.Server;

import java.io.* ;
import java.net.* ;
import java.util.* ;

import com.networking.group.Server.WebServer;

public final class ClientHandler implements Runnable {
    final static String CRLF = "\r\n";
    Socket socket;
    BufferedReader clientReader;
    DataOutputStream clientWriter;
    User user;

    public ClientHandler(Socket socket) throws Exception
    {
        this.socket = socket;
        this.clientReader = new BufferedReader( new InputStreamReader( socket.getInputStream() ));
        this.clientWriter = new DataOutputStream( socket.getOutputStream() );
    }
    
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void processRequest() throws Exception
    {
        try {
        // Initial Setup to 
            BufferedReader clientReader = this.clientReader;
            DataOutputStream clientWriter = this.clientWriter;

            String userID = clientReader.readLine();
            User existingUser = WebServer.totalUsers.get(userID); 
            
            while (existingUser != null) {
                clientWriter.writeBytes("false" + CRLF);
                userID = clientReader.readLine();
                existingUser = WebServer.totalUsers.get(userID); 
            } 
            existingUser = new User();
            existingUser.UserID = userID;
            existingUser.connectionSocket = this.socket;
            existingUser.clientWriter = clientWriter;
            clientWriter.writeBytes("true" + CRLF);
            WebServer.totalUsers.put(userID, existingUser);
            this.user = existingUser;

            while (socket.isConnected()) {
                String command = clientReader.readLine();

                switch (command) {
                    case "joinpublic":
                        Group userGroup = WebServer.publicGroups.get(0);
                        userGroup.AddUser(this.user);
                        clientWriter.writeBytes("Welcome to the public board " + userID + CRLF);
                        userGroup.SendStatusMessageToUsers("User " + userID + " has join the group " + userGroup.Name + "\n" + CRLF);
                        userGroup.SendLastTwoMessagesToUser(userID);
                        break;
                    case "makepost":
                        System.out.println(userID + " wants to see make a post");
                        String subject = clientReader.readLine();
                        String message = clientReader.readLine();
                        Group publicGroup = WebServer.publicGroups.get(0);
                        if (publicGroup.IsUserInGroup(user.UserID)) {
                            publicGroup.AddMessage(new Message(UUID.randomUUID(), subject, message, user));
                        } else {
                            clientWriter.writeBytes("Please join a group before posting to it");
                        }

                        break;
                    case "viewusers":
                        Collection<User> allUsers = WebServer.publicGroups.get(0).GetAllUsers();
                        clientWriter.writeBytes("All current users for public board: \n");
                        for(User user : allUsers) {
                            clientWriter.writeBytes("\t - " + user.UserID);
                        };
                        clientWriter.writeBytes(CRLF);
                        break;
                    case "leavepublic":
                        WebServer.publicGroups.get(0).RemoveUser(userID);
                        clientWriter.writeBytes(userID + "left the public board" + CRLF);
                        break;
                    case "seemessage":
                        // Needs user to pull exisiting messages
                        UUID messageId = UUID.fromString(clientReader.readLine());
                        Group messageGroup = WebServer.publicGroups.get(0);
                        Collection<Message> m = messageGroup.GetAllMessages();
                        Message requestedMessage = messageGroup.GetMessage(messageId);
                        if (requestedMessage == null) {
                            clientWriter.writeBytes("Message does not exist" + CRLF);
                        } else {
                            clientWriter.writeBytes(requestedMessage.ConvertMessageToRequestMessage() + CRLF);
                        }
                        // WebServer.publicGroups.get(0).;

                        break;
                    case "grouplist":
                        Collection<Group> allGroups = WebServer.publicGroups.values();
                        clientWriter.writeBytes("All current message boards: \n");
                        for (Group group : allGroups) {
                            clientWriter.writeBytes("\t -" + group.Name + "\n");
                        }
                        clientWriter.writeBytes(CRLF);

                        break;
                    case "groupjoin":
                        // Joins a private group
                        clientWriter.writeBytes("What private board you would like to join (numbers 1-5)" + CRLF);
                        int requestedGroup = Integer.parseInt(clientReader.readLine());
                        Group privateGroup = WebServer.publicGroups.get(requestedGroup);
                        privateGroup.AddUser(this.user);
                        clientWriter.writeBytes("Welcome to private board " + privateGroup.Name + " " + userID + CRLF);
                        privateGroup.SendStatusMessageToUsers("User " + userID + " has join the group " + privateGroup.Name + "\n" + CRLF);
                        break;
                    case "grouppost":
                        // Send a post to the private group
                        clientWriter.writeBytes("What message board would you like to make a post to? (numbers 1-5)" + CRLF);
                        int groupNumber = Integer.parseInt(clientReader.readLine());
                        System.out.println(userID + " wants to see make a post");
                        String sub = clientReader.readLine();
                        String mess = clientReader.readLine();
                        Group postGroup = WebServer.publicGroups.get(groupNumber);
                        if (postGroup.IsUserInGroup(user.UserID)) {
                            postGroup.SendMessageToUsers(new Message(UUID.randomUUID(), sub, mess, user));
                        };

                        break;
                    case "groupusers":
                        // Lists users in private group
                        clientWriter.writeBytes("What message board would you like to see the users of? (numbers 1-5)" + CRLF);
                        groupNumber = Integer.parseInt(clientReader.readLine());
                        userGroup = WebServer.publicGroups.get(groupNumber);
                        allUsers = userGroup.GetAllUsers();
                        clientWriter.writeBytes("All current users for private board: " + userGroup.Name + "\n");
                        for(User user : allUsers) {
                            clientWriter.writeBytes("\t - " + user.UserID);
                        };
                        clientWriter.writeBytes(CRLF);
                        break;
                    case "groupleave":
                        clientWriter.writeBytes("What message board would you like to leave? (numbers 1-5)" + CRLF);
                        int leaveNumber = Integer.parseInt(clientReader.readLine());
                        WebServer.publicGroups.get(leaveNumber).RemoveUser(userID);
                        clientWriter.writeBytes(userID + "left the public board" + CRLF);

                        break;
                    case "groupseemessage":
                        // Sees message within private grup

                        break;

                    case "disconnect":
                        // Sees message within private grup
                        Collection<Group> groups = WebServer.publicGroups.values();
                        for ( Group group : groups) {
                            group.RemoveUser(userID);
                        }
                        WebServer.totalUsers.put(userID, null);
                        closeSocketAndStreams();
                        break;
                    default:
                        break;
                } 
            }
        } catch (SocketException exception) {
            Collection<Group> groups = WebServer.publicGroups.values();

            for (Group group : groups) {
                if (group.IsUserInGroup(user.UserID)) {
                    group.RemoveUser(user.UserID);
                };
            }
        }

        closeSocketAndStreams();
    }

    public void closeSocketAndStreams() {
        try {
            if (this.clientWriter != null) {
                this.clientWriter.close();
            }
            if (this.clientReader != null) {
                this.clientReader.close();
            }
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException exception) {
            System.out.println(exception.getStackTrace());
        }
    }
}

