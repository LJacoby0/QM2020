package org.firstinspires.ftc.teamcode.feeder.feederautos;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "BLUE Color Auto Test")
public class BLUEColorAutoTest extends ColorAutoTestOperation {
    @Override
    public Alliance getAlliance() {
        return Alliance.BLUE;
    }
}
