package com.networking.group.Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

import com.networking.group.Classes.Client.ReaderClientHelper;

/**
 * Hello world!
 *
 */
public class WebClient 
{
    final static String CRLF = "\r\n";
    static Socket serverSocket;
    static BufferedReader serverReader;
    static DataOutputStream serverWriter;
    static Boolean isConnected = false;
    static Thread readerThread;
    static BufferedReader terminalReader;
    public static void main( String[] args )
    {
        try {
            terminalReader = new BufferedReader(new InputStreamReader(System.in));
            String command;
            Boolean active = true;
            
            helpClient();
            
            while (active) {
                command = terminalReader.readLine().toUpperCase();
                switch (command) {
                    case "/CONNECT":
                        if (!isConnected) {
                            setUsername();
                        } else {
                            System.out.println("Please only connect to one server at a time");
                        }
                        break;
                    case "/JOINPUBLIC":
                        if (isConnected) {
                            serverWriter.writeBytes("joinpublic" + CRLF);
                        } else {
                            System.out.println("Please connect before running any other commands");
                        }
                        
                        break;
                    case "/MAKEPOST":
                        if (isConnected) {
                            serverWriter.writeBytes("makepost" + CRLF);
                            sendMessage();
                        } else {
                            System.out.println("Please connect before running any other commands");
                        }
                        
                        break;
                    case "/VIEWUSERS":
                        if (isConnected){
                            serverWriter.writeBytes("viewusers" + CRLF);
                        } else {
                            System.out.println("Please connect before running any other commands");
                        }

                        break;
                    case "/LEAVEPUBLIC":
                        if (isConnected) {
                            serverWriter.writeBytes("leavepublic" + CRLF);
                        } else {
                            System.out.println("Please connect before running any other commands");
                        }

                        break;
                    case "/SEEMESSAGE":
                        if (isConnected) {
                            serverWriter.writeBytes("seemessage" + CRLF);
                            System.out.println("Enter the message id");
                            String messageId = terminalReader.readLine();
                            serverWriter.writeBytes(messageId + CRLF);
                        } else {
                            System.out.println("Please connect before running any other commands");
                        }
                        break;
                    case "/DISCONNECT":
                        if (isConnected) {
                            serverWriter.writeBytes("disconnect" + CRLF);
                            serverReader.close();
                            serverWriter.close();
                            serverSocket.close();
                            // readerThread;
                        } else {
                            System.out.println("Please connect before running any other commands");
                        }
                        break;
                    case "/GROUPLIST":
                        if (isConnected) {
                            serverWriter.writeBytes("grouplist" + CRLF);
                            System.out.println("What is the group ID?\n");
                            String response = terminalReader.readLine();
                            serverWriter.writeBytes(response + CRLF);
                        } else {
                            System.out.println("Please connect before running any other commands");
                        }
                        break;
                    case "/GROUPJOIN":
                        if (isConnected) {
                            serverWriter.writeBytes("groupjoin" + CRLF);
                            String response = terminalReader.readLine();
                            serverWriter.writeBytes(response + CRLF);
                        } else {
                            System.out.println("Please connect before running any other commands");
                        }
                        break;
                    case "/GROUPPOST":
                        if (isConnected) {
                            serverWriter.writeBytes("grouppost" + CRLF);
                            String groupID = terminalReader.readLine();
                            serverWriter.writeBytes(groupID + CRLF);
                            sendMessage();
                        } else {
                            System.out.println("Please connect before running any other commands");
                        }
                        break;
                    case "/GROUPUSERS":
                        if (isConnected) {
                            serverWriter.writeBytes("groupusers" + CRLF);
                            // System.out.println("What is the group ID?\n");
                            String response = terminalReader.readLine();
                            serverWriter.writeBytes(response + CRLF);
                        } else {
                            System.out.println("Please connect before running any other commands");
                        }
                        break;
                    case "/GROUPLEAVE":
                        if (isConnected) {
                            serverWriter.writeBytes("groupleave" + CRLF);
                            System.out.println("What is the group ID?\n");
                            String response = terminalReader.readLine();
                            serverWriter.writeBytes(response + CRLF);
                        }   
                        break;
                    case "/GROUPSEEMESSAGE":
                        if (isConnected) {
                            serverWriter.writeBytes("groupseemessage" + CRLF);
                            String groupId = terminalReader.readLine();
                            
                            serverWriter.writeBytes(groupId + CRLF);
                            String messageId = terminalReader.readLine();
                            serverWriter.writeBytes(messageId + CRLF);
                        } else {
                            System.out.println("Please connect before running any other commands");
                        }
                        break;
                    case "HELP":
                        helpClient();
                        break;
                    case "":
                        break;
                    default:
                        System.out.println("Please enter a supported commmand");
                }
            }

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            
        }
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

    static void setUsername() throws IOException {
        System.out.println("Enter server address: ");
        String serverAddress = terminalReader.readLine();
        System.out.println("Enter server port number: ");
        String serverPort = terminalReader.readLine();
        Integer number = Integer.valueOf(serverPort);
        serverSocket = new Socket(serverAddress, number);
        // connectServer(goofySocket);

        serverReader = new BufferedReader( new InputStreamReader( serverSocket.getInputStream() ));
        serverWriter = new DataOutputStream( serverSocket.getOutputStream() );

        System.out.println("Enter User ID: ");
        String userID = terminalReader.readLine();
        

        serverWriter.writeBytes(userID + CRLF);

        String acceptedUsername = serverReader.readLine();
        
        
        while (!acceptedUsername.equals("true")) {
            System.out.println("Sorry User ID is already taken");
            System.out.println("Enter User ID: ");
            userID = terminalReader.readLine();

            serverWriter.writeBytes(userID + CRLF);

            acceptedUsername = serverReader.readLine();
        }
        System.out.println("User login succesful " + userID + "\n");
        readerThread = new Thread(new ReaderClientHelper(serverReader));
        readerThread.start();
        isConnected = true;
    }

    static void sendMessage() throws IOException {
        System.out.println("Please enter your subject");
        String subject = terminalReader.readLine();
        System.out.println("Please enter your message");
        String message = terminalReader.readLine();
        serverWriter.writeBytes(subject + CRLF);
        serverWriter.writeBytes(message + CRLF);
    }
}


