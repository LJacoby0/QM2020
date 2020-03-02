package org.firstinspires.ftc.teamcode.feeder.feederautos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;


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
    BNO055IMU               imu;
    Orientation             lastAngles = new Orientation();
    double                  globalAngle, power = .30, correction;


    public abstract Alliance getAlliance();

    @Override
    public void runOpMode() throws InterruptedException {
        HardwareFeeder robot = new HardwareFeeder();
        robot.init(hardwareMap);

        robot.ledColorFLashYellow();


        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);

        telemetry.addData("Mode", "calibrating...");
        telemetry.update();

        // make sure the imu gyro is calibrated before continuing.
        while (!isStopRequested() && !imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }

        telemetry.addData("Mode", "waiting for start");
        telemetry.addData("imu calib status", imu.getCalibrationStatus().toString());


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


        robot.ledColorGreen();
        waitForStart();
        runtime.reset();


        while (opModeIsActive()) {
            robot.ledOff();
            // convert the RGB values to HSV values.
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
            while (sensorDistance.getDistance(DistanceUnit.CM) >= 4) {
                robot.drive(0.25);
            }

            runtime.reset();
            while(runtime.seconds() < 0.3) {
                robot.driveStop();
            }

            //Drive until block is black
            while (hsvValues[0] <= 100) {
                robot.strafe(blueNegativeFactor * -0.20);
            }

            runtime.reset();
            //timed auto into position
            robot.ledColorOrange();
            while(runtime.seconds() < 0.3) {
                robot.drive(0.1);
            }

            //Servo code to grab block here
            robot.ledColorGreen();
            runtime.reset();

            //Move backwards
            while(runtime.seconds() < .8) {
                robot.drive(-.7);
            }

            runtime.reset();
            //Go back to other side of the field
            while(runtime.seconds() < 2.5) {
                robot.strafe(blueNegativeFactor * 0.8);

            }

            //Servo drop block code here


            //THIS IS THE ROTATE CODE BUT IDK HOW TO PUT IT INTO A FUNCTION SO ITS A MESS:


            //rotate(20,20);




            double  leftPower, rightPower;
            int degrees = 90; //CHANGE THIS TO ADJUST ROTATION DEGREES
            // restart imu movement tracking.
            resetAngle();

            // getAngle() returns + when rotating counter clockwise (left) and - when rotating
            // clockwise (right).


            if (degrees < 0)
            {   // turn right.
                leftPower = power;
                rightPower = -power;
            }
            else if (degrees > 0)
            {   // turn left.
                leftPower = -power;
                rightPower = power;
            }
            else return;

            // set power to rotate.
//            fr.setPower(leftPower);
//            rightMotor.setPower(rightPower);

            robot.turn(rightPower);

            // rotate until turn is completed.
            if (degrees < 0)
            {
                // On right turn we have to get off zero first.
                while (opModeIsActive() && getAngle() == 0) {}

                while (opModeIsActive() && getAngle() > degrees) {}
            }
            else    // left turn.
                while (opModeIsActive() && getAngle() < degrees) {}

            // turn the motors off.
//            rightMotor.setPower(0);
//            leftMotor.setPower(0);
            robot.driveStop();

            // wait for rotation to stop.
            sleep(1000);

            // reset angle tracking on new heading.
            resetAngle();

            //THEN PLATFORM CODE









        }

        //back to park
        runtime.reset();


        }












    public void resetAngle()
    {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
    }

    private double getAngle()
    {
        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler angles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }




//        runtime.reset();
//        while(runtime.seconds() < 0.5) {
//            robot.driveStop();
//        }
//
//        //move under the bridge
//        runtime.reset();
//        while(runtime.seconds() < 4) {
//            robot.strafe(blueNegativeFactor * 0.5);
//        }
    }

