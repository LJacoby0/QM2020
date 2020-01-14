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

public class HardwarePot {
    /* Public OpMode members. */
    DcMotor  leftDrive   = null;
    DcMotor  rightDrive  = null;
    DcMotor frontDrive = null;
    DcMotor backDrive = null;
    DcMotor drag = null;
    DcMotor lift = null;
    Servo righthand = null;
    Servo lefthand = null;


//    public static final double hand       =  0.5;
//    public static final double ARM_UP_POWER    =  0.45 ;
//    public static final double ARM_DOWN_POWER  = -0.45 ;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();


    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftDrive  = hwMap.get(DcMotor.class, "leftDrive");
        rightDrive = hwMap.get(DcMotor.class, "rightDrive");
        frontDrive  = hwMap.get(DcMotor.class, "frontDrive");
        backDrive  = hwMap.get(DcMotor.class, "backDrive");
        drag  = hwMap.get(DcMotor.class, "drag");
        lift  = hwMap.get(DcMotor.class, "lift");


        // Set motor directions
        leftDrive.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightDrive.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        backDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        drag.setDirection(DcMotor.Direction.FORWARD);
        lift.setDirection(DcMotor.Direction.FORWARD);


        // Set rb to brake when power is zero
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drag.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Set all motors to zero power
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        frontDrive.setPower(0);
        backDrive.setPower(0);
        drag.setPower(0);
        lift.setPower(0);


        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drag.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);



        righthand = hwMap.get(Servo.class, "righthand");
        lefthand = hwMap.get(Servo.class, "lefthand");

    }
    void driveStop() {
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        frontDrive.setPower(0);
        backDrive.setPower(0);
    }

    void driveForwards() {
        rightDrive.setPower(-Constants.MAX_DRIVE_SPEED); //Front
        leftDrive.setPower(Constants.MAX_DRIVE_SPEED); //Back
        frontDrive.setPower(0);  //Left
        backDrive.setPower(0); //Right
    }
    void driveBackwards() {

        rightDrive.setPower(Constants.MAX_DRIVE_SPEED); //Front
        leftDrive.setPower(-Constants.MAX_DRIVE_SPEED); //Back
        frontDrive.setPower(0); //Left
        backDrive.setPower(0); //Right
    }

    void driveLeft() {
        rightDrive.setPower(0); //Front
        leftDrive.setPower(0); //Back
        frontDrive.setPower(-Constants.MAX_DRIVE_SPEED-.2); //Left
        backDrive.setPower(Constants.MAX_DRIVE_SPEED+.2); //Right
    }

    void driveRight() {
        rightDrive.setPower(0); //Front
        leftDrive.setPower(0); //Back
        frontDrive.setPower(Constants.MAX_DRIVE_SPEED+.2); //Left
        backDrive.setPower(-Constants.MAX_DRIVE_SPEED-.2); //Right
    }

}

