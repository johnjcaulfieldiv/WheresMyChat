package com.WMC.Client;

import java.awt.Color;
import java.io.Serializable;

/**
 * Model of colors for use in {@link ClientInitializeWindow}
 * 
 * Holds background color for text areas and
 * color for text in text areas
 */
public class ColorScheme implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Color backgroundColor;
	private Color foregroundColor;
	private Color textColor;
	
	/**
	 * Default constructor uses default JFrame and JTextArea colors
	 * frame: gray
	 * textArea: white
	 * text: black
	 */
	public ColorScheme() {
		backgroundColor = new Color(240,240,240);
		foregroundColor = new Color(255,255,255);
		textColor = new Color(0,0,0);
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public Color getForegroundColor() {
		return foregroundColor;
	}
	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}
	public Color getTextColor() {
		return textColor;
	}
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
}
