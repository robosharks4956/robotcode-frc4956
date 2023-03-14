// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.BasicMotor;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.SimpleSparkMAX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final LEDs m_leds = new LEDs();
  private final BasicMotor m_basicMotor = new BasicMotor();
  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);
  private final SendableChooser<Command> m_chooser = new SendableChooser<>();
  private final SimpleSparkMAX m_SparkMAX = new SimpleSparkMAX();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
    m_chooser.setDefaultOption("move", new RunCommand(() -> m_basicMotor.set(.5)));
    m_chooser.addOption("move faster", new RunCommand(() -> m_basicMotor.set(1)));
    m_chooser.addOption("timed move", new RunCommand(() -> {
      m_basicMotor.set(.5);
    }).withTimeout(2)
    .andThen(() -> m_basicMotor.set(0), m_basicMotor));
    m_chooser.addOption("timed move 2", 
    Commands.runOnce(()-> m_basicMotor.set(.5), m_basicMotor)
    .repeatedly()
    .withTimeout(2)
    .andThen(() -> m_basicMotor.set(0), m_basicMotor));
    SmartDashboard.putData (m_chooser);
  }
  public void setAllianceLEDs() {
    Alliance alliance = DriverStation.getAlliance();
    if (DriverStation.getAlliance() == Alliance.Red) {
      m_leds.setRed();
    }
    if (DriverStation.getAlliance() == Alliance.Blue) {
      m_leds.setBlue();
    }
  }
  public void setRainbow(){
    m_leds.rainbow();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    
    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    m_driverController.b().whileTrue(new RunCommand(() -> m_basicMotor.set(.5)));
    m_driverController.a().whileTrue(new RunCommand(() -> m_basicMotor.set(0)));
    m_driverController.x().whileTrue(new RunCommand(() -> m_SparkMAX.set(1)).repeatedly());
    m_driverController.y().whileTrue(new RunCommand(() -> m_SparkMAX.set(0)).repeatedly());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return m_chooser.getSelected();
  }

}
