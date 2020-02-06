package org.firstinspires.ftc.teamcode.feeder.feederautos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Blue Feeder Auto")
public class BlueFeederAuto extends FeederAutoOperation {
    @Override
    public Alliance getAlliance() {
        return Alliance.BLUE;
    }
}
