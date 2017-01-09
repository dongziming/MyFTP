package model;

import java.util.Date;

public class MyS3Object {
	private String fileName;
	private double size;
	private String modifierTime;

	public MyS3Object(String fileName, double size, String modifierTime) {
		super();
		this.fileName = fileName;
		this.size = size;
		this.modifierTime = modifierTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public String getModifierTime() {
		return modifierTime;
	}

	public void setModifierTime(String modifierTime) {
		this.modifierTime = modifierTime;
	}
}
