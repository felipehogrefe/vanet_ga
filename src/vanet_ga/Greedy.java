package vanet_ga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Greedy {;
	private ArrayList<Integer[]> matrix;
	private static final int k = 5, iTime = 30;
	private int seed=62;
	private Random random = new Random(seed);
	
	public Greedy(ArrayList<Integer[]> m){
		this.matrix = m;		
	}
		
	public Integer[] generateGreedyIndividual(){
		int rsuQtd = k;
		int numVehicles = matrix.get(0).length;
		int numIntersections = matrix.size();
		int S[] = initializeIntersections(numIntersections);
		int imutS[] = initializeIntersections(numIntersections);
		ArrayList<RSU> rsus = new ArrayList<RSU>();
		Integer s[] = new Integer[k];
		int tj[] = initializetj(numVehicles);
		ArrayList<Integer[]> topT = new ArrayList<Integer[]>();
		
		while(rsuQtd >= 1){
			int W[] = populateW(numIntersections);
			for(int i = 0; i < numVehicles; i++){				
				int Tj[] = new int[numIntersections];
				for(int j = 0; j < numIntersections; j++){
					Tj[j] = matrix.get(j)[i];
				}								
				int time = tj[i];				
				for(int n = 0; n<numIntersections; n++){					
					int Tij = Tj[n];
					if(Tij > time){
						if(time < 0){
							tj[i] = 0;
							time = 0;
						}
						Tij = time;
					}
					W[n] = W[n] + Tij;
				}
			}
			int w = randTopTen(W);
			for(int m = 0; m<numVehicles; m++){
				if(tj[m]>0){
					tj[m] = tj[m] - matrix.get(w)[m];
				}else{
					tj[m] = 0;
				}
			}
			rsus.add(new RSU(w,W[w]));
			rsuQtd--;			
			s[rsuQtd] = w;
			S = remove(w,S);
		}
		
//		for(int i : s){
//			System.out.print(i + " ");
//		}
//		System.out.println();

		return s;
	}
	
	private int getRSU(int[] s, int w) {
		for(int i  = 0;i<s.length;i++){
			if(i == w) return s[i];
		}
		for(int i  = 0;i<s.length;i++){
			if(s[i] == w) return s[i];
		}
		return -1;
	}

	private int[] remove(int toRemove, int[] array){
		int newArray[] = new int[array.length -1];
		int i = 0, j = 0;
		while(i < newArray.length){
			if(array[i] == toRemove){
				j++;
			}
			newArray[i] = array[j];
			i++;
			j++;
		}
		return newArray;
	}
	
	private int randTopTen(int[] W){
		int[] array = new int[W.length];
		for(int i = 0; i<W.length;i++){
			array[i] = W[i];
		}
		int actual = random.nextInt(10);
		Integer large[] = new Integer[10];
		int max = 0, index = 0;
		for (int j = 0; j < 10; j++) {
	        max = array[0];
	        index = 0;
	        for (int i = 1; i < W.length; i++) {
	            if (max < array[i]) {
	                max = array[i];
	                index = i;
	            }
	        }
	        large[j] = index;
	        array[index] = Integer.MIN_VALUE;
	    }		
		return large[actual];
	}
	
	private int[] populateW(int intersections) {
		int i[] = new int[intersections];
		for(int j = 0; j<i.length; j++){
			i[j] = 0;
		}
		return i;
	}

	private int[] initializetj(int numVehicles) {
		int i[] = new int[numVehicles];
		for(int j = 0; j<i.length; j++){
			i[j] = iTime;
		}
		return i;
	}

	private int[] initializeIntersections(int intersections) {
		int i[] = new int[intersections];
		for(int j = 0; j<i.length; j++){
			i[j] = j;
		}
		return i;
	}
	
	private int iMax(int[] W){
		int max = 0;
		int index = 0;
		for(int i = 0; i<W.length; i++){
			if(W[i] > max){
				max = W[i];
				index = i;
			}
		}
		return index;
	}
	
	private Integer[] topTen(int[] W){
		Integer large[] = new Integer[10];
		int max = 0, index;
		for (int j = 0; j < 10; j++) {
	        max = W[0];
	        index = 0;
	        for (int i = 1; i < W.length; i++) {
	            if (max < W[i]) {
	                max = W[i];
	                index = i;
	            }
	        }
	        large[j] = max;
	        W[index] = Integer.MIN_VALUE;
	    }		
		return large;
	}

}
