import java.io.*;
import java.net.*;
import java.util.*;

public class client {
  private Socket socket              = null;
  private BufferedReader streamIn    = null;
  private PrintStream streamOut      = null;

  public static void main(String[] args) throws IOException {
    client c = null;
    if (args.length != 2) {
      System.err.println(
      "Usage: java client <host name> <port number>");
      System.exit(1);
    }
    else {
      String hostName = args[0];
      int portNumber = Integer.parseInt(args[1]);
      c = new client(hostName, portNumber);
    }
  }
  public client(String hostName, int portNumber) {
    try {
      socket = new Socket(hostName, portNumber);
      streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      streamOut = new PrintStream(socket.getOutputStream());
      run();
      close();
    }
    catch (IOException e) {
      System.out.println(e);
    }
    catch(NullPointerException e) {
      System.exit(0);
    }
  }

  private void run(){
    //Should be hello message
    try {
      System.out.println(streamIn.readLine());
    }
    catch(IOException e) {
      System.out.println(e);
    }
    String scannerInput = "";
    loop : while(true) {
      try {
        Scanner scanner = new Scanner(System.in);
        scannerInput = scanner.nextLine();
        streamOut.println(scannerInput);
        String serverOutput = streamIn.readLine();

        switch (serverOutput) {
          case "-1":
          System.out.println("receive: incorrect operation command.");
          break;
          case "-2":
          System.out.println("receive: number of inputs is less than two.");
          break;
          case "-3":
          System.out.println("receive: number of inputs is more than four.");
          break;
          case "-4":
          System.out.println("receive: one or more of the inputs contain(s) non-number(s).");
          break;
          case "-5":
          System.out.println("receive: exit.");
          break loop;
          default:
          System.out.println("receive: " + serverOutput);
          break;
        }
      }
      catch (IOException e) {
        System.out.println(e);
      }
    }
  }

  private void close() {

    try{
      streamIn.close();
      streamOut.close();
      socket.close();
    }
    catch(IOException e) {
      System.out.println(e);
    }
  }
}
