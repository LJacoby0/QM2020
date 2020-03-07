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

        runtime.reset();
        waitForStart();

        while (opModeIsActive()) {
            pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
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

            //TODO: Test Red sIde
            //Move forwards until 4cm away from block
            rb.setPlatformUp(true);
            rb.blockUp(); //Set Other Mechanisms out of the way

            rb.driveForwardByEncoder(1370, rb.BL, .69);
            telemetry.addData("2. Block Found Hue", hsvValues[0]);
            telemetry.update();
            runtime.reset();
            //Pause after moving to block
            while (runtime.seconds() < 1.0) { //TODO: Adjust this timing
                rb.driveStop();
            }
            runtime.reset();
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

            double backToBlockBeginning = runtime.seconds();
            runtime.reset();

            rb.driveForwardByEncoder(200, rb.BL, .20); //Go forward a little bit

            rb.blockDown(); //Grab first skystone //TODO: Fix this

            sleep(1500); //stop  after finding block for servo to go down

            rb.driveForwardByEncoder(-274, rb.BL, .5); //Back Up a little

            runtime.reset(); //Reset Time
            while (runtime.seconds()<backToBlockBeginning) {
                rb.strafe(blueNegativeFactor * -0.25); //go Back to beginning of block line
            }

            int strafeToMidPoint = (int) (4346 * blueNegativeFactor);
            rb.strafeRightByEncoder(strafeToMidPoint, rb.BL, .7); //go to midpoint for blocks //TODO: Check to see where this ends up
            rb.blockUp(); //Eject skystone
            sleep(1000);
            int strafeToPlatform = (int) (2173 * blueNegativeFactor);
            rb.strafeRightByEncoder(strafeToPlatform, rb.BL, .7); //go to platform for blocks //TODO: Check to see where this ends up

            rb.driveForwardByEncoder(300,rb.BL, .18);//Move slightly into platform //TODO: Check to see where this ends up

            rb.setPlatformUp(false); //Put platform grabbers down

            rb.driveForwardByEncoder(-500,rb.BL, .20); //Move backwards a little TODO: check values

            int turnTo = (int) (2173 * blueNegativeFactor);
            rb.turnClockwiseByEncoder(turnTo, rb.BL, 0.14); //Rotate 90  TODO: find values for rotate

            runtime.reset();
            while (runtime.seconds() < 6) {
                rb.tapeOut();
                rb.driveForwardByEncoder(-500, rb.BL, 0.40); //Push into wall TODO: find values

            }

            rb.setPlatformUp(true); //Lift platform pullers before end of auto

            stop();

        }
    }
}