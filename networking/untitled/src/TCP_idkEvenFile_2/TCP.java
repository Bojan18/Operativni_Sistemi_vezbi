package TCP_idkEvenFile_2;

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
                    String secondMsg = "203213:receive";
                    os.write((secondMsg + "\n").getBytes());
                    os.flush();
                }
                if (serverResp.startsWith("203213:send")) {
                    String[] strs = serverResp.split(":");
                    String fileName = strs[2];

                    File file = new File(fileName);

                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    try (FileWriter fw = new FileWriter(fileName)) {
                        while (true) {
                            String line = bf.readLine();
                            if (line == null) {
                                continue;
                            }
                            if (line.endsWith(":over")) {
                                break;
                            }

                            fw.write(line + "\n");
                            fw.flush();
                        }
                    }
                    String thirdMsg = "203213:size:" + file.length();
                    os.write((thirdMsg + "\n").getBytes());
                    os.flush();
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
