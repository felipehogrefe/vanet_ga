package vanet_ga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Greedy {;
	private ArrayList<ArrayList<Integer>> matrix;
	private int seed=62, k, iTime;
	private Random random = new Random(seed);
	
	public Greedy(ArrayList<ArrayList<Integer>> m, int k, int it){
		this.matrix = m;		
		this.k = k;
		this.iTime = it;
	}
		
	public ArrayList<Integer> generateGreedyIndividual(){
		int rsuQtd = 0;
		int numVehicles = matrix.get(0).size();
		int numIntersections = matrix.size();
		ArrayList<Integer> S = initializeIntersections(numIntersections);
		ArrayList<RSU> rsus = new ArrayList<RSU>();
		ArrayList<Integer> s = new ArrayList<Integer>();
		ArrayList<Integer> tj = initializetj(numVehicles);
		ArrayList<Integer[]> topT = new ArrayList<Integer[]>();
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
			int w = randTopTen(W,s);
			System.out.print(w + " ");
			for(int m = 0; m<numVehicles; m++){
				if(tj.get(m)>0){
					int aux = tj.get(m);
					tj.set(m, aux - matrix.get(w).get(w));
				}else{
					tj.set(m, 0);
				}
			}
			s.add(w);
//			System.out.println("\n"+w+" - "+S.indexOf(w));
//			for(int i : S){
//				System.out.print(i + " ");
//			}
			//S.set(S.indexOf(w));
			//rsus.add(new RSU(getRSU(w,s,S.length),W[w]));
			//TODO: fix the value that are added to 's', now it corresponds to the possition in the current S array, not the real one.		
			//s[rsuQtd] = getRSU(w,s,S.length);
			//s.add(removew(S,w));
			rsuQtd++;	
		}
		

		System.out.print("---"+W.get(43)+"--- ");
		for(int i = 0;i<s.size();i++){
			for(int j = 0;j<s.size();j++){
				if(s.get(j)==s.get(i) && i!=j) System.out.print("erro ");
			}
			System.out.print(""+s.get(i) + " ");
		}
		System.out.println();

		return s;
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

	private ArrayList<Integer> initializeIntersections(int intersections) {
		ArrayList<Integer> a = new ArrayList<Integer>(); 
		for(int j = 0; j<intersections; j++){
			a.add(j);
		}
		return a;
	}
}
