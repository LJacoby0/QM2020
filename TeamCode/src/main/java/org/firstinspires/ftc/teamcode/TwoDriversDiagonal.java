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

    /**
     * Main Diagonal Driving Function
     * Basic Controls: Left stick is 360 degree motion and right stick is just rotation
     * @param x_stick x value of left stick value -/+
     * @param y_stick y value of left stick
     * @param x_right_stick x value or right stick
     * @param multiplier pre-determined multiplier in constants file, can be modified by slow mode
     */

    // diaDrive(leftX, leftY, rightX, pow);
    public void diaDrive(double x_stick, double y_stick, double x_right_stick, double multiplier) {
        //Calculates a power level for all motors rather than using pre-determined levels, allows for a mix of motors to be running for more control.
        rb.frontDrive.setPower(x_stick * multiplier + x_right_stick);
        rb.backDrive.setPower(-x_stick * multiplier + x_right_stick);
        rb.leftDrive.setPower(y_stick * multiplier + x_right_stick);
        rb.rightDrive.setPower(-y_stick * multiplier + x_right_stick);
        //TODO: mess w/ multipliers
        //TODO: Verify that this code actually works
    }


    private void drive() {
        //Start w/ no movement
        double leftPower = 0;
        double rightPower = 0;
        double frontPower = 0;
        double backPower = 0;
        double power;
        //Get Stick Values
        double leftY = gamepad1.left_stick_y;
        double leftX = gamepad1.left_stick_x;
        double rightX = gamepad1.right_stick_x;
        double rightY = gamepad1.right_stick_y;
        boolean rightb = gamepad1.right_bumper;
        boolean leftb = gamepad1.left_bumper;


        power = 1;

        // closed .84
        // open .46
        //
        // this moves spinning to the driver right stick and makes it more sensitive
//        double allpower = 0;
//        if (rightX < -0.13 || rightX > 0.13) {
//            allpower = Range.clip(rightX, Constants.MIN_DRIVE_SPEED, Constants.MAX_DRIVE_SPEED);
//            rb.frontDrive.setPower(allpower);
//            rb.backDrive.setPower(allpower);
//            rb.rightDrive.setPower(allpower);
//            rb.leftDrive.setPower(allpower);
//        }

        //idk why we even have these buttons but ok...
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



//Old vars
//            float leftY = -gamepad1.left_stick_y;
//            float leftX = gamepad1.left_stick_x;
//            float rightX = gamepad1.right_stick_x;


        //Slow mode code, should be irrelevant if variable speed is added to main code. If the driver prefers it tho, it can be used
        double pow;
        if (gamepad1.right_trigger >= TRIGGER_THRESHOLD) {
            pow = DRIVE_POWER_SLOW; //Slow constant
        } else {
            pow = DRIVE_POWER;
        }


        //MAIN:
        //Deadzone:  TODO: Make adjustable deadzone


        if (leftX * leftX + leftY * leftY >= DRIVE_STICK_THRESHOLD_SQUARED || Math.abs(rightX) >= DRIVE_STICK_THRESHOLD) {

            diaDrive(leftX, leftY, rightX, pow);


        } else {
            rb.driveStop();
        }


        telemetry.addData("NS Motors", "front (%.2f), back (%.2f)", frontPower, backPower);
        telemetry.addData("Side Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);

    }
}

