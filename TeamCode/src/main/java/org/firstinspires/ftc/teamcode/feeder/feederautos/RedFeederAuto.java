package org.firstinspires.ftc.teamcode.feeder.feederautos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "!RED Feeder Auto (Timed)")
public class RedFeederAuto extends FeederAutoOperation {
    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }
}
