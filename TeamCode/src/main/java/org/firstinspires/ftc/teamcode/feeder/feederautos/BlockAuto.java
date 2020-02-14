package org.firstinspires.ftc.teamcode.feeder.feederautos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "!Block Auto")
public class BlockAuto extends BlockAutoOperation {
    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }
}
