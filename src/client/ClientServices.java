package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientServices {

    protected static void connectToServer(){
        try {
            ClientVariables.socket = new Socket(ClientVariables.IP4AddressOfServer, ClientVariables.PORT);
            ClientVariables.printWriter = new PrintWriter(ClientVariables.socket.getOutputStream(), true);
            ClientVariables.bufferedReader = new BufferedReader(new InputStreamReader(ClientVariables.socket.getInputStream()));

            ClientVariables.isInServerPath = true;

        } catch (IOException e) {
            System.out.println("IP of server is incorrect or is not running");
            ClientVariables.requestLine = "";
            Request.requestIPAddressOfServer();
        }
    }

    protected static void writeToServer(String s) {
        ClientVariables.printWriter.println(" " + s);
        ClientVariables.printWriter.println("~");
        ClientVariables.printWriter.flush();
    }
}
