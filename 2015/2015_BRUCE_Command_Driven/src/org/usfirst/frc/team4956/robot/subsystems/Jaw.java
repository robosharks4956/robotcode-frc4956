package org.usfirst.frc.team4956.robot.subsystems;

import org.usfirst.frc.team4956.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Jaw extends Subsystem {

    DoubleSolenoid jawSolenoid; 

    public Jaw() {
        super();
        
        jawSolenoid = new DoubleSolenoid(RobotMap.forwardJawSolenoid, RobotMap.reverseJawSolenoid);
    }

    public void initDefaultCommand() {}
    public void log() {
    	//SmartDashboard.putString("Arm Setting", armSolenoid.get().toString());
    }
    
    /**
     * Set the Arm solenoid to forward
     */
    public void raise() {
    	jawSolenoid.set(Value.kForward);
    }

    /**
     * Set the Arm solenoid to reverse
     */
    public void lower() {
    	jawSolenoid.set(Value.kReverse);
    }
    
    /**
     * Set the Arm solenoid to off
     */
    public void stop() {
    	jawSolenoid.set(Value.kOff);
    }
}
