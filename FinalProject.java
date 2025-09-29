package EECS1021;

import org.firmata4j.I2CDevice;
import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;
import javax.swing.*;
import java.io.IOException;

public class FinalProject {
    static IODevice myGroveBoard;
    static Pin sensor;
    static Pin mosfet;
    static SSD1306 oled;
    static long sensVal = 0;
    static long moistSoilVal = 680;
    static String pumpState = "OFF";
    static String graphTitle = "SOIL MOISTURE VS TIME";

    public static String BPM = "0";

    public static void main(String[] args) throws IOException {
        String portName = "/dev/cu.usbserial-0001";
        myGroveBoard  = new FirmataDevice(portName);

        JFrame frame = new JFrame("");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final GraphTest chart = new GraphTest( graphTitle);
        frame.add(chart);
        frame.pack();
        frame.setVisible(true);

        try {
            myGroveBoard.start();
            myGroveBoard.ensureInitializationIsDone();
            I2CDevice i2cObject = myGroveBoard.getI2CDevice((byte) 0x3C);
            oled = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64);
            oled.init();

            sensor = myGroveBoard.getPin(17);
            sensor.setMode(Pin.Mode.ANALOG);

            mosfet = myGroveBoard.getPin(3);
            mosfet.setMode(Pin.Mode.OUTPUT);

            sensVal = sensor.getValue();

            while (true) {
                mosfet.setValue(0);
                sensVal = sensor.getValue();
                //set sensor to 1
                if (sensVal > moistSoilVal)    {
                    mosfet.setValue(1);
                    pumpState = "ON";
                } else {
                    pumpState = "OFF";
                }
                oled.clear();
                oled.getCanvas().drawString(0, 0, "Moisture: " + FinalProject.sensVal);
                oled.getCanvas().drawString(0, 10, "PUMP IS " + FinalProject.pumpState);
                oled.display();
                chart.update(sensVal);
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
