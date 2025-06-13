package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.simplicityftc.commandbase.InstantCommand;
import org.simplicityftc.commandbase.SequentialCommand;
import org.simplicityftc.commandbase.SleepCommand;
import org.simplicityftc.commandbase.WaitUntilCommand;
import org.simplicityftc.drivetrain.DrivetrainSettings;
import org.simplicityftc.drivetrain.MecanumDrive;
import org.simplicityftc.drivetrain.follower.Drivetrain;
import org.simplicityftc.drivetrain.follower.Follower;
import org.simplicityftc.util.SimpleOpMode;
import org.simplicityftc.util.math.Pose;

@Autonomous(group = "Tuners")
public class P2PTest extends SimpleOpMode {

    Drivetrain drivetrain;
    Follower follower;

    @Override
    public void onInit() {
        drivetrain = new MecanumDrive();
        follower = drivetrain.getFollower();

        DrivetrainSettings.localizer.setPose(new Pose()); //wtf is this why is it static
        //something's wrong, wtf did i do?? TODO: move drivetrain settings to the drivetrain class itself
        //am i skibid?? brainrot is consuming me ahhlkjdfgl;kjdsf;gjdlfk
        //am i skibid?? brainrot is consuming me ahhlkjdfgl;kjdsf;gjdlfk
        //am i skibid?? brainrot is consuming me ahhlkjdfgl;kjdsf;gjdlfk
        //am i skibid?? brainrot is consuming me ahhlkjdfgl;kjdsf;gjdlfk
        //am i skibid?? brainrot is consuming me ahhlkjdfgl;kjdsf;gjdlfk
        //am i skibid?? brainrot is consuming me ahhlkjdfgl;kjdsf;gjdlfk

        commandScheduler.schedule(new SequentialCommand(
                new SleepCommand(1),
                new InstantCommand(() -> drivetrain.getFollower().add(new Pose(60, 0, 0))),
                new WaitUntilCommand(() -> drivetrain.getFollower().atTarget()),
                new InstantCommand(() -> follower.add(new Pose(60, 0, Math.toRadians(90)))),
                new WaitUntilCommand(() -> follower.atTarget()),
                new InstantCommand(() -> follower.add(new Pose(60, 60, Math.toRadians(90)))),
                new WaitUntilCommand(() -> follower.atTarget()),
                new InstantCommand(() -> follower.add(new Pose(60, 60, Math.toRadians(180)))),
                new WaitUntilCommand(() -> follower.atTarget()),
                new InstantCommand(() -> follower.add(new Pose(0, 60, Math.toRadians(180)))),
                new WaitUntilCommand(() -> follower.atTarget()),
                new InstantCommand(() -> follower.add(new Pose(0, 60, Math.toRadians(270)))),
                new WaitUntilCommand(() -> follower.atTarget()),
                new InstantCommand(() -> follower.add(new Pose(0, 0, Math.toRadians(270)))),
                new WaitUntilCommand(() -> follower.atTarget()),
                new InstantCommand(() -> follower.add(new Pose(0, 0, Math.toRadians(0)))),
                new WaitUntilCommand(() -> follower.atTarget()),
                new SleepCommand(5),
                new InstantCommand(() -> {
                    follower.add(new Pose())
                            .add(new Pose())
                            .clear()
                            .add(new Pose(15, 0, 0))
                            .add(new Pose(0, 0, 0));
                })
        ));
        drivetrain.setDriveMode(Drivetrain.DriveMode.AUTONOMOUS);
    }

    @Override
    public void run() {
        drivetrain.update();
        commandScheduler.run();
    }
}
