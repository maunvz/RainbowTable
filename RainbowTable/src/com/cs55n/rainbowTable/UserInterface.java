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
import javax.swing.JProgressBar;
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
	JPanel mainPanel;
	public UserInterface(){
		super();
		mainPanel = new JPanel();
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
		
		mainPanel.add(gen_button);
		mainPanel.add(load_button);
		add(mainPanel);
		pack();
		this.setLocation(400,400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void getTableParameters(){
		final JFileChooser fc = new JFileChooser();
		fc.showSaveDialog(this);

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		JTextField n_input = new JTextField();n_input.setColumns(12);
		JLabel n_label = new JLabel("Number of chains: ");
		JTextField k_input = new JTextField();k_input.setColumns(12);
		JLabel k_label = new JLabel("Number of steps: ");
		panel.add(n_label);
		panel.add(n_input);
		panel.add(k_label);
		panel.add(k_input);
		int selection = JOptionPane.showConfirmDialog(null, panel, "Generate new Rainbow Table", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(selection == JOptionPane.OK_OPTION){
			try{
				final int n = Integer.parseInt(n_input.getText());
				final int k = Integer.parseInt(k_input.getText());
				final GenerationDisplay display = new GenerationDisplay(n, k);
				mainPanel.add(display);
				pack();
				RainbowTableGenerator gen = new RainbowTableGenerator(fc.getSelectedFile(), 8, n, k, display, this);
				gen.execute();
			} catch (NumberFormatException e){
				JOptionPane.showConfirmDialog(null, "Please input numbers only", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
	public void openTable(){
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(this);
		fc.getSelectedFile();
		table = new RainbowTable(0);
		table.loadFromFile(fc.getSelectedFile());
	}
	public static void main(String args[]){
		new UserInterface();
	}
	class GenerationDisplay extends JPanel{
		private static final long serialVersionUID = 1L;
		JLabel steps_label;
		JLabel chains_label;
		JLabel progress_label;
		JProgressBar progress_bar;
		int total;
		public GenerationDisplay(int total, int steps){
			steps_label = new JLabel("Total Steps: " + steps);
			chains_label = new JLabel("Total Chains: " + total);
			progress_label = new JLabel("Finished: ");
			progress_bar = new JProgressBar();
			progress_bar.setMaximum(total);
			this.total=total;
			
			this.setLayout(new GridLayout(0,1));
			this.add(steps_label);
			this.add(chains_label);
			this.add(progress_label);
			this.add(progress_bar);
		}
		public void setDone(int done){
			progress_bar.setValue(done);
			progress_label.setText("Finished: "+done+"/"+total);
		}
	}
}

