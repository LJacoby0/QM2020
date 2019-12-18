package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.Constants.DRIVE_POWER;
import static org.firstinspires.ftc.teamcode.Constants.DRIVE_POWER_SLOW;
import static org.firstinspires.ftc.teamcode.Constants.DRIVE_STICK_THRESHOLD;
import static org.firstinspires.ftc.teamcode.Constants.DRIVE_STICK_THRESHOLD_SQUARED;
import static org.firstinspires.ftc.teamcode.Constants.TRIGGER_THRESHOLD;

@TeleOp(name = "TwoDriversDiagonal", group = "Sensor")
public class TwoDriversDiagonal extends LinearOpMode {

    private HardwarePot rb = new HardwarePot();
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {
        rb.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            drive();
            drag();
            arm();
            hand();

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }

    private void drag() {
        if (gamepad2.dpad_up) {
            rb.drag.setPower(-.3);
        } else if (gamepad2.dpad_down) {
            rb.drag.setPower(.3);
        } else {
            rb.drag.setPower(0);
        }
    }

    private void arm() {
        if (gamepad2.left_bumper) {
            rb.lift.setPower(.3);
        } else if (gamepad2.right_bumper) {
            rb.lift.setPower(-.3);
        } else {
            rb.lift.setPower(0);
        }
    }

    private void hand() {
        if (gamepad2.dpad_left) {
            rb.righthand.setPosition(Constants.OPEN_HAND);//open
        } else if (gamepad2.dpad_right) {
            rb.righthand.setPosition(Constants.CLOSED_HAND);//close
        }
    }

    public void theDrive(double x_stick, double y_stick, double x_right_stick, double multiplier) {
        double fbPower = x_stick;
        double rlPower = y_stick;
        rb.frontDrive.setPower(fbPower * multiplier + x_right_stick);
        rb.backDrive.setPower(-fbPower * multiplier + x_right_stick);
        rb.leftDrive.setPower(rlPower * multiplier + x_right_stick);
        rb.rightDrive.setPower(-rlPower * multiplier + x_right_stick);
    }

    private void drive() {
        double leftPower = 0;
        double rightPower = 0;
        double frontPower = 0;
        double backPower = 0;
        double power;

        double leftY = gamepad1.left_stick_y;
        double leftX = gamepad1.left_stick_x;
        double rightX = gamepad1.right_stick_x;
        double rightY = gamepad1.right_stick_y;
        boolean rightb = gamepad1.right_bumper;
        boolean leftb = gamepad1.left_bumper;

//        if (gamepad1.right_trigger >= Constants.TRIGGER_THRESHOLD) {
//            power = Constants.DRIVE_POWER_SLOW;
//        } else {
//            power = Constants.DRIVE_POWER;
//        }
        power = 1;

        // closed .84
        // open .46
        //this moves spinning to the driver right stick and makes it more sensitive
//        double allpower = 0;
//        if (rightX < -0.13 || rightX > 0.13) {
//            allpower = Range.clip(rightX, Constants.MIN_DRIVE_SPEED, Constants.MAX_DRIVE_SPEED);
//            rb.frontDrive.setPower(allpower);
//            rb.backDrive.setPower(allpower);
//            rb.rightDrive.setPower(allpower);
//            rb.leftDrive.setPower(allpower);
//        }

//
        if (rightb) {
            rb.frontDrive.setPower(Constants.SPIN_SPEED);
            rb.backDrive.setPower(Constants.SPIN_SPEED);
            rb.rightDrive.setPower(Constants.SPIN_SPEED);
            rb.leftDrive.setPower(Constants.SPIN_SPEED);
        } else if (leftb) {
            rb.frontDrive.setPower(-Constants.SPIN_SPEED);
            rb.backDrive.setPower(-Constants.SPIN_SPEED);
            rb.rightDrive.setPower(-Constants.SPIN_SPEED);
            rb.leftDrive.setPower(-Constants.SPIN_SPEED);
        }


        /**
         * Driving code.
         *
         * @param x_stick       The x value of the left joystick
         * @param y_stick       The y value of the left joystick
         * @param x_right_stick The x value of the right joystick
         * @param multiplier    Number to multiply the power by.
         */
//            float leftY = -gamepad1.left_stick_y;
//            float leftX = gamepad1.left_stick_x;
//            float rightX = gamepad1.right_stick_x;


        double pow;
        if (gamepad1.right_trigger >= TRIGGER_THRESHOLD) {
            pow = DRIVE_POWER_SLOW;
        } else {
            pow = DRIVE_POWER;
        }

        if (leftX * leftX + leftY * leftY >= DRIVE_STICK_THRESHOLD_SQUARED || Math.abs(rightX) >= DRIVE_STICK_THRESHOLD) {
            theDrive(leftX, leftY, rightX, power);
        } else {
            rb.driveStop();
        }


//        double fbPower = x_stick;
//        double rlPower = y_stick;
//        fMotor.setPower(fbPower * multiplier+x_right_stick);
//        bMotor.setPower(-fbPower * multiplier+x_right_stick);
//        lMotor.setPower(rlPower * multiplier+x_right_stick);
//        rMotor.setPower(-rlPower * multiplier+x_right_stick);
//
//        if (rightX < -0.13 || rightX > 0.13) {
//            frontPower = Range.clip(rightX, Constants.MIN_DRIVE_SPEED, Constants.MAX_DRIVE_SPEED);
//            rb.frontDrive.setPower(frontPower * power);
//            rb.backDrive.setPower(frontPower * power);
//            rb.rightDrive.setPower(frontPower * power);
//            rb.leftDrive.setPower(frontPower * power);
//        }
//        else if (leftY < -0.13 || leftY > 0.13) {
//            telemetry.addData("controller Y is: ", "", leftY);
//            //tried to add slow mode
//            frontPower = Range.clip(leftY, Constants.MIN_DRIVE_SPEED, Constants.MAX_DRIVE_SPEED);
//            rb.leftDrive.setPower(-frontPower * power);
//            rb.rightDrive.setPower(frontPower * power);
//            rb.frontDrive.setPower(0);
//            rb.backDrive.setPower(0);
//        } else if (leftX < -0.13 || leftX > 0.13) {
//            telemetry.addData("controller X is: ", "", leftX);
//            //tried to add slow mode
//            leftPower = Range.clip(leftX, Constants.MIN_DRIVE_SPEED, Constants.MAX_DRIVE_SPEED);
//            rb.leftDrive.setPower(0);
//            rb.rightDrive.setPower(0);
//            rb.frontDrive.setPower(leftPower * power);
//            rb.backDrive.setPower(-leftPower * power);
//        } else {
//            //rb.driveStop();
//            rb.leftDrive.setPower(0);
//            rb.rightDrive.setPower(0);
//            rb.frontDrive.setPower(0);
//            rb.backDrive. setPower(0);
//        }

        telemetry.addData("NS Motors", "front (%.2f), back (%.2f)", frontPower, backPower);
        telemetry.addData("Side Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);

    }
}

