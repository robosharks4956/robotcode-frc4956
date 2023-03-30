package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoOutScaleDrop extends CommandGroup {

    public AutoOutScaleDrop(double turnDirection) {
    	addSequential(new AutoDriveStraight(309, true)); // Straight Long Enough To Turn 309in
        addSequential(new AutoDriveTurn(turnDirection < 0 ? RobotMap.right90 : RobotMap.left90));
        //addSequential(new AutoDriveStraight(309)); // Drive To Scale??
        // Add Drop Code
    }
}
