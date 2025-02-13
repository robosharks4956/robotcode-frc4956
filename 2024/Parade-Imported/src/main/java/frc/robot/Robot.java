// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
public class Robot extends TimedRobot {
  private final PWMSparkMax m_leftbackMotor = new PWMSparkMax(0);
  private final PWMSparkMax m_righfronttMotor = new PWMSparkMax(2);
  private final PWMSparkMax m_leftfrontMotor = new PWMSparkMax(3);
  private final PWMSparkMax m_rightbackMotor = new PWMSparkMax(1);
  private final DifferentialDrive m_robotDrive =
      new DifferentialDrive(m_leftfrontMotor::set,m_righfronttMotor ::set);
  private final XboxController m_stick = new XboxController(0);

  SlewRateLimiter filter=new SlewRateLimiter(3); // Higher accelerates faster
SlewRateLimiter turnfilter=new SlewRateLimiter(4); // Higher accelerates faster
  public Robot() {
    SendableRegistry.addChild(m_robotDrive,m_leftfrontMotor );
    SendableRegistry.addChild(m_robotDrive,m_righfronttMotor );
  }

  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_righfronttMotor.setInverted(true);
    m_righfronttMotor.addFollower(m_rightbackMotor);
    m_leftfrontMotor .addFollower(m_leftbackMotor);
  }

  @Override
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.
    double forward=-(m_stick.getRightTriggerAxis()-m_stick.getLeftTriggerAxis());
    m_robotDrive.arcadeDrive(filter.calculate(forward),turnfilter.calculate(-m_stick.getLeftX()));
  }
}
