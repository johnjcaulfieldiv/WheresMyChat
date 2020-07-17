package com.WMC.Client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

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
	
	/**
	 * writes this {@link ColorScheme} to a file
	 * @param filename
	 */
	public void writeToFile(String filename) {

		try {
			File f = new File(filename);
			f.getParentFile().mkdirs();
			f.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
			
            bw.write("fg:" + foregroundColor.getRGB());
            bw.newLine();
            bw.write("bg:" + backgroundColor.getRGB());
            bw.newLine();
            bw.write("text:" + textColor.getRGB());
        } catch (Exception e) { 
            e.printStackTrace();
        } 
	}
	
	/**
	 * reads a {@link ColorScheme} from file
	 * @param filename - path to file to be read from
	 * @return {@link ColorScheme} object read from file
	 */
	public static ColorScheme getFromFile(String filename) {
		
		ColorScheme cs = new ColorScheme();
		
		try {
			File f = new File(filename);
			f.getParentFile().mkdirs();
			f.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}			
				
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line = br.readLine();			
			while (line != null) {
				String [] s = line.split(":");
				Color newColor = new Color(Integer.parseInt(s[1]));
				
				if (s[0].equals("fg"))
					cs.foregroundColor = newColor;
				else if (s[0].equals("bg"))
					cs.backgroundColor = newColor;
				else if (s[0].equals("text"))
					cs.textColor = newColor;
				
				line = br.readLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return cs;
	}
}
