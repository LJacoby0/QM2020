package org.firstinspires.ftc.teamcode.feeder.feederautos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;
import android.app.Activity;
import android.view.View;


import org.firstinspires.ftc.teamcode.feeder.HardwareFeeder;

public class SensorColor {

    private ColorSensor colorSensor;
    private DistanceSensor sensorDistance;
    private float hsvValues[] = {0F, 0F, 0F};
    private final float values[] = hsvValues;
    private final double SCALE_FACTOR = 255;
    private View relativeLayout;
    public void initColorSensor(HardwareMap hardwareMap) {
        HardwareFeeder robot = new HardwareFeeder();
//        robot.ledColorFLashYellow();
        // get a reference to the color sensor.
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
        // get a reference to the distance sensor that shares the same name.
        sensorDistance = hardwareMap.get(DistanceSensor.class, "colorSensor");

        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);
//        robot.ledColorGreen();
    }
    public void RGBToTelemetry(LinearOpMode opMode){
        HardwareFeeder robot = new HardwareFeeder();
//        robot.ledOff();
        // send the info back to driver station using telemetry function.
        opMode.telemetry.addData("Distance (cm)",
                String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)));
        opMode.telemetry.addData("Alpha", colorSensor.alpha());
        opMode.telemetry.addData("Red  ", colorSensor.red());
        opMode.telemetry.addData("Green", colorSensor.green());
        opMode.telemetry.addData("Blue ", colorSensor.blue());
        opMode.telemetry.addData("Hue", hsvValues[0]);
        opMode.telemetry.update();

        opMode.telemetry.update();
    }
    public double getRed(){
        HardwareFeeder robot = new HardwareFeeder();
//        robot.ledOff();
        return colorSensor.red();
    }
    public double getGreen(){
        HardwareFeeder robot = new HardwareFeeder();
//        robot.ledOff();
        return colorSensor.green();
    }
    public double getBlue(){
        HardwareFeeder robot = new HardwareFeeder();
//        robot.ledOff();
        return colorSensor.blue();
    }
    public boolean isYellow(){
        if (getBlue() < 100 && getGreen() > 100 && getRed() > 150){
            return true;
        } else {
            return false;
        }
    }
}
