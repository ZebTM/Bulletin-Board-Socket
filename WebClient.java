package com.networking.group;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Scanner; 

/**
 * Hello world!
 *
 */
public class WebClient 
{
    final static String CRLF = "\r\n";
    public static void main( String[] args )
    {
        try {
            Socket serverSocket = new Socket("localhost", 6789);
            // serverSocket.connect();

            BufferedReader serverReader = new BufferedReader( new InputStreamReader( serverSocket.getInputStream() ));
            DataOutputStream serverWriter = new DataOutputStream( serverSocket.getOutputStream() );

            // Terminal reader to get user input
            BufferedReader terminalReader = new BufferedReader(new InputStreamReader(System.in));
            
            System.out.println("Enter User ID: ");
			String userID = terminalReader.readLine();
            

            serverWriter.writeBytes(userID + CRLF);

            String acceptedUsername = serverReader.readLine();
            
            
            while (acceptedUsername.equals("true" + CRLF)) {
                System.out.println("Sorry User ID is already taken \n");
                System.out.println("Enter User ID: ");
                userID = terminalReader.readLine();
    
                serverWriter.writeBytes(userID + CRLF);
    
                acceptedUsername = serverReader.readLine();
            }
            
            String fullCommand;
            StringTokenizer tokenizer;
            String command;
            while (serverSocket.isConnected()) {
                fullCommand = terminalReader.readLine() + CRLF;

                tokenizer = new StringTokenizer(fullCommand);
                command = tokenizer.nextToken().toUpperCase();
                switch (command) {
                    case "/CONNECT":
                        System.out.println("Enter server address: ");
                        String serverAddress = terminalReader.readLine();
                        System.out.println("Enter server port number: ");
                        String serverPort = terminalReader.readLine();
                        Integer number = Integer.valueOf(serverPort);
                        Socket goofySocket = new Socket(serverAddress, number);
                        connectServer(goofySocket);
                        break;
                    case "/JOINPUBLIC":

                        break;
                    case "/MAKEPOST":

                        break;
                    case "/VIEWUSERS":

                        break;
                    case "/LEAVEPUBLIC":

                        break;
                    case "/SEEMESSAGE":

                        break;
                    case "/DISCONNECT":

                        break;
                    case "/GROUPLIST":

                        break;
                    case "/GROUPJOIN":

                        break;
                    case "/GROUPPOST":

                        break;
                    case "/GROUPUSERS":

                        break;
                    case "/GROUPLEAVE":

                        break;
                    case "/GROUPSEEMESSAGE":

                        break;
                    case "HELP":
                        helpClient();
                        break;
                }
                
                System.out.println(tokenizer.nextToken());
                System.out.println(tokenizer.nextToken());
                
            }

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    // PUBLIC REQUIREMENTS //
    static void connectServer(Socket serverSocket) {
        // command followed by the address and port number of a running bulletin board server to connect to. 
        System.out.println("Successfully connected to server :)");
    }

    static void joinPublic() {
        // command to join the single message board  
    }

    static void makePost(String header, String content) {
        // command followed by the message subject and the message content or main body to post a message to the board. 
    }

    static void viewUsers() {
        // command to retrieve a list of users in the same group. 
    }

    static void leavePublic() {
        // command to leave the group. 
    }

    static void seeMessage(int messageID){
        // command followed by message ID to retrieve the content of the message. 
    }

    static void disconnect(){
        // command to disconnect from the server and exit the client program.
    }

    // PRIVATE REQUIREMENTS //
    static void groupList() {
        // retrieve a list of all groups that can be joined. 
    }

    static void groupJoin(){
        // command followed by the group id/name to join a specific group. 
    }

    static void groupPost(int groupID, String header, String content){
        //command followed by the group id/name, the message subject, and the message content or main body to post a message to a message board owned by a specific group. 
    }

    static void groupUsers(int groupID) {
        //command followed by the group id/name to retrieve a list of users in the given group. 
    }

    static void groupLeave(int groupID) {
        // command followed by the group id/name to leave a specific group. 
    }

    static void groupSeeMessage(int groupID, int messageID) {

    }

    static void helpClient() {
        System.out.println("Commands: ");
        System.out.println("/connect - Command followed by the address and port number of a running bulletin board server to connect to. ");
        System.out.println("/joinPublic - Command to join the public message board");
        System.out.println("/makePost - Command followed by the message subject and the message content or main body to post a message to the board.");
        System.out.println("/viewUsers - Command to retrieve a list of users in the public group");
        System.out.println("/leavePublic - Command to leave the public group.");
        System.out.println("/seeMessage -  Command followed by message ID to retrieve the content of the retrieved message.");
        System.out.println("/disconnect - Command to disconnect from the server and exit the client program.");
        System.out.println("/groupList -  Command to retrieve a list of all groups that can be joined. ");
        System.out.println("/groupJoin - command followed by the group id/name to join a specific group. ");
        System.out.println("/groupPost - command followed by the group id/name, the message subject, and the message content or main body to post a message to a message board owned by a specific group. ");
        System.out.println("/groupUsers - command followed by the group id/name to retrieve a list of users in the given group. ");
        System.out.println("/groupLeave - command followed by the group id/name to leave a specific group.");
        System.out.println("/groupSeeMessage - command followed by the group id/name and message ID to retrieve the content of the message posted earlier on a message board owned by a specific group.");
    }
}
