package com.networking.group;
// /**
// * Assignment 1
// * Zachary Brown
// **/

import java.io.* ;
import java.net.* ;
import java.util.* ;

import com.google.gson.Gson;
import com.networking.group.Classes.Group;
import com.networking.group.Classes.Message;
public final class WebServer
{
    private static List<Group> publicGroups;
    public static void main(String argv[]) throws Exception
    {
        // Set the port number.
        int port = 6789;
        
        publicGroups = new ArrayList<Group>();
        // set groups here
        publicGroups.add(new Group("Public"));
        // publicGroups.add(new Group("Public"));
        // publicGroups.add(new Group("Public"));
        // publicGroups.add(new Group("Public"));
        // publicGroups.add(new Group("Public"));
        // Establish our Grousps
        // Establish the listen socket.
        // System.out.println( "Loading contents of URL: " + server );
        ServerSocket serverSocket = new ServerSocket( port);

        // Process HTTP service requests in an infinite loop.
        while (true) {
            // Listen for a TCP connection request.            
            Socket socket = serverSocket.accept(); 

            // Construct an object to process the HTTP request message.
            HttpRequest request = new HttpRequest( socket );
            // Create a new thread to process the request.
            Thread thread = new Thread(request);
            // Start the thread.
            thread.start();
        }   
    }
}

final class HttpRequest implements Runnable
{
    final static String CRLF = "\r\n";
    Socket socket;
    InputStream inputStream;
    DataOutputStream outputStream;
    // Constructor
    public HttpRequest(Socket socket) throws Exception
    {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = new DataOutputStream( socket.getOutputStream() );
    }
    
    // Implement the run() method of the Runnable interface.
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void processRequest() throws Exception
    {
        InputStream is = this.inputStream;
        DataOutputStream os = this.outputStream;

        BufferedReader br = new BufferedReader( new InputStreamReader( is ));

        String requestLine = br.readLine();
        if (requestLine == null) {
            return;
        }
        
        System.out.println("User: has connected");

        // Has user disconnected
        Boolean hasUserDisconnected = false;
        String headerLine = null;
        

        while (!hasUserDisconnected) {
            while ((headerLine = br.readLine()).length() != 0) {
                if (headerLine.contains("exit")) {
                    hasUserDisconnected = true;
                }
                System.out.println(headerLine);
            }
            writeMessageToClient(headerLine);
            // hasUserDisconnected = true;
            
            
        
        
        
        
        }
        System.out.println("User has left");

        os.writeBytes(CRLF);

        os.close();
        br.close();
        socket.close();
    }

    public void writeMessageToClient(String message) {
        // this.writeBytes
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;


        statusLine = "HTTP/1.1 200 OK" + CRLF;
        contentTypeLine = "Content-type: " + 
            "application/json" + CRLF;
        
        // String curTime = String.valueOf(System.currentTimeMillis());
        // entityBody = "Hello World at " + curTime;

        
        Gson gson = new Gson();
        Message testMessage = new Message(UUID.randomUUID(), "Hello World");
        entityBody = gson.toJson(testMessage);

        DataOutputStream os = this.outputStream;
        try {
            os.writeBytes(statusLine);
            os.writeBytes(contentTypeLine);
            os.writeBytes(CRLF);
            os.writeBytes(entityBody);
        } catch ( IOException exception) {

        }
        
    }

    public void readMessageFromClient() {
        InputStream is = this.inputStream;


    }
}
