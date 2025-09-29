package EECS1021;

import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;

import java.io.IOException;

public class SoilTest {

    static IODevice myGroveBoard;
    static Pin sensor;
    static long sensVal = 0;
    public static String BPM = "0";


    public static void main(String[] args) throws IOException {
        String portName = "/dev/cu.usbserial-0001";
        myGroveBoard  = new FirmataDevice(portName);

        try {
            myGroveBoard.start();
            myGroveBoard.ensureInitializationIsDone();

            sensor = myGroveBoard.getPin(17);
            sensor.setMode(Pin.Mode.ANALOG);

            while (true) {
                long sensVal = sensor.getValue();
                System.out.println(sensVal);
                Thread.sleep(300);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        finally {
            myGroveBoard.stop();
        }
    }
}
