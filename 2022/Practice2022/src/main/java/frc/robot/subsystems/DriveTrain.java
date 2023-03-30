package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveTrain extends SubsystemBase {

  CANSparkMax frontLeft, frontRight, rearLeft, rearRight;
  MecanumDrive drive;

  public DriveTrain() {

      frontLeft = new CANSparkMax(25, MotorType.kBrushless);
      frontRight = new CANSparkMax(26, MotorType.kBrushless);
      rearLeft = new CANSparkMax(27, MotorType.kBrushless);
      rearRight = new CANSparkMax(28, MotorType.kBrushless);

      // Invert the front right motor
      frontRight.setInverted(true);
      frontLeft.setInverted(false);
      rearLeft.setInverted(false);
      rearRight.setInverted(true);

      drive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);
      drive.setMaxOutput(1);
      drive.setDeadband(0.05);
      drive.setExpiration(0.2); // Increase expiration to eliminate annoying console 
  }

  public void MecanumDrive(double x, double y, double z) {

    // Need to add a deadband for z (rotation) because our joysticks stick
    // and even a small z (like 0.05) will make the robot drive diagonally
    z = applyDeadband(z, 0.1);

    // Feed inputs to the mecanum drive class
    drive.driveCartesian(y, x, z);
    
    
   // Print inputs and outputs to the screen for debugging
   // DecimalFormat f = new DecimalFormat("#0.00");
   // System.out.println("(y,x,z): (" + f.format(x) + "," + f.format(y) + "," + f.format(z) + ") flm: " + f.format(front_left_motor.getSpeed()) + "|| blm: " + f.format(back_left_motor.getSpeed()) + "|| frm: " +
   //  f.format(front_right_motor.getSpeed()) + "|| brm: " + f.format(back_right_motor.getSpeed()));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  // applyDeadband shamelessly copied from RobotDriveBase
  protected double applyDeadband(double value, double deadband) {
    if (Math.abs(value) > deadband) {
      if (value > 0.0) {
        return (value - deadband) / (1.0 - deadband);
      } else {
        return (value + deadband) / (1.0 - deadband);
      }
    } else {
      return 0.0;
    }
  }
}
