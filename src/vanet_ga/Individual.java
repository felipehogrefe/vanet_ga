package vanet_ga;

import java.util.ArrayList;

public class Individual{
	private ArrayList<Integer> rsus;
	public Individual(){
		this.rsus = new ArrayList<Integer>();
	}
	public Integer get(int i){
		return rsus.get(i);
	}
	public void add(int i){
		this.rsus.add(i);
	}
	public int indexOf(int i){
		return rsus.indexOf(i);
	}
	public int size(){
		return rsus.size();
	}
}
