/**
 * Да се имплементира клиент за праќање мејл кој ќе се поврзе со централен сервер.
 * Серверот го користи TCP протоколот и слуша за дојдовни конекции на порта 9753
 * на следната адреса: 194.149.135.49. Вие треба да го имплементирате само клиентот кој ќе се поврзе со постоечкиот сервер
 * Вашата прва порака која ќе треба да ја испратите да се логирате на серверот треба
 * да биде: hello:123456 доколку 123456 е вашиот индекс. За успешно логирање,
 * ќе добиете повратна порака од серверот во остварената конекција преку socket.
 * -----------------------------------------------------1!!!
 * <p>
 * Повратната порака е во формат: 123456:hello\n. Доколку ова е неуспешно,
 * потребно е да ја терминирате конекцијата и да пробате од ново да се конектирате.
 * По добивањето на потврда дека сте успешно поврзани, може да се прати барање за отпочнување на комуникација.
 * Ова барање треба да биде во формат: 123456:send: . За успешно испратено барање ќе добиете потврдна порака.
 * <p>
 * По добивањето на потврда дека сте успешно поврзани, може да се прати барање за испраќање на фајл.
 * Фајлот што е поставен на ispiti треба да биде испратен на серверот со користење на истата конекција.
 * За да го испратите фајлот, прво треба да се испрати следното заглавје:
 * 123456:attach:filename.txt Веднаш после тоа продолжувате со испраќање на целата содржина на текстуалниот фајл.
 * -----------------------------------------------------2!!!
 * <p>
 * Откако ќе се испрати целата содржина, завршувањето на трансмисијата треба да се изврши со 123456:over
 * Доколку содржината е успешно примеа, серверот ќе испрат потврда. За било какви грешки при пренос,
 * серверот ќе ви врати соодветна порака
 * <p>
 * Дополнително, потребно е да ја испратите големината на фајлот преку посебна порака. Пораката за
 * испраќање на големината на фајлот е во формат: 123456:fileSize:135, во случај да вашиот индекс е
 * 123456, а големината на фајлот е 135 бајти. Оваа порака може да се прати пред или по испраќањето на фајлот и ќе се бодува посебно.
 * Треба да се имплементира само клиентот. За да се добијат бодови на задачата, треба да се изврши и да успее да искомуницира со серверот и да се прикачи кодот со кој сте ја оствариле комуникацијата.
 * Сите пораки треба да завршуваат со нов ред.
 *
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

package TCP2_file;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

//public class TCP_fileSizeAndContent extends Thread {
//    private String ip;
//    private int port;
//    InputStream inputStream;
//    OutputStream outputStream;
//    Scanner scanner;
//    BufferedReader bufferedReader;
//
//    public TCP_fileSizeAndContent(String ip, int port) {
//        this.ip = ip;
//        this.port = port;
//    }
//
//    @Override
//    public void run() {
//        try {
//            Socket socket = new Socket(ip, port);
//            inputStream = socket.getInputStream();
//            outputStream = socket.getOutputStream();
//            scanner = new Scanner(inputStream);
//            String firstMessage = "hello:203213";
//            outputStream.write((firstMessage + "\n").getBytes(StandardCharsets.UTF_8));
//            outputStream.flush();
//
//            String serverResponse = scanner.nextLine();
//            System.out.println(serverResponse);
//
////             String secondMessage = "203213:send:";
////             outputStream.write((secondMessage + "\n").getBytes(StandardCharsets.UTF_8));
//////             outputStream.flush();
////            String serverSecondResponse = scanner.nextLine();
////            System.out.println(serverSecondResponse);
////
////
////            File file = new File("C:\\Users\\38975\\Desktop\\os\\networking\\untitled\\src\\TCP2_file\\mailContentsFile.txt");
////
////            bufferedReader = new BufferedReader(new FileReader(file));
////            String line;
////
////            outputStream.write(("203213:attach:mailContentsFile.txt" + "\n").getBytes(StandardCharsets.UTF_8));
////            outputStream.flush();
////            while ((line=bufferedReader.readLine()) != null){
////                outputStream.write((line + "\n").getBytes(StandardCharsets.UTF_8));
////                outputStream.flush();
////                System.out.println(line);
////            }
////
////            outputStream.write(("203213:over" + "\n").getBytes(StandardCharsets.UTF_8));
////            outputStream.flush();
////
////
////
////            String serverResponse2 = scanner.nextLine();
////            System.out.println(serverResponse2);
////
////            long fileSize = file.length();
////            String sendFileSize = "203213:fileSize:";
////            outputStream.write((sendFileSize + fileSize + "\n").getBytes(StandardCharsets.UTF_8));
////            outputStream.flush();
////            System.out.println(sendFileSize + fileSize);
////
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static void main(String[] args) {
//        TCP_fileSizeAndContent client = new TCP_fileSizeAndContent("194.149.135.49" , 9357 );
//        client.start();
//    }
//}
public class TCP_fileSizeAndContent extends Thread {

    private int port = 9357;
    private String IP = "194.149.135.49";
    public TCP_fileSizeAndContent(String IP, int port) {
        this.port = port;
        this.IP = IP;
    }

    @Override
    public void run() {
        //Prov pravam socket!, socketo prima IP i port
        //sam ke se stave u try/catch
        try {
            Socket socket = new Socket(IP, port);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            Scanner scanner = new Scanner(is);
            String firstMessage = "hello:203213";

            os.write((firstMessage + "\n").getBytes());
            os.flush();

            String serverResponse = scanner.nextLine();
            System.out.println(serverResponse);

//            String secondMessage = "203213:send";
//            os.write((secondMessage + "\n").getBytes());
//            os.flush();
//
//            String serverResponse2 = scanner.nextLine();
//            System.out.println(serverResponse2);

            File file = new File("C:\\Users\\38975\\Desktop\\os\\networking\\untitled\\src\\TCP2_file\\mailContentsFile.txt");

            String thirdMessage = "203213:attach:mailContentsFile.txt";
            os.write((thirdMessage + "\n").getBytes());
            os.flush();

            System.out.println(scanner.nextLine());

            BufferedReader bf = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bf.readLine()).equals("203213:over")){
                os.write((line + "\n").getBytes());
                os.flush();
                System.out.println(line);
            }

//            String fourthMessage = "203213:over";
//            os.write((fourthMessage + "\n").getBytes());
//            os.flush();
//
//            String serverResponse3 = scanner.nextLine();
//            System.out.println(serverResponse3);

            long fileSize = file.length();
            String fifthMessage = "203213:fileSize:";
            os.write((fifthMessage + fileSize + "\n").getBytes());
            os.flush();

            System.out.println(fifthMessage + fileSize);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        TCP_fileSizeAndContent client = new TCP_fileSizeAndContent("194.149.135.49", 9753);
        client.start();
    }
}
