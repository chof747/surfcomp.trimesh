package org.chof.surfcomp.trimesh.exception;

public class TrianglePointMissing extends TrimeshException {
	
	private static final long serialVersionUID = -8106612172893157305L;
	
	protected boolean aMissing;
	protected boolean bMissing;
	protected boolean cMissing;

	public TrianglePointMissing(String msg, boolean a, boolean b, boolean c) {
		super(msg);
		aMissing = a;
		bMissing = b;
		cMissing = c;
	}

	public boolean isAMissing() {
		return aMissing;
	}

	public boolean isBMissing() {
		return bMissing;
	}

	public boolean isCMissing() {
		return cMissing;
	}
	
	
	
}
