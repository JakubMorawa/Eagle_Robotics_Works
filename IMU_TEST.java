package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
@TeleOp(name = "IMU TEST", group = "Robot")
public class IMU_TEST extends OpMode {
    TestBenchIMU bench = new TestBenchIMU();
    @Override
    public void init() {
        bench.init(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("Heading", bench.getHeading());
    }
}
