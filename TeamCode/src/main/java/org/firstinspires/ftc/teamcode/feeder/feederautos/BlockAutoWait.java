package org.firstinspires.ftc.teamcode.feeder.feederautos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "OLD Block Auto Wait")
public class BlockAutoWait extends BlockAutoWaitOperation {
    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }
}
