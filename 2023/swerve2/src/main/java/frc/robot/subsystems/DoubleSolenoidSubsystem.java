package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DoubleSolenoidSubsystem extends SubsystemBase {
  
  DoubleSolenoid solenoid;

  public DoubleSolenoidSubsystem(int forward, int reverse, String name) {
     solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, forward, reverse);
    
     ShuffleboardTab supporttab = Shuffleboard.getTab("Support");
     supporttab
      .add(name, solenoid)
      .withWidget(BuiltInWidgets.kRelay);
    }

  public void set(boolean forward){
    solenoid.set(forward ? Value.kForward : Value.kReverse);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
