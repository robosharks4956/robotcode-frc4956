// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int DRIVE_CONTROLLER_PORT = 0;
    public static final int SUPPORT_CONTROLLER_PORT = 1;
  }

  public static class SwerveConstants {
    public static final String SWERVE_DIRECTORY = "swerve";
    public static final double MAX_SPEED = 10;
  }

  public static class LiftConstants {
    public static final int MOTOR_CONTROLLER_ID = 10;
  }

  public static class CoralManipulatorConstants {
    public static final int ANGLE_MOTOR_CONTROLLER_ID = 14;
    public static final int LATCH_ACTUATOR_PWM_CHANNEL = 6;
  }

  public static class TailFinConstants {
    public static final int MOTOR_CONTROLLER_ID = 12;
  }
}
