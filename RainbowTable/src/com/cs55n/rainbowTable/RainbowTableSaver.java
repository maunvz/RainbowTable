package com.cs55n.rainbowTable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.swing.SwingWorker;

public class RainbowTableSaver extends SwingWorker<Void, Integer>{
	File file;
	RainbowTable table;
	UserInterface.GenerationDisplay display;
	UserInterface ui;
	public RainbowTableSaver(File file, RainbowTable table, UserInterface.GenerationDisplay display, UserInterface ui){
		super();
		this.ui = ui;
		this.display = display;
		this.table = table;
		this.file = file;
	}
	@Override
	protected Void doInBackground() throws Exception {
		saveToFile();
		return null;
	}
	//saves 4 bytes for number of chains, then the pairs (6 bytes start, 32 end) repeating
	public void saveToFile(){
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(ByteBuffer.allocate(4).putInt(table.chains.length).array());
			fos.write(ByteBuffer.allocate(4).putInt(table.steps).array());
			for(int i=0; i<table.chains.length; i++){
				fos.write(table.chains[i][0]);
				fos.write(table.chains[i][1]);
				display.setDone(i+1);
			}
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
