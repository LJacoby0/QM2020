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
        robot.init(hardwareMap,this);

        ElapsedTime runtime = new ElapsedTime();

        waitForStart();
        runtime.reset();
while (opModeIsActive()) {
    //back to park
    while (opModeIsActive() && runtime.seconds() < 1.75) {
        robot.tapeOut();
    }
    robot.tapeStop();
    robot.driveStop();
}
    }
}
