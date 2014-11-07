package com.cs55n.rainbowTable;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class RainbowTableSearcher extends SwingWorker<Void, Integer>{
	RainbowTable table;
	UserInterface.GenerationDisplay display;
	UserInterface ui;
	MathOps mathops;
	String hash;
	public RainbowTableSearcher(String hash,RainbowTable table, UserInterface.GenerationDisplay display, UserInterface ui){
		this.ui = ui;
		this.hash = hash;
		this.display = display;
		this.table = table;
		mathops = new MathOps(6);
	}
	@Override
	protected Void doInBackground() throws Exception {
		display.setNK(table.steps, table.steps);
		String pass = breakHash(hash);
		JOptionPane.showMessageDialog(ui, pass);
		return null;
	}
	public String breakHash(String hash){
		display.setStatus("Searching....");
		byte[] hashBytes = MathOps.hexToBytes(hash);
		for(int i=0; i<table.steps; i++){
			System.out.println(MathOps.bytesToHex(hashBytes));
			int index = searchB(hashBytes);
			if(index!=-1){
				return "Found at index: "+index+" chain number "+i;
			}
			hashBytes = mathops.hash(mathops.reduce(hashBytes, i));
			display.setDone(i+1);
		}
		return "Not Found";
	}
	//return -1 if not found, or index of chain if found
	public int searchA(byte[] key){
		for(int i=0; i<table.chains.length; i++){
			if(MathOps.bytesEqual(key, table.chains[i][1]))return i;
		}
		return -1;
	}
	
	public int searchB(byte[] key) {
        return searchB(key, 0, table.chains.length);
    }
    public int searchB(byte[] key, int lo, int hi) {
        // possible key indices in [lo, hi)
        if (hi <= lo) return -1;
        int mid = lo + (hi - lo) / 2;
        int cmp = MathOps.compareBytes(table.chains[mid][1], key);
        if      (cmp > 0) return searchB(key, lo, mid);
        else if (cmp < 0) return searchB(key, mid+1, hi);
        else              return mid;
    }
}
