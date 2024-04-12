package com.networking.group.Classes;

import java.io.* ;
import java.net.* ;
import java.util.* ;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.networking.group.WebServer;

public final class ClientHandler implements Runnable {
    final static String CRLF = "\r\n";
    Socket socket;
    BufferedReader bufferedReader;
    DataOutputStream outputStream;

    public ClientHandler(Socket socket) throws Exception
    {
        this.socket = socket;
        this.bufferedReader = new BufferedReader( new InputStreamReader( socket.getInputStream() ));
        this.outputStream = new DataOutputStream( socket.getOutputStream() );
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
        BufferedReader br = this.bufferedReader;
        try {
            HttpRequest request;
            String requestLine;
            if ((requestLine = br.readLine()) == null) {
                return;
            };
            request = parseURL(br, requestLine);
            // System.out.println(request.method);
            
            while (socket.isConnected()) {
                switch (request.getMethod()) {
                    case "POST":
                        handlePost(request);
                        break;
                    case "GET":
                        handleGet(request);
                        break;

                    case "DELETE":
                        handleDelete(request);
                        break;
                    default:
                        System.out.println("METHOD NOT FOUND");
                        break;
                }
                requestLine = null;
            }
        } catch (IOException exception) {
            System.out.println(exception);
        }

        // Close All relevant sockets at end of session
        this.outputStream.close();
        br.close();
        socket.close();
    }

    public void closeSocketAndStreams() {
        try {
            if (this.outputStream != null) {
                this.outputStream.close();
            }
            if (this.bufferedReader != null) {
                this.bufferedReader.close();
            }
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException exception) {
            System.out.println(exception.getStackTrace());
        }
        
    }

    public void handlePost(HttpRequest request) {
        switch (request.getUrl()) {
            case "connect":
                Gson gson = new Gson();
                UserCreate userID = new UserCreate();
                try {
                    userID = gson.fromJson(request.getBody(), UserCreate.class);
                    User existingUser = WebServer.totalUsers.get(userID.UserID);
                    if (existingUser != null) {
                        sendResponse(409, null);
                        break;
                    } 
                    existingUser = new User();
                    existingUser.UserID = userID.UserID;
                    existingUser.connectionSocket = this.socket;
                    WebServer.totalUsers.put(userID.UserID, existingUser);

                    sendResponse(200, null);
                } catch (JsonSyntaxException exception) {
                    System.out.println(exception);
                    sendResponse(400, null);
                    break;
                }
                
                

                

                break;
            case "join": 

                break;
            case "post":
        }
    }

    public void handleDelete(HttpRequest request) {
        switch (request.getUrl()) {
            case "exit":

                // W
                // Completetly leave server
                // Close socket
                break;
            case "leave":
                // leave public group
                break;
            case "groupleave":
                // leave specific group
                break;
        }
    }

    public void handleGet(HttpRequest request) {

    }

    public void sendResponse(Integer statusCode, JsonObject object  ) {
        // this.writeBytes

        
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;


        statusLine = "HTTP/1.1 " + getStatusCodeString(statusCode) + CRLF;
        if (object != null) {
            contentTypeLine = "Content-type: " + 
            "application/json" + CRLF;
            Gson gson = new Gson();
            entityBody = gson.toJson(object);
        }
        

        DataOutputStream os = this.outputStream;
        try {
            os.writeBytes(statusLine);
            if (object != null) {
            os.writeBytes(contentTypeLine);
            }
            os.writeBytes(CRLF);
            if (object != null) {
                os.writeBytes(entityBody);
            }
            
            os.writeBytes(CRLF);
            // os.close();
        } catch ( IOException exception) {

        }
    }

    public String getStatusCodeString(Integer statusCode) {
        switch (statusCode) {
            case 200:
                return "200 OK";
            case 100:
                return "100 Continue";
            default:
                return "400 Bad Request";
        }
    }

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

    public void readMessageFromClient() {
        // InputStream is = this.inputStream;


    }

    public HttpRequest parseURL(BufferedReader data, String requestLine) throws IOException {
        final List<String> metadataLines = new ArrayList<>();

        // final StringBuilder lineBuilder = new StringBuilder();
        String line;
        int contentLength = 0;
        while ((line = data.readLine()) != null && !line.isEmpty()) {
            System.out.println(line);
            metadataLines.add(line + "\n");
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        };

        char[] buffer = new char[contentLength];
        data.read(buffer);
        String requestBody = new String(buffer);


        final String firstLine = requestLine;
        final String method = firstLine.split("\\s+")[0];
        // Get rid of '/' char
        final String url = firstLine.split("\\s+")[1].substring(1);

        
        final String body = requestBody;

        final Map<String, String> headers = new HashMap<>();

        for (int i = 1; i < metadataLines.size(); i++) {
            String headerLine = metadataLines.get(i);
            if (headerLine.trim().isEmpty()) {
                break;
            }

            String key = headerLine.split(":\\s")[0];
            String value = headerLine.split(":\\s")[1];

            headers.put(key, value);
        }

        return new HttpRequest(method, url, headers, body);
    }
}
