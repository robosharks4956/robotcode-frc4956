package frc.robot;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import swervelib.math.Matter;
import com.pathplanner.lib.util.PIDConstants;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final class DriveConstants {

    // We have the SDS Mk4 L2 swerve drive, for future reference.

    /**
     * The left-to-right distance between the drivetrain wheels
     *
     * Should be measured from center to center.
     */
    public static final double DRIVETRAIN_TRACKWIDTH_METERS = 0.60325;
    /**
     * The front-to-back distance between the drivetrain wheels.
     *
     * Should be measured from center to center.
     */
    public static final double DRIVETRAIN_WHEELBASE_METERS = 0.48895;

    public static final int FRONT_LEFT_MODULE_DRIVE_MOTOR = 21;
    public static final int FRONT_LEFT_MODULE_STEER_MOTOR = 22;
    public static final int FRONT_LEFT_MODULE_STEER_ENCODER = 0;
    public static final double FRONT_LEFT_MODULE_STEER_OFFSET = (0.875); // FIXME Measure and set front left steer offset

    public static final int FRONT_RIGHT_MODULE_DRIVE_MOTOR = 23;
    public static final int FRONT_RIGHT_MODULE_STEER_MOTOR = 24;
    public static final int FRONT_RIGHT_MODULE_STEER_ENCODER = 1;
    public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = (0.321); // FIXME Measure and set front right steer offset

    public static final int BACK_LEFT_MODULE_DRIVE_MOTOR = 25;
    public static final int BACK_LEFT_MODULE_STEER_MOTOR = 26;
    public static final int BACK_LEFT_MODULE_STEER_ENCODER = 2;
    public static final double BACK_LEFT_MODULE_STEER_OFFSET = (0.682); // FIXME Measure and set back left steer offset

    public static final int BACK_RIGHT_MODULE_DRIVE_MOTOR = 27;
    public static final int BACK_RIGHT_MODULE_STEER_MOTOR = 28;
    public static final int BACK_RIGHT_MODULE_STEER_ENCODER = 3;
    public static final double BACK_RIGHT_MODULE_STEER_OFFSET = (.558); // FIXME Measure and set back right steer offset
  }

  public static final int DRIVE_CONTROLLER_PORT = 0;
  public static final int SUPPORT_CONTROLLER_PORT = 1;

  public static final class NoteMechanismIDs {
    public static final int INTAKE_MOTOR = 12;
    public static final int SHOOTING_MOTOR = 15;
    public static final int SHOOTER_CORNER_MOTOR = 17;
    public static final int COLOR_SENSOR = 9;
  }

  public static final class ClimberIDsAndPorts {
    public static final int CLIMBER_LEFT_MOTOR = 55;
    public static final int CLIMBER_RIGHT_MOTOR = 56;
    public static final int LATCH_RIGHT_SERVO = 6;
    public static final int LATCH_LEFT_SERVO = 5;
  }

  public static class AprilTagIDs {
    public static final int BLUE_SPEAKER = 7; // Or maybe 8
    public static final int RED_SPEAKER = 3; // Or maybe 4
    public static final int BLUE_AMP = 6;
    public static final int RED_AMP = 5;
  }

  public static final int LED_PORT = 8;

    public static final double ROBOT_MASS = (148 - 20.3) * 0.453592; // 32lbs * kg per pound
  public static final Matter CHASSIS    = new Matter(new Translation3d(0, 0, Units.inchesToMeters(8)), ROBOT_MASS);
  public static final double LOOP_TIME  = 0.13; //s, 20ms + 110ms sprk max velocity lag
  public static final double MAX_SPEED  = Units.feetToMeters(14.5);
      // Maximum speed of the robot in meters per second, used to limit acceleration.

  public static final class AutonConstants
  {

    public static final PIDConstants TRANSLATION_PID = new PIDConstants(0.7, 0, 0);
    public static final PIDConstants ANGLE_PID       = new PIDConstants(0.4, 0, 0.01);
  }

  public static class OperatorConstants
  {

    // Joystick Deadband
    public static final double LEFT_X_DEADBAND  = 0.1;
    public static final double LEFT_Y_DEADBAND  = 0.1;
    public static final double RIGHT_X_DEADBAND = 0.1;
    public static final double TURN_CONSTANT    = 6;
  }
}
