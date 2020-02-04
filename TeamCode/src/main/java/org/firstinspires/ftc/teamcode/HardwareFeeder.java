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

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;

public class HardwareFeeder {
    /* Public OpMode members. */
    DcMotor FR   = null;
    DcMotor FL  = null;
    DcMotor BR = null;
    DcMotor BL = null;
    DcMotor intakeleft = null;
    DcMotor intakeright = null;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();


    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        FR  = hwMap.get(DcMotor.class, "FR");
        FL = hwMap.get(DcMotor.class, "FL");
        BR  = hwMap.get(DcMotor.class, "BR");
        BL  = hwMap.get(DcMotor.class, "BL");
        intakeright  = hwMap.get(DcMotor.class, "intakeright");
        intakeleft  = hwMap.get(DcMotor.class, "intakeleft");


        // Set motor directions
        FR.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        FL.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        BR.setDirection(DcMotor.Direction.FORWARD);
        BL.setDirection(DcMotor.Direction.FORWARD);
        intakeright.setDirection(DcMotor.Direction.FORWARD);
        intakeleft.setDirection(DcMotor.Direction.REVERSE);


        // Set rb to brake when power is zero

        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Set all motors to zero power
        FR.setPower(0);
        FL.setPower(0);
        BR.setPower(0);
        BL.setPower(0);
        intakeright.setPower(0);
        intakeleft.setPower(0);


        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    //Driving Functions
    void driveStop() {
        FR.setPower(0);
        FL.setPower(0);
        BR.setPower(0);
        BL.setPower(0);
    }

    void driveForwards() {
        FR.setPower(-Constants.MAX_DRIVE_SPEED);
        FL.setPower(Constants.MAX_DRIVE_SPEED);
        BR.setPower(-Constants.MAX_DRIVE_SPEED);
        BL.setPower(Constants.MAX_DRIVE_SPEED);
    }
    void driveForwards(double speed) {
        FR.setPower(-speed);
        FL.setPower(speed);
        BR.setPower(-speed);
        BL.setPower(speed);
    }

    void driveBackwards() {
        FR.setPower(Constants.MAX_DRIVE_SPEED);
        FL.setPower(-Constants.MAX_DRIVE_SPEED);
        BR.setPower(Constants.MAX_DRIVE_SPEED);
        BL.setPower(-Constants.MAX_DRIVE_SPEED);
    }

    void driveBackwards(double speed) {
        FR.setPower(speed);
        FL.setPower(-speed);
        BR.setPower(speed);
        BL.setPower(-speed);
    }

    void turnRight(double speed) {
        FR.setPower(-speed);
        FL.setPower(-speed);
        BR.setPower(-speed);
        BL.setPower(-speed);
    }

    void turnLeft(double speed) {
        FR.setPower(speed);
        FL.setPower(speed);
        BR.setPower(speed);
        BL.setPower(speed);
    }

    void intakeIn() {
        intakeleft.setPower(-Constants.MAX_DRIVE_SPEED);
        intakeright.setPower(-Constants.MAX_DRIVE_SPEED);
    }

    void intakeOut(){
        intakeleft.setPower(Constants.MAX_DRIVE_SPEED);
        intakeright.setPower(Constants.MAX_DRIVE_SPEED);
    }
    void intakeStop(){
        intakeleft.setPower(0);
        intakeright.setPower(0);
    }



}

