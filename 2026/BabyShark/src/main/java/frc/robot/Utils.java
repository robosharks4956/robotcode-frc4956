// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;

public final class Utils {
  /**
   * Returns a modified joystick axis for better controls.
   *
   * @param input      The unmodified joystick axis.
   * @param maxPercent The maximum distance from 0 the modified axis can be.
   * @param deadband   The distance from 0 where the unmodified axis can be
   *                   treated as 0.
   * @param smoothing  The exponent used to smooth out the axis.
   * @return The modified joystick axis.
   */
  public static double modifyAxis(
      double input, double maxPercent, double deadband, double smoothing) {
    return MathUtil.clamp(
        Math.copySign(Math.pow(Math.abs(MathUtil.applyDeadband(input, deadband)), smoothing), input)
            * maxPercent,
        -maxPercent,
        maxPercent);
  }
}
