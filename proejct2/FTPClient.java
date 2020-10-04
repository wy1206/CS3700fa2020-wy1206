import java.io.*;

import java.net.*;

public class FTPClient {
  private static String host = "3700.network";
  private static int port = 21;
  private static String username = "wangyuan3";
  private static String password = "l6JrI1twUEOYpNA2Fzyk";
  private static Socket sock,dataSock = null;
  private static BufferedReader br;
  private static PrintStream out;
  private static BufferedReader dataBr;
  private static PrintStream dataOut;

  private static BufferedReader fr1;
  private static BufferedWriter fw1;


  /**
   * Read the URL from the parameters.
   * make it recognizable in a format of ftp://[username:[password]@]host[:port]/path
   */
  private static String ReadURL(String url) {
    String[] path = url.split("://|@|:");
    String result = "";
    System.out.println(path[0]);
    switch (path.length){
      case 2 :
        // if it was only ftp://host/[path], get rid of hostname
        String[] dir = path[1].split("/");
        host = dir[0];
        port = 21;
        result = path[1].substring(dir[0].length());
        System.out.println(result);
        break;
      case 3 :
        // ftp://host:port/[path]
        String[] dir1 = path[2].split("/");
        result = path[2].substring(dir1[0].length());
        port = Integer.parseInt(dir1[0]); // set the port to the given port number
        System.out.println(result);
        break;
      case 5 :
        // ftp://username:password@host:port/[path]
        String[] dir3 = path[4].split("/");
        username = path[1];  // set up the username
        password = path[2];  // set up the password
        host = path[3]; // set up the host;
        port = Integer.parseInt(dir3[0]);
        result = path[4].substring(dir3[0].length());
        System.out.println(result);
        break;
      default:
        System.out.println("Invalid URL!\n" +
                "logged in as wangyuan3");
    }
    return result;
  }

  /**
   * connect to the FTP server as the control channel.
   * @throws IOException when it failed to create socket
   */
  private static synchronized void connectControl(
          String host, int port, String username, String password) throws IOException{
    //create socket connected to the FTP server.
    try {
      sock = new Socket(host, port);
    } catch (IOException ioe) {
      System.out.println("unable to create socket!");
    }
    br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    out = new PrintStream(sock.getOutputStream());

    String message = br.readLine();
    System.out.println(message);
    out.print("USER " + username +"\r\n");
    message = br.readLine();
    System.out.println(message);
    out.print("PASS " + password +"\r\n");
    message = br.readLine();
    System.out.println(message);

  }

  /**
   * open a new socket which connects to the data channel of the FTP server.
   * @throws IOException when it failed to create socket
   */
  private static synchronized void connectData () throws IOException{
    String dataHost;
    int dataPort;
    out.print("PASV\r\n");
    String addr = br.readLine().substring(26);
    System.out.println(addr);
    // get the correct ip address(first four numbers of the address)
    String[] addrs = addr.split(",");
    if (addrs.length != 6) {
      throw new IOException("invalid ip address!");
    }
    else {
      dataHost = addrs[0].replaceAll("\\(", "") + "."
              + addrs[1] + "." + addrs[2] + "." + addrs[3];
      // get the correct host of the ip address (last two numbers)
      dataPort = (Integer.parseInt((addrs[4])) << 8) + Integer.parseInt(
              addrs[5].replaceAll("[).]", ""));

      System.out.println(dataHost);

      //create socket to data channel
      try {
        dataSock = new Socket(dataHost, dataPort);
        System.out.println("Successful connected to data chanel!");
      } catch (IOException ioe) {
        System.out.println("unable to create data socket!");
      }

      dataBr = new BufferedReader(new InputStreamReader(dataSock.getInputStream()));
      dataOut = new PrintStream(dataSock.getOutputStream());

//      String message = dataBr.readLine();
//      System.out.println(message);
    }

  }

  /**
   * Set up the correct mode for data channel.
   * @throws IOException when it failed to read or write from stream
   */
  private static synchronized void setMode() throws IOException{
    // set the connection type to 8-bit binary data mode.
    out.print("TYPE I\r\n");
    br.readLine();
    // set the connection to stream mode
    out.print("MODE S\r\n");
    br.readLine();
    // set the connection to file-oriented mode
    out.print("STRU F\r\n");
    br.readLine();
  }

  private static synchronized void closeData() throws IOException {
    dataOut.print("QUIT\r\n");
    dataSock.close();
    System.out.println("connection closed!");
  }

  /**
   * disconnect from the FTP server.
   * @throws IOException if it failed to read or write from stream
   */
  private static synchronized void disconnect() throws IOException{
    out.print("QUIT\r\n");
    sock.close();
    System.out.println("connection closed!");
  }

