import oshi.hardware.NetworkIF;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

import static java.lang.Math.pow;

public class TrafficCount {

    ArrayList<NetworkIF> net;
    Enumeration<NetworkInterface> e;

    private double recvSt;
    private double recvFin;
    private double recvBegin;

    TrafficCount(){
        net = new ArrayList<>();
        try {
            e = NetworkInterface.getNetworkInterfaces();  //Подключение сетевых интерефейсов данного пк
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        while (e.hasMoreElements()) {
            NetworkInterface ni = e.nextElement();
            System.out.println("Net interface: " + ni.getName()); //Вывод всех сетевых интерфейсов
            net.add(new NetworkIF());
            net.get(net.size() - 1).setNetworkInterface(ni);
        }
        net.get(0).updateNetworkStats();
        System.out.println("IP-адрес: " + Arrays.toString(net.get(0).getIPv4addr()));
        recvBegin = (net.get(0).getBytesRecv()) / pow(2,20); //Получение первоначальных данных о трафике
    }
    //Подсчет входящего трафика с момента запуска программы
    public double trafCount() {
        net.get(0).updateNetworkStats();
        recvFin =+ (net.get(0).getBytesRecv() / pow(2,20)) - recvBegin;
        return recvFin;
    //Подсчет скорости входящего трафика
    }
    public double trafPerSec() {
        double recvPerSec = (recvFin - recvSt);
        recvSt = recvFin;
        return recvPerSec;
    }
}
