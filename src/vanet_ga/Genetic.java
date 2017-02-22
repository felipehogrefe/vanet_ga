package vanet_ga;

import java.util.ArrayList;
import java.util.Random;

public class Genetic {
	private static final int seed = 10;
	
	private int numberOfGenerations, populationsSize, numberOfRSUs, iTime;
	private Double crossoverProb, mutationProb;
	private ArrayList<ArrayList<Integer>> population;
	private ArrayList<ArrayList<Integer>> matrix;

	public Genetic(int ng, int ps, int nor, int it, Double cp, Double mp, ArrayList<ArrayList<Integer>> m){
		this.numberOfGenerations = ng;
		this.populationsSize = ps;
		this.numberOfRSUs = nor;
		this.iTime = it;
		this.crossoverProb = cp;
		this.mutationProb = mp;
		this.matrix = m;
	}
	
	public void evolve(){
		Random randomGenerator = new Random(seed);
		population = new ArrayList<ArrayList<Integer>>();
		int greedyPopulation = populationsSize/2;
		int randPopulation = populationsSize - greedyPopulation;
		int numberOfIntersections = matrix.size();
		int numberOfVehicles = matrix.get(0).size();
		
		for(int i = 0; i<randPopulation;i++){
			population.add(generateRandIndividual(numberOfIntersections,randomGenerator));
		}

		Greedy greedy = new Greedy(matrix,numberOfRSUs,iTime,true);
		for(int i = 0; i<randPopulation;i++){
			population.add(greedy.generateGreedyIndividual());
		}
		
	}
	
	public ArrayList<Integer> generateRandIndividual(int noi, Random rg){
		Random randomGenerator = rg;
		ArrayList<Integer> individual = new ArrayList<Integer>();
		for(int i = 0; i<numberOfRSUs; i++){	
			individual.add(insert(randomGenerator, individual, noi));			
		}
		
		return individual;
	}
	
	public Integer insert(Random rg, ArrayList<Integer> ind, int noi){
		int j = rg.nextInt(noi);
		for(Integer i: ind){
			if (i == j) return insert(rg, ind, noi);
		}
		return j;
	}
}
