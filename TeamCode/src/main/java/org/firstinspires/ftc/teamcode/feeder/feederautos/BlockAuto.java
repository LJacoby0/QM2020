package org.firstinspires.ftc.teamcode.feeder.feederautos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Only Tape Park Auto")
public class BlockAuto extends BlockAutoOperation {
    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }
}
