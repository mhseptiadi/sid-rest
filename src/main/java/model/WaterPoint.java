package model;

public class WaterPoint {
	String total;
	String percentWorking;
	String numberWorking;
	public String getTotal() {
		return total;
	}
	public String getPercentWorking() {
		return percentWorking;
	}
	public String getNumberWorking() {
		return numberWorking;
	}
	public String getNumberNotWorking() {
		return numberNotWorking;
	}
	String numberNotWorking;
	public void setTotal(String total) {
		this.total = total;
	}
	public void setPercentWorking(String percentWorking) {
		this.percentWorking = percentWorking;
	}
	public void setNumberWorking(String numberWorking) {
		this.numberWorking = numberWorking;
	}
	public void setNumberNotWorking(String numberNotWorking) {
		this.numberNotWorking = numberNotWorking;
	}
}
