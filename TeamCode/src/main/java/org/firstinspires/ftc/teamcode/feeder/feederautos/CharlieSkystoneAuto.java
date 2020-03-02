package org.firstinspires.ftc.teamcode.feeder.feederautos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.feeder.HardwareFeeder;
import org.firstinspires.ftc.teamcode.feeder.MecanumOdometry;

public class CharlieSkystoneAuto extends LinearOpMode {

    private HardwareFeeder rb = new HardwareFeeder();

    private MecanumOdometry odometry = new MecanumOdometry();

    private ElapsedTime runtime = new ElapsedTime();
    @Override
    public void runOpMode() throws InterruptedException {
        odometry.start(rb.FR.getCurrentPosition(),rb.FL.getCurrentPosition(),rb.BL.getCurrentPosition());
        rb.strafeToY(5, .7, odometry, runtime, 5);
    }
}
