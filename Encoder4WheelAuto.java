package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="4 Wheel Encoder Auto", group="Robot")
public class Encoder4WheelAuto extends LinearOpMode {

    // Motors
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    private ElapsedTime runtime = new ElapsedTime();

    // Encoder constants
    static final double COUNTS_PER_MOTOR_REV = 537.7;
    static final double DRIVE_GEAR_REDUCTION = 1.0;
    static final double WHEEL_DIAMETER_INCHES = 4.0;

    static final double COUNTS_PER_INCH =
            (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                    (WHEEL_DIAMETER_INCHES * Math.PI);

    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;

    @Override
    public void runOpMode() {

        // Initialize motors
        frontLeft = hardwareMap.get(DcMotor.class, "front_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backLeft = hardwareMap.get(DcMotor.class, "back_left");
        backRight = hardwareMap.get(DcMotor.class, "back_right");

        // Reverse left motors
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        // Reset encoders
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Run with encoders
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        // Autonomous path
        encoderDrive(DRIVE_SPEED, 48,48,48,48,5);     // forward
        encoderDrive(TURN_SPEED, 12,-12,12,-12,4);    // turn right
        encoderDrive(DRIVE_SPEED,-24,-24,-24,-24,4);  // backward

        telemetry.addLine("Path Complete");
        telemetry.update();
    }

    // Encoder drive method for 4 motors
    public void encoderDrive(double speed,
                             double flInches,
                             double frInches,
                             double blInches,
                             double brInches,
                             double timeoutS) {

        int newFL;
        int newFR;
        int newBL;
        int newBR;

        if (opModeIsActive()) {

            // Calculate targets
            newFL = frontLeft.getCurrentPosition() + (int)(flInches * COUNTS_PER_INCH);
            newFR = frontRight.getCurrentPosition() + (int)(frInches * COUNTS_PER_INCH);
            newBL = backLeft.getCurrentPosition() + (int)(blInches * COUNTS_PER_INCH);
            newBR = backRight.getCurrentPosition() + (int)(brInches * COUNTS_PER_INCH);

            // Set targets
            frontLeft.setTargetPosition(newFL);
            frontRight.setTargetPosition(newFR);
            backLeft.setTargetPosition(newBL);
            backRight.setTargetPosition(newBR);

            // Run to position
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();

            // Start motion
            frontLeft.setPower(Math.abs(speed));
            frontRight.setPower(Math.abs(speed));
            backLeft.setPower(Math.abs(speed));
            backRight.setPower(Math.abs(speed));


            while (opModeIsActive()
                    && runtime.seconds() < timeoutS
                    && frontLeft.isBusy()
                    && frontRight.isBusy()
                    && backLeft.isBusy()
                    && backRight.isBusy()) {

                telemetry.addData("FL", frontLeft.getCurrentPosition());
                telemetry.addData("FR", frontRight.getCurrentPosition());
                telemetry.addData("BL", backLeft.getCurrentPosition());
                telemetry.addData("BR", backRight.getCurrentPosition());
                telemetry.update();
            }

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(100);
        }
    }
}
