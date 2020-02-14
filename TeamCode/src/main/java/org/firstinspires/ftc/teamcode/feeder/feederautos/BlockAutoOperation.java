package org.firstinspires.ftc.teamcode.feeder.feederautos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.feeder.HardwareFeeder;

import static org.firstinspires.ftc.teamcode.feeder.feederautos.Alliance.BLUE;
import static org.firstinspires.ftc.teamcode.feeder.feederautos.Alliance.RED;


public abstract class BlockAutoOperation extends LinearOpMode {

    public abstract Alliance getAlliance();

    @Override
    public void runOpMode() throws InterruptedException {
        double blueNegativeFactor = getAlliance() == BLUE ? -1 : 1;

        HardwareFeeder robot = new HardwareFeeder();
        robot.init(hardwareMap);

        ElapsedTime runtime = new ElapsedTime();

        waitForStart();
        runtime.reset();

        //back to park
        runtime.reset();
        while(runtime.seconds() < 25) {
            robot.driveStop();

        }
        robot.driveStop();


        //back to park
        runtime.reset();
        while(runtime.seconds() < 0.9) {
            robot.drive(1);
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
