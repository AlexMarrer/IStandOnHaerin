package Pruefung2.Logic;

import java.util.ArrayList;

import Pruefung2.Data;

public class VerifyLogic implements IVerifyLogic {
	
	@Override
	public Boolean verifyTitle(ArrayList<Data> solrData) {
		
		for (Data data : solrData) {
			String[] textArray = data.getText().split("\\s+", 6);
			String[] titleArray = data.getTitle().split("\\s+", 5);
			titleArray[titleArray.length-1] = titleArray[titleArray.length-1].replaceAll("\\]$", "").trim();
			for(int i = 0; i < titleArray.length; i++) {
				if(!titleArray[i].equals(textArray[i])) {
					return false;
				}
			}
		}
		return true;
	}
}
