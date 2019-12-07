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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.teamcode.HardwarePot;

@Autonomous(name="Red Platform Auto")
public class Auto_RedPlatform_DriveByTime extends LinearOpMode {

    private HardwarePot         rb   = new HardwarePot();   // Use hardware
    private ElapsedTime     runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        rb.init(hardwareMap);

        // Send telemetry message to signify rb waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way

        // Init: Move hand up
        runtime.reset();
        rb.righthand.setPosition(0);
        while (opModeIsActive() && (runtime.seconds() < 1.4)) {
            rb.drag.setPower(-.3);
            telemetry.addData("Path", "Init: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        rb.drag.setPower(0); // lower claw
        runtime.reset();

        //back up
        while (opModeIsActive() && (runtime.seconds() < .5)) {
            goBack(Constants.FORWARD_SPEED);
            telemetry.addData("Path", "Back: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        goStop();
        runtime.reset();

        //go right to platform
        while (opModeIsActive() && (runtime.seconds() < .7)) {
            goRight(Constants.FORWARD_SPEED);
            telemetry.addData("Path", "Right: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        goStop();
        runtime.reset();

        //slowly go right to platform
        while (opModeIsActive() && (runtime.seconds() < .5)) {
            goRight(Constants.SLOW_DRIVE_SPEED);
            telemetry.addData("Path", "Right: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        goStop();
        runtime.reset();

        // lower claw
        while (opModeIsActive() && (runtime.seconds() < 1.2)) {
            rb.drag.setPower(.3); // lower claw
            telemetry.addData("Path", "Lower Claw: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        rb.drag.setPower(0); // lower claw
        runtime.reset();


        //Drive left with platform
        while (opModeIsActive() && (runtime.seconds() < 1.3)) {
            goLeft(Constants.FORWARD_SPEED);
            telemetry.addData("Path", "Leg 4: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        goStop();
        runtime.reset();

        //slowly drive left w platform
        while (opModeIsActive() && (runtime.seconds() < 1)) {
            goLeft(Constants.SLOW_DRIVE_SPEED);
            telemetry.addData("Path", "Leg 4: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        goStop();
        runtime.reset();


        //let go of platform
        while (opModeIsActive() && (runtime.seconds() < 1.7)) {
            rb.drag.setPower(-.3); // lower claw (NEEDS TESTING TO SET VALUE)
            telemetry.addData("Path", "Leg 5: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        rb.drag.setPower(0);
        rb.righthand.setPosition(Constants.CLOSED_HAND);
        runtime.reset();

        //leave platform
        while (opModeIsActive() && (runtime.seconds() < 2.5)) {
            goFront(Constants.SLOW_DRIVE_SPEED);
            telemetry.addData("Path", "Leg 5: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        goStop();
        runtime.reset();


        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }

    public void goFront(double speed){
        rb.frontDrive.setPower(0);
        rb.backDrive.setPower(0);
        rb.leftDrive.setPower(speed);
        rb.rightDrive.setPower(-speed);
    }

    public void goBack(double speed){
        rb.frontDrive.setPower(0);
        rb.backDrive.setPower(0);
        rb.leftDrive.setPower(-speed);
        rb.rightDrive.setPower(speed);
    }

    public void goRight(double speed){
        rb.frontDrive.setPower(speed);
        rb.backDrive.setPower(-speed);
        rb.leftDrive.setPower(0);
        rb.rightDrive.setPower(0);
    }
    public void goLeft(double speed){
        rb.frontDrive.setPower(-speed);
        rb.backDrive.setPower(speed);
        rb.leftDrive.setPower(0);
        rb.rightDrive.setPower(0);
    }

    public void goStop(){
        rb.frontDrive.setPower(0);
        rb.backDrive.setPower(0);
        rb.leftDrive.setPower(0);
        rb.rightDrive.setPower(0);
    }

}
