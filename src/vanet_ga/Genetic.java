package vanet_ga;

import java.util.ArrayList;
import java.util.Random;

public class Genetic {
	private static final int seed = 10;
	
	private int numberOfGenerations, populationsSize, numberOfRSUs;
	private Double crossoverProb, mutationProb;
	private ArrayList<Integer[]> population;
	private ArrayList<Integer[]> matrix;

	public Genetic(int ng, int ps, int nor, Double cp, Double mp, ArrayList<Integer[]> m){
		this.numberOfGenerations = ng;
		this.populationsSize = ps;
		this.numberOfRSUs = nor;
		this.crossoverProb = cp;
		this.mutationProb = mp;
		this.matrix = m;
	}
	
	public void evolve(){
		Random randomGenerator = new Random(seed);
		population = new ArrayList<Integer[]>();
		int greedyPopulation = populationsSize/2;
		int randPopulation = populationsSize - greedyPopulation;
		int numberOfIntersections = matrix.size();
		int numberOfVehicles = matrix.get(0).length;
		
		for(int i = 0; i<randPopulation;i++){
			population.add(generateRandIndividual(numberOfIntersections,randomGenerator));
		}

		Greedy greedy = new Greedy(matrix);
		for(int i = 0; i<randPopulation;i++){
			population.add(greedy.generateGreedyIndividual());
		}
		
	}
	
	public Integer[] generateRandIndividual(int noi, Random rg){
		Random randomGenerator = rg;
		Integer[] individual = new Integer[numberOfRSUs];
		for(int i = 0; i<numberOfRSUs; i++){
			individual[i] = -1;			
		}
		for(int i = 0; i<numberOfRSUs; i++){	
			individual[i] = insert(randomGenerator, individual, noi);			
		}
		
		return individual;
	}
	
	public int insert(Random rg, Integer[] ind, int noi){
		int j = rg.nextInt(noi);
		for(Integer i: ind){
			if (i == j) return insert(rg, ind, noi);
		}
		return j;
	}
}
