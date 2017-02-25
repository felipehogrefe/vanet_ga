package vanet_ga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	private static final String[] path = {"E0-T-60s-4CP","E1-T-60s-4CP","E2-T-60s-4CP","E3-T-60s-4CP"};
	

	public static void main(String[] args) {
		long millis = System.nanoTime();
		try {
			Genetic genetic = new Genetic(200,200,5,30,0.90,0.10,readMatrix(path[3]));
			genetic.evolve();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		millis = System.nanoTime() - millis;
        millis=millis/1000000;
        System.out.println(millis);
	}
	
	private static ArrayList<ArrayList<Integer>> readMatrix(String path) throws IOException{
		ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();		
		BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
		String iLine = "";
		while((iLine = reader.readLine())!=null){
			String sData[] = iLine.split(" ");
			ArrayList<Integer> data = new ArrayList<Integer>();
			for(int i  = 0;i<sData.length;i++){
				data.add(Integer.parseInt(sData[i]));
			}
			matrix.add(data);
		}
		reader.close();
		return matrix;
	}
}
