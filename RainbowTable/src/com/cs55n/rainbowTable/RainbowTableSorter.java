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
		mergeSort(table.chains);
		return null;
	}
	protected void done(){
		super.done();
	}
	
	public void mergeSort(byte[][][] array){
		byte[][][] tmp = new byte[array.length][][];
		mergeSort(array, tmp,  0,  array.length - 1);
	}
	private void mergeSort(byte[][][] array, byte[][][] tmp, int left, int right){
		if( left < right ){
			int center = (left + right) / 2;
			mergeSort(array, tmp, left, center);
			mergeSort(array, tmp, center + 1, right);
			merge(array, tmp, left, center + 1, right);
		}
		display.incrementDone();
	}
    private void merge(byte[][][] array, byte[][][] tmp, int left, int right, int rightEnd){
        int leftEnd = right - 1;
        int k = left;
        int num = rightEnd - left + 1;

        while(left <= leftEnd && right <= rightEnd)
            if(MathOps.compareBytes(array[left][1], array[right][1]) <= 0)
                tmp[k++] = array[left++];
            else
                tmp[k++] = array[right++];
        while(left <= leftEnd)    // Copy rest of first half
            tmp[k++] = array[left++];
        while(right <= rightEnd)  // Copy rest of right half
            tmp[k++] = array[right++];
        // Copy tmp back
        for(int i = 0; i < num; i++, rightEnd--)
            array[rightEnd] = tmp[rightEnd];
    }
}
