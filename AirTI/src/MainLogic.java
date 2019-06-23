import javax.sound.sampled.LineUnavailableException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainLogic {
    public static void main(String[] arg) {
        double tem;
        double trafCount;
        double trafPerSec;
        double temMax = 0;
        double trafCountMax = 0;
        double trafPerSecMax = 0;
        TempCPU tcpu = new TempCPU();
        TrafficCount traf = new TrafficCount();
        Server server = new Server(); //Запуск сервера
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            //Ввод ограничений пользователем
            System.out.println("Введите максимально допустимую температуры ");
        try {
            temMax = Double.parseDouble(in.readLine());
            System.out.println("Введите максимально допустимое количество входящего трафика ");
            trafCountMax = Double.parseDouble(in.readLine());
            System.out.println("Введите максимальную допустимую скорость входящего трафика ");
            trafPerSecMax = Double.parseDouble(in.readLine());
            server.sendDouble(temMax);
            server.sendDouble(trafPerSecMax);
            server.sendDouble(trafCountMax);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true){
            try {
                tem = tcpu.temp();                                          //Получение температуры процессора
                System.out.println("Текущая температура = " + tem + "°");
                server.sendDouble(tem);                                     //Отправка данных на смартфон
                //Воспроизведение предупреждающего сообщения сигнала при превышения допустимой температуры
                if (tem > temMax) {
                    System.out.println("Превышение допустимой температуры");
                    Sound.tone(400, 500);
                }
                //Получение количества трафика
                trafCount = new BigDecimal(traf.trafCount()).setScale(3, RoundingMode.HALF_UP).doubleValue();
                //Получение скорости входящего трафика
                trafPerSec = new BigDecimal(traf.trafPerSec() / 5).setScale(3, RoundingMode.HALF_UP).doubleValue();
                server.sendDouble(trafPerSec);      //Отправка данных на смартфон
                server.sendDouble(trafCount);
                System.out.println("Общее количество входящего трафика = " + trafCount + " Mbytes");
                //Воспроизведение предупреждающего сообщения и сигнала при превышения допустимого количества трафика
                if(trafCount > trafCountMax){
                    System.out.println("Превышение допустимого количества входящего трафика");
                    Sound.tone(300, 500);
                }
                System.out.println("Скорость входящего трафика = " + trafPerSec + " Kbytes");
            }catch (IOException er) {
                server.disconnect();
                server = null;
                break;
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(5000);  //5 секундная пауза
            } catch (InterruptedException el) {
                el.printStackTrace();
            }

        }
    }
}
