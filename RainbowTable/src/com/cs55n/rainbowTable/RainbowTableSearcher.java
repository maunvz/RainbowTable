package com.cs55n.rainbowTable;

import javax.swing.SwingWorker;

public class RainbowTableSearcher extends SwingWorker<Void, Integer>{
	RainbowTable table;
	UserInterface.GenerationDisplay display;
	UserInterface ui;
	MathOps mathops;
	String hash;
	int i;
	public RainbowTableSearcher(String hash,RainbowTable table, UserInterface.GenerationDisplay display, UserInterface ui, int i){
		this.ui = ui;
		this.hash = hash;
		this.display = display;
		this.table = table;
		mathops = new MathOps(6);
		this.i=i;
	}
	@Override
	protected Void doInBackground() throws Exception {
		if(i==-1)display.setNK(table.chains.length, table.steps);
		String pass = breakHash(hash);
		ui.foundPass(pass, i);
		return null;
	}
	public String breakHash(String hash){
		if(i==-1)display.setStatus("Searching....");
		byte[] hashBytes = MathOps.hexToBytes(hash);
		
		int modifications = 0;	
		while (modifications < table.steps) {
			int index = searchB(hashBytes);
			if (index != -1) {
				return getPassword(index, modifications);
			}				
			hashBytes = mathops.hash(mathops.reduce(hashBytes));
			modifications++;
			if(i==-1)display.setDone(modifications);
		}
		return "Not Found";
	}
	public String getPassword(int index, int modNo){
		byte[] matchedChain = table.chains[index][0];
		for (int j = 1; j < table.steps-modNo; j++) {
			matchedChain = mathops.hash(matchedChain);
			matchedChain = mathops.reduce(matchedChain);
			//System.out.println(new String(matchedChain));
		}
		return new String(matchedChain);
	}
	//return -1 if not found, or index of chain if found
	public int searchA(byte[] key){
		for(int i=0; i<table.chains.length; i++){
			if(MathOps.bytesEqual(key, table.chains[i][1]))return i;
		}
		return -1;
	}
	
	public String toString(byte[] array) {
		String result = "";
		for (int i = 0; i < array.length; i++) {
			result += (char)array[i];
		}
		return result;
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
