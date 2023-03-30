package org.usfirst.frc.team4956.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoInCrossLine extends CommandGroup {

    public AutoInCrossLine() {
    	addSequential(new TimeDrive(0.8, 5)); // Want it to go 110in 
    }
}
