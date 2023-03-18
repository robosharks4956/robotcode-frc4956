
package org.usfirst.frc.team4956.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**										
 * The main autonomous command.
 */

public class Autonomous extends CommandGroup 
{
    
	public double toteHeight = 15;
	public double containerHeight = 22;
	
	public Autonomous()
	{
		// Push tote forward to center of auto zone and stopping.
		addSequential(new TimedDrive(0.75, 3));
	}
}