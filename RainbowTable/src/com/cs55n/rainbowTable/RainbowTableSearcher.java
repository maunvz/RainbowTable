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
		if(!table.ready)return null;
		if(i==-1)display.setNK(table.chains.length, table.steps);
		String pass = "Not Found";
		try{
		pass = breakHash(hash);
		} catch (Exception e){
			e.printStackTrace();
		}
		ui.foundPass(pass, i, table.index);
		return null;
	}
	public String breakHash(String hash) throws Exception{
		if(i==-1)display.setStatus("Searching....");
		byte[] hashBytes = MathOps.hexToBytes(hash);
		int modifications = table.steps-1;	
		
		while (modifications > 0) {
			byte[] hashStep=MathOps.copyBytes(hashBytes);
			byte[] reduceStep = null;
			
			for(int step = modifications; step < table.steps-1; step++){
				reduceStep = mathops.reduce(hashStep, step);
				hashStep = mathops.hash(reduceStep);
			}
			int index = searchB(hashStep);
			if (index != -1) {
				String str = getPassword(index, modifications, hashBytes);
				if(!str.equals("Not Found"))return str;
			}
			modifications--;
			if(i==-1)display.setDone(modifications);
		}
		return "Not Found";
	}
	public String getPassword(int index, int modNo, byte[] hashBytes){
		byte[] matchedChain = table.chains[index][0];
		for (int j = 0; j < table.steps; j++) {
			matchedChain = mathops.hash(matchedChain);
			matchedChain = mathops.reduce(matchedChain, j);
			if(MathOps.bytesEqual(mathops.hash(matchedChain), hashBytes)){
				return new String(matchedChain);
			}
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
