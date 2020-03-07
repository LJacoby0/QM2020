package org.firstinspires.ftc.teamcode.feeder.feederautos;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcontroller.external.samples.SampleRevBlinkinLedDriver;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;


import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.teamcode.feeder.HardwareFeeder;

import static org.firstinspires.ftc.teamcode.feeder.feederautos.Alliance.BLUE;

import static org.firstinspires.ftc.teamcode.feeder.feederautos.Alliance.RED;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

public abstract class ColorAutoTestOperation extends LinearOpMode {

    ColorSensor sensorColor;
    DistanceSensor sensorDistance;
    Orientation lastAngles = new Orientation();
    double globalAngle, power = .30, correction;
    IMU imu = new IMU();

    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;


    public abstract Alliance getAlliance();

    @Override
    public void runOpMode() throws InterruptedException {


        HardwareFeeder rb = new HardwareFeeder();
        rb.init(hardwareMap, this);

        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        pattern = RevBlinkinLedDriver.BlinkinPattern.STROBE_GOLD;
        blinkinLedDriver.setPattern(pattern);

        double blueNegativeFactor = getAlliance() == BLUE ? -1 : 1;

        ElapsedTime runtime = new ElapsedTime();
// get a reference to the color sensor.
        sensorColor = hardwareMap.get(ColorSensor.class, "colorSensor");

        // get a reference to the distance sensor that shares the same name.
        sensorDistance = hardwareMap.get(DistanceSensor.class, "colorSensor");

        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // sometimes it helps to multiply the raw RGB values with a scale factor
        // to amplify/attentuate the measured values.
        final double SCALE_FACTOR = 255;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        // wait for the start button to be pressed.

        //block mechanism
//        robot.blockUp(true);//goes up to release block
//        robot.blockUp(false);//goes down to pick up block

        //robot.ledColorGreen();
        pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
        blinkinLedDriver.setPattern(pattern);

        runtime.reset();
        waitForStart();

        while (opModeIsActive()) {
            pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
            blinkinLedDriver.setPattern(pattern);            // convert the RGB values to HSV values.
            // multiply by the SCALE_FACTOR.
            // then cast it back to int (SCALE_FACTOR is a double)
            Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                    (int) (sensorColor.green() * SCALE_FACTOR),
                    (int) (sensorColor.blue() * SCALE_FACTOR),
                    hsvValues);

            // send the info back to driver station using telemetry function.
            telemetry.addData("Distance (cm)",
                    String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)));
            telemetry.addData("Alpha", sensorColor.alpha());
            telemetry.addData("Red  ", sensorColor.red());
            telemetry.addData("Green", sensorColor.green());
            telemetry.addData("Blue ", sensorColor.blue());
            telemetry.addData("Hue", hsvValues[0]);
            telemetry.update();


            // change the background color to match the color detected by the RGB sensor.
            // pass a reference to the hue, saturation, and value array as an argument
            // to the HSVToColor method.
            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
                }
            });

            telemetry.update();
            //Move forwards until 4cm away from block
            rb.driveForwardByEncoder(1370, rb.BL, .69);

            telemetry.update();

            telemetry.addData("2. Block Found Hue", hsvValues[0]);
            telemetry.update();
            runtime.reset();

            while (runtime.seconds() < 1.5) {
                rb.driveStop();
            }

            //Drive until block is black
            do {
                Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                        (int) (sensorColor.green() * SCALE_FACTOR),
                        (int) (sensorColor.blue() * SCALE_FACTOR),
                        hsvValues);
                rb.strafe(blueNegativeFactor * 0.25);
            }
            while (hsvValues[0] <= 103);
            {

            }

            rb.driveForwardByEncoder(200, rb.BL, .20); //Go forward a little bit
            rb.blockDown(); //Grab block

            sleep(3000); //stop for 3 seconds after finding block

            runtime.reset();
            //timed auto into position

            //robot.ledColorOrange();
//            while(runtime.seconds() < 0.3) {
//                robot.drive(0.1);
//            }

            //Servo code to grab block here
            //robot.ledColorGreen();


            runtime.reset();
            rb.driveForwardByEncoder(-750, rb.BL, .5);

            rb.strafeRightByEncoder(300, rb.BL, .3);




            stop();

        }
    }
}