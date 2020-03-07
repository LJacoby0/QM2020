package org.firstinspires.ftc.teamcode.feeder;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.HardwarePot;


import static org.firstinspires.ftc.teamcode.Constants.DRIVE_STICK_THRESHOLD;



@TeleOp(name = "!QM Feeder TeleOP", group = "Sensor")
public class Feeder extends LinearOpMode {

    private HardwareFeeder rb = new HardwareFeeder();
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;


    //private MecanumOdometry odometry = new MecanumOdometry();

    @Override
    public void runOpMode() {
//        rb.ledColorFLashYellow();
        telemetry.addData("Status", "Initializing");
        telemetry.update();
        rb.init(hardwareMap, this);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
//        rb.ledColorGreen();
        //odometry.start(rb.FR.getCurrentPosition(),rb.FL.getCurrentPosition(),rb.BL.getCurrentPosition());
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        pattern = RevBlinkinLedDriver.BlinkinPattern.STROBE_GOLD;
        blinkinLedDriver.setPattern(pattern);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            drive();
            intake();
            platform();
            tapeMeasure();
            capstone();
            blockGrabber();
            //compassDriving();


           //  Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
            /* CONTROLS:
            * Driver: (Start + A)
            * Left Stick - Movement
            * Right Stick - Rotation
            *
            * Gunner: (Start + B)
            * Right Trigger - Intake
            * Left Trigger - Outtake
            * A - Block Grabber (only in emergencies)
            * Y - Capstone down
            * X - Capstone up
            * Left Stick Up - Tape Measure Eject
            * Left Stick Down - Tape Measure Input
            *
            * */
        }
    }

    private void drive() {
        //Init variables
        double leadleftPower = 0;
        double leadrightPower = 0;
        double rearRightPower = 0;
        double rearLeftPower = 0;

        double leftY = gamepad1.left_stick_y;
        double leftX = gamepad1.left_stick_x;
        double rightX = gamepad1.right_stick_x;

        pattern = RevBlinkinLedDriver.BlinkinPattern.ORANGE;
        blinkinLedDriver.setPattern(pattern);

        if (rightX < -DRIVE_STICK_THRESHOLD || rightX > DRIVE_STICK_THRESHOLD || leftY < -DRIVE_STICK_THRESHOLD || leftY > DRIVE_STICK_THRESHOLD || leftX < -DRIVE_STICK_THRESHOLD || leftX > DRIVE_STICK_THRESHOLD) {
            double drive = -gamepad1.left_stick_y*1.10;
            double turn  =  gamepad1.right_stick_x*1.25;
            double strafe = gamepad1.left_stick_x;

            leadleftPower = Range.clip(drive + turn + strafe, -1.0, 1.0);
            leadrightPower = Range.clip(drive - turn - strafe, -1.0, 1.0);
            rearLeftPower = Range.clip(drive + turn - strafe, -1.0, 1.0);
            rearRightPower = Range.clip(drive - turn + strafe, -1.0, 1.0);

            rb.drive(-leadrightPower, -leadleftPower, -rearRightPower, -rearLeftPower);
            telemetry.addData("Front-right motor", "%5.2f", leadrightPower);
            telemetry.addData("Back-right motor", "%5.2f", rearRightPower);
            telemetry.addData("Front-left motor", "%5.2f", leadleftPower);
            telemetry.addData("Back-left motor", "%5.2f", rearLeftPower);
            telemetry.update();
        }
        else {
            rb.driveStop();
        }

    }

    private void intake(){
        boolean rightb = gamepad2.right_bumper;
        boolean leftb = gamepad2.left_bumper;

        if (rightb && gamepad2.right_stick_button) {
            rb.intakeIn(-1);
        } else if (leftb) {
            rb.intakeIn();
        }
        else if (rightb) {
            rb.intakeOut();
        }
        else{
            rb.intakeStop();
        }

    }

    private void capstone(){
        boolean y = gamepad2.y;
        boolean x = gamepad2.x;
        if(x){
            rb.capservo.setPosition(FeederConstants.CAP_DOWN);
        }else if (y){
            rb.capservo.setPosition(FeederConstants.CAP_UP);

        }

    }

    private void platform(){
        boolean dpadUp = gamepad2.dpad_up;
        boolean dpadDown = gamepad2.dpad_down;
//        if(rightTrigger>.15){
//            rb.setPlatformUp(false);
//        }else if(leftTrigger>.15){
//            rb.setPlatformUp(true);
//        }

        if(dpadUp){
            rb.setPlatformUp(true);
        }else if(dpadDown){
            rb.setPlatformUp(false);
        }
    }

    private void tapeMeasure(){
        double leftx = gamepad2.left_stick_y;

        if(leftx > .1){
            rb.tapeIn();
        } else if(leftx < -.1){
            rb.tapeOut();
        }else{
            rb.tapeStop();
        }
    }

    private void blockGrabber(){

        double rightTrigger = gamepad2.right_trigger;

        if (gamepad2.a){
            rb.blockDown();
        } else {
            rb.blockUp();
        }
     }


}
