package driver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ProteinMatrix {
	public int[][] matrix;
	public ArrayList<Coor> coordinates;
	public ArrayList<Integer> values;
	public int size;
	public String encoding;
	public int score;
	
	public ProteinMatrix(int size) {
		matrix = new int[size*2][size*2];
		coordinates = new ArrayList<Coor>();
		values = new ArrayList<Integer>();
		this.size = size;
	}
	
	public ProteinMatrix(int size, ArrayList<Integer> values) {
		matrix = new int[size*2][size*2];
		coordinates = new ArrayList<Coor>();
		this.values = values;
		this.size = size;
	}
	
	public ProteinMatrix(String fileName) throws IOException {
		FileReader fr =  new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		line = br.readLine();
		
		int size = Integer.parseInt(line);
		
		matrix = new int[size*2][size*2];
		coordinates = new ArrayList<Coor>();
		values = new ArrayList<Integer>();
		this.size = size;
					
		for (int i = 0; i < size; i++) {
			line = br.readLine();
			int tempValue = Integer.parseInt(line);
			values.add(tempValue);
		}
		
		br.close();
	}
}
