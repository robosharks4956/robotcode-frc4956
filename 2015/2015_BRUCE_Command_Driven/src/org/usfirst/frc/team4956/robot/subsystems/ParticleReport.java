package org.usfirst.frc.team4956.robot.subsystems;

import java.util.Comparator;

//A structure to hold measurements of a particle
public class ParticleReport implements Comparator<ParticleReport>, Comparable<ParticleReport>{
	public double PercentAreaToImageArea;
	public double Area;
	public double ConvexHullArea;
	public double BoundingRectLeft;
	public double BoundingRectTop;
	public double BoundingRectRight;
	public double BoundingRectBottom;
	
	public int compareTo(ParticleReport r)
	{
		return (int)(r.Area - this.Area);
	}
	
	public int compare(ParticleReport r1, ParticleReport r2)
	{
		return (int)(r1.Area - r2.Area);
	}
}
