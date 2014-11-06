package com.cs55n.rainbowTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.SwingWorker;

public class RainbowTableLoader extends SwingWorker<Void, Integer>{
	File file;
	RainbowTable table;
	UserInterface.GenerationDisplay display;
	UserInterface ui;
	public RainbowTableLoader(File file, RainbowTable table, UserInterface.GenerationDisplay display, UserInterface ui){
		this.file = file;
		this.table = table;
		this.display = display;
		this.ui = ui;
	}
	@Override
	protected Void doInBackground() throws Exception {
		loadFromFile();
		return null;
	}
	protected void done(){
		super.done();
		ui.tableReady();
	}
	public void loadFromFile(){
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] lengthBytes = new byte[4];
			fis.read(lengthBytes, 0, 4);
			int length = MathOps.bytesToInt(lengthBytes);
			display.setNK(length, 0);
			table.chains = new byte[length][2][0];
			for(int i=0; i<length; i++){
				table.chains[i][0] = new byte[8];
				table.chains[i][1] = new byte[32];
				fis.read(table.chains[i][0]);
				fis.read(table.chains[i][1]);
				display.setDone(i+1);
			}
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
