package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "AprilTag Encoder Search", group = "Vision")
public class HuskyLensTest extends LinearOpMode {

    private HuskyLens huskyLens;
    private DcMotor leftFront;

    int stepSize = 3;
    int targetPosition = 0;
    double waitTimeSec = 0.5;

    ElapsedTime waitTimer = new ElapsedTime();

    enum SearchState {
        MOVING,
        WAITING,
        TAG_FOUND
    }

    SearchState state = SearchState.WAITING;

    @Override
    public void runOpMode() {

        huskyLens = hardwareMap.get(HuskyLens.class, "huskyLens");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");

//        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        if (!huskyLens.knock()) telemetry.addData("HuskyLens", "Communication FAILED");
        else telemetry.addData("HuskyLens", "Connected");

        huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);

        telemetry.addLine("Press Start");
        telemetry.update();
        waitForStart();

        waitTimer.reset();

        while (opModeIsActive()) {

            HuskyLens.Block[] blocks = huskyLens.blocks();

            telemetry.addData("State", state);
            telemetry.addData("Blocks", blocks.length);
            telemetry.addData("Encoder", leftFront.getCurrentPosition());
            telemetry.addData("Target", targetPosition);

            // ---------------- TAG FOUND ----------------
            if (blocks.length > 0) {
                state = SearchState.TAG_FOUND;
                leftFront.setPower(0);

                HuskyLens.Block tag = blocks[0];
                telemetry.addLine("APRIL TAG FOUND");
                telemetry.addData("Tag ID", tag.id);
                telemetry.addData("X", tag.x);
                telemetry.addData("Y", tag.y);
                telemetry.addData("Width", tag.width);
                telemetry.addData("Height", tag.height);
            }

            else {
                leftFront.setPower(0.1);
                sleep(100);
                leftFront.setPower(0);
                sleep(300);
            }

            telemetry.update();
        }
    }
}