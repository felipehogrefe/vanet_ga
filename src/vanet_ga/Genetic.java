package vanet_ga;

import java.util.ArrayList;
import java.util.Random;

public class Genetic {
	private static final int seed = 10;

	private int numberOfGenerations, populationsSize, numberOfRSUs, iTime, numberOfIntersections, numberOfVehicles;
	private double crossoverProb, mutationProb;
	private ArrayList<Individual> population;
	private ArrayList<ArrayList<Integer>> matrix;
	private Random randomGenerator;

	public Genetic(int ng, int ps, int nor, int it, Double cp, Double mp, ArrayList<ArrayList<Integer>> m) {
		this.numberOfGenerations = ng;
		this.populationsSize = ps;
		this.numberOfRSUs = nor;
		this.iTime = it;
		this.crossoverProb = cp;
		this.mutationProb = mp;
		this.matrix = m;
		this.randomGenerator = new Random(seed);

		initPopulation();
	}

	private void initPopulation() {
		population = new ArrayList<Individual>();
		int greedyPopulation = populationsSize / 2;
		int randPopulation = populationsSize - greedyPopulation;
		numberOfIntersections = matrix.size();
		numberOfVehicles = matrix.get(0).size();

		for (int i = 0; i < randPopulation; i++) {
			population.add(generateRandIndividual(numberOfIntersections));
		}

		Greedy greedy = new Greedy(matrix, numberOfRSUs, iTime, true);
		for (int i = 0; i < randPopulation; i++) {
			population.add(greedy.generateGreedyIndividual());
		}
	}

	public void evolve() {
		
		ArrayList<Double> popFitness;
		Individual fittest;

		while (numberOfGenerations > 0) {
			//calculate the fitness of each individual
			popFitness = populationFitness();
			
			//start the next generation
			ArrayList<Individual> newPopulation = new ArrayList<Individual>();
			
			//get the fittest for elitsm:
			fittest = getFittest(popFitness);
			newPopulation.add(fittest);
			population.remove(fittest);
			
			//in order to complete the first half of the new population
			//we realizae a tournament selection
			while(newPopulation.size()<(populationsSize/2)){
				Individual newInd = tournamentSelection(popFitness);
				System.out.println(newPopulation.size());
				newPopulation.add(newInd);
			}
			
			while(newPopulation.size()<populationsSize){
				newPopulation.add(generateRandIndividual(numberOfIntersections));
			}
			
			population = newPopulation;
			
			numberOfGenerations--;
		}
	}
	
	public Individual tournamentSelection(ArrayList<Double> popFitness){
		int index1 = randomGenerator.nextInt(population.size());
		Individual ind1 = population.get(index1);
		population.remove(index1);
		
		int index2 = randomGenerator.nextInt(population.size());		
		Individual ind2 = population.get(index2);	
		population.remove(index2);
				
		return (calcFitness(ind1)>calcFitness(ind2)?ind1:ind2);
	}
	
	
	
	public Individual getFittest(ArrayList<Double> popFitness){
		int indexMax = 0;
		for(int i = 0;i<populationsSize;i++){
			double actual = popFitness.get(i);
			if(actual>popFitness.get(indexMax)) indexMax = i;
		}

		return population.get(indexMax);		
	}

	public Individual generateRandIndividual(int noi) {
		Individual individual = new Individual();
		for (int i = 0; i < numberOfRSUs; i++) {
			individual.add(insert(individual, noi));
		}

		return individual;
	}

	public Integer insert(Individual ind, int noi) {
		int j = randomGenerator.nextInt(noi - 1);
		for(int i =0;i<ind.size();i++){
			if (ind.get(i) == j)
				return insert(ind, noi);
		}
		return j;
	}

	/**
	 * 
	 * @return an array with the fitness of each individual
	 */
	public ArrayList<Double> populationFitness() {
		ArrayList<Double> fitness = new ArrayList<Double>();

		for (int k = 0; k < population.size(); k++) {
			fitness.add(calcFitness(population.get(k)));
		}
		

		return fitness;
	}

	public Double calcFitness(Individual ind) {
		
		int coverage = 0;
		for (int j = 0; j < numberOfVehicles; j++) {
			int sum = 0;
			for (int i = 0; i < numberOfRSUs; i++) {
				sum += matrix.get(ind.get(i)).get(j);
			}
			if (sum >= iTime)
				coverage++;
		}

		return (double) coverage*100 / numberOfVehicles;
	}
}
