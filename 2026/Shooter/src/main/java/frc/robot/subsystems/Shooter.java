package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.XboxController;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;

public class Shooter {
    SparkMax leftLeader;
    XboxController joystick;

    public Shooter() {
        leftLeader = new SparkMax(1, MotorType.kBrushless);

        SparkMaxConfig globalConfig = new SparkMaxConfig();

        leftLeader.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        
    }

    public void set(double speed) {
        leftLeader.set(speed);
    }
}
