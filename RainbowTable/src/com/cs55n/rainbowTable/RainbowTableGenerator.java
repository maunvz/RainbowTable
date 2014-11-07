package com.cs55n.rainbowTable;

import java.io.File;
import java.util.Random;

import javax.swing.SwingWorker;

/* This class will be used to generate the RainbowTable
 * It needs to take N and K as inputs, then output a
 * rainbow table using those values. It needs to avoid collisions
 * (no two chains the same) and be as "complete" as possible
 * complete means that all words can be found
 */

public class RainbowTableGenerator extends SwingWorker<Void, Integer>{
	File file;
	int passwordLength, chains, steps;
	UserInterface.GenerationDisplay display;
	UserInterface ui;
	public RainbowTableGenerator(File file,int passwordLength, int chains, int steps, UserInterface.GenerationDisplay display, UserInterface ui){
		super();
		this.file = file;
		this.passwordLength = passwordLength;
		this.chains = chains;
		this.steps = steps;
		this.ui = ui;
		this.display = display;
	}
	protected Void doInBackground() throws Exception {
		generate();
		return null;
	}
	protected void done(){
		ui.tableReady();
	}
	public void generate(){
		display.setStatus("Generating chains");
		MathOps mathops = new MathOps(passwordLength);
		RainbowTable table = new RainbowTable(chains, steps);
		Random rand = new Random();
		char[] charset = getCharset();
		//generate it here
		for(int i=0; i<chains; i++){
			byte[][] chain = new byte[2][];
			byte[] start = randomPassword(passwordLength, charset, rand);
			byte[] end = mathops.hash(start);
			for(int j=0; j<steps-1; j++){
				end = mathops.hash(mathops.reduce(end, j));
			}
			chain[0] = start;
			chain[1] = end;
			table.addChain(chain);
			display.setDone(i+1);
		}
		display.setStatus("Sorting chains");
		new RainbowTableSorter(table, display, ui).execute();
		display.setStatus("Saving chains");
		new RainbowTableSaver(file, table, display, ui).execute();;
		ui.table = table;
	}
	public static char[] getCharset(){
		char[] charset = new char[26];
		int index = 0;
		//for(int i=65; i<=90; i++)charset[index++]=(char)i;//add capital letters
		for(int i=97; i<=122; i++)charset[index++]=(char)i;//add lower case letters
		//for(int i=48; i<=57; i++)charset[index++]=(char)i;//add numbers
		return charset;
	}
	public static byte[] randomPassword(int passwordLength, char[] charset, Random rand){
		String pass="";
		for(int i=0; i<passwordLength; i++){
			pass+=charset[rand.nextInt(charset.length)];
		}
		return pass.getBytes();
	}
}
