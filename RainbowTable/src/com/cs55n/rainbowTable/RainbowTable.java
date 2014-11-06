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
	public RainbowTable(int n){
		chains = new byte[n][2][0];
		lastIndex=0;
	}
	public void addChain(byte[][] chain){
		chains[lastIndex++]=chain;
	}
	public String breakHash(){
		return "";
	}
}
