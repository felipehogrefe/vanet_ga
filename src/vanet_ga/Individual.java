package vanet_ga;

import java.util.ArrayList;
import java.util.Random;

public class Individual{
	private ArrayList<Integer> rsus;
	private static final boolean randMutation = true;
	public Individual(){
		this.rsus = new ArrayList<Integer>();
	}
	public void mutateRandom(double probability, Random rg, int nIntersections){
		int nRsus = rsus.size();
		if(rg.nextDouble()>probability){
			int mutatedGen = rg.nextInt(nRsus);
			int newGen = rg.nextInt(nIntersections);
			while(contains(newGen)){
				newGen = rg.nextInt(nIntersections);
			}
			set(mutatedGen,newGen);
		}
	}
	
	public void mutate(double probability, Random rg, int nIntersections){
		if(randMutation){
			mutateRandom(probability,rg,nIntersections);
		}else{
			mutateWorstGen(probability,rg,nIntersections);
		}
		
	}
	
	public void mutateWorstGen(double probability, Random rg, int nIntersections ){
		
	}
	
	public Double calcFitness(int numberOfVehicles, int numberOfRSUs, ArrayList<ArrayList<Integer>> matrix, int iTime) {
		int coverage = 0;
		for (int j = 0; j < numberOfVehicles; j++) {
			int sum = 0;
			for (int i = 0; i < numberOfRSUs; i++) {
				sum += matrix.get(this.get(i)).get(j);
			}
			if (sum >= iTime)
				coverage++;
		}

		return (double) coverage*100 / numberOfVehicles;
	}
	
	public Individual getBetter(Individual ind2, int numberOfVehicles, int numberOfRSUs, ArrayList<ArrayList<Integer>> matrix, int iTime){
		return (this.calcFitness(numberOfVehicles, numberOfRSUs, matrix, iTime))>ind2.calcFitness(numberOfVehicles, numberOfRSUs, matrix, iTime)?this:ind2;
	}
	
	public boolean contains(int i){
		return rsus.indexOf(i)>0;
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
	public void set(int index, int element){
		rsus.set(index, element);
	}
}
