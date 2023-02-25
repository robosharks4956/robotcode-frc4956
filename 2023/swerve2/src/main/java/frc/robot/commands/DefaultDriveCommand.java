package frc.robot.commands;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class DefaultDriveCommand extends CommandBase {
    private final Drivetrain m_drivetrainSubsystem;

    private final DoubleSupplier m_translationXSupplier;
    private final DoubleSupplier m_translationYSupplier;
    private final DoubleSupplier m_rotationSupplier;
    private final BooleanSupplier m_fieldrelativeSupplier;
    private final DoubleSupplier m_maxspeedSupplier;


    public DefaultDriveCommand(Drivetrain drivetrainSubsystem,
                               DoubleSupplier translationXSupplier,
                               DoubleSupplier translationYSupplier,
                               DoubleSupplier rotationSupplier,
                               BooleanSupplier fieldrelativeSupplier,
                               DoubleSupplier maxspeedSupplier) {
        this.m_drivetrainSubsystem = drivetrainSubsystem;
        this.m_translationXSupplier = translationXSupplier;
        this.m_translationYSupplier = translationYSupplier;
        this.m_rotationSupplier = rotationSupplier;
        this.m_fieldrelativeSupplier = fieldrelativeSupplier;
        this.m_maxspeedSupplier = maxspeedSupplier;

        addRequirements(drivetrainSubsystem);
    }

    @Override
    public void execute() {
        // You can use `new ChassisSpeeds(...)` for robot-oriented movement instead of field-oriented movement
        double speedmultiplier = -m_maxspeedSupplier.getAsDouble();
        double rotatespeedmultiplier = speedmultiplier * 0.5;
        if (m_fieldrelativeSupplier.getAsBoolean()==true){
                 m_drivetrainSubsystem.drive(
                ChassisSpeeds.fromFieldRelativeSpeeds(
                            m_translationXSupplier.getAsDouble()*speedmultiplier,
                            m_translationYSupplier.getAsDouble()*speedmultiplier,
                            m_rotationSupplier.getAsDouble()* rotatespeedmultiplier,
                            m_drivetrainSubsystem.getGyroscopeRotation() 
                    )
            );    
            }
        else{
            m_drivetrainSubsystem.drive(
                        new ChassisSpeeds(
                                m_translationXSupplier.getAsDouble()*speedmultiplier,
                                m_translationYSupplier.getAsDouble()*speedmultiplier,
                                m_rotationSupplier.getAsDouble()*rotatespeedmultiplier
                        )
                );
        }
    }

    @Override
    public void end(boolean interrupted) {
        m_drivetrainSubsystem.drive(new ChassisSpeeds(0.0, 0.0, 0.0));
    }
}
