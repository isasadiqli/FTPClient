package client;

import colors.ConsoleColors;

import java.util.Scanner;

public class Request {

    protected static void requestIPAddressOfServer() {

        Scanner scanner = new Scanner(System.in);
        System.out.println(ConsoleColors.CYAN + "Please enter IPv4 address of server you want to connect or enter exit to exit: " + ConsoleColors.RESET);
        System.out.print("IP> ");
        ClientVariables.IP4AddressOfServer = scanner.nextLine();

        if (ClientVariables.IP4AddressOfServer.equals("exit"))
            System.exit(0);

        ClientVariables.requestLine = (ClientVariables.IP4AddressOfServer + "> " + ClientVariables.pathForServer + "> ");

        ClientServices.connectToServer();
        requestCommand();
    }

    protected static void requestCommand() {
        if (ClientVariables.isAdminAccessGranted)
            System.out.print(ConsoleColors.YELLOW + "admin> " + ClientVariables.IP4AddressOfServer + "> " + ClientVariables.pathForServer + "> " + ConsoleColors.RESET);
        else
            System.out.print(ConsoleColors.YELLOW + ClientVariables.IP4AddressOfServer + "> " + ClientVariables.pathForServer + "> " + ConsoleColors.RESET);
        Scanner scanner = new Scanner(System.in);

        ClientVariables.command = scanner.nextLine();
        ClientServices.writeToServer(ClientVariables.command);
        Commands.exec(ClientVariables.command);
    }


}
