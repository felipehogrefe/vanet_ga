package vanet_ga;

public class RSU {
	private int position, tTime;
	public RSU(int p, int t){
		this.position = p;
		this.tTime = t;
	}
	
	public void print(){
		System.out.println("intersection: "+ position + " - totalTime(W): " +tTime);
	}
}
