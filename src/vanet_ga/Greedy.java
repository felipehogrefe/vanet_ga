package vanet_ga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Greedy {;
	private ArrayList<ArrayList<Integer>> matrix, original;
	private int seed=62, k, iTime;
	private Random random = new Random(seed);
	
	public Greedy(ArrayList<ArrayList<Integer>> m, int k, int it){
		this.original = m;	
		this.k = k;
		this.iTime = it;
	}
	
	public ArrayList<Integer> generateGreedyIndividual(){
		matrix = copyMatrix(original);
		int rsuQtd = 0;
		int numVehicles = matrix.get(0).size();
		int numIntersections = matrix.size();
		ArrayList<Integer> s = new ArrayList<Integer>();
		ArrayList<Integer> tj = initializetj(numVehicles);
		ArrayList<Integer> W = populateW(numIntersections);
		while(rsuQtd < k){
			for(int i = 0; i < numVehicles; i++){				
				ArrayList<Integer> Tj = new ArrayList<Integer>();
				for(int j = 0; j < numIntersections; j++){
					Tj.add(matrix.get(j).get(i));
				}								
				int time = tj.get(i);				
				for(int n = 0; n<numIntersections; n++){					
					int Tij = Tj.get(n);
					if(Tij > time){
						if(time < 0){
							tj.set(i,0);
							time = 0;
						}
						Tij = time;
					}
					int Wn = W.get(n);
					W.set(n, Wn+Tij);
				}
			}
			//at the method 'randTopTen' we ensure that RSUs arent 
			//being deployed at the same intersections, it does the 
			//work of lines 7 and 8 of algorithm 1
			int w = randTopTen(W,s);
			for(int i = 0; i<numVehicles; i++){
				if(matrix.get(w).get(i)>=tj.get(i)){
					tj.set(i, 0);
				}else{
					int aux = tj.get(i);
					tj.set(i, aux - matrix.get(w).get(i));
				}
				matrix.get(w).set(i,0);	
			}
			s.add(w);
			rsuQtd++;	
		}
		return s;
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
		
	private int randTopTen(ArrayList<Integer> W, ArrayList<Integer> s){
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
		int actual = random.nextInt(10);
		//in order to ensure that RSUs arent being deployed into the 
		//same intersections we look at previously deployed RSUs and 
		//prevent the actual one to be palced at the same intersection:
		while(s.indexOf(large[actual])!=-1){
			actual = random.nextInt(10);
		}
		return large[actual];
	}
	
	private ArrayList<Integer> populateW(int intersections) {
		ArrayList<Integer> a = new ArrayList<Integer>(); 
		for(int j = 0; j<intersections; j++){
			a.add(0);
		}
		return a;
	}

	private ArrayList<Integer> initializetj(int numVehicles) {
		ArrayList<Integer> a = new ArrayList<Integer>(); 
		for(int j = 0; j<numVehicles; j++){
			a.add(iTime);
		}
		return a; 
	}
}
