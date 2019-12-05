package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.Constants.DRIVE_POWER;
import static org.firstinspires.ftc.teamcode.Constants.DRIVE_POWER_SLOW;
import static org.firstinspires.ftc.teamcode.Constants.DRIVE_STICK_THRESHOLD;
import static org.firstinspires.ftc.teamcode.Constants.DRIVE_STICK_THRESHOLD_SQUARED;

import static org.firstinspires.ftc.teamcode.Constants.TRIGGER_THRESHOLD;

public class CharlieTeleOp {
    @TeleOp(name = "UPTeleOp", group = "TeleOp")
    public class UPTeleOp extends OpMode {

        /**
         * Amount of time elapsed
         */
        private ElapsedTime runtime = new ElapsedTime();

        private CharlieOmniMovement rb = new CharlieOmniMovement();

        /**
         * This method will be called once when the INIT button is pressed.
         */
        @Override
        public void init() {
            telemetry.addData("Status", "Initializing");

            rb.init(hardwareMap, null);

            telemetry.addData("Status", "Initialized");
        }

        /**
         * This method will be called once when the PLAY button is first pressed.
         */
        @Override
        public void start() {
            // Reset elapsed time
            runtime.reset();
        }

        /**
         * This method will be called repeatedly in a loop while this op mode is running
         */
        @Override
        public void loop() {
            telemetry.addData("Status", "Looping");

            driveChassis();
        }

        /**
         * Drive the chassis according to joysticks
         */
        private void driveChassis() {
            float leftY = -gamepad1.left_stick_y;
            float leftX = gamepad1.left_stick_x;
            float rightX = gamepad1.right_stick_x;

            double pow;
            if (gamepad1.right_trigger >= TRIGGER_THRESHOLD) {
                pow = DRIVE_POWER_SLOW;
            } else {
                pow = DRIVE_POWER;
            }

            if (leftX * leftX + leftY * leftY >= DRIVE_STICK_THRESHOLD_SQUARED || Math.abs(rightX) >= DRIVE_STICK_THRESHOLD) {
                rb.drive(leftX, leftY, rightX, pow);
            } else {
                rb.driveStop();
            }
        }
    }
}
