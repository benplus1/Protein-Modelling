package driver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
	
	public int calculate() {
		int sum = 0;

		int x;
		int y;

		int curr = 0;

		for(Coor coor : coordinates){
			x = coor.xCoor;
			y = coor.yCoor;

			curr = matrix[x][y];

			if (x != 0) {
				sum += curr*matrix[x-1][y];
			}
			if (x != matrix[0].length-1) {
				sum += curr*matrix[x+1][y];
			}
			if (y != 0) {
				sum += curr*matrix[x][y-1];
			}
			if (y != matrix.length-1) {
				sum += curr*matrix[x][y+1];
			}
			if (x != 0 && y != matrix[0].length-1) {
				sum += curr*matrix[x-1][y+1];
			}
			if (x != matrix.length-1 && y != 0) {
				sum += curr*matrix[x+1][y-1];
			}

		}

		return sum/2;
	}
	
	public void printCoordinates() {
		for (int i = 0; i < coordinates.size(); i++) {
			System.out.println(coordinates.get(i).xCoor + ", " + coordinates.get(i).yCoor);
		}
	}
	
	public void printProteinMatrix() {
		System.out.println("the matrix in pictoral form: ");
		printMatrix(matrix);
		System.out.println("the values of the protein: ");
		System.out.println(Arrays.toString(values.toArray()));
		System.out.println("the coordinates of the folding: ");
		printCoordinates();
		System.out.println("the size for this matrix is: " + size);
		System.out.println("the score for this matrix is: " + score);
		System.out.println("the encoding for this matrix is: " + encoding);
	}
	
	public void printMatrix(int[][] matrix) {
		for (int[] row : matrix)
		{
		    System.out.println(Arrays.toString(row));
		}
	}
}
