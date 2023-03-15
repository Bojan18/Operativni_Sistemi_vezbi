import java.io.*;
import java.net.Socket;

/**
 * Да се имплементира клиент за праќање мејл кој ќе се поврзе со централен сервер.
 * Серверот го користи TCP протоколот и слуша за дојдовни конекции на порта 9357
 * на следната адреса: 194.149.135.49.
 * <p>
 * Вие треба да го имплементирате само клиентот кој ќе се поврзе со постоечкиот сервер
 * Вашата прва порака која ќе треба да ја испратите да се логирате на серверот треба
 * да биде: hello:123456 доколку 123456 е вашиот индекс.
 * <p>
 * За успешно логирање,
 * ќе добиете повратна порака од серверот во остварената конекција преку socket.
 * Повратната порака е во формат: 123456:hello\n.
 * <p>
 * Доколку ова е неуспешно,
 * потребно е да ја терминирате конекцијата и да пробате од ново да се конектирате.
 * По добивањето на потврда дека сте успешно поврзани, може да се прати барање за отпочнување на комуникација.
 * Ова барање треба да биде во формат: 123456:send: . За успешно испратено барање ќе добиете потврдна порака.
 * <p>
 * По добивањето на потврда дека сте успешно поврзани, може да се прати барање за испраќање на фајл.
 * Фајлот што е поставен на ispiti треба да биде испратен на серверот со користење на истата конекција.
 * За да го испратите фајлот, прво треба да се испрати следното заглавје:
 * 123456:attach:filename.txt Веднаш после тоа продолжувате со испраќање на целата содржина на текстуалниот фајл.
 * Откако ќе се испрати целата содржина, завршувањето на трансмисијата треба да се изврши со 123456:over
 * Доколку содржината е успешно примеа, серверот ќе испрат потврда. За било какви грешки при пренос,
 * серверот ќе ви врати соодветна порака
 * Дополнително, потребно е да ја испратите големината на фајлот преку посебна порака. Пораката за
 * испраќање на големината на фајлот е во формат: 123456:fileSize:135, во случај да вашиот индекс е
 * 123456, а големината на фајлот е 135 бајти. Оваа порака може да се прати пред или по испраќањето на фајлот и ќе се бодува посебно.
 * Треба да се имплементира само клиентот. За да се добијат бодови на задачата, треба да се изврши и да успее да искомуницира со серверот и да се прикачи кодот со кој сте ја оствариле комуникацијата.
 * Сите пораки треба да завршуваат со нов ред.
 * Остварена комуникација без прикачен код нема да се оценува.
 * Прикачен код кој не остварил комуникација нема да се оценува.
 * Во кодот внатре во коментар ставете го вашето име, презиме и број на индекс. Доколку ова не е поставено, решението нема да се прегледува.
 * Плагијати најстрого ќе бидат казнети.
 * <p>
 * Бодување:
 * успешно отворена конекција со hello порака: 20 поени
 * успешно пратено барање за комуникација: 10 поени
 * успешно испратена содржина од фајл: 20 поени
 * успешно пратена големина на фајл: 10 поени
 * Важни напомени:
 * Не отворајте повеќе од една socket конекција до серверот. Во спротивно, ќе биде блокирана комунијација со вашата ИП адреса на одредено време.
 * <p>
 * По било каква примена порака за грешка од серверот, потребно е да го рестартирате вашиот клиентски програм и да пробате одново.
 * <p>
 * Решенијата се прикачуваат на посебен линк за тоа кој е достапен на ispiti. Прикачените кодови овде нема да се земат во предвид!
 */
// 206055

import java.net.*;
import java.io.*;

public class TCP {
    public static final String IP = "194.149.135.49";
    public static final int port = 9357;

    public static void main(String[] args) {
        try (Socket socket = new Socket(IP, port)) {
            ClientReceive cr = new ClientReceive(socket);
            ClientSend cs = new ClientSend(socket, "hello:206055");

            cs.start();
            cr.start();

            cr.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientSend extends Thread {
    private final Socket socket;
    private final String message;

    public ClientSend(Socket socket, String message) {
        this.socket = socket;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println(message);
            out.flush();
            System.out.println("Sent: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientReceive extends Thread {
    private final Socket socket;

    public ClientReceive(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String str = in.readLine();

                if (str == null) {
                    continue;
                }

                System.out.println("Received: " + str);

                if (str.equals("206055:hello")) {
                    ClientSend csReceive = new ClientSend(socket, "206055:receive");
                    csReceive.start();
                }

                if (str.startsWith("206055:send")) {
                    String[] strs = str.split(":");

                    File f = new File(strs[2]);

                    if (!f.exists()) {
                        f.createNewFile();
                    }

                    try (FileWriter fw = new FileWriter(f)) {
                        while (true) {
                            String input = in.readLine();

                            if (input == null) {
                                continue;
                            }

                            if (input.equals("206055:over")) {
                                break;
                            }

                            fw.write(input);
                            fw.write("\n");

                            fw.flush();
                        }
                    }

                    ClientSend csSize = new ClientSend(socket, "206055:size:" + f.length());
                    csSize.start();
                }

//                if (str.startsWith("Server: I received your calculated file size!")) {
//                    System.out.println("SUCCESS!");
//                    System.exit(0);
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
