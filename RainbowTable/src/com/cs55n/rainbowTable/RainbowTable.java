package com.cs55n.rainbowTable;


/* This class needs a data structure to hold all the
 * start/end points for the generated chains, a searching
 * function, and a way to save to file and load from file
 */

public class RainbowTable {
	//store pairs - start(plain text) and end(hash)
	//one pair is byte[][] where byte[0][] is the start and byte[1][] is the end
	//2^28 would be a good size according to the article
	byte[][][] chains;
	//last index that is created, only used when creating a table
	int lastIndex;
	int steps;
	int index;
	boolean ready;
	public RainbowTable(int n, int steps, int index){
		chains = new byte[n][][];
		lastIndex=0;
		this.steps = steps;
		this.index=index;
		ready=false;
	}
	public void addChain(byte[][] chain){
		chains[lastIndex++]=chain;
	}
}
