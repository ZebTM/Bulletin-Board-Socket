package com.networking.group;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

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
                    case "CONNECT":

                        break;
                    case "EXIT":

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

    static void helpClient() {
        System.out.println("Commands: ");
        System.out.println("Connect: \n - Connects you to the server that  ");
        System.out.println("Join: \n - Joins the public group of the server you are connected to ");
        System.out.println("Post: ");
        System.out.println("Users: ");
        System.out.println("Leave: ");
        System.out.println("Message: ");
        System.out.println("Exit: ");
        System.out.println("Groups: ");
        System.out.println("Groupjoin: ");
        System.out.println("Grouppost: ");
        System.out.println("Groupusers: ");
        System.out.println("Groupleave: ");
        System.out.println("Groupjoin: ");
        
    }
}
