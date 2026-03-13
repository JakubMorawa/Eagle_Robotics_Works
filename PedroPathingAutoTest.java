package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp
public class PedroPathingAutoTest extends OpMode {

    private Follower follower;
    private Timer pathTimer, opModeTimer;

    public enum PathState {

        DRIVE_START_POS_SHOOT_POS,
        SHOOT_PRELOAD,

        MEET_ARTIFACTS
    }

    PathState pathState;

    private final Pose startPose = new Pose(52.59934853420195, 99.08794788273615, Math.toRadians(143));
    private final Pose shootPose = new Pose(42.846905537459286, 84.11074918566769, Math.toRadians(143));
    private final Pose meetPose = new Pose(42.846905537459286, 84.11074918566769, Math.toRadians(180));

    public PathChain driveStartPosToShootPos, driveShootPosToMeetPos;


    public void buildPaths(){
        driveStartPosToShootPos = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(),shootPose.getHeading())
                .build();
        driveShootPosToMeetPos = follower.pathBuilder()
                .addPath(new BezierLine(shootPose, meetPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(),meetPose.getHeading())
                .build();
    }

    public void statePathUpdate(){
        switch(pathState){
            case DRIVE_START_POS_SHOOT_POS:
                follower.followPath(driveStartPosToShootPos, true);
                setPathState(PathState.SHOOT_PRELOAD); //reset timer and new state
                break;
            case SHOOT_PRELOAD:
                if(!follower.isBusy() && pathTimer.getElapsedTimeSeconds() >3) {
                    // put in shooting code
                    //add check if 3 seconds elapsed
                    telemetry.addLine("Done Shooting");
                    follower.followPath(driveShootPosToMeetPos, true);
                    setPathState(PathState.MEET_ARTIFACTS);
                }
                break;
            case MEET_ARTIFACTS:
                if(!follower.isBusy()) {

                    telemetry.addLine("Done Path");
                }
            default:
                telemetry.addLine("No State Commanded");
                break;
        }
    }

    public void setPathState(PathState newState){
        pathState = newState;
        pathTimer.resetTimer();
    }

    @Override
    public void init() {
        pathState = PathState.DRIVE_START_POS_SHOOT_POS;
        pathTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        //put in other initial mechanisms like camera and motors

        buildPaths();
        follower.setPose(startPose);
    }

    public void start(){
        opModeTimer.resetTimer();
        setPathState(pathState);
    }

    @Override
    public void loop() {
        follower.update();
        statePathUpdate();

        telemetry.addData("Path state", pathState.toString());
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.addData("Path time", pathTimer.getElapsedTimeSeconds());
    }
}
