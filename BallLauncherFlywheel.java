package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp(name="Ball Launcher Flywheel", group="Robot")
public class BallLauncherFlywheel extends LinearOpMode {

    DcMotorEx flywheel;

    // PIDF values (starting point)
    static final double P = 5;
    static final double I = 0;
    static final double D = 0;
    static final double F = 12;

    // Desired speed in ticks per second
    static final double TARGET_VELOCITY = 2000;

    @Override
    public void runOpMode() {

        flywheel = hardwareMap.get(DcMotorEx.class, "ballLauncher");

        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pidf = new PIDFCoefficients(P, I, D, F);
        flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        waitForStart();

        while(opModeIsActive()) {

            // Press A to start flywheel
            if(gamepad1.a) {
                flywheel.setVelocity(TARGET_VELOCITY);
            }

            // Press B to stop
            if(gamepad1.b) {
                flywheel.setVelocity(0);
            }

            telemetry.addData("Flywheel Velocity", flywheel.getVelocity());
            telemetry.update();
        }
    }
}
