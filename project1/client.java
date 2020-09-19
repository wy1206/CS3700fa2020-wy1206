import java.io.*;
import java.net.*;

public class client {
  // count the occurrence of given char in the given string.
  private static int countSymbol(char symbol, String str) {
    int count = 0;
    for (int i = 0; i < str.length(); i++){
      if(str.charAt(i) == symbol){
        count++;
      }
    }
    return count;
  }

  public static void main(String[] args) throws IOException {
    String HOSTNAME = "3700.network";
    int PORT = 27993;
    String NID = "001248102\n";

    Socket sock = null;

    //establish socket connection
    try {
      sock = new Socket(HOSTNAME, PORT);
    } catch (IOException ioe){
      System.out.println("unable to create socket!");
    }


    // get the output stream and input stream from/to the socket.
    OutputStream toServer = sock.getOutputStream();
    InputStream fromServer = sock.getInputStream();
    // construct the print stream to the server
    PrintStream out = new PrintStream(toServer);
    //construct the reader for input stream
    InputStreamReader inputReader = new InputStreamReader(fromServer);
    //construct the buffer for input stream reader
    BufferedReader buffer = new BufferedReader(inputReader);

    //send the HELLO message.
    out.print("cs3700fall2020 HELLO " + NID);
    out.flush();
    System.out.println("Hello message sent!");

    String message = buffer.readLine();

    while(message != null){
      System.out.println("Message received!\nmsg: " + message);
      if (message.startsWith("cs3700fall2020 FIND")){

        // calling out countSymbol method
        int count = countSymbol(message.charAt(20), message.substring(22));

        System.out.println(count);
        out.print("cs3700fall2020 COUNT " + count + "\n");
        out.flush();
        //read the next line in buffer
        message = buffer.readLine();
      }
      else if (message.startsWith("cs3700fall2020 BYE")) {
        System.out.println(message.substring(19));
        sock.close();
        break;
      }
    }

  }
}
