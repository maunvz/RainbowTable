package com.cs55n.rainbowTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

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
	public int addChain(byte[][] chain){
		chains[lastIndex++]=chain;
		return lastIndex;
	}
	public String breakHash(){
		return "";
	}
	//saves 4 bytes for number of chains, then the pairs (8 bytes start, 32 end) repeating
	public void saveToFile(File file){
		if(lastIndex != chains.length){
			System.out.println("Table not fully generated");
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(ByteBuffer.allocate(4).putInt(chains.length).array());
			for(int i=0; i<chains.length; i++){
				fos.write(chains[i][0]);
				fos.write(chains[i][1]);
			}
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void loadFromFile(File file){
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] lengthBytes = new byte[4];
			fis.read(lengthBytes, 0, 4);
			int length = MathOps.bytesToInt(lengthBytes);
			chains = new byte[length][2][0];
			for(int i=0; i<length; i++){
				chains[i][0] = new byte[8];
				chains[i][1] = new byte[32];
				fis.read(chains[i][0]);
				fis.read(chains[i][1]);
			}
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
