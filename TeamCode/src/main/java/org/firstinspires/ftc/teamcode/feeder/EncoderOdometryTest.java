package org.firstinspires.ftc.teamcode.feeder;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "EncoderOdometryTest", group = "Test")
public class EncoderOdometryTest extends LinearOpMode {
    private HardwareFeeder rb = new HardwareFeeder();

    private MecanumOdometry odometry = new MecanumOdometry();
    @Override
    public void runOpMode() throws InterruptedException {
        rb.init(hardwareMap,this);
        telemetry.addData("FR Encoder",rb.FR.getCurrentPosition());
        telemetry.addData("FL Encoder",rb.FL.getCurrentPosition());
        telemetry.addData("BL Encoder",rb.BL.getCurrentPosition());
        telemetry.update();
        odometry.start(rb.FR.getCurrentPosition(),rb.FL.getCurrentPosition(),rb.BL.getCurrentPosition());
        while (opModeIsActive()){
            odometry.update(rb.FR.getCurrentPosition(),rb.FL.getCurrentPosition(),rb.BL.getCurrentPosition());
            telemetry.addData("FR Encoder",rb.FR.getCurrentPosition());
            telemetry.addData("FL Encoder",rb.FL.getCurrentPosition());
            telemetry.addData("BL Encoder",rb.BL.getCurrentPosition());
            telemetry.update();
        }
    }
}
