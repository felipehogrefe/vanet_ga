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

	public Genetic(int nGenerations, int popSize, int nRsus, int time, Double crossProb, Double mutProb, ArrayList<ArrayList<Integer>> m) {
		this.numberOfGenerations = nGenerations;
		this.populationsSize = popSize;
		this.numberOfRSUs = nRsus;
		this.iTime = time;
		this.crossoverProb = crossProb;
		this.mutationProb = mutProb;
		this.matrix = m;
		this.randomGenerator = new Random(seed);

		initPopulation();
	}

	private void initPopulation() {
		System.out.print("initializing population...");
		population = new ArrayList<Individual>();
		int greedyPopulation = populationsSize / 2;
		int randPopulation = populationsSize - greedyPopulation;
		numberOfIntersections = matrix.size();
		numberOfVehicles = matrix.get(0).size();

		for (int i = 0; i < randPopulation; i++) {
			population.add(generateRandIndividual(numberOfIntersections));
		}

		Greedy greedy = new Greedy(matrix, numberOfRSUs, iTime, true);
		for (int i = 0; i < greedyPopulation; i++) {
			population.add(greedy.generateGreedyIndividual());
		}
		System.out.println(" done");
	}

	public void evolve() {
		System.out.println("evolving...");
		ArrayList<Double> popFitness;
		Individual fittest;

		while (numberOfGenerations > 0) {
			System.out.println(numberOfGenerations);
			//calculate the fitness of each individual
			popFitness = populationFitness();
			
			//start the next generation
			ArrayList<Individual> newPopulation = new ArrayList<Individual>();
			
			//get the fittest for elitsm:
			fittest = getFittest(popFitness);
			fittest.print();System.out.println("--");
			newPopulation.add(fittest);
			
			//in order to fill the first half of the new population
			//we realizae a tournament selection
			while(newPopulation.size()<(populationsSize/2)){
				Individual newInd = tournamentSelection(popFitness);
				newPopulation.add(newInd);
			}
			
			//here we do crossovers to fullfil the population
			ArrayList<Individual> childs = crossover(newPopulation);
			
			//in the last step we mutate childs	
			for(Individual ind : childs){
				ind.mutate(mutationProb, randomGenerator, numberOfIntersections);
				newPopulation.add(ind);
			}
		
			population = newPopulation;
			
			for(Individual ind : population){
				ind.print();
			}
			
			numberOfGenerations--;
		}
	}
	
	public ArrayList<Individual> crossover(ArrayList<Individual> curPopulation){
		ArrayList<Individual> childs = new ArrayList<Individual>();
		int curPopSize = curPopulation.size();
		int childsSize = populationsSize-curPopulation.size();
		while(childs.size()<childsSize){
			//checks if there will be a crossover interaction
			if(randomGenerator.nextDouble()<crossoverProb){
				int ind1index = randomGenerator.nextInt(curPopSize);
				Individual ind1 = curPopulation.get(ind1index);
				int ind2index = randomGenerator.nextInt(curPopSize);
				while(ind1index==ind2index){
					ind2index = randomGenerator.nextInt(curPopSize);
				}
				Individual ind2 = curPopulation.get(ind2index);
				
				//TODO the crossover point is completely random?
				int crossPoint = randomGenerator.nextInt(numberOfRSUs);
				
				//generate and add the childs
				Individual child1 = ind1.generateChildOPC(ind2, crossPoint,numberOfIntersections,randomGenerator);
				//child1.print();
				Individual child2 = ind2.generateChildOPC(ind1, crossPoint,numberOfIntersections,randomGenerator);	
				//child2.print();			
				childs.add(child1);
				childs.add(child2);
			}	
		}
		return childs;
	}
	
	public Individual tournamentSelection(ArrayList<Double> popFitness){
		int index1 = randomGenerator.nextInt(population.size());
		Individual ind1 = population.get(index1);
		population.remove(index1);
		
		int index2 = randomGenerator.nextInt(population.size());		
		Individual ind2 = population.get(index2);	
		population.remove(index2);
				
		return ind1.getBetter(ind2, numberOfVehicles, numberOfRSUs, matrix, iTime);
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
		double fit = 0;
	//	System.out.println("aaaaa");
		for (int k = 0; k < population.size(); k++) {
			Double f = population.get(k).calcFitness(numberOfVehicles, numberOfRSUs, matrix, iTime);
			fitness.add(f);
		//	System.out.print(f+" ");
			fit +=f;
		}
	//	System.out.println(fit);
		return fitness;
	}

}
