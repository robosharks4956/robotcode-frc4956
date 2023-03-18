package org.usfirst.frc.team4956.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveAndHoldTote extends CommandGroup {
	public double toteHeight = 15;
	public DriveAndHoldTote()
	{
		addSequential(new TimedDrive(-0.7, 2.3));
		addParallel(new HoldElevatorSetpoint(toteHeight, 2.3));
	}
}
