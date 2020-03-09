package org.firstinspires.ftc.teamcode.feeder.feederautos;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.feeder.HardwareFeeder;

import java.util.Locale;

import static org.firstinspires.ftc.teamcode.feeder.feederautos.Alliance.BLUE;

public abstract class TWOSkyStoneOperation extends LinearOpMode {

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

        double blueNegativeFactor;
        if (getAlliance() == BLUE) blueNegativeFactor = -1;
        else blueNegativeFactor = 1;

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
            // multiply by the SCALE_FACTOR.
            // then cast it back to int (SCALE_FACTOR is a double)
            Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                    (int) (sensorColor.green() * SCALE_FACTOR),
                    (int) (sensorColor.blue() * SCALE_FACTOR),
                    hsvValues);

            // send the info back to driver station using telemetry function.
            telemetry.addData("Distance (cm)", String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)));
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

            //After Setup:

            rb.setPlatformUp(true);
            rb.blockUp();
            //Set Other Mechanisms out of the way

            rb.driveForwardByEncoder(-1450, rb.BL, .90); //Drive Up to Blocks with intake in front

            boolean inBlock = true;
            int blockNumber = 0;
            int blockInterval = (int) (blueNegativeFactor * -500);
            int parkingturn = (int)(blueNegativeFactor * 1300); //test this

            while (inBlock) {
                Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                        (int) (sensorColor.green() * SCALE_FACTOR),
                        (int) (sensorColor.blue() * SCALE_FACTOR),
                        hsvValues); //GET HUE VALUE
                sleep(500);
                if (hsvValues[0] >= 100 || blockNumber >= 4) {
                    inBlock = false;
                } else {
                    inBlock = true;
                    blockNumber++;
                    rb.strafeRightByEncoder(blockInterval, rb.BL, 0.25);
                }
            }


            if(blockNumber >= 4 ){//failsafe
                int failsafestrafe = 0;
                if(blueNegativeFactor == -1){
                    failsafestrafe = 150;
                }
                else{
                    failsafestrafe = -500;
                }
                rb.strafeRightByEncoder(failsafestrafe, rb.BL, 0.25);
                rb.driveForwardByEncoder(-100, rb.BL, .40); //move forward
                rb.intakeIn(1); //turn intake on
                rb.turnClockwiseByEncoder(500, rb.BL, .50); //rotate into block
                rb.driveForwardByEncoder(-600, rb.BL, .40); // move into block
                rb.turnClockwiseByEncoder(-360, rb.BL, .50);//rotate back
                rb.driveForwardByEncoder(850, rb.BL, .60); //back out a little
                rb.intakeIn(.4); //slow dowhile
                int strafeToEject = (int) (blueNegativeFactor * (4000 + Math.abs(blockInterval * blockNumber))); //value for Blocks --> 3350 + number of blocks * blockInterval value
                rb.strafeRightByEncoder(strafeToEject, rb.BL, .80); //Strafe to eject
                rb.intakeOut(); //eject skystone
                rb.turnClockwiseByEncoder(parkingturn, rb.BL, .50);//rotate to park
                runtime.reset();
                while (runtime.seconds() < 1.75) {
                    rb.tapeOut();
                }
                rb.tapeStop();
                stop();
            }


            rb.driveForwardByEncoder(-100, rb.BL, .40); //move forward
            rb.intakeIn(.9); //turn intake on
            rb.turnClockwiseByEncoder(-500, rb.BL, .50); //rotate into block
            rb.driveForwardByEncoder(-600, rb.BL, .40); // move into block
            rb.intakeIn(.6); //slow down the intake speed so we mess something up
            rb.turnClockwiseByEncoder(360, rb.BL, .50);//rotate back
            rb.driveForwardByEncoder(850, rb.BL, .60); //back out a little
            int strafeToEject = (int) (blueNegativeFactor * (3450 + Math.abs(blockInterval * blockNumber))); //value for Blocks --> 3350 + number of blocks * blockInterval value
            rb.strafeRightByEncoder(strafeToEject, rb.BL, .80); //Strafe to eject
            rb.intakeOut(); //eject skystone
            sleep(1200); // wait for block to be ejected
            int parkafterone = (int)(4*blockInterval/3);
            int specialturn = (int)(blueNegativeFactor * -50);
            rb.turnClockwiseByEncoder(specialturn, rb.BL, .50); //rotate into block
            rb.strafeRightByEncoder(parkafterone, rb.BL, .80); //Strafe to eject
            rb.turnClockwiseByEncoder(parkingturn, rb.BL, .50);//rotate to park
            runtime.reset();
            while (runtime.seconds() < 2) {
                rb.tapeOut();
            }
            rb.tapeStop();
            /*int specialturn = (int)(blueNegativeFactor * -50);
            rb.turnClockwiseByEncoder(specialturn, rb.BL, .50); //rotate into block
            int driveback = -strafeToEject;
            int drivebackone = 0;
            if(blockNumber >= 3) { //if we get the fourth block or fifth block, go for the 5th block instead
                drivebackone = (int)(-(blueNegativeFactor * blockInterval * 3));
            }
            else{
                drivebackone = (int)((blueNegativeFactor * blockInterval * 3));
            }
            rb.strafeRightByEncoder(driveback, rb.BL, .7); //going back for a second block
            rb.driveForwardByEncoder(-300, rb.BL, .40); //move forward
            rb.strafeRightByEncoder(drivebackone, rb.BL, .7); //going back fir a second block
            rb.driveForwardByEncoder(-630, rb.BL, .40); //move forward

            if(blockNumber == 2 && blueNegativeFactor == 1){
                rb.intakeIn(1); //turn intake on
                rb.turnClockwiseByEncoder(500, rb.BL, .50); //rotate into block
                rb.driveForwardByEncoder(-600, rb.BL, .40); // move into block
                rb.turnClockwiseByEncoder(-490, rb.BL, .50);//rotate back
            }
            else{
                rb.intakeIn(1); //turn intake on
                rb.turnClockwiseByEncoder(-500, rb.BL, .50); //rotate into block
                rb.driveForwardByEncoder(-600, rb.BL, .40); // move into block
                rb.turnClockwiseByEncoder(490, rb.BL, .50);//rotate back
            }
            rb.driveForwardByEncoder(650, rb.BL, .60); //back out a little
            rb.intakeIn(.4); //slow down the intake speed so we mess something up
            int strafeToEjecttwo = (int) (-driveback); //value for Blocks --> 3350 + number of blocks * blockInterval value
            int strafeToEjectthree = -drivebackone;

            rb.strafeRightByEncoder(strafeToEjecttwo, rb.BL, .80); //Strafe to eject
            rb.driveForwardByEncoder(300, rb.BL, .40); //move forward
            rb.strafeRightByEncoder(strafeToEjectthree, rb.BL, .80);
            rb.strafeRightByEncoder(blockInterval, rb.BL, .80);//Strafe to eject
            rb.intakeOut(); //eject skystone
            sleep(1200); // wait for block to be ejected

            rb.turnClockwiseByEncoder(parkingturn, rb.BL, .50);//rotate to park
            runtime.reset();
            while (runtime.seconds() < 2) {
                rb.tapeOut();
            }
            rb.tapeStop();*/


