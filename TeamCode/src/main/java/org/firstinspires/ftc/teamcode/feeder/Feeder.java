/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * (todd was here)
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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

    private MecanumOdometry odometry = new MecanumOdometry();

    @Override
    public void runOpMode() {
        rb.ledColorFLashYellow();
        telemetry.addData("Status", "Initializing");
        telemetry.update();
        rb.init(hardwareMap, this);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        rb.ledColorGreen();
        odometry.start(rb.FR.getCurrentPosition(),rb.FL.getCurrentPosition(),rb.BL.getCurrentPosition());

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            drive();
            intake();
            platform();
            odometry.update(rb.FR.getCurrentPosition(),rb.FL.getCurrentPosition(),rb.BL.getCurrentPosition());
            telemetry.addData("x",odometry.getX());
            telemetry.addData("y", odometry.getY());
            telemetry.addData("theta", odometry.getTheta());
            telemetry.update();
//            emergencyEject();
            //compassDriving();

            // Show the elapsed game time and wheel power.
//            telemetry.addData("Status", "Run Time: " + runtime.toString());
//            telemetry.update();
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


        if (rightX < -DRIVE_STICK_THRESHOLD || rightX > DRIVE_STICK_THRESHOLD || leftY < -DRIVE_STICK_THRESHOLD || leftY > DRIVE_STICK_THRESHOLD || leftX < -DRIVE_STICK_THRESHOLD || leftX > DRIVE_STICK_THRESHOLD) {
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;
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

//        telemetry.addData("Front Motors", "Left (%.2f), Right (%.2f)", leadleftPower, leadrightPower);
//        telemetry.addData("Rear Motors", "left (%.2f), right (%.2f)", rearLeftPower, rearRightPower);

    }
    private void intake(){
        boolean rightb = gamepad2.right_bumper;
        boolean leftb = gamepad2.left_bumper;

        if (rightb) {
            rb.intakeOut();
            rb.ledColorFLashYellow();
        } else if (leftb) {
            rb.intakeIn();
            rb.ledColorOrange();

        }
        //emergency eject
        else if (rightb && gamepad2.right_stick_button) {
            rb.intakeIn(-1);
        }
        else{
            rb.intakeStop();
        }

    }

//    private void emergencyEject(){
//
//        if (gamepad2.x || gamepad2.left_stick_button) {
//            rb.intakeIn(-1);
//        }
//        else{
//            rb.intakeStop();
//        }
//
//    }

    private void platform(){
        double rightTrigger = gamepad2.right_trigger;
        double leftTrigger = gamepad2.left_trigger;
        boolean dpadUp = gamepad2.dpad_up;
        boolean dpadDown = gamepad2.dpad_down;
        boolean y = gamepad2.y;
//        if(rightTrigger>.15){
//            rb.setPlatformUp(false);
//        }else if(leftTrigger>.15){
//            rb.setPlatformUp(true);
//        }

        if(dpadUp){
            rb.setPlatformUp(true);
        }else if(dpadDown){
            rb.setPlatformUp(false);
        } else if(y){
            rb.placeCap();
        }

        if(rightTrigger < -.13){
            rb.setPlatformUp(true);
        }else if(leftTrigger < -.13){
            rb.setPlatformUp(false);
        }
    }
//
//    private void compassDriving() {
//        if (gamepad1.dpad_up = true) {
//            rb.drive(1);
//        }
//
//        else if (gamepad1.dpad_down= true) {
//            rb.drive(-1);
//        }
//
//        else if (gamepad1.dpad_right = true) {
//            rb.strafe(1);
//        }
//
//        else if (gamepad1.dpad_left = true) {
//            rb.strafe(-1);
//        }
//    }
}
