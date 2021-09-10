package client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Commands {
    private static HashMap<String, String> commands = new HashMap<>();

    private static void initialize() {
        commands.put("bye", "      Disconnect from FTP server.");               //+
        commands.put("exit", "     Exit FTP application.");                     //+
        commands.put("download", " Downloads a file.");                         //+
        commands.put("ldelete", "  Deletes a file from client.");               //+

        commands.put("lls", "      Lists files of local path");                 //+
        commands.put("ls", "       Lists files of path");                       //+
        commands.put("cd", "       Changes server path.");                      //+
        commands.put("lcd", "      Changes local path");                        //+
        commands.put("help", "     Lists commands");                            //+
        commands.put("login", "    Login as admin");                            //+

        if (ClientVariables.isAdminAccessGranted) {
            commands.put("upload", "   Uploads a file.");                       //+
            commands.put("delete", "   Deletes a file from server.");           //+
        }
    }

    private static void bye() {
        try {
            ClientVariables.socket.close();
            ClientVariables.printWriter.close();
            ClientVariables.bufferedReader.close();
            ClientVariables.isAdminAccessGranted = false;
            Request.requestIPAddressOfServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void exit() {
        try {
            ClientVariables.socket.close();
            ClientVariables.printWriter.close();
            ClientVariables.bufferedReader.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void close() {
        exit();
    }

    private static void ldelete(String fileName) {
        String message;
        File file = new File(ClientVariables.pathForClient + fileName);

        if (file.delete())
            message = file.getName() + " is deleted";
        else
            message = "Delete operation is failed";

        System.out.println(message);
    }

    private static void delete() {
        try {
            System.out.println(ClientVariables.bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void download(String fileName) throws IOException {

        int length = Integer.parseInt(ClientVariables.bufferedReader.readLine());
        System.out.println("length of b array" + length);

        InputStream inputStream = ClientVariables.socket.getInputStream();

        System.out.println("filename " + fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(ClientVariables.pathForClient + fileName);

        byte[] b_arr = new byte[length];

        inputStream.read(b_arr, 0, length);


        fileOutputStream.write(b_arr, 0, b_arr.length);

        fileOutputStream.close();

        System.out.println("File receive is successful");

    }

    private static void upload(String fileName) {
        try {

            File file = new File(ClientVariables.pathForClient + fileName);
            System.out.println("File name " + file.getName());

            byte[] b_arr = Files.readAllBytes(Paths.get(String.valueOf(file)));
            System.out.println("length of b array" + b_arr.length);
            ClientVariables.printWriter.println(b_arr.length);

            OutputStream outputStream = ClientVariables.socket.getOutputStream();

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(b_arr, 0, b_arr.length);
            bufferedOutputStream.flush();

            System.out.println("File send is successful");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int lls(String path) {

        int count = 0, i = 0;

        File file = new File(path);
        File[] files = file.listFiles();

        if (files != null) {
            Arrays.sort(files);
        }

        for (File f : files) {
            if (f.canRead())
                count++;

            i++;
            System.out.println(i + ". " + f.getName());
        }

        return count;
    }

    private static int ls(String path) {

        int count = 0;
        try {
            String ls_server = ClientVariables.bufferedReader.readLine();
            String[] list = ls_server.split(";");

            for (String l :
                    list) {
                count++;
                System.out.println(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    private static void lcd(String d) {
        File file = new File(ClientVariables.pathForClient);
        File[] files = file.listFiles();
        ArrayList<String> filesAL = new ArrayList<>();

        if (files != null) {
            for (File f :
                    files) {
                filesAL.add(f.getName());
            }

            if (filesAL.contains(d))
                ClientVariables.pathForClient += (d + "\\");
            else
                System.out.println(ClientVariables.pathForClient + d + " does not exists");
        } else
            System.out.println(ClientVariables.pathForClient + " is empty");
    }

    private static void cd(String d) {
        ClientServices.writeToServer(d);

        try {
            String p = ClientVariables.bufferedReader.readLine();
            if (p.equals("dnte"))
                System.out.println(ClientVariables.pathForServer + d + " does not exists");
            else if (p.equals("ie"))
                System.out.println(ClientVariables.pathForServer + " is empty");
            else
                ClientVariables.pathForServer = p;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void help() {
        initialize();
        commands.forEach((key, value) -> System.out.println(key + value));
    }

    private static void login() {

        StringBuilder passwordCheckStatus = new StringBuilder();
        try {
            passwordCheckStatus.append(ClientVariables.bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (passwordCheckStatus.toString().equals("")) {
            ClientVariables.isAdminAccessGranted = true;
        } else {
            System.out.println("Password is not valid\n");
        }

    }

    protected static void exec(String command) {
        String[] c = command.split(" ", 2);
        switch (c[0]) {
            case "bye":
                bye();
                break;

            case "exit":
                exit();
                break;

            case "close":
                close();
                break;

            case "ldelete":
                if (c.length == 2)
                    ldelete(c[1]);
                else
                    System.out.println("Format has to be as following: ldelete <file name>");
                break;

            case "delete":
                if (ClientVariables.isAdminAccessGranted)
                    if (c.length == 2)
                        delete();
                    else
                        System.out.println("Format has to be as following: delete <file name>");
                else
                    System.out.println("You don't have admin access");
                break;

            case "download":
                try {
                    if (c.length == 2)
                        download(c[1]);
                    else
                        System.out.println("Format has to be as following: download <file name>");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "upload":
                if (ClientVariables.isAdminAccessGranted)
                    if (c.length == 2)
                        upload(c[1]);
                    else
                        System.out.println("Format has to be as following: upload <file name>");
                else
                    System.out.println("You don't have admin access");
                break;

            case "lls":
                int countC = lls(ClientVariables.pathForClient); //--
                System.out.println("There are " + countC + " files in the client directory");
                break;

            case "ls":
                int countS = ls(ClientVariables.pathForServer); //--
                System.out.println("There are " + countS + " files in the server directory");
                break;

            case "lcd":
                if (c.length == 2)
                    lcd(c[1]);
                else
                    ClientVariables.pathForClient = "D:\\";
                break;
            case "cd":
                if (c.length == 2)
                    cd(c[1]);
                else
                    ClientVariables.pathForServer = "D:\\";
                break;

            case "help":
                help();
                break;

            case "login":
                if (c.length == 2)
                    login();
                else
                    System.out.println("Format has to be as following: login <password>");
                break;

            default:
                System.out.println("Unknown command. To see commands enter help");
        }
        System.out.println("Local path: " + ClientVariables.pathForClient);
        Request.requestCommand();
    }
}
