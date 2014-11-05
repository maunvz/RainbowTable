package com.cs55n.rainbowTable;

import java.io.File;

/* This class will be used to generate the RainbowTable
 * It needs to take N and K as inputs, then output a
 * rainbow table using those values. It needs to avoid collisions
 * (no two chains the same) and be as "complete" as possible
 * complete means that all words can be found
 */

public class RainbowTableGenerator {
	private int passwordLength;
	private int steps;
	private int chains;
	private MathOps mathops;
	public RainbowTableGenerator(int passwordLength, int chains, int steps){
		this.steps = steps;
		this.chains = chains;
		this.passwordLength = passwordLength;
		mathops = new MathOps(passwordLength);
	}
	//generates a rainbowTable at file
	public RainbowTable generate(File file){
		RainbowTable table = new RainbowTable();
		//generate it here
		
		table.saveToFile(file);
		return table;
	}
}
