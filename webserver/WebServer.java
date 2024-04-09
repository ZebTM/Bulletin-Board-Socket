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
            // Keep this socket alive until either we close it or a user disconnects without telling us
            socket.setKeepAlive(true);

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
        // Get the request line of the HTTP request message.
        String requestLine = br.readLine();
        // Display the request line.
        System.out.println();
        System.out.println(requestLine);

        // Get and display the header lines.
        String headerLine = null;
        while ((headerLine = br.readLine() ) != null && headerLine.length() != 0 ) {
            System.out.println(headerLine);
        }

        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken();


        String clientSentence;
        String capitalizedSentence;
        clientSentence = br.readLine();
        System.out.println("Received: " + clientSentence);
        capitalizedSentence = clientSentence.toUpperCase() + '\n';
        os.writeBytes(capitalizedSentence);
        os.writeBytes(String.valueOf(System.currentTimeMillis()));
        Thread.sleep(1000);
        os.writeBytes(String.valueOf(System.currentTimeMillis()));
        

        // Close streams and socket.

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
            "text/plain" + CRLF;
        
        String curTime = String.valueOf(System.currentTimeMillis());
        entityBody = "Hello World at " + curTime;

        byte[] buffer = new byte[1024];
        int bytes = 0;
        DataOutputStream os = this.outputStream;
        try {
            os.writeBytes(statusLine);
            os.writeBytes(contentTypeLine);
            os.writeBytes(entityBody);
        } catch ( IOException exception) {

        }
        
    }

    private static void writeBytes(FileInputStream fis, OutputStream os ) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes = 0;

        while ( ( bytes = fis.read( buffer ) ) != -1 ) {
            os.write( buffer, 0, bytes );
        }
    }
}
