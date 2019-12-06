/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
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

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.ConservativeTranslationAdvice;
import org.firstinspires.ftc.teamcode.HardwarePot;


@TeleOp(name = "TwoDrivers", group = "Sensor")
public class TwoDrivers extends LinearOpMode {

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

    private void drag(){
        if(gamepad2.dpad_up){
            rb.drag.setPower(-.3);
        }else if(gamepad2.dpad_down){
            rb.drag.setPower(.3);
        }else{
            rb.drag.setPower(0);
        }
    }
    private void arm(){
        if(gamepad2.left_bumper){
            rb.lift.setPower(.3);
        } else if(gamepad2.right_bumper){
            rb.lift.setPower(-.3);
        }else {
            rb.lift.setPower(0);
        }
    }
    private void hand(){
        if(gamepad2.dpad_left){
            rb.righthand.setPosition(.46);//open
        } else if(gamepad2.dpad_right){
            rb.righthand.setPosition(.84);//close
        }
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
        if (rightX < -0.13 || rightX > 0.13) {
            frontPower = Range.clip(rightX, Constants.MIN_DRIVE_SPEED, Constants.MAX_DRIVE_SPEED);
            rb.frontDrive.setPower(frontPower * power);
            rb.backDrive.setPower(frontPower * power);
            rb.rightDrive.setPower(frontPower * power);
            rb.leftDrive.setPower(frontPower * power);
        }
        else if (leftY < -0.13 || leftY > 0.13) {
            telemetry.addData("controller Y is: ", "", leftY);
            //tried to add slow mode
            frontPower = Range.clip(leftY, Constants.MIN_DRIVE_SPEED, Constants.MAX_DRIVE_SPEED);
            rb.leftDrive.setPower(-frontPower * power);
            rb.rightDrive.setPower(frontPower * power);
            rb.frontDrive.setPower(0);
            rb.backDrive.setPower(0);
        } else if (leftX < -0.13 || leftX > 0.13) {
            telemetry.addData("controller X is: ", "", leftX);
            //tried to add slow mode
            leftPower = Range.clip(leftX, Constants.MIN_DRIVE_SPEED, Constants.MAX_DRIVE_SPEED);
            rb.leftDrive.setPower(0);
            rb.rightDrive.setPower(0);
            rb.frontDrive.setPower(leftPower * power);
            rb.backDrive.setPower(-leftPower * power);
        } else {
            //rb.driveStop();
            rb.leftDrive.setPower(0);
            rb.rightDrive.setPower(0);
            rb.frontDrive.setPower(0);
            rb.backDrive.setPower(0);
        }

        telemetry.addData("NS Motors", "front (%.2f), back (%.2f)", frontPower, backPower);
        telemetry.addData("Side Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);

    }
}
