package org.firstinspires.ftc.teamcode.feeder.feederautos;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Color Auto Test")
public class ColorAutoTest extends ColorAutoTestOperation {
    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }
}
