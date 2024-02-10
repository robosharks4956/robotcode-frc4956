// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDs extends SubsystemBase {
  AddressableLED m_led;
  boolean ledStarted = false;
  AddressableLEDBuffer m_ledBuffer;
  public int m_rainbowFirstPixelHue;
  /** Creates a new LEDs. */
  
  private ShuffleboardTab supporttab = Shuffleboard.getTab("Support");

  private GenericEntry LEDToggle = supporttab
      .add("LEDs", true)
      .withWidget(BuiltInWidgets.kToggleSwitch)
      .getEntry();

  public LEDs() {
    m_led = new AddressableLED(9);

    // Reuse buffer
    // Default to a length of 60, start empty output
    // Length is expensive to set, so only set it once, then just update data
    m_ledBuffer = new AddressableLEDBuffer(54);
    m_led.setLength(m_ledBuffer.getLength());

    // Set the data
    m_led.setData(m_ledBuffer);
    m_led.start();
    
  }
  public void setAll(int r, int g, int b) {
  for (var i = 0; i < m_ledBuffer.getLength(); i++) {
    // Sets the specified LED to the RGB values for red
    m_ledBuffer.setRGB(i, r, g, b);
   }
   m_led.setData(m_ledBuffer);

  }
  public void setRed(){
    setAll (255, 0, 0);
  }
  public void setBlue(){
    setAll (0, 0, 255);
  }
  public void rainbow() {
    // For every pixel
    for (var i = 0; i < m_ledBuffer.getLength(); i++) {
      // Calculate the hue - hue is easier for rainbows because the color
      // shape is a circle so only one value needs to precess
      final var hue = (m_rainbowFirstPixelHue + (i * 180 / m_ledBuffer.getLength())) % 180;
      // Set the value
      m_ledBuffer.setHSV(i, hue, 255, 128);
    }
    // Increase by to make the rainbow "move"
    m_rainbowFirstPixelHue += 3;
    // Check bounds
    m_rainbowFirstPixelHue %= 180;
    m_led.setData(m_ledBuffer);
  }

  public Color currentColor = Color.blue;
  public void setColor(Color targetColor){
    if (currentColor != targetColor){
      if (targetColor == Color.blue){
        setBlue();
      }
      if (targetColor == Color.red){
        setRed();
      }
    }
   currentColor = targetColor;

  }

  public enum Color {
    red,
    blue,
    rainbow
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  if (LEDToggle.getBoolean(true) == true) {
    if (!ledStarted) {
//      m_led.start();
      ledStarted = true;
    }

  }
  else {
    ledStarted = false;
    m_led.stop();
  } 
  if (currentColor == Color.rainbow){
    rainbow();
  }
}
}
