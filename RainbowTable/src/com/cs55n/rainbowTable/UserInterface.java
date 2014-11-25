package com.cs55n.rainbowTable;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
	TablePanel tablePanel;
	SearchPanel searchPanel;
	GenerationDisplay display;
	int times;
	int done;
	byte[][] starts;
	byte[][] ends;
	
	public UserInterface(){
		super();
		setTitle("Rainbow Table Tool");	
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		
		tablePanel = new TablePanel();
		panel.add(tablePanel);
		
		display = new GenerationDisplay();
		panel.add(display);
		
		searchPanel = new SearchPanel();
		searchPanel.setEnabled(false);
		panel.add(searchPanel);
		
		add(panel);
		pack();
		this.setLocation(400,400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void getTableParameters(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		JTextField n_input = new JTextField();n_input.setColumns(12);
		JLabel n_label = new JLabel("Number of chains: ");
		JTextField k_input = new JTextField();k_input.setColumns(12);
		JLabel k_label = new JLabel("Number of steps: ");
		JLabel csv_label = new JLabel("Export CSV: ");
		JCheckBox csv_input = new JCheckBox();
		
		panel.add(n_label);
		panel.add(n_input);
		panel.add(k_label);
		panel.add(k_input);
		panel.add(csv_label);
		panel.add(csv_input);
		
		int selection = JOptionPane.showConfirmDialog(null, panel, "Generate new Rainbow Table", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(selection == JOptionPane.OK_OPTION){
			try{
				final JFileChooser fc = new JFileChooser();
				fc.showSaveDialog(this);

				final int n = Integer.parseInt(n_input.getText());
				final int k = Integer.parseInt(k_input.getText());
				display.setNK(n, k);
				RainbowTableGenerator gen = new RainbowTableGenerator(fc.getSelectedFile(), 6, n, k, display, this);
				gen.export = csv_input.isSelected();
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
		table = new RainbowTable(0, 0);
		new RainbowTableLoader(fc.getSelectedFile(), table, display, this).execute();
	}
	public void tableReady(){
		tablePanel.setEnabled(false);
		searchPanel.setEnabled(true);
	}
	public void foundPass(String pass, int i){
		if(i==-1){
			JOptionPane.showMessageDialog(this, pass);
			return;
		}
		ends[i] = pass.getBytes();
		done++;
		if(done==times){
			int correct = 0;
			for(int j=0; j<times; j++)
				if(MathOps.bytesEqual(starts[j],ends[j]))
					correct++;
			System.out.println(correct+"/"+times+" were found");
		}
	}
	class GenerationDisplay extends JPanel{
		private static final long serialVersionUID = 1L;
		JLabel status_label;
		JLabel steps_label;
		JLabel chains_label;
		JLabel progress_label;
		JProgressBar progress_bar;
		int total;
		int current;
		public GenerationDisplay(){
			status_label = new JLabel("Current Task: ");
			steps_label = new JLabel("Total Steps: ");
			chains_label = new JLabel("Total Chains: ");
			progress_label = new JLabel("Finished: ");
			progress_bar = new JProgressBar();
			this.setLayout(new GridLayout(0,1));
			this.add(status_label);
			this.add(steps_label);
			this.add(chains_label);
			this.add(progress_label);
			this.add(progress_bar);
			current = 0;
		}
		public void setNK(int total, int steps){
			steps_label.setText("Total Steps: "+steps);
			chains_label.setText("Total Chains: "+total);
			progress_bar.setMaximum(total);
			this.total=total;
		}
		public void setDone(int done){
			current = done;
			progress_bar.setValue(done);
			progress_label.setText("Finished: "+done+"/"+total);
		}
		public void incrementDone(){
			setDone(current+1);
		}
		public void setStatus(String status){
			status_label.setText("Current Task: " + status);
		}
	}
	class SearchPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		JLabel hash_label;
		JTextField hash_field;
		JButton search_button;
		JButton test_button;
		public SearchPanel(){
			hash_label = new JLabel("Hash: ");
			hash_field = new JTextField();
			hash_field.setColumns(32);
			search_button = new JButton("Search");
			search_button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					new RainbowTableSearcher(hash_field.getText(), table, display, UserInterface.this, -1).execute();
				}
			});
			test_button = new JButton("Test");
			test_button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					char[] charset = RainbowTableGenerator.getCharset();
					Random rand = new Random();
					MathOps ops = new MathOps(6);
					
					times = Integer.parseInt(JOptionPane.showInputDialog("How many to test?"));
					done = 0;
					starts = new byte[times][];
					ends = new byte[times][];
					
					for(int i=0; i<times; i++){
						starts[i] = RainbowTableGenerator.randomPassword(6, charset, rand);
						byte[] hash = ops.hash(starts[i]);
						new RainbowTableSearcher(MathOps.bytesToHex(hash), table, display, UserInterface.this, i).execute();						
					}
				}
			});
			
			setLayout(new FlowLayout());
			add(hash_label);
			add(hash_field);
			add(search_button);
			add(test_button);
		}
		public void setEnabled(boolean enabled){
			super.setEnabled(enabled);
			hash_label.setEnabled(enabled);
			hash_field.setEnabled(enabled);
			search_button.setEnabled(enabled);
		}
	}
	class TablePanel extends JPanel{
		private static final long serialVersionUID = 1L;
		JButton load_button;
		JButton gen_button;
		public TablePanel(){
			//Will ask for a N and K and path, then generate a table
			gen_button = new JButton("Generate Table");
			gen_button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					getTableParameters();
				}
			});
			
			//Will ask for path, then load a table
			load_button = new JButton("Load Table");
			load_button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					openTable();
				}
			});
			add(gen_button);
			add(load_button);			
		}
		public void setEnabled(boolean enabled){
			super.setEnabled(enabled);
			gen_button.setEnabled(enabled);
			load_button.setEnabled(enabled);
		}
	}
	public static void main(String args[]){
		new UserInterface();
	}
}
