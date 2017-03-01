package vanet_ga;

import java.util.ArrayList;
import java.util.Random;

public class Individual{
	private ArrayList<Integer> rsus;
	private static final boolean randMutation = true;
	public Individual(){
		this.rsus = new ArrayList<Integer>();
	}
	
	public void mutate(double probability, Random rg, int nIntersections){
		if(randMutation){
			mutateRandom(probability,rg,nIntersections);
		}else{
			mutateWorstGen(probability,rg,nIntersections);
		}
		
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
	
	public void mutateWorstGen(double probability, Random rg, int nIntersections ){
		
	}
	
	/**
	 * generates given a point we get genes from one indivual
	 *  before that point and from the other after that point
	 * @param ind2
	 * @param crossPoint
	 * @return
	 */
	public Individual generateChildOPC(Individual ind2, int crossPoint){
		//TODO correct insertion of same gene
		Individual child = new Individual();
		int rsusSize = this.rsus.size();
		for(int i = 0;i<rsusSize;i++){
			if(i<=crossPoint){
				//before the crosspoint we add the genes from individual 1 
				int newGen = this.get(i);
				if(child.contains(newGen)){
					newGen = ind2.get(i);
				}
				child.add(newGen);
			}else{
				//after the crosspoint we add the genes from individual 2 
				int newGen = ind2.get(i);
				if(child.contains(newGen)){
					newGen = this.get(i);
				}
				child.add(newGen);
			}
		}
		return child;
	}
	
	public Individual generateChildFO(Individual ind2, int crossPoint){
		return null;
	}
	
	public Individual generateChildBTM(Individual ind2, int crossPoint){
		return null;
	}
	
	public int evaluateRsu(ArrayList<ArrayList<Integer>> matrix, int rsu){
		int nVehicles = matrix.get(0).size();
		int sum = 0;
		for(int i=0;i<nVehicles;i++){
			if(matrix.get(rsu).get(i)!=0) sum++;
		}
		return sum;
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
		System.out.println("better");
		System.out.println(this.calcFitness(numberOfVehicles, numberOfRSUs, matrix, iTime) + " - "+this);

		System.out.println(ind2.calcFitness(numberOfVehicles, numberOfRSUs, matrix, iTime)+ " - "+ind2);
		
		System.out.println((this.calcFitness(numberOfVehicles, numberOfRSUs, matrix, iTime))>ind2.calcFitness(numberOfVehicles, numberOfRSUs, matrix, iTime)?this:ind2);
		
		return (this.calcFitness(numberOfVehicles, numberOfRSUs, matrix, iTime))>ind2.calcFitness(numberOfVehicles, numberOfRSUs, matrix, iTime)?this:ind2;
	}
	
	public boolean contains(int i){
		return rsus.indexOf(i)>0;
	}
	
	public void print(){
		System.out.print("[");
		for(Integer i : rsus){
			System.out.print(i+" ");
		}
		System.out.println("]");
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
