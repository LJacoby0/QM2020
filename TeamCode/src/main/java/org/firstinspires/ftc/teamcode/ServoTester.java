

        package org.firstinspires.ftc.teamcode;

        import com.qualcomm.robotcore.eventloop.opmode.Disabled;
        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
        import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//        import com.qualcomm.robotcore.hardware.CRServo;
        import com.qualcomm.robotcore.hardware.Servo;


        import org.firstinspires.ftc.teamcode.HardwarePot;

        /**
 * This OpMode scans a single servo back and forwards until Stop is pressed.
 * The code is structured as a LinearOpMode
 * INCREMENT sets how much to increase/decrease the servo position each cycle
 * CYCLE_MS sets the update period.
 *
 * This code assumes a Servo configured with the name "left_hand" as is found on a pushbot.
 *
 * NOTE: When any servo position is set, ALL attached servos are activated, so ensure that any other
 * connected servos are able to move freely before running this test.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
        @TeleOp(name = "Servo Tester", group = "Sensor")
        public class ServoTester extends LinearOpMode {

            // Define class members
        private double position1 = 0.5; // Start at halfway position so was .5
                private double position2 = 0.5; // Start at halfway position so was .5
                private double position3 = 0.5; // Start at halfway position so was .5
                private double position4 = 0.5; // Start at halfway position so was .5





                private final double INCREMENT = 0.01 ;
        private final int DELAY = 100;//was 100

        @Override
        public void runOpMode() {

        // Change the text in quotes to match any servo name on your rb.
//        CRServo servo = hardwareMap.get(CRServo.class, "hand");
        Servo servo = hardwareMap.get(Servo.class, "platform left");
        Servo servo2 = hardwareMap.get(Servo.class, "platform right");
//        Servo servo3 = hardwareMap.get(Servo.class, "capstone servo");
        Servo servo4 = hardwareMap.get(Servo.class, "blockservo");




                // Wait for the start button
        telemetry.addData(">", "Press Start to scan Servo." );
        telemetry.update();
        waitForStart();


        // Scan servo till stop pressed.
        while(opModeIsActive()){

                // slew the servo, according to the rampUp (direction) variable.
                if (gamepad1.left_bumper) {
                        // Keep stepping up until we hit the max value.
                        position1 += INCREMENT;
                } else if (gamepad1.right_bumper) {
                        // Keep stepping down until we hit the min value.
                        position1 -= INCREMENT;
                }
                if (gamepad1.dpad_up) {
                        // Keep stepping up until we hit the max value.
                        position2 += INCREMENT;
                } else if (gamepad1.dpad_down) {
                        // Keep stepping down until we hit the min value.
                        position2 -= INCREMENT;
                }

                if (gamepad1.y) {
                        // Keep stepping up until we hit the max value.
                        position3 += INCREMENT;
                } else if (gamepad1.a) {
                        // Keep stepping down until we hit the min value.
                        position3 -= INCREMENT;
                }

                if (gamepad1.x) {
                        position4 += INCREMENT;
                }
                else if (gamepad1.b) {
                        position4 += INCREMENT;
                }

                // Display the current value
                telemetry.addData("Servo Left Position", "%5.2f", position1);
                telemetry.addData("Servo right Position", "%5.2f", position2);
                telemetry.addData("Servo right Position", "%5.2f", position3);
                telemetry.addData("Block Servo Position", "%5.2f", position4);



                telemetry.addData(">", "Press Stop to end test.");
                telemetry.update();

                // Set the servo to the new position and pause;
                servo.setPosition(position1);
                servo2.setPosition(position2);
//                servo3.setPosition(position3);
                servo4.setPosition(position4);


                sleep(DELAY);
                idle();
        }

        // Signal done;
        telemetry.addData(">", "Done");
        telemetry.update();
        }
        }