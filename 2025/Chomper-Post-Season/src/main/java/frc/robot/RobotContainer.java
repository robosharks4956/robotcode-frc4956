// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.SwerveSubsystem;

public class RobotContainer {
  public final SwerveSubsystem m_swerve = new SwerveSubsystem();
  
  private final CommandXboxController m_driveController = new CommandXboxController(0);
  private final CommandXboxController m_supportController = new CommandXboxController(1);

  private final SendableChooser<Command> m_chooser = new SendableChooser<Command>();

  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    m_chooser.setDefaultOption("Nothing", Commands.none());

    Shuffleboard.getTab("Drive").add("Autonomous Chooser", m_chooser).withWidget(BuiltInWidgets.kComboBoxChooser);

    m_swerve.setDefaultCommand(m_swerve.controllerDriveCommand(
      m_driveController::getLeftX,
      m_driveController::getLeftY,
      m_driveController::getRightX,
      m_driveController.rightBumper(),
      m_driveController.leftBumper()
    ));
  }

  public Command getAutonomousCommand() {
    return m_chooser.getSelected();
  }
}
