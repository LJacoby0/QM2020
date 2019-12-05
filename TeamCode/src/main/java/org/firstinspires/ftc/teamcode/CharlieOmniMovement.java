package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class CharlieOmniMovement {
    DcMotor fMotor = null;
    DcMotor bMotor = null;
    DcMotor lMotor = null;
    DcMotor rMotor = null;

    Servo leftServo = null;
    Servo rightServo = null;

    LinearOpMode opMode;

    void init(HardwareMap hardwareMap, LinearOpMode opMode) {
        this.opMode = opMode;

        // Initialize drive motors
        fMotor = hardwareMap.get(DcMotor.class, "frontDrive");
        bMotor = hardwareMap.get(DcMotor.class, "backDrive");
        lMotor = hardwareMap.get(DcMotor.class, "leftDrive");
        rMotor = hardwareMap.get(DcMotor.class, "rightDrive");

        // Initialize servos
        leftServo = hardwareMap.get(Servo.class, "LS");
        rightServo = hardwareMap.get(Servo.class, "RS");

        // Set motor directions
        fMotor.setDirection(DcMotor.Direction.FORWARD);
        bMotor.setDirection(DcMotor.Direction.FORWARD);
        lMotor.setDirection(DcMotor.Direction.FORWARD);
        rMotor.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to brake when power is zero
        fMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    void driveStop() {
        fMotor.setPower(0);
        bMotor.setPower(0);
        lMotor.setPower(0);
        rMotor.setPower(0);
    }
    /**
     * Driving code.
     *
     * @param x_stick       The x value of the left joystick
     * @param y_stick       The y value of the left joystick
     * @param x_right_stick The x value of the right joystick
     * @param multiplier    Number to multiply the power by.
     */
    void drive(double x_stick, double y_stick, double x_right_stick, double multiplier) {
        double fbPower = x_stick;
        double rlPower = y_stick;
        fMotor.setPower(fbPower * multiplier+x_right_stick);
        bMotor.setPower(-fbPower * multiplier+x_right_stick);
        lMotor.setPower(rlPower * multiplier+x_right_stick);
        rMotor.setPower(-rlPower * multiplier+x_right_stick);
    }
    void driveForward(double power) {
        fMotor.setPower(0);
        lMotor.setPower(power);
        bMotor.setPower(0);
        rMotor.setPower(power);
    }

    void driveRight(double power) {
        fMotor.setPower(power);
        lMotor.setPower(0);
        bMotor.setPower(power);
        rMotor.setPower(0);
    }

    void turnClockwise(double power) {
        fMotor.setPower(power);
        lMotor.setPower(power);
        bMotor.setPower(power);
        rMotor.setPower(power);
    }

    }
