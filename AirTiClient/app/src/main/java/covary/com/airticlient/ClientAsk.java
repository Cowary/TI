package covary.com.airticlient;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;



public class ClientAsk extends AsyncTask<Void, Double, Void>  {

    private TextView[] t;
    private TextView[] err;
    private double maxTem;
    private double maxSpeed;
    private double maxRec;
    private String text = "";
    private double value1;
    private double value2;
    private double value3;

    private Socket socket;
    private DataInputStream in;
    private int serverPort = 9056; // здесь обязательно нужно указать порт к которому привязывается сервер.
    private String address = ""; // это IP-адрес компьютера, где исполняется наша серверная программа.
    private Integer[] values;

    //Получение данных из активности
    ClientAsk(TextView[] t, TextView[] err, String address)  {
        this.t = t;
        this.err = err;
        this.address = address;
    }

    //Подключение к серверу и получение ограничений с него
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            InetAddress ipAddress = null; // создаем объект который отображает вышеописанный IP-адрес.
            ipAddress = InetAddress.getByName(address);
            System.out.println("Подключение по адресу: " + address + " и порту: " + serverPort);
            socket = new Socket(ipAddress, serverPort); // создаем сокет используя IP-адрес и порт сервера.
            System.out.println("Подключение к сервуру успешно ");
            // Берем входной поток сокета, теперь можем получать данные от сервера.
            InputStream sin = socket.getInputStream();
            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            in = new DataInputStream(sin);
            //Получаем ограния с сервера
            maxTem = in.readDouble();
            maxSpeed = in.readDouble();
            maxRec = in.readDouble();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Получение основных данных с сервера и отображение их
    @Override
    protected Void doInBackground(Void... params) {
        while (true) {
            //Отключение сокета по нажатию кнопки стоп
            try {
                if(isCancelled()){
                    socket.close();
                    return null;
                }
                //Получение данных от сервера
                value1 = in.readDouble();
                value2 = in.readDouble();
                value3 = in.readDouble();
                //Вывод полученных данных
                publishProgress(value1,value2,value3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Double ... values) {
        super.onProgressUpdate(values);
        text = Double.toString(values[0]);
        t[0].setText((text + "°")); //Вывод температуры
        //Проверка не вышла ли температура за ограничения
        if (values[0] > maxTem) {
            err[0].setText("Превышение допустимой температуры процессора");
            err[0].setVisibility(View.VISIBLE);
        }else if(values[0] <= maxTem){
            err[0].setVisibility(View.INVISIBLE);
        }
        text = Double.toString(values[1]);
        t[1].setText((text + " Kbytes/sec"));//Вывод скорости
        //Проверка не вышла ли скорость за ограничения
        if (values[1] > maxSpeed) {
            err[1].setText("Превышение допустимой скорости входящего трафика");
            err[1].setVisibility(View.VISIBLE);
        }else if(values[1] <= maxSpeed){
            err[1].setVisibility(View.INVISIBLE);
        }
        text = Double.toString(values[2]);
        t[2].setText((text + " Mbytes")); //Вывод трафика
        //Прооверка лимита трафика
        if (values[2] > maxRec) {
            err[2].setText("Превышение допустимого количества входящего трафика");
            err[2].setVisibility(View.VISIBLE);
        }else if(values[2] <= maxRec){
            err[2].setVisibility(View.INVISIBLE);
        }
    }
    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
    
}
