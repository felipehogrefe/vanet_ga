package vanet_ga;

import java.util.ArrayList;
import java.util.Random;

public class Greedy {;
	private ArrayList<ArrayList<Integer>> matrix, original;
	private int k, iTime;
	private Random random;
	private boolean randomIndividual;
	
	public Greedy(ArrayList<ArrayList<Integer>> m, int k, int it, boolean ri, Random r){
		this.original = m;	
		this.k = k;
		this.iTime = it;
		this.randomIndividual=ri;
		this.random = r;
	}
	
	public Individual generateGreedyIndividual(){
		matrix = copyMatrix(original);
		int rsuQtd = 0;
		int numVehicles = matrix.get(0).size();
		int numIntersections = matrix.size();
		Individual ind = new Individual();
		ArrayList<Integer> tj = initializetj(numVehicles);
	
		while(rsuQtd < k){
			ArrayList<Integer> W = new ArrayList<Integer>();	
			for(int i = 0; i < numIntersections; i++){	
				int wr = 0;
				for(int v = 0; v<numVehicles; v++){
					int vTime = matrix.get(i).get(v);
					if(vTime > tj.get(v)){
						vTime = tj.get(v);
					}
					wr += vTime;
				}
				W.add(wr);
			}
			int w = randTopTen(W,ind);
			ind.add(w);
			for(int i = 0; i<numVehicles; i++){
				if(matrix.get(w).get(i)>=tj.get(i)){
					tj.set(i, 0);
				}else{
					int aux = tj.get(i);
					tj.set(i, aux - matrix.get(w).get(i));
				}
				matrix.get(w).set(i,0);	
			}
			rsuQtd++;	
		}
		return ind;
	}
		
	private ArrayList<ArrayList<Integer>> copyMatrix(ArrayList<ArrayList<Integer>> m){
		ArrayList<ArrayList<Integer>> newMatrix = new ArrayList<ArrayList<Integer>>();
		for(ArrayList<Integer> i : m){
			ArrayList<Integer> nLine = new ArrayList<Integer>();
			for(Integer j : i){
				nLine.add(j);
			}
			newMatrix.add(nLine);
		}			
		return newMatrix;
	}
		
	private int randTopTen(ArrayList<Integer> W, Individual ind){
		ArrayList<Integer> array = new ArrayList<Integer>();
		for(int i = 0; i<W.size();i++){
			array.add(W.get(i));
		}
		Integer large[] = new Integer[10];
		int max = 0, index = 0;
		for (int j = 0; j < 10; j++) {
	        max = array.get(0);
	        index = 0;
	        for (int i = 1; i < W.size(); i++) {
	            if (max < array.get(i)) {
	                max = array.get(i);
	                index = i;
	            }
	        }
	        large[j] = index;
	        array.set(index,Integer.MIN_VALUE);
	    }			
		//set to return large[0] if the individual shouldnt be randomcally generated
		if(randomIndividual){
			int actual = random.nextInt(10);
			while(ind.indexOf(large[actual])!=-1){
				actual = random.nextInt(10);
			}
			return large[actual];
			
		} else {
			return large[0];
		}
	}

	private ArrayList<Integer> initializetj(int numVehicles) {
		ArrayList<Integer> a = new ArrayList<Integer>(); 
		for(int j = 0; j<numVehicles; j++){
			a.add(iTime);
		}
		return a; 
	}
}
