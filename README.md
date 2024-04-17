# Bulletin-Board-Socket
This project is brought to you by by Sam Puffer, Zach Brown, Caleb Copley

How to Run:
Server - 
    Go to WebServer.java
        - Located at socket-chat-app\src\main\java\com\networking\group\Server\WebServer.java
        - If you have VSCODE and can run the Java from command or for windows
        - & 'pathToJava.exe' 'pathToArgFile' 'com.networking.group.Server.WebServer'
Client
    Go to WebClient.java
        - Located at socket-chat-app\src\main\java\com\networking\group\Client\WebClient.java
        - If you have VSCODE and can run the Java from command or for windows
        - & 'pathToJava.exe' 'pathToArgFile' 'com.networking.group.Client.WebClient'

FOR THE GRADER{
The following commands have been renamed for the purposes of clarity and brevity. Each one is prefaced by a "/" for clarity and intent.
    - /connect
        - User was already connected at startup. Prints "User is already connected"
    - /joinPublic
        - replaces join as the public message board join command
    - /makePost
        - replaces post as the public message post
    - /viewUsers
        - replaces users as a retriaval of a list of users in group
    - /leavePublic
        - replaces leave. leaves the group
    - /seeMessage
        - replaces message. Requests message content
    - /disconnect
        - replaces exit. 
    - /groupList
        - replaces groups.
    - /groupJoin
        - Same as standard "groupjoin"
    - /groupPost
        - Same as standard "grouppost"
    - /groupUsers
        - Same as standard "groupusers"
    - /groupLeave
        - Same as standard "groupleave"
    - /groupSeeMessage
        - Replaces 
}

Issues Encountered{
    1. Command for connect was rendered obsolete because of automatic connection on start up.
    2. Non-Built-In type casting for integers in connect function
    3. 
    4. 
    5. 
}

Issue Solutions{
    1. Command for connect was repurposed to print that the user is already connected. In this assignment, one server is present so it was deemed necessary to "remove" the command.
    2. Typecasting using valueof
}