  /**
   * Upload the file to the FTP server
   * @param src path of the source file
   * @param des path of the destination
   * @throws IOException ...
   */
  private static synchronized void fileUpload(String src, String des) throws IOException {
    out.print("STOR " + des + "/" + src +"\r\n");
    System.out.println(br.readLine());
    File file = new File(src);
    fr1 = new BufferedReader(new FileReader(file));

    String buffer;
    StringBuilder bin = new StringBuilder();
    while ((buffer = fr1.readLine()) != null) {
      bin.append(buffer);
    }
    fr1.close();
    dataOut.print(bin);

  }

  /**
   * Download file from the FTP server
   * @param src path of the source file
   * @param des paht of the destination
   * @throws IOException ...
   */
  private static synchronized void fileDownload(String src, String des) throws IOException {
    out.print("RETR " + src + "\r\n");
    System.out.println(br.readLine());
    String[] path = src.split("/");
    String filename = path[path.length - 1];
    File file = new File(des + "/" + filename);
    fw1 = new BufferedWriter(new FileWriter(file));
    String buffer;
    String bin = "";
    while ((buffer = dataBr.readLine()) != null) {
      bin += buffer;
    }
    fw1.write(bin);
    fw1.close();
    System.out.println(bin);
  }

  public static void main(String[] args) throws Exception {
    int flag = 0; // flag for STOR/RETR, 0:=LIST, 1:=STOR, 2:=RETR 3:= error
    String operation, param1 = "", param2 = "";

    if (args.length == 2 && args[1].startsWith("ftp")) {
      param1 = ReadURL(args[1]);
    }
    else if (args.length == 3) {
      param1 = args[1];
      param2 = args[2];
      if(args[1].startsWith("ftp")){
      flag += 2;
      }
      if(args[2].startsWith("ftp")){
      flag += 1;
      }
    } else {
      System.out.println("Invalid operation! \n" +
              "Usage: operation <URL1> (<URL2>)\n" +
              "1. ls <URL>\n" +
              "2. mkdir <URL>\n" +
              "3. rm <URL>\n" +
              "4. rmdir <URL>\n" +
              "5. cp <ARG1> <ARG2>\n" +
              "6. mv <ARG1> <ARG2>\n");
      System.exit(1);
    }

    operation = args[0];

    try {
      connectControl(host, port, username, password);
      System.out.println("Successful connected!");
    } catch (IOException ioe){
      System.out.println("unable to create socket!");
    }

    setMode();

    switch (operation) {
      case "ls" :
        connectData();

        param1 = ReadURL(args[1]);
        System.out.println(param1);
        out.print("LIST " + param1 + "\r\n");
        System.out.println(dataBr.readLine());

        closeData();
        break;
      case "mkdir":
        param1 = ReadURL(args[1]);
        out.print("MKD " + param1 + "\r\n");
        System.out.println(br.readLine());
        break;
      case "rmdir" :
        param1 = ReadURL(args[1]);
        out.print("RMD " + param1 + "\r\n");
        System.out.println(br.readLine());
        break;
      case "rm" :
        connectData();

        param1 = ReadURL(args[1]);
        out.print("DELE " + param1 + "\r\n");
        System.out.println(br.readLine());

        closeData();
        break;
      case "cp" :
        connectData();

        if (flag == 1) {
          param2 = ReadURL(args[2]); // args[2] is the URL
          fileUpload(param1, param2);
        } else if (flag == 2) {
          param1 = ReadURL(args[1]); // args[1] is the URL
          fileDownload(param1, param2);
        } else {
          System.out.println("invalid path for file transfer!");
        }

        closeData();
        break;
      case "mv" :
        connectData();

        if (flag == 1) {
          param2 = ReadURL(args[2]); // URL : args[2]
          fileUpload(param1, param2);
          File file = new File(param1);
          System.out.println(file.delete());
        } else if (flag == 2) {
          param1 = ReadURL(args[1]); // URL : args[1]
          fileDownload(param1, param2);
          out.print("DELE " + param1 +"\r\n");
      }

        closeData();
        break;
      default :
        System.out.println("Invalid operation! \n" +
                "Usage: operation <URL1> (<URL2>)\n" +
                "1. ls <URL>\n" +
                "2. mkdir <URL>\n" +
                "3. rm <URL>\n" +
                "4. rmdir <URL>\n" +
                "5. cp <ARG1> <ARG2>\n" +
                "6. mv <ARG1> <ARG2>\n");
        System.exit(1);
    }
    disconnect();
  }
}
