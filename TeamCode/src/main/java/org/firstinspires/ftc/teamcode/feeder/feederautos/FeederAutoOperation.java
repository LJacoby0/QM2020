package org.firstinspires.ftc.teamcode.feeder.feederautos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.feeder.HardwareFeeder;

import static org.firstinspires.ftc.teamcode.feeder.feederautos.Alliance.BLUE;
import static org.firstinspires.ftc.teamcode.feeder.feederautos.Alliance.RED;


public abstract class FeederAutoOperation extends LinearOpMode {

    public abstract Alliance getAlliance();

    @Override
    public void runOpMode() throws InterruptedException {
        double blueNegativeFactor = getAlliance() == BLUE ? -1 : 1;

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

        //back towards the wall
        runtime.reset();
        while(runtime.seconds() < 1.3) {
            robot.drive(-0.5);
        }

        //rotate
        runtime.reset();
        while(runtime.seconds() < 2.5) {
            if(getAlliance() == BLUE) {
                robot.drive(0.5, -0.5);
            } else {
                robot.drive(-0.5, 0.5);
            }
        }

        // move the platform up
        robot.setPlatformUp(true);

        //forward to platform
        runtime.reset();
        while(runtime.seconds() < 1.5) {
            robot.drive(0.5);
        }
        robot.driveStop();


        //back to park
        runtime.reset();
        while(runtime.seconds() < 0.9) {
            robot.drive(-1);
        }
        robot.driveStop();

//        runtime.reset();
//        while(runtime.seconds() < 0.5) {
//            robot.driveStop();
//        }
//
//        //move under the bridge
//        runtime.reset();
//        while(runtime.seconds() < 4) {
//            robot.strafe(blueNegativeFactor * 0.5);
//        }
        robot.driveStop();
    }
}
