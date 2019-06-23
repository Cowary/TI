import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
     ServerSocket ss;
     Socket socket;
     OutputStream sout;
     DataOutputStream out;
     Server() {
            int port = 9056;
            try {
                ss = new ServerSocket(port);  // создаем сокет сервера и привязываем его к вышеуказанному порту
                System.out.println("Ожидается подключение клиента...");

                socket = ss.accept(); // заставляем сервер ждать подключений и выводим сообщение клиент связался с сервером
                System.out.println("Клиент подклчен.");
                System.out.println();

                //Выходной поток сокета, теперь можно отсылать данные клиенту.
                sout = socket.getOutputStream();

                // Конвертируем поток в другой тип, чтоб легче обрабатывать текстовые сообщения.
                out = new DataOutputStream(sout);

            } catch(Exception x) { x.printStackTrace(); }
        }
    //Отправка данных формата double клиенту
    public void sendDouble(Double ms) throws IOException {
            out.writeDouble(ms);
            out.flush();
    }
    //Отключение сокета
    public void disconnect(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

