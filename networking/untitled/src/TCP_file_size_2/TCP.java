package TCP_file_size_2;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCP extends Thread{
    private int port = 9753;
    private String IP = "194.149.135.49";

    public TCP(int port, String IP) {
        this.port = port;
        this.IP = IP;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(IP, port);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            Scanner scanner = new Scanner(is);

            String firstMsg = "hello:203213";
            os.write((firstMsg + "\n").getBytes());
            os.flush();

            String serverResp1 = scanner.nextLine();
            System.out.println(serverResp1);

            File file = new File("C:\\Users\\38975\\Desktop\\os\\networking\\untitled\\src\\TCP_file_size_2\\file");

            String secondMsg = "203213:attach:file.txt";
            os.write((secondMsg + "\n").getBytes());
            os.flush();

            BufferedReader bf = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bf.readLine()) != null){
                os.write((line + "\n").getBytes());
                os.flush();
                System.out.println(line);
            }

            String thirdMsg = "203213:over";
            os.write((thirdMsg + "\n").getBytes());
            os.flush();

//            String serverResp2 = scanner.nextLine();
//            System.out.println(serverResp2); neznam dali treba, dava mi Bad command, valjda ne

            String fourthMsg = "203213:fileSize:";
            long fileSize = file.length();
            os.write((fourthMsg + fileSize + "\n").getBytes());
            os.flush();

            System.out.println(fourthMsg + fileSize);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        TCP tcp = new TCP(9357, "194.149.135.49");

        tcp.start();
    }

}
