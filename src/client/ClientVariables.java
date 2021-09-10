package client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientVariables {
    protected final static int PORT = 7777;
    protected static String IP4AddressOfServer = "";

    protected static String pathForServer = "D:\\";
    protected static String pathForClient = "D:\\";

    protected static boolean isInServerPath;
    protected static boolean isAdminAccessGranted = false;

    protected static Socket socket;
    protected static PrintWriter printWriter;
    protected static BufferedReader bufferedReader;

    protected static String requestLine = IP4AddressOfServer + "> ";

    protected static String command = "";
}
