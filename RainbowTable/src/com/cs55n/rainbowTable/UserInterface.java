package com.cs55n.rainbowTable;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	RainbowTable table;
	
	public UserInterface(){
		super();
		setTitle("Rainbow Table Tool");
		
		//Will ask for a N and K and path, then generate a table
		JButton gen_button = new JButton("Generate Table");
		gen_button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				getTableParameters();
			}
		});
		
		//Will ask for path, then load a table
		JButton load_button = new JButton("Load Table");
		load_button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				openTable();
			}
		});
		
		JPanel panel = new JPanel();
		panel.add(gen_button);
		panel.add(load_button);
		add(panel);
		pack();
		this.setLocation(400,400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void getTableParameters(){
		JFileChooser fc = new JFileChooser();
		fc.showSaveDialog(this);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		JTextField n_input = new JTextField();n_input.setColumns(12);
		JLabel n_label = new JLabel("Number of chains: ");
		JTextField k_input = new JTextField();k_input.setColumns(12);
		JLabel k_label = new JLabel("Number of steps: ");
		JTextField path_input = new JTextField();n_input.setColumns(12);
		JLabel path_label = new JLabel("Path: ");
		panel.add(n_label);
		panel.add(n_input);
		panel.add(k_label);
		panel.add(k_input);
		panel.add(path_label);
		panel.add(path_input);
		int selection = JOptionPane.showConfirmDialog(null, panel, "Generate new Rainbow Table", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(selection == JOptionPane.OK_OPTION){
			try{
				int n = Integer.parseInt(n_input.getText());
				int k = Integer.parseInt(k_input.getText());
				table = new RainbowTableGenerator(8,n,k).generate(fc.getSelectedFile());
			} catch (NumberFormatException e){
				JOptionPane.showConfirmDialog(null, "Please input numbers only", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
	public void openTable(){
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(this);
		fc.getSelectedFile();
		table = new RainbowTable();
		table.loadFromFile(fc.getSelectedFile());
	}
	public static void main(String args[]){
		new UserInterface();
	}
}

