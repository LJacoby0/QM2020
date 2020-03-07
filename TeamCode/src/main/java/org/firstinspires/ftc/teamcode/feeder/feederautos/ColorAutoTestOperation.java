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


            rb.driveForwardByEncoder(-1450, rb.BL, .90);

            boolean inBlock = true;
            int blockNumber = 0;
            int blockInterval = (int) (blueNegativeFactor *-500);

            while (inBlock)
            {


                Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                        (int) (sensorColor.green() * SCALE_FACTOR),
                        (int) (sensorColor.blue() * SCALE_FACTOR),
                        hsvValues);

                if (hsvValues[0] >= 90) {
                    inBlock = false;


                }
                else {
                    inBlock=true;
                    blockNumber++;
                    rb.strafeRightByEncoder(blockInterval, rb.BL, 0.25);
                }
            }
            rb.driveForwardByEncoder(-100, rb.BL, .40);
            rb.turnClockwiseByEncoder(-500, rb.BL,.50 );
            rb.intakeIn(1);
            rb.driveForwardByEncoder(-600, rb.BL, .40);
            rb.turnClockwiseByEncoder(500, rb.BL,.70 );



//            rb.intakeIn(1);
//
//            rb.turnClockwiseByEncoder(-1100, rb.BL,.50);
//
//            rb.driveForwardByEncoder(-400, rb.BL, .20);
//
//            rb.driveForwardByEncoder(800, rb.BL, .90);
//
//            rb.turnClockwiseByEncoder(1100, rb.BL,.50 );
//
//            rb.strafeRightByEncoder(-1600, rb.BL, 1); //left
//
//            rb.intakeOut();
//
//            sleep(1000);
//
//            rb.strafeRightByEncoder(3000, rb.BL, 1);
//
//            rb.turnClockwiseByEncoder(-450, rb.BL,.50 );

//            telemetry.addData("2. Block Found Hue", hsvValues[0]);
//            telemetry.update();
//            runtime.reset();
//            //Pause after moving to block
//            while (runtime.seconds() < 1.0) { //TODO: Adjust this timing
//                rb.driveStop();
//            }
//            runtime.reset();
//            //Drive until block is black
////            do {
////                Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
////                        (int) (sensorColor.green() * SCALE_FACTOR),
////                        (int) (sensorColor.blue() * SCALE_FACTOR),
////                        hsvValues);
////                rb.strafe(blueNegativeFactor * 0.25);
////            }
//
//            boolean inBlock = true;
//            int blockNumber = 0;
//            int blockInterval = (int) (blueNegativeFactor *700);
//
//            while (inBlock== true)
//            {
//                rb.strafeRightByEncoder(blockInterval, rb.BL, 0.25);
//                sleep(2000);
//
//                Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
//                        (int) (sensorColor.green() * SCALE_FACTOR),
//                        (int) (sensorColor.blue() * SCALE_FACTOR),
//                        hsvValues);
//
//                if (hsvValues[0] <= 110) {
//                    inBlock=true;
//                    blockNumber++;
//
//                }
//                else {
//                    inBlock=false;
//                }
//            }
//
//
//            sleep(9000);
//
//            double backToBlockBeginning = runtime.seconds();
//            runtime.reset();
//            int alignBlockGrabber = (int) (-275 * blueNegativeFactor);
//            rb.strafeRightByEncoder(alignBlockGrabber,rb.BL ,20);
//
//            rb.driveForwardByEncoder(500, rb.BL, .20); //Go forward a little bit
//
//
//            sleep(500);
//            rb.blockDown(); //Grab first skystone //TODO: Fix this
//
//            sleep(1500); //stop  after finding block for servo to go down
//
//            rb.driveForwardByEncoder(-750, rb.BL, .5); //Back Up a little
//
//            runtime.reset(); //Reset Time
//            int backToBlock = (int) (-700* blockNumber * blueNegativeFactor);
//                rb.strafeRightByEncoder(backToBlock, rb.BL, .25); //go Back to beginning of block line
//
//
//            int strafeToMidPoint = (int) (-3350 * blueNegativeFactor);
//            rb.strafeRightByEncoder(strafeToMidPoint, rb.BL, .7); //go to midpoint for blocks //TODO: Check to see where this ends up
//            rb.blockUp(); //Eject skystone
//            sleep(1000);
//
//            int strafeToPlatform = (int) (-1860 * blueNegativeFactor);\

//            rb.strafeRightByEncoder(strafeToPlatform, rb.BL, .7); //go to platform for blocks //TODO: Check to see where this ends up
//
//            rb.driveForwardByEncoder(1200,rb.BL, .18);//Move slightly into platform //TODO: Check to see where this ends up
//
//            rb.setPlatformUp(false); //Put platform grabbers down
//            sleep(1000);
//            rb.driveForwardByEncoder(-1500,rb.BL, .59); //Move backwards a little TODO: check values
//
//            int turnTo = (int) (-1900 * blueNegativeFactor);
//            rb.turnClockwiseByEncoder(turnTo, rb.BL, 0.20); //Rotate 90  TODO: find values for rotate
//
//            runtime.reset();
//            while (runtime.seconds() < 4) {
//                rb.tapeOut();
//                //rb.driveForwardByEncoder(-1000, rb.BL, 0.60); //Push into wall TODO: find values
//
//            }
//
//            rb.setPlatformUp(true); //Lift platform pullers before end of auto

            stop();

        }
    }
}