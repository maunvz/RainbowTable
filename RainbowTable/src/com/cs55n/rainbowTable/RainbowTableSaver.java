package com.cs55n.rainbowTable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.swing.SwingWorker;

public class RainbowTableSaver extends SwingWorker<Void, Integer>{

	@Override
	protected Void doInBackground() throws Exception {
		return null;
	}
	//saves 4 bytes for number of chains, then the pairs (8 bytes start, 32 end) repeating
	public static void saveToFile(File file, RainbowTable table){
		if(table.lastIndex != table.chains.length){
			System.out.println("Table not fully generated");
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(ByteBuffer.allocate(4).putInt(table.chains.length).array());
			for(int i=0; i<table.chains.length; i++){
				fos.write(table.chains[i][0]);
				fos.write(table.chains[i][1]);
			}
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
