package org.usfirst.frc.team4956.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoInCrossLine extends CommandGroup {

    public AutoInCrossLine() {
    	addSequential(new AutoDriveStraight(110, true)); // Want it to go 110in 
    }
}
