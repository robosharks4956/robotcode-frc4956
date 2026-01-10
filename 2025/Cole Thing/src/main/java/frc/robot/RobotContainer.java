// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.swerve.SwerveDriveSubsystem;

public class RobotContainer {
  private final SwerveDriveSubsystem m_swerveDrive = new SwerveDriveSubsystem();

  private final CommandXboxController m_driveController = new CommandXboxController(0);
  private final CommandXboxController m_supportController = new CommandXboxController(1);

  private final SendableChooser<Command> m_autonomousChooser = new SendableChooser<>();

  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    m_swerveDrive.setDefaultCommand(m_swerveDrive.controllerDriveCommand(
      m_driveController::getLeftX,
      m_driveController::getLeftY,
      m_driveController::getRightX,
      m_driveController.rightBumper(),
      m_driveController.leftBumper()
    ));

    m_driveController.a().and(m_driveController.b()).whileTrue(
      m_swerveDrive.aimWheelsCommand(m_driveController::getLeftX, m_driveController::getLeftY)
    );

    m_driveController.x().or(m_driveController.y()).whileTrue(m_swerveDrive.lockPoseCommand());

    m_autonomousChooser.addOption("Nothing", Commands.none());
    
    SmartDashboard.putData("Autonomous", m_autonomousChooser);
  }

  public Command getAutonomousCommand() {
    return m_autonomousChooser.getSelected();
  }
}
