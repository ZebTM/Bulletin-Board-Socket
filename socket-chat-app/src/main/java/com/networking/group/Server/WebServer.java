package com.networking.group.Server;
// /**
// * Assignment 2
// * Zachary Brown, Caleb Copley, Sam Puffer
// **/

import java.net.* ;
import java.util.concurrent.ConcurrentHashMap;

import com.networking.group.Classes.*;
import com.networking.group.Classes.Server.ClientHandler;
import com.networking.group.Classes.Server.Group;
import com.networking.group.Classes.Server.User;
public final class WebServer
{
    public static ConcurrentHashMap<Integer, Group> publicGroups;
    public static ConcurrentHashMap<String, User> totalUsers;
    public static void main(String argv[]) throws Exception
    {
        // Set the port number.
        int port = 6789;
        
        publicGroups = new ConcurrentHashMap<Integer, Group>();
        totalUsers = new ConcurrentHashMap<String, User>();
        // set groups here
        publicGroups.put(1, new Group("Public"));

        // Establish the listen socket.
        ServerSocket serverSocket = new ServerSocket( port);

        // Process HTTP service requests in an infinite loop.
        while (true) {
            // Listen for a TCP connection request.            
            Socket socket = serverSocket.accept(); 
            // socket.setKeepAlive(true);
            // Construct an object to process the Client Requests.
            ClientHandler request = new ClientHandler( socket );
            // Create a new thread to process the request.
            Thread thread = new Thread(request);
            // Start the thread.
            thread.start();
        }   
    }
}