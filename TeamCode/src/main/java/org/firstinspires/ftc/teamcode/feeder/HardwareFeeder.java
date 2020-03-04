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
package org.firstinspires.ftc.teamcode.feeder;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.feeder.FeederConstants;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;


public class HardwareFeeder {
    /* Public OpMode members. */
    public DcMotor FR   = null;
    public DcMotor FL  = null;
    public DcMotor BR = null;
    public DcMotor BL = null;

    DcMotor tape = null;
    DcMotor intakeleft = null;
    DcMotor intakeright = null;

    Servo platformleft = null;
    Servo platformright = null;
    Servo blockservo = null;
    Servo capservo = null;

    RevBlinkinLedDriver blinkinLedDriver ;
    RevBlinkinLedDriver.BlinkinPattern pattern ;


    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();
    LinearOpMode opMode;


    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap, LinearOpMode opMode) {
        this.opMode = opMode;
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        FR  = hwMap.get(DcMotor.class, "FR");
        FL = hwMap.get(DcMotor.class, "FL");
        BR  = hwMap.get(DcMotor.class, "BR");
        BL  = hwMap.get(DcMotor.class, "BL");
        intakeright  = hwMap.get(DcMotor.class, "intakeright");
        intakeleft  = hwMap.get(DcMotor.class, "intakeleft");
        tape  = hwMap.get(DcMotor.class, "tape");
        platformleft = hwMap.get(Servo.class, "platform left");
        platformright = hwMap.get(Servo.class, "platform right");
        capservo = hwMap.get(Servo.class, "capservo");
        blockservo = hwMap.get(Servo.class, "blockservo");

        blinkinLedDriver = hwMap.get(RevBlinkinLedDriver.class, "blinkin");

        // Set motor directions
        FR.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        FL.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        BR.setDirection(DcMotor.Direction.REVERSE);
        BL.setDirection(DcMotor.Direction.FORWARD);
        intakeright.setDirection(DcMotor.Direction.FORWARD);
        intakeleft.setDirection(DcMotor.Direction.REVERSE);
        tape.setDirection(DcMotor.Direction.FORWARD);


        // Set rb to brake when power is zero

        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        tape.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Set all motors to zero power
        FR.setPower(0);
        FL.setPower(0);
        BR.setPower(0);
        BL.setPower(0);
        intakeright.setPower(0);
        intakeleft.setPower(0);
        tape.setPower(0);



        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        tape.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }

    //Driving Functions
    public void driveStop() {
        FR.setPower(0);
        FL.setPower(0);
        BR.setPower(0);
        BL.setPower(0);
    }

//    void drives() {
//        FR.setPower(-Constants.MAX_DRIVE_SPEED);
//        FL.setPower(Constants.MAX_DRIVE_SPEED);
//        BR.setPower(-Constants.MAX_DRIVE_SPEED);
//        BL.setPower(Constants.MAX_DRIVE_SPEED);
//    }
public void drive(double speed) {
    FR.setPower(speed);
    FL.setPower(speed);
    BR.setPower(speed);
    BL.setPower(speed);
}

    public void drive(double leftPower, double rightPower) {
        FR.setPower(rightPower);
        FL.setPower(leftPower);
        BR.setPower(rightPower);
        BL.setPower(leftPower);
    }

    public void drive(double frontrightPower, double frontleftPower, double backrightPower, double backleftPower) {
        FR.setPower(frontrightPower);
        FL.setPower(frontleftPower);
        BR.setPower(backrightPower);
        BL.setPower(backleftPower);
    }

//    void driveBackwards() {
//        FR.setPower(Constants.MAX_DRIVE_SPEED);
//        FL.setPower(-Constants.MAX_DRIVE_SPEED);
//        BR.setPower(Constants.MAX_DRIVE_SPEED);
//        BL.setPower(-Constants.MAX_DRIVE_SPEED);
//    }
//
//    void driveBackwards(double speed) {
//        FR.setPower(speed);
//        FL.setPower(-speed);
//        BR.setPower(speed);
//        BL.setPower(-speed);
//    }

    public void strafe(double speed) {
        FR.setPower(speed);
        FL.setPower(-speed);
        BR.setPower(-speed);
        BL.setPower(speed);
    }

    public void turn(double speed) {
        FR.setPower(speed);
        FL.setPower(-speed);
        BR.setPower(speed);
        BL.setPower(-speed);
    }

    public void setPlatformUp(boolean isUp) {
        if(isUp) {
            platformleft.setPosition(FeederConstants.LEFT_PLATFORM_UP);
            platformright.setPosition(FeederConstants.RIGHT_PLATFORM_UP);
        } else {
            platformleft.setPosition(FeederConstants.LEFT_PLATFORM_DOWN);
            platformright.setPosition(FeederConstants.RIGHT_PLATFORM_DOWN);
        }
    }

    public void blockUp(boolean isUp) {
        if(isUp) {
            blockservo.setPosition(FeederConstants.BLOCK_UP);
        } else {
            blockservo.setPosition(FeederConstants.BLOCK_DOWN);

        }
    }


    public void intakeIn() {
        intakeleft.setPower(-Constants.MAX_DRIVE_SPEED);
        intakeright.setPower(-Constants.MAX_DRIVE_SPEED);
    }
    public void intakeIn(double speed) {
        intakeleft.setPower(-speed);
        intakeright.setPower(-speed);
    }

    public void intakeOut(){
        intakeleft.setPower(Constants.MAX_DRIVE_SPEED);
        intakeright.setPower(Constants.MAX_DRIVE_SPEED);
    }
    public void intakeStop(){
        intakeleft.setPower(0);
        intakeright.setPower(0);
    }
    public void tapeIn(){
        tape.setPower(1);
    }
    public void tapeOut(){
        tape.setPower(-1);
    }

    public void tapeStop(){
        tape.setPower(0);

    }

    public void ledColorFLashYellow() {
        pattern = RevBlinkinLedDriver.BlinkinPattern.STROBE_GOLD;
        blinkinLedDriver.setPattern(pattern);
    }

    public void ledColorGreen() {
        pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
        blinkinLedDriver.setPattern(pattern);
    }

    public void ledColorOrange() {
        pattern = RevBlinkinLedDriver.BlinkinPattern.ORANGE;
        blinkinLedDriver.setPattern(pattern);
    }

    public void flashRed() {
        pattern = RevBlinkinLedDriver.BlinkinPattern.STROBE_RED;
        blinkinLedDriver.setPattern(pattern);
    }

    public void ledOff() {
        pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
        blinkinLedDriver.setPattern(pattern);
    }

    public static double angleDiff(double theta1, double theta2) {
        return Math.atan2(Math.sin(theta1 - theta2), Math.cos(theta1 - theta2));
    }

    public void goToTheta(double targetTheta, double power, MecanumOdometry odometry, ElapsedTime runtime, double timeout) {
        odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
        if (angleDiff(odometry.getTheta(), targetTheta) > FeederConstants.THETA_TOLERANCE) {
            turn(power);
            double startTime = runtime.seconds();
            while (opMode.opModeIsActive() && runtime.seconds() - startTime < timeout && angleDiff(odometry.getTheta(), targetTheta) > FeederConstants.THETA_TOLERANCE) {
                odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
                Thread.yield();
                opMode.telemetry.update();
            }
            driveStop();
            odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
        } else if (angleDiff(odometry.getTheta(), targetTheta) < -FeederConstants.THETA_TOLERANCE) {
            turn(-power);
            double startTime = runtime.seconds();
            while (opMode.opModeIsActive() && runtime.seconds() - startTime < timeout && angleDiff(odometry.getTheta(), targetTheta) < -FeederConstants.THETA_TOLERANCE) {
                odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
                Thread.yield();
                opMode.telemetry.update();
            }
            driveStop();
            odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
        }
    }

    public void strafeToX(double targetX, double power, MecanumOdometry odometry, ElapsedTime runtime, double timeout){
        int forwardsBackwardsFactor;
        if (odometry.getTheta() > Math.PI/2 && odometry.getTheta() < 3 * Math.PI / 2){
            goToTheta(Math.PI,.7,odometry, runtime , timeout);
            forwardsBackwardsFactor = -1;
        } else {
            goToTheta(0,.7,odometry, runtime , timeout);
            forwardsBackwardsFactor = 1;
        }
        odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
        if (odometry.getX() < targetX){
            strafe(power * forwardsBackwardsFactor);
            double startTime = runtime.seconds();
            while (opMode.opModeIsActive() && runtime.seconds() - startTime < timeout && odometry.getX() < targetX) {
                odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
                Thread.yield();
                opMode.telemetry.update();
            }
            driveStop();
            odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
        } else if (odometry.getX() > targetX){
            strafe(-power * forwardsBackwardsFactor);
            double startTime = runtime.seconds();
            while (opMode.opModeIsActive() && runtime.seconds() - startTime < timeout && odometry.getX() > targetX) {
                odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
                Thread.yield();
                opMode.telemetry.update();
            }
            driveStop();
            odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
        }
    }

    public void strafeToY(double targetY, double power, MecanumOdometry odometry, ElapsedTime runtime, double timeout){
        int forwardsBackwardsFactor;
        if (odometry.getTheta() > Math.PI && odometry.getTheta() < 2 * Math.PI){
            goToTheta(3 * Math.PI / 2,.7,odometry, runtime , timeout);
            forwardsBackwardsFactor = 1;
        } else {
            goToTheta(Math.PI / 2,.7,odometry, runtime , timeout);
            forwardsBackwardsFactor = -1;
        }
        odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
        if (odometry.getY() < targetY){
            strafe(power * forwardsBackwardsFactor);
            double startTime = runtime.seconds();
            while (opMode.opModeIsActive() && runtime.seconds() - startTime < timeout && odometry.getY() < targetY) {
                odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
                Thread.yield();
                opMode.telemetry.update();
            }
            driveStop();
            odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
        } else if (odometry.getY() > targetY){
            strafe(-power * forwardsBackwardsFactor);
            double startTime = runtime.seconds();
            while (opMode.opModeIsActive() && runtime.seconds() - startTime < timeout && odometry.getY() > targetY) {
                odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
                Thread.yield();
                opMode.telemetry.update();
            }
            driveStop();
            odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
        }
    }
    public void driveToX(double targetX, double power, MecanumOdometry odometry, ElapsedTime runtime, double timeout){
        int forwardsBackwardsFactor;
        if (odometry.getTheta() > Math.PI && odometry.getTheta() < 2 * Math.PI){
            goToTheta(3 * Math.PI / 2,.7,odometry, runtime , timeout);
            forwardsBackwardsFactor = 1;
        } else {
            goToTheta(Math.PI / 2,.7,odometry, runtime , timeout);
            forwardsBackwardsFactor = -1;
        }
        odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
        if (odometry.getX() < targetX){
            drive(power * forwardsBackwardsFactor);
            double startTime = runtime.seconds();
            while (opMode.opModeIsActive() && runtime.seconds() - startTime < timeout && odometry.getX() < targetX) {
                odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
                Thread.yield();
                opMode.telemetry.update();
            }
            driveStop();
            odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
        } else if (odometry.getX() > targetX){
            drive(-power * forwardsBackwardsFactor);
            double startTime = runtime.seconds();
            while (opMode.opModeIsActive() && runtime.seconds() - startTime < timeout && odometry.getX() > targetX) {
                odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
                Thread.yield();
                opMode.telemetry.update();
            }
            driveStop();
            odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
        }
    }
    public void driveToY(double targetY, double power, MecanumOdometry odometry, ElapsedTime runtime, double timeout){
        int forwardsBackwardsFactor;
        if (odometry.getTheta() > Math.PI/2 && odometry.getTheta() < 3 * Math.PI / 2){
            goToTheta(Math.PI,.7,odometry, runtime , timeout);
            forwardsBackwardsFactor = -1;
        } else {
            goToTheta(0,.7,odometry, runtime , timeout);
            forwardsBackwardsFactor = 1;
        }
        odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
        if (odometry.getY() < targetY){
            drive(power * forwardsBackwardsFactor);
            double startTime = runtime.seconds();
            while (opMode.opModeIsActive() && runtime.seconds() - startTime < timeout && odometry.getY() < targetY) {
                odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
                Thread.yield();
                opMode.telemetry.update();
            }
            driveStop();
            odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
        } else if (odometry.getY() > targetY){
            drive(-power * forwardsBackwardsFactor);
            double startTime = runtime.seconds();
            while (opMode.opModeIsActive() && runtime.seconds() - startTime < timeout && odometry.getY() > targetY) {
                odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
                Thread.yield();
                opMode.telemetry.update();
            }
            driveStop();
            odometry.update(FR.getCurrentPosition(),FL.getCurrentPosition(),BL.getCurrentPosition());
        }
    }
}

