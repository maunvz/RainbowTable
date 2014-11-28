package com.cs55n.rainbowTable;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
	ArrayList<RainbowTable> tables;
	ArrayList<GenerationDisplay> displays;
	JPanel displayPanel;
	
	TablePanel tablePanel;
	SearchPanel searchPanel;
	int times;
	int readyTables;
	ArrayList<Integer> done;
	ArrayList<Integer> correct;
	byte[][] starts;
	byte[][][] ends;
	boolean[] testCorrect;
	
	public UserInterface(){
		super();
		setTitle("Rainbow Table Tool");	
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		readyTables=0;
		done = new ArrayList<Integer>();
		correct = new ArrayList<Integer>();
		
		tablePanel = new TablePanel();
		panel.add(tablePanel);
		tables = new ArrayList<RainbowTable>();
		displays = new ArrayList<GenerationDisplay>();
		displayPanel = new JPanel();
		panel.add(displayPanel);
		
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
				
				GenerationDisplay display = new GenerationDisplay();
				displays.add(display);
				displayPanel.add(display);
				display.setNK(n, k);
				pack();

				RainbowTableGenerator gen = new RainbowTableGenerator(fc.getSelectedFile(), 6, n, k, display, this, tables.size());
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
		RainbowTable table = new RainbowTable(0, 0, tables.size());
		tables.add(table);
		
		GenerationDisplay display = new GenerationDisplay();
		displays.add(display);
		displayPanel.add(display);		
		pack();

		new RainbowTableLoader(fc.getSelectedFile(), table, display, this).execute();
	}
	public void tableReady(){
		searchPanel.setEnabled(true);
		readyTables++;
	}
	public void foundPass(String pass, int i, int index){
		if(i==-1){
			ends[index][0] = pass.getBytes();
			done.set(0, done.get(0)+1);
			if(done.get(0)==readyTables){
				String str = "Not Found";
				for(int j=0; j<tables.size(); j++){
					if(ends[j][0]==null)continue;
					if(ends[j][0].length!=9)
						str=new String(ends[j][0]);					
				}
				JOptionPane.showMessageDialog(this, str);				
			}
			return;
		}
		ends[index][i] = pass.getBytes();
		done.set(index, done.get(index)+1);
		displays.get(index).progress_bar.setMaximum(times);
		displays.get(index).setDone(done.get(index));
		if(MathOps.bytesEqual(starts[i],ends[index][i])){
			correct.set(index, correct.get(index)+1);
			testCorrect[i]=true;
		}
		
		int totalCorrect = 0;
		for(int j=0; j<times; j++)if(testCorrect[j])totalCorrect++;
		displays.get(index).status_label.setText(correct.get(index)+"/"+times+" were found, total "+totalCorrect+"/"+times);
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
					done.clear();
					done.add(0);
					ends = new byte[tables.size()][1][];
					for(int i=0; i<tables.size(); i++)
						new RainbowTableSearcher(hash_field.getText(), tables.get(i), displays.get(i), UserInterface.this, -1).execute();
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
					done.clear();correct.clear();
					for(int i=0; i<tables.size(); i++){
						done.add(0);correct.add(0);
					}
					starts = new byte[times][];
					ends = new byte[tables.size()][times][];
					testCorrect = new boolean[times];
					for(int index=0; index<tables.size(); index++){
						for(int i=0; i<times; i++){
							starts[i] = RainbowTableGenerator.randomPassword(6, charset, rand);
							byte[] hash = ops.hash(starts[i]);
							for(int j=0; j<tables.size(); j++)
								new RainbowTableSearcher(MathOps.bytesToHex(hash), tables.get(j), displays.get(j), UserInterface.this, i).execute();
						}						
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
		/*
		MathOps mathops = new MathOps(6);
		byte[] hash = mathops.hash("vctqiu".getBytes());
		for(int i=0; i<10; i++){
			byte[] reduced = mathops.reduce(hash, i);
			System.out.println(new String(reduced));			
		}
		*/
	}
}
