package org.usfirst.frc.team4956.robot.commands;

import org.usfirst.frc.team4956.robot.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoInCurvesDrop extends CommandGroup {

    public AutoInCurvesDrop() {
    	addSequential(new AutoDriveStraight(30, true)); // Straight Long Enough To Turn
        addSequential(new AutoDriveTurn(RobotMap.left90));
        addSequential(new AutoDriveStraight(95, false)); // Straight Long Enough To Turn
        addSequential(new AutoDriveTurn(RobotMap.right90));
    	addSequential(new AutoDriveStraight(50,  false)); // Straight Long Enough To Turn
    	addSequential(new AutoLiftPunch(3, 10));
    }
}
