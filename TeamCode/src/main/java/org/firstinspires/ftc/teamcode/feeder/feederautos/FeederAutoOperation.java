package org.firstinspires.ftc.teamcode.feeder.feederautos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.feeder.HardwareFeeder;

public abstract class FeederAutoOperation extends LinearOpMode {

    public abstract Alliance getAlliance();

    @Override
    public void runOpMode() throws InterruptedException {
        double blueNegativeFactor = getAlliance() == Alliance.BLUE ? -1 : 1;

        HardwareFeeder robot = new HardwareFeeder();
        robot.init(hardwareMap);

        ElapsedTime runtime = new ElapsedTime();

        waitForStart();
        runtime.reset();

        //move forward
        while(runtime.seconds() < 0.3) {
            robot.drive(0.5);
        }
        robot.driveStop();

        // move left a bit
        runtime.reset();
        while(runtime.seconds() < 0.5) {
            robot.strafe(blueNegativeFactor * -0.5);
        }
        robot.driveStop();

        //move backward again
        runtime.reset();
        while(runtime.seconds() < 1) {
            robot.drive(-1);
        }

        //forward to platform
        runtime.reset();
        while(runtime.seconds() < 1.5) {
            robot.drive(0.5);
        }
        robot.driveStop();

        robot.setPlatformUp(false);

        runtime.reset();
        while(runtime.seconds() < 0.5) {
            robot.driveStop();
        }

        //back home
        runtime.reset();
        while(runtime.seconds() < 1.5) {
            robot.drive(-0.5);
        }

    }
}
