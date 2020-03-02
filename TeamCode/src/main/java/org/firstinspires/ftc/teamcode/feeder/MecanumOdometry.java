package org.firstinspires.ftc.teamcode.feeder;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.feeder.feederautos.IMU;

public class MecanumOdometry {
    // Encoder constants
    public static final double CM_PER_TICK_A = -100.0 / 1673.0;
    public static final double CM_PER_TICK_B = -100.0 / 1693.0;
    public static final double CM_PER_TICK_C = -100.0 / 1434.0;
    // This is theoretically correct, but may need to be changed in practice
    public static final double STRAFE_CONSTANT = .5;

    // The current position and orientation of the robot
    private double x = 0;
    private double y = 0;
    private double theta = 0;

    // The angle at which the robot was facing when the OpMode started
    public float startingAngle = 0;

    // The previous encoder values
    private double oldA = 0;
    private double oldB = 0;
    private double oldC = 0;

    private double dx = 0;
    private double dy = 0;
    // The IMU handler - tracks the robot's orientation
    public IMU imu = new IMU();

    /**
     * Should be called in when the OpMode is initialized
     */
    public void init(HardwareMap hardwareMap) {
        imu.initIMU(hardwareMap);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTheta() {
        return theta;
    }

    /**
     * Should be called when the OpMode is started
     * Once again
     * @param a The current front-right encoder value
     * @param b The current front-left encoder value
     * @param c The current back-left encoder value
     */
    public void start(double a, double b, double c) {
        imu.update();
        startingAngle = imu.getYAngle();
        x = 0;
        y = 0;
        oldA = a;
        oldB = b;
        oldC = c;
    }

    /**
     * Updates the position and orientation based on new encoder values and the IMU
     *
     * @param a The current front-right encoder value
     * @param b The current front-left encoder value
     * @param c The current back-left encoder value
     */
    public void update(double a, double b, double c) {
        // Calculate the differences between the new and old encoder values
        // and convert them to centimeters.
        double dA = (a - oldA) * CM_PER_TICK_A;
        double dB = (b - oldB) * CM_PER_TICK_B;
        double dC = (c - oldC) * CM_PER_TICK_C;


        oldA = a;
        oldB = b;
        oldC = c;

        // Update the orientation using data from the IMU, the angle gotten might be incorrect
        imu.update();
        theta = imu.getYAngle() - startingAngle;

        // Update the total displacement using the orientation and encoder displacements
        double averageEncoderChange = (Math.abs(dB) + Math.abs(dC)) / 2;
        if (dB > 0 && dC < 0) {
            dx = averageEncoderChange * STRAFE_CONSTANT * Math.cos(theta);
            dy = averageEncoderChange * STRAFE_CONSTANT * Math.sin(theta);
        } else if (dB < 0 && dC > 0) {
            dx = -averageEncoderChange * STRAFE_CONSTANT * Math.cos(theta);
            dy = -averageEncoderChange * STRAFE_CONSTANT * Math.sin(theta);
        } else {
            dx = (dA + dB) / 2 * Math.sin(theta);
            dy = (dA + dB) / 2 * Math.cos(theta);
        }

        // Add the displacement to the total position
        this.x += dx;
        this.y += dy;
    }
}
