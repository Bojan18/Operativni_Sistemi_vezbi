package TCP_idkEvenFile;

import java.io.*;
import java.net.Socket;

public class TCP extends Thread {
    public String IP = "194.149.135.49";
    public int port = 9357;

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


            while (true) {
                String serverResp = bf.readLine();
                System.out.println(serverResp);

                if (serverResp.equals("203213:hello")) {
                    os.write(("203213:receive" + "\n").getBytes());
                    os.flush();
                }
                if (serverResp.startsWith("203213:send:")) {
                    String[] message = serverResp.split(":");
                    String fileName = message[2];
                    File file = new File(fileName);

                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    try(FileWriter fw = new FileWriter(file)) {
                        while (true) {
                            String line = bf.readLine();

                            if(line == null) continue;

                            if (line.equals("203213:over")) {
                                break;
                            }

                            fw.write(line + "\n");
                            fw.flush();
                        }
                    }
                    os.write(("203213:size:" + file.length() + "\n").getBytes());
                    os.flush();
//                    System.out.println("206055:size:" + file.length());
                }
                if(serverResp.equals("Server: I received your calculated file size!")){
                    System.out.println("Success"); // ovoa ne mora mislam
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        TCP client = new TCP("194.149.135.49", 9357);

        client.start();
    }
}
