### Public Message Board Requirements
- A client joins the public message board by connecting to a dedicated server, which is a standalone process
- Prompted to enter a non-existent user name, no authentication process
- Server keeps track of all people who join/leave the group
- When a user joins/leaves all other users are notified by the server
- When a user joins the group, he/she can only see the last 2 messages that were posted on the board by other clients who joined earlier
- A list of users belonging to the group is displayed once a new user joins (in this part, the list represents all users/clients that have joined earlier)
- When a user posts a new message, other users in the same group should see the posted message.
- Messages are displayed in the following format: “Message ID, Sender, Post Date, Subject.” A user can retrieve the content of a message by contacting the server and providing the message ID as a parameter.
- Your client program should also provide the option to leave the group. Once a user leaves the group, the server notifies all other users in the same group of this event.

### Private Message Board Requirements
- Extend the public message board to allow users to join multiple private groups. Once a user is connected to the server, the server provides a list of 5 groups.
- The user can then select the desired group by id or by name. A user can join multiple groups at the same time.
- A user in one group cannot see users in other groups as well as the messages they have posted to their private board in other groups.'
- All other requirements are the same as the public message board

### Hints
- Both the client and server have to communicate using an agreed-upon protocol.
- Protocol messages should have a format understood by both the client and the server
- You may use plain text, or consider using XML or JSON representations for all or parts of the protocol messages
- Since you are required to use unicast sockets, the server should keep a list of all connected clients
- Consider using mulithreading, one thread for each TCP connection

### Grading Rubric 
- Functionality (70%): 40% for Public Message Board 30% for Private
- Usability (15%): User friendly, gives recommended user input commands to handle requests
	- Public Message Board
		- %connect command followed by the address and port number of a running bulletin board server to connect to. 
		- %join command to join the single message board  
		- %post command followed by the message subject and the message content or main body to post a message to the board. 
		- %users command to retrieve a list of users in the same group. 
		- %leave command to leave the group. 
		- %message command followed by message ID to retrieve the content of the message. 
		- %exit command to disconnect from the server and exit the client program.
	- Private Message Board
		- %groups command to retrieve a list of all groups that can be joined. 
		- %groupjoin command followed by the group id/name to join a specific group. 
		- %grouppost command followed by the group id/name, the message subject, and the message content or main body to post a message to a message board owned by a specific group. 
		- %groupusers command followed by the group id/name to retrieve a list of users in the given group. 
		- %groupleave command followed by the group id/name to leave a specific group. 
		- %groupmessage command followed by the group id/name and message ID to retrieve the content of the message posted earlier on a message board owned by a specific group.
- Documentation (15%): Provide a Makefile and a README file
	- README should contain instructions on how to compile and run your server/client programs (if certain software or packages need to be installed, provide instructions as well), usability instructions ONLY if different from suggested above, and a description of any major issues you came across and how you handled them