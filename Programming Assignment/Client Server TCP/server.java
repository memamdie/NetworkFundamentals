import java.net.*;
import java.io.*;

public class server {

  private Socket socket             = null;
  private ServerSocket serverSocket = null;
  private BufferedReader streamIn   = null;
  private PrintStream streamOut     = null;

    public static void main(String[] args) throws IOException {
        server s = null;
        if (args.length != 1) {
            System.err.println("Usage: java server <port number>");
            System.exit(1);
        }
        else {
          int portNumber = Integer.parseInt(args[0]);
          s = new server(portNumber);
        }
    }

    public server(int portNumber) {
      try {
        serverSocket = new ServerSocket(portNumber);
        socket = serverSocket.accept();
        streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        streamOut = new PrintStream(socket.getOutputStream());
        System.out.println("get connection from ... " + socket.getRemoteSocketAddress().toString());
        streamOut.println("Hello");
        run();
        close();
        System.exit(0);
      }
      catch (IOException e) {
        System.out.println(e);
      }
    }
    private void run() {
      while (true) {
        try {
          String input = streamIn.readLine();
          int result = performOperation(input);
          System.out.println("get:" + input + ", return: " + result);
          if (result == -5) {
            System.exit(0);
            break;
          }
          else {
            streamOut.println(Integer.toString(result));
          }
        }
        catch (IOException e) {
          System.out.println(e);
        }
      }
    }
    private void close() {
      try {
        if (socket != null)    socket.close();
        if (streamIn != null)  streamIn.close();
      }
      catch (IOException e) {
        System.out.println(e);
      }
    }
    private int performOperation(String input) {
        int[] arr = {0, 0, 0, 0};
        int output = 0;
        int index = 0;
        //find the first space
        int firstSpace = input.indexOf(' ');
        if (input.equals("bye")) return -5;
        //get from start of string to first space
        String operation = input.substring(0, firstSpace);
        if (!(operation.equals("add") || operation.equals("subtract") || operation.equals("multiply"))) return -1;
        //remove bottom part of string (operation)
        input = input.substring(firstSpace+1);
        //while you havent reached the end of the string get the number and insert it into an array
        while(input.length() > 0) {
            if (index > 3) return -3;
            int whiteSpace = input.indexOf(' ');
            if (whiteSpace != -1) {
                try {
                    int foundNumber = Integer.parseInt(input.substring(0, whiteSpace));
                    arr[index] = foundNumber;
                }
                catch (NumberFormatException e) {
                    return -4;
                }
                index++;
                input = input.substring(whiteSpace+1);
            }
            else {
                try {
                    int foundNumber = Integer.parseInt(input);
                    arr[index] = foundNumber;
                }
                catch (NumberFormatException e) {
                    return -4;
                }
                index++;
                input = "";
            }
        }
        if (index < 2) return -2;
        //if statement to determine operation
        switch (operation) {
            //iterate through array to perform operation with array operands
            case "add":
                for (int i = 0; i < index; i++) {
                    output += arr[i];
                }
                break;
            case "subtract":
                for (int i = 0; i < index; i++) {
                    if (i == 0) {
                      output = arr[i];
                    }
                    else {
                      output -= arr[i];
                    }
;                }
                break;
            case "multiply":
                for (int i = 0; i < index; i++) {
                    output *= arr[i];
                }
                break;
            default:
                break;

        }
        return output;
    }
}
