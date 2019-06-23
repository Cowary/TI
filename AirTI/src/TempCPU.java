import oshi.hardware.platform.linux.LinuxSensors;



public class TempCPU {

    private LinuxSensors sen;
    private double temp;

    TempCPU(){
        sen = new LinuxSensors();
    } //Получение доступа к сенсорам

    public double temp() {
        temp = sen.getCpuTemperature(); //Получение данных температуры процессора

        return temp;
    }
}
