package com.networking.group.Classes.Server;

import java.io.* ;
import java.net.* ;
import java.util.* ;

import com.networking.group.Client.WebClient;
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
                    WebServer.publicGroups.get(1).AddUser(this.user);
                    clientWriter.writeBytes("Welcome to the public board " + userID + CRLF);
                    break;
                case "makepost":
                    System.out.println(userID + " wants to see make a post");
                    String subject = clientReader.readLine();
                    String message = clientReader.readLine();
                    Group publicGroup = WebServer.publicGroups.get(0);
                    if (publicGroup.IsUserInGroup(user.UserID)) {
                        publicGroup.SendMessageToUsers(new Message(UUID.randomUUID(), subject, message, user));
                    };
                case "viewusers":
                    Collection allUsers = WebServer.totalUsers.values();
                    clientWriter.writeBytes("All current users for public board: " + allUsers + CRLF);
                    break;
                case "leavepublic":
                    WebServer.publicGroups.get(0).RemoveUser(userID);
                    clientWriter.writeBytes(userID + "left the public board" + CRLF);
                    break;
                case "seemessage":
                    // Needs user to pull exisiting messages
                case "grouplist":
                    Collection allGroups = WebServer.publicGroups.values();
                    clientWriter.writeBytes("All current message boards: " + allGroups + CRLF);
                case "groupjoin":
                    // Joins a private group
                    clientWriter.writeBytes("What private board you would like to join (numbers 1-5)" + CRLF);
                    int requestedGroup = Integer.parseInt(clientReader.readLine());
                    WebServer.publicGroups.get(requestedGroup).AddUser(this.user);
                    clientWriter.writeBytes("Welcome to private board " + requestedGroup + " " + userID + CRLF);
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
                case "groupusers":
                    // Lists users in private group
                case "groupleave":
                    clientWriter.writeBytes("What message board would you like to leave? (numbers 1-5)" + CRLF);
                    int leaveNumber = Integer.parseInt(clientReader.readLine());
                    WebServer.publicGroups.get(leaveNumber).RemoveUser(userID);
                    clientWriter.writeBytes(userID + "left the public board" + CRLF);
                case "groupseemessage":
                    // Sees message within private grup
                default:
                    break;
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

    // public void handlePost(HttpRequest request) {
    //     switch (request.getUrl()) {
    //         case "connect":
    //             Gson gson = new Gson();
    //             UserCreate userID = new UserCreate();
    //             try {
    //                 userID = gson.fromJson(request.getBody(), UserCreate.class);
    //                 User existingUser = WebServer.totalUsers.get(userID.UserID);
    //                 if (existingUser != null) {
    //                     sendResponse(409, null);
    //                     break;
    //                 } 
    //                 existingUser = new User();
    //                 existingUser.UserID = userID.UserID;
    //                 existingUser.connectionSocket = this.socket;
    //                 // Add user to client handler and list of users
    //                 this.user = existingUser;
    //                 WebServer.totalUsers.put(userID.UserID, existingUser);

    //                 sendResponse(200, null);
    //                 break;
    //             } catch (JsonSyntaxException exception) {
    //                 System.out.println(exception);
    //                 sendResponse(400, null);
    //                 break;
    //             }
                
    //         case "join": 
    //             Group mainGroup = WebServer.publicGroups.get(1);
    //             mainGroup.AddUser(this.user);
    //             mainGroup.SendMessageToUsers(new Message(UUID.randomUUID(), "New User", "Welcome"));

    //             break;
    //         case "post":
    //     }
    // }

    // public void sendResponse(Integer statusCode, JsonObject object  ) {
    //     // this.writeBytes

        
    //     String statusLine = null;
    //     String contentTypeLine = null;
    //     String entityBody = null;


    //     statusLine = "HTTP/1.1 " + getStatusCodeString(statusCode) + CRLF;
    //     if (object != null) {
    //         contentTypeLine = "Content-type: " + 
    //         "application/json" + CRLF;
    //         Gson gson = new Gson();
    //         entityBody = gson.toJson(object);
    //     }
        

    //     DataOutputStream os = this.clientWriter;
    //     try {
    //         os.writeBytes(statusLine);
    //         if (object != null) {
    //         os.writeBytes(contentTypeLine);
    //         }
    //         os.writeBytes(CRLF);
    //         if (object != null) {
    //             os.writeBytes(entityBody);
    //         }
            
    //         os.writeBytes(CRLF);
    //         // os.close();
    //     } catch ( IOException exception) {

    //     }
    // }

    // public String getStatusCodeString(Integer statusCode) {
    //     switch (statusCode) {
    //         case 200:
    //             return "200 OK";
    //         case 100:
    //             return "100 Continue";
    //         default:
    //             return "400 Bad Request";
    //     }
    // }

    // public void writeMessageToClient(String message) {
    //     // this.writeBytes

        
    //     String statusLine = null;
    //     String contentTypeLine = null;
    //     String entityBody = null;


    //     statusLine = "HTTP/1.1 200 OK" + CRLF;
    //     contentTypeLine = "Content-type: " + 
    //         "application/json" + CRLF;
        
    //     // String curTime = String.valueOf(System.currentTimeMillis());
    //     // entityBody = "Hello World at " + curTime;

        
    //     Gson gson = new Gson();
    //     Message testMessage = new Message(UUID.randomUUID(), message);
    //     entityBody = gson.toJson(testMessage);

    //     DataOutputStream os = this.outputStream;
    //     try {
    //         os.writeBytes(statusLine);
    //         os.writeBytes(contentTypeLine);
    //         os.writeBytes(CRLF);
    //         os.writeBytes(entityBody);
    //         os.writeBytes(CRLF);
    //         this.outputStream.close();
    //     } catch ( IOException exception) {

    //     }
    // }

    // public void readMessageFromClient() {
    //     // InputStream is = this.inputStream;


    // }

    // public HttpRequest parseURL(BufferedReader data, String requestLine) throws IOException {
    //     final List<String> metadataLines = new ArrayList<>();

    //     // final StringBuilder lineBuilder = new StringBuilder();
    //     String line;
    //     int contentLength = 0;
    //     while ((line = data.readLine()) != null && !line.isEmpty()) {
    //         System.out.println(line);
    //         metadataLines.add(line + "\n");
    //         if (line.startsWith("Content-Length:")) {
    //             contentLength = Integer.parseInt(line.split(":")[1].trim());
    //         }
    //     };

    //     char[] buffer = new char[contentLength];
    //     data.read(buffer);
    //     String requestBody = new String(buffer);


    //     final String firstLine = requestLine;
    //     final String method = firstLine.split("\\s+")[0];
    //     // Get rid of '/' char
    //     final String url = firstLine.split("\\s+")[1].substring(1);

        
    //     final String body = requestBody;

    //     final Map<String, String> headers = new HashMap<>();

    //     for (int i = 1; i < metadataLines.size(); i++) {
    //         String headerLine = metadataLines.get(i);
    //         if (headerLine.trim().isEmpty()) {
    //             break;
    //         }

    //         String key = headerLine.split(":\\s")[0];
    //         String value = headerLine.split(":\\s")[1];

    //         headers.put(key, value);
    //     }

    //     return new HttpRequest(method, url, headers, body);
    // }
}
