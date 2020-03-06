//package org.firstinspires.ftc.teamcode.feeder.feederautos;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.teamcode.feeder.HardwareFeeder;
//import org.firstinspires.ftc.teamcode.feeder.MecanumOdometry;
//
//import static org.firstinspires.ftc.teamcode.Constants.CLOSED_HAND;
//import static org.firstinspires.ftc.teamcode.Constants.OPEN_HAND;
//import static org.firstinspires.ftc.teamcode.feeder.FeederConstants.BLOCK_DOWN;
//import static org.firstinspires.ftc.teamcode.feeder.FeederConstants.BLOCK_UP;
//
//@Autonomous(name="CharlieSkystoneAuto")
//public class CharlieSkystoneAuto extends LinearOpMode {
//
//    private HardwareFeeder rb = new HardwareFeeder();
//
//    private MecanumOdometry odometry = new MecanumOdometry();
//
//    private ElapsedTime runtime = new ElapsedTime();
//
//    private SensorColor colorSensor = new SensorColor();
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        odometry.start(rb.FR.getCurrentPosition(),rb.FL.getCurrentPosition(),rb.BL.getCurrentPosition());
//        String skystone;
//        rb.driveToY(12, .7,odometry,runtime,5 );
//        if (!colorSensor.isYellow()){
//            skystone = "Stone 1";
//            rb.strafeToX(-1, .7, odometry, runtime, 5);
//        } else {
//            rb.strafeToX(-2,.7, odometry, runtime,5);
//            if (!colorSensor.isYellow()){
//                skystone = "Stone 2";
//                rb.strafeToX(-3,.7, odometry, runtime,5);
//            } else {
//                skystone = "Stone 3";
//                rb.strafeToX(-5,.7, odometry, runtime,5);
//            }
//        }
//        rb.driveToY(3, .7, odometry, runtime, 5);
//        rb.blockservo.setPosition(BLOCK_DOWN);
//        rb.driveToY(1.5, .7, odometry, runtime, 5);
//        rb.goToTheta(3 * Math.PI / 2, .7, odometry, runtime, 2);
//        rb.driveToX(10, .7, odometry, runtime, 5);
//        rb.blockservo.setPosition(BLOCK_UP);
//        switch (skystone){
//            case "Stone 1":
//                rb.driveToX(-7,.7, odometry,runtime,5);
//                break;
//            case "Stone 2":
//                rb.driveToX(-9,.7, odometry,runtime,5);
//                break;
//            case "Stone 3":
//                rb.driveToX(-11,.7, odometry,runtime,5);
//                break;
//        }
//        rb.goToTheta(0,.7, odometry,runtime,5);
//        rb.driveToY(3,.7, odometry,runtime,5);
//        rb.blockservo.setPosition(BLOCK_DOWN);
//        rb.driveToY(1, .7, odometry, runtime, 5);
//        rb.goToTheta(3 * Math.PI / 2, .7, odometry, runtime, 2);
//        rb.driveToX(10, .7, odometry, runtime, 5);
//        rb.blockservo.setPosition(BLOCK_UP);
//        rb.strafeToY(0, .7, odometry, runtime, 5);
//        rb.driveToX(13, .7, odometry, runtime, 5);
//    }
//}
