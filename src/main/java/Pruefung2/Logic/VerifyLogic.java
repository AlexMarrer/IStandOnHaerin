package Pruefung2.Logic;

import java.util.ArrayList;

import Pruefung2.Data;

public class VerifyLogic implements IVerifyLogic {
	
	@Override
	public Boolean verifyTitle(ArrayList<Data> solrData) {
		
		for (Data data : solrData) {
			String[] textArray = data.getText().split("\\s+", 6);
			String[] titleArray = data.getTitle().split("\\s+", 6);
			titleArray[titleArray.length-1] = titleArray[titleArray.length-1].replaceAll("\\]$", "").trim();
			for(int i = 0; i < titleArray.length -1; i++) {
				 textArray[i] = textArray[i].replace(",", "").replace(".", "");
				 titleArray[i] = titleArray[i].replace(",", "").replace(".", "");
				if(!titleArray[i].equals(textArray[i])) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean verifyAmountOfData(ArrayList<Data> specificAmountOfData, int specificAmount) {
		if(specificAmountOfData.size() != specificAmount)
			return false;		
		return true;
	}
}