//            int turnTo = (int) (1900 * blueNegativeFactor);
//            rb.turnClockwiseByEncoder(turnTo, rb.BL, 0.20); //Rotate 90

//- is right for turns

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
//            while (runtime.seconds() < 1.0) {
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
//            rb.blockDown(); //Grab first skystone
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
//            rb.strafeRightByEncoder(strafeToMidPoint, rb.BL, .7); //go to midpoint for blocks
//            rb.blockUp(); //Eject skystone
//            sleep(1000);
//
//            int strafeToPlatform = (int) (-1860 * blueNegativeFactor);\

//            rb.strafeRightByEncoder(strafeToPlatform, rb.BL, .7); //go to platform for blocks
//
//            rb.driveForwardByEncoder(1200,rb.BL, .18);//Move slightly into platform
//
//            rb.setPlatformUp(false); //Put platform grabbers down
//            sleep(1000);
//            rb.driveForwardByEncoder(-1500,rb.BL, .59); //Move backwards a little
//
//            int turnTo = (int) (-1900 * blueNegativeFactor);
//            rb.turnClockwiseByEncoder(turnTo, rb.BL, 0.20); //Rotate 90
//
//            runtime.reset();
//            while (runtime.seconds() < 4) {
//                rb.tapeOut();
//                //rb.driveForwardByEncoder(-1000, rb.BL, 0.60); //Push into wall
//
//            }
//
//            rb.setPlatformUp(true); //Lift platform pullers before end of auto

            stop();

        }
    }
}