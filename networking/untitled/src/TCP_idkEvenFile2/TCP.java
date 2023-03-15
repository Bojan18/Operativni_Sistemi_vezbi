package TCP_idkEvenFile2;

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

            String firstMsg = "hello:203213";
            os.write((firstMsg + "\n").getBytes());
            os.flush();

            while (true){
                String line = bf.readLine();
                System.out.println(line);

                if(line.equals("203213:hello")){
                    os.write(("203213:receive" + "\n").getBytes());
                    os.flush();
                }

                if(line.contains("send")){
                    String[] msg = line.split(":");
                    String fileName = msg[2];
                    File file = new File(fileName);

                    if(!file.exists()){
                        file.createNewFile();
                    }

                    FileWriter fw = new FileWriter(file);
                    while (true){
                        String str = bf.readLine();

                        if(str == null) continue;

                        if(str.equals("203213:over")){
                            break;
                        }

                        fw.write(str + "\n");
                        fw.flush();
                    }
                    os.write(("203213:size:" + file.length() + "\n").getBytes());
                    os.flush();
//                    System.out.println("203213:send:" + file.length()); 151
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        TCP tcp = new TCP("194.149.135.49", 9357);
        tcp.start();
    }
}
