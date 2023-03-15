package TCP_FileSizeAndContent_2;

import java.io.*;
import java.net.Socket;

public class TCP extends Thread{
    private String IP;
    private int port;

    public TCP(String IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(IP, port);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            File file = new File("C:\\Users\\38975\\Desktop\\os\\networking\\untitled\\src\\TCP_FileSizeAndContent_2\\mailContentsFile.txt");
            BufferedReader bf1 = new BufferedReader(new FileReader(file));

            String firstMsg = "hello:203213";
            os.write((firstMsg + "\n").getBytes());
            os.flush();

            while (true){
                String serverResp = bf.readLine();
                System.out.println(serverResp); //203213:hello treba da vrne

                String secondMsg = "203213:send:";
                os.write((secondMsg + "\n").getBytes());
                os.flush();
                //posle ovoa servero treba da vrne nesto idk so

                String thirdMsg = "203213:attach:mailContentsFile.txt";
                os.write((thirdMsg + "\n").getBytes());
                os.flush();

                String line;
                while ((line = bf.readLine()).equals("203213:over")){
                    os.write((line + "\n").getBytes());
                    os.flush();
                    System.out.println(line);
                }

                String fourthMsg = "203213:fileSize:";
                os.write((fourthMsg + file.length() + "\n").getBytes());
                os.flush();
                System.out.println("203213:fileSize:" + file.length());

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        TCP tcp = new TCP("194.149.135.49", 9753);
        tcp.start();
    }

}
