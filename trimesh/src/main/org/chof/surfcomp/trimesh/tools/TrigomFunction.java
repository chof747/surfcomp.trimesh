package org.chof.surfcomp.trimesh.tools;

/**
 * Enumeration of trigonometric functions
 * <p>
 * Contains representation of trigonometric functions with simple calculation</p>
 * @author chof
 *
 */
public enum TrigomFunction {
	SIN(),
	COS(),
	TAN(),
	COTAN();
	
	public double calculateFromCos(double cos) {
		switch (this) {
		case SIN:
			return Math.sqrt(1-cos*cos);
			
		case COTAN:
			return cos/Math.sqrt(1-cos*cos);
			
		case TAN:
			return Math.sqrt(1-cos*cos)/cos;

		default:
			return cos;
		}
	}
}
