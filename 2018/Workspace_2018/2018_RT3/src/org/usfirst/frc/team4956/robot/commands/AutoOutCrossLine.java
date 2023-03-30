package org.usfirst.frc.team4956.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoOutCrossLine extends CommandGroup {

    public AutoOutCrossLine() {
    	addSequential(new AutoDriveStraight(150, true)); // Want it to go 110in 
    }
}
