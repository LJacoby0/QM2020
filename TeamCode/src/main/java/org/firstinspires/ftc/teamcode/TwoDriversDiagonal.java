package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

//Constant Imports from Constants.java
import static org.firstinspires.ftc.teamcode.Constants.DRIVE_POWER;
import static org.firstinspires.ftc.teamcode.Constants.DRIVE_POWER_SLOW;
import static org.firstinspires.ftc.teamcode.Constants.DRIVE_STICK_THRESHOLD;
import static org.firstinspires.ftc.teamcode.Constants.DRIVE_STICK_THRESHOLD_SQUARED;
import static org.firstinspires.ftc.teamcode.Constants.ROTATION_POWER;
import static org.firstinspires.ftc.teamcode.Constants.ROTATION_POWER_SLOW;
import static org.firstinspires.ftc.teamcode.Constants.TRIGGER_THRESHOLD;



@TeleOp(name = "TwoDriversDiagonal 1/24", group = "Sensor")
public class TwoDriversDiagonal extends LinearOpMode {

    private HardwarePot rb = new HardwarePot();
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {
        //Init
        rb.init(hardwareMap);
        rb.righthand.setPosition(0.75);
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
            compass();


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }

//Gunner Functions:

    private void drag() {
        if (gamepad2.y) {
            rb.drag.setPower(-.3);
        } else if (gamepad2.a) {
            rb.drag.setPower(.6);
        } else {
            rb.drag.setPower(0);
        }
    }

    private void arm() {
        //TODO: if we have time, maybe add encoders to this motor so the height can be preset and
        // toggled using triggers. definitely include an override though for manual control.
        if (gamepad2.left_bumper) {
            rb.lift.setPower(.7);
        } else if (gamepad2.right_bumper) {
            rb.lift.setPower(-.3);
        }

        //Additional Controls Using Left stick up and down...
        else if (gamepad2.left_stick_x > .3) {
            rb.lift.setPower(.9);
        } else if (gamepad2.left_stick_x < -.3) { //TODO: Figure out why this doesn't go down
            rb.lift.setPower(-.3);
        }

        //if no input, turn motor off
        else {
            rb.lift.setPower(0);
        }
    }

    private void hand() {
        if (gamepad2.dpad_up) {
            rb.righthand.setPosition(Constants.CLOSED_HAND);

        } else if (gamepad2.dpad_down) {
            rb.righthand.setPosition(Constants.OPEN_HAND);

        }
    }

    private void compass() {
        if (gamepad1.dpad_up) {

            rb.driveForwards(); //see hardwarepot.java

        } else if (gamepad1.dpad_down) {

            rb.driveBackwards(); //see hardwarepot.java

        } else if (gamepad1.dpad_left) {

            rb.driveLeft(); //see hardwarepot.java

        } else if (gamepad1.dpad_right) {

            rb.driveRight(); //see hardwarepot.java
        }
    }


    /**
     * Main Diagonal Driving Function
     * Basic Controls: Left stick is 360 degree motion and right stick is just rotation
     *
     * @param x_stick       x value of left stick value -/+
     * @param y_stick       y value of left stick
     * @param x_right_stick x value or right stick
     * @param multiplier    pre-determined multiplier in constants file, can be modified by slow mode
     */

    // diaDrive(leftX, leftY, rightX, pow);
    public void diaDrive(double x_stick, double y_stick, double x_right_stick, double multiplier) {

        //Calculates a power level for all motors rather than using pre-determined levels, allows for a mix of motors to be running for more control.
        rb.backDrive.setPower(-x_stick * multiplier + x_right_stick);
        rb.frontDrive.setPower(x_stick * multiplier + x_right_stick);
        rb.rightDrive.setPower(y_stick * multiplier + x_right_stick ); // *1.4
        rb.leftDrive.setPower(-y_stick * multiplier + x_right_stick); // *.85


    }


    private void drive() {
        //Start w/ no movement
        double leftPower = 0;
        double rightPower = 0;
        double frontPower = 0;
        double backPower = 0;

        //Get Stick Values
        double leftY = gamepad1.left_stick_y;
        double leftX = gamepad1.left_stick_x;
        double rightX = gamepad1.right_stick_x;
        double rightY = gamepad1.right_stick_y;
        boolean rightb = gamepad1.right_bumper;
        boolean leftb = gamepad1.left_bumper;


        //slower spinning for alignment
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


        //Slow movement trigger
        //Lets driver use the right or left trigger to slow down the movement and rotation of the robot
        double movementModifier;
        double rotationModifier;
        if (gamepad1.right_trigger >= TRIGGER_THRESHOLD || gamepad1.left_trigger >= TRIGGER_THRESHOLD) {
            movementModifier = DRIVE_POWER_SLOW; //Slow constant
            rotationModifier = ROTATION_POWER_SLOW;

        } else {
            movementModifier = DRIVE_POWER;
            rotationModifier = ROTATION_POWER;
        }


        if ((leftX * leftX) + (leftY * leftY) >= DRIVE_STICK_THRESHOLD_SQUARED || Math.abs(rightX) >= DRIVE_STICK_THRESHOLD) {

            diaDrive(leftX, leftY, rightX * rotationModifier, movementModifier);

            //maybe add data into the console?... :
            telemetry.addData("Left Y is: ", "", leftY);
            telemetry.addData("Left X is: ", "", leftX);
            telemetry.addData("Right X is: ", "", rightX);


        } else {
            rb.driveStop();
        }

        telemetry.addData("NS Motors", "front (%.2f), back (%.2f)", frontPower, backPower);
        telemetry.addData("Side Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
        telemetry.update();

    }
}