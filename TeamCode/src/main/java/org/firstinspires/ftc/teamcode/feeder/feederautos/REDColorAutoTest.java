package org.firstinspires.ftc.teamcode.feeder.feederautos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "RED Color Auto Test")
public class REDColorAutoTest extends ColorAutoTestOperation {
    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }
}
