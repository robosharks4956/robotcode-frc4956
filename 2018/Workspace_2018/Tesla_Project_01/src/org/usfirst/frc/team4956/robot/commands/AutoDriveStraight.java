package org.usfirst.frc.team4956.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoDriveStraight extends CommandGroup {

    public AutoDriveStraight(double speed, double time) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	addSequential(new TimeDrive(speed, time));
    }
}

