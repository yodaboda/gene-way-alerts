package com.geneway.alerts.message;



public class GeneWayEmailAlertMessage extends AbstractAlertMessage {
	
	@Override
	public String getSubject() {
		return "itsTimeToTakeYourMealTitle";
	}

	@Override
	public String getBody() {
		return "itsTimeToTakeYourMeal";
	}


}
