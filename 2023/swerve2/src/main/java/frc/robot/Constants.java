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
  /**
     * The left-to-right distance between the drivetrain wheels
     *
     * Should be measured from center to center.
     */
    public static final double DRIVETRAIN_TRACKWIDTH_METERS = 0.50165;
    /**
     * The front-to-back distance between the drivetrain wheels.
     *
     * Should be measured from center to center.
     */
    public static final double DRIVETRAIN_WHEELBASE_METERS = 0.6223;

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

    // public static final int BASE_SOLENOID_FORWARD = 3;
    // public static final int BASE_SOLENOID_REVERSE = 2;
    // public static final int GRABBER_SOLENOID_FORWARD = 1;
    // public static final int GRABBER_SOLENOID_REVERSE = 0;
  
    public static class OperatorConstants {
      public static final int kDriverControllerPort = 0;
      public static final int kSupportControllerPort = 1;
    }
}
