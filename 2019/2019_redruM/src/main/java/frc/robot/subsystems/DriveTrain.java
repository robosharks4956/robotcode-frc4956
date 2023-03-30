/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

//import java.text.DecimalFormat;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import frc.robot.RobotMap;
import frc.robot.commands.MecanumDriveWithController;
//import frc.robot.test.MecanumDriveDS;

/**
 * Add your docs here.
 */
public class DriveTrain extends Subsystem {

  public Spark front_left_motor, front_right_motor
    , back_left_motor, back_right_motor;
  MecanumDrive drive;

  public DriveTrain() {
    super();
    initMotors();
  }

  public void initMotors() {
    front_left_motor = new Spark(RobotMap.sparkFrontLeftMotor);
    back_left_motor = new Spark(RobotMap.sparkBackLeftMotor);
    front_right_motor = new Spark(RobotMap.sparkFrontRightMotor);
    back_right_motor = new Spark(RobotMap.sparkBackRightMotor);

    // Invert the front right motor
    front_right_motor.setInverted(true);

    drive = new MecanumDrive(front_left_motor, back_left_motor, front_right_motor, back_right_motor);
    drive.setMaxOutput(1);
    drive.setDeadband(0.05);
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
  public void initDefaultCommand() {
    setDefaultCommand(new MecanumDriveWithController());
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
