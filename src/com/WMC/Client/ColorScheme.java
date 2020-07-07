package com.WMC.Client;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	 * Serializes this {@link ColorScheme} to a file
	 * @param filename
	 */
	public void writeToFile(String filename) {

		URL url = ClientChatWindow.class.getResource(filename);
		
		try { 
            FileOutputStream file = new FileOutputStream(url.getPath()); 
            ObjectOutputStream out = new ObjectOutputStream(file); 
            
            out.writeObject(this); 
  
            out.close(); 
            file.close();
        } catch (IOException e) { 
            e.printStackTrace();
        } 
	}
	
	/**
	 * Deserializes a {@link ColorScheme} from file
	 * @param filename - path to file to be read from
	 * @return {@link ColorScheme} object read from file
	 */
	public static ColorScheme getFromFile(String filename) {

		URL url = ClientChatWindow.class.getResource(filename);
		
		try {
	        FileInputStream file = new FileInputStream(url.getPath());
	        ObjectInputStream in = new ObjectInputStream (file); 

	        ColorScheme cs = (ColorScheme) in.readObject(); 
	
	        in.close(); 
	        file.close();
	        
            System.out.println("Successfully read colorScheme from file");
	        
	        return cs;
	    }  catch (Exception e) { 
	    	e.printStackTrace();
	    	return null;
	    }
	}
}
