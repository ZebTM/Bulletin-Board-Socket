package webserver;
// /**
// * Assignment 1
// * Zachary Brown
// **/

import java.io.* ;
import java.net.* ;
import java.util.* ;
public final class WebServer
{
    public static void main(String argv[]) throws Exception
    {
        // Set the port number.
        int port = 6789;

        // Establish the listen socket.
        // String server = "www.javaworld.com";
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
    // Constructor
    public HttpRequest(Socket socket) throws Exception
    {
        this.socket = socket;
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
        // Get a reference to the socket's input and output streams.
        InputStream is = socket.getInputStream();
        DataOutputStream os = new DataOutputStream( socket.getOutputStream() );
        // Set up input stream filters.
        

        BufferedReader br = new BufferedReader( new InputStreamReader( is ));
        // Get the request line of the HTTP request message.
        String requestLine = br.readLine();
        // Display the request line.
        System.out.println();
        System.out.println(requestLine);

        // Get and display the header lines.
        String headerLine = null;
        while ((headerLine = br.readLine()).length() != 0) {
            System.out.println(headerLine);
        }

        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken();

        

        String fileName = tokens.nextToken();
        
        fileName = "." + fileName;  

        FileInputStream fis = null;
        boolean fileExists = true;
        try {
            fis = new FileInputStream( fileName );
        } catch (FileNotFoundException e) {
            fileExists = false;
        }

        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;

        
		if (fileExists) {
			statusLine = "HTTP/1.1 200 OK" + CRLF;
			contentTypeLine = "Content-type: " + 
				contentType( fileName ) + CRLF;
		} else {
            statusLine = "HTTP/1.0 404 Not Found" + CRLF;
            contentTypeLine = "Content-type: text/html";
            entityBody = "\n\n Not Found";
        }

		os.writeBytes(statusLine);

		// Send the content type line.
		os.writeBytes(contentTypeLine);

		// Send a blank line to indicate the end of the header lines.
		os.writeBytes(CRLF);

        if ( fileExists ) {
            writeBytes(fis, os);
            fis.close();
        } else {
			fis = new FileInputStream( "./notFound.html");
            writeBytes(fis, os);
        }

        // Close streams and socket.
        os.close();
        br.close();
        socket.close();
    }

    public static String contentType(String fileName) {
        if ( fileName.endsWith(".htm") || fileName.endsWith( ".html" ) ) {
            return "text/html";
        }
        else if ( fileName.endsWith(".gif") ) {
            return "image/gif";
        }
        else if ( fileName.endsWith(".jpg") || fileName.endsWith( ".jpeg" ) ) {
            return "image/jpeg";
        }
        else if ( fileName.endsWith(".txt") ) {
            return "text/plain";
        }
        return "application/octet-stream";
    }

    private static void writeBytes(FileInputStream fis, OutputStream os ) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes = 0;

        while ( ( bytes = fis.read( buffer ) ) != -1 ) {
            os.write( buffer, 0, bytes );
        }
    }
}
