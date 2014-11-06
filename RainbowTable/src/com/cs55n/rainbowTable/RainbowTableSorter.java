package com.cs55n.rainbowTable;

import javax.swing.SwingWorker;

public class RainbowTableSorter extends SwingWorker<Void, Integer>{
	RainbowTable table;
	UserInterface.GenerationDisplay display;
	UserInterface ui;

	public RainbowTableSorter(RainbowTable table, UserInterface.GenerationDisplay display, UserInterface ui){
		super();
		this.table = table;
		this.display = display;
		this.ui = ui;
	}
	@Override
	protected Void doInBackground() throws Exception {
		return null;
	}
	protected void done(){
		super.done();
	}
}
