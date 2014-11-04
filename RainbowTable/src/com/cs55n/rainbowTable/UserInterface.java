package com.cs55n.rainbowTable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/* This class is the GUI that gives the user the option 
 * to generate a table, choose N and K, and choose where
 * to save it. Once the table is being generated, it needs
 * to show the progress. It also needs an input for a hash
 * of a password, which it can search for in the loaded table
 * and output the unhashed form when it is found, as well as
 * some sort of progress display for the searching (may or
 * may not be possible to show progress for this)
 */

public class UserInterface extends JFrame{
	private static final long serialVersionUID = 1L;
	public UserInterface(){
		super();
		setTitle("Rainbow Table Tool");
		
		//Will ask for a N and K and path, then generate a table
		JButton gen_button = new JButton("Generate Table");
		
		//Will ask for path, then load a table
		JButton load_button = new JButton("Load Table");

		JPanel panel = new JPanel();
		panel.add(gen_button);
		panel.add(load_button);
		add(panel);
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public static void main(String args[]){
		new UserInterface();
	}
}

