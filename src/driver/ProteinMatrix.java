package driver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProteinMatrix {
	public int[][] matrix;
	public ArrayList<Coor> coordinates;
	public ArrayList<Integer> muval;
	public ArrayList<Integer> gammaval;
	public int[] sigmaval;
	public int size;
	public String encoding;
	public float score;
	
	public ProteinMatrix(int size) {
		matrix = new int[size*2][size*2];
		coordinates = new ArrayList<Coor>();
		muval = new ArrayList<Integer>();
		gammaval = new ArrayList<Integer>();
		sigmaval = new int[size];
		Arrays.fill(sigmaval, 1);
		this.size = size;
	}
	
	public ProteinMatrix(int size, ArrayList<Integer> muval) {
		matrix = new int[size*2][size*2];
		coordinates = new ArrayList<Coor>();
		this.muval = muval;
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
		muval = new ArrayList<Integer>();
		gammaval = new ArrayList<Integer>();
		sigmaval = new int[size];
		Arrays.fill(sigmaval, 1);
		this.size = size;
					
		for (int i = 0; i < size; i++) {
			line = br.readLine();
			int tempValue = Integer.parseInt(line);
			muval.add(tempValue);
		}
		
		br.close();
	}
	
	public ProteinMatrix(String fileName, boolean gammaVal) throws IOException {
		FileReader fr =  new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		line = br.readLine();
		
		int size = Integer.parseInt(line);
		
		matrix = new int[size*2][size*2];
		coordinates = new ArrayList<Coor>();
		muval = new ArrayList<Integer>();
		gammaval = new ArrayList<Integer>();
		sigmaval = new int[size];
		Arrays.fill(sigmaval, 1);
		this.size = size;
					
		for (int i = 0; i < size; i++) {
			line = br.readLine();
			int tempValue = Integer.parseInt(line);
			gammaval.add(tempValue);
		}
		
		br.close();
	}
	
	public void generateMu() {
		for (int i = 0; i < gammaval.size(); i++) {
			int buffer = (int) Math.floor(4/sigmaval[i]);
			int totalnum = 1;
			int runningsum = gammaval.get(i);
			for (int x = 1; x <= buffer; x++) {
				int currIndex = i-sigmaval[i]*x;
				if (currIndex >= gammaval.size() || currIndex < 0) {
					break;
				}
				totalnum++;
				runningsum += gammaval.get(currIndex);
			}
			for (int x = 1; x <= buffer; x++) {
				int currIndex = i+sigmaval[i]*x;
				if (currIndex >= gammaval.size() || currIndex < 0) {
					break;
				}
				totalnum++;
				runningsum += gammaval.get(currIndex);
			}
			int currmu = Math.round(runningsum/(float) totalnum);
			if (muval.size()-1 < i) {
				muval.add(currmu);
			}
			else {
				muval.set(i, currmu);
			}
		}
	}
	
	public void generateSigma() {
		for (int i = 0; i < (coordinates.size()-2); i++) {
			Coor target = coordinates.get(i+2);
			int tempX = target.xCoor;
			int tempY = target.yCoor;
			Coor curr = coordinates.get(i);
			int currX = curr.xCoor;
			int currY = curr.yCoor;
			//x-1, y; x+1, y; x, y-1; x, y+1; x-1, y+1; x+1, y-1
			if ((tempX==(currX-1) && tempY == (currY))) {
				sigmaval[i] = 2;
				sigmaval[i+1] = 2;
				sigmaval[i+2] = 2;
				continue;
			}
			if ((tempX==(currX+1) && tempY == (currY))) {
				sigmaval[i] = 2;
				sigmaval[i+1] = 2;
				sigmaval[i+2] = 2;
				continue;
			}
			if ((tempX==(currX) && tempY == (currY-1))) {
				sigmaval[i] = 2;
				sigmaval[i+1] = 2;
				sigmaval[i+2] = 2;
				continue;
			}
			if ((tempX==(currX) && tempY == (currY+1))) {
				sigmaval[i] = 2;
				sigmaval[i+1] = 2;
				sigmaval[i+2] = 2;
				continue;
			}
			if ((tempX==(currX-1) && tempY == (currY+1))) {
				sigmaval[i] = 2;
				sigmaval[i+1] = 2;
				sigmaval[i+2] = 2;
				continue;
			}
			if ((tempX==(currX+1) && tempY == (currY-1))) {
				sigmaval[i] = 2;
				sigmaval[i+1] = 2;
				sigmaval[i+2] = 2;
				continue;
			}
		}
		for (int i = coordinates.size()-1; i > 1; i--) {
			Coor target = coordinates.get(i-2);
			int tempX = target.xCoor;
			int tempY = target.yCoor;
			Coor curr = coordinates.get(i);
			int currX = curr.xCoor;
			int currY = curr.yCoor;
			//x-1, y; x+1, y; x, y-1; x, y+1; x-1, y+1; x+1, y-1
			if ((tempX==(currX-1) && tempY == (currY))) {
				sigmaval[i] = 2;
				sigmaval[i-1] = 2;
				sigmaval[i-2] = 2;
				continue;
			}
			if ((tempX==(currX+1) && tempY == (currY))) {
				sigmaval[i] = 2;
				sigmaval[i-1] = 2;
				sigmaval[i-2] = 2;
				continue;
			}
			if ((tempX==(currX) && tempY == (currY-1))) {
				sigmaval[i] = 2;
				sigmaval[i-1] = 2;
				sigmaval[i-2] = 2;
				continue;
			}
			if ((tempX==(currX) && tempY == (currY+1))) {
				sigmaval[i] = 2;
				sigmaval[i-1] = 2;
				sigmaval[i-2] = 2;
				continue;
			}
			if ((tempX==(currX-1) && tempY == (currY+1))) {
				sigmaval[i] = 2;
				sigmaval[i-1] = 2;
				sigmaval[i-2] = 2;
				continue;
			}
			if ((tempX==(currX+1) && tempY == (currY-1))) {
				sigmaval[i] = 2;
				sigmaval[i-1] = 2;
				sigmaval[i-2] = 2;
				continue;
			}
		}
	}
	
	public float calculate(float mean_field, float entropy, float temperature) {
		int sum = 0;

		int x;
		int y;

		float curr = 0;

		for(Coor coor : coordinates){
			x = coor.xCoor;
			y = coor.yCoor;

			curr = matrix[x][y];
			
			boolean calculated = false;

			if (x != 0 && !calculated) {
				sum += (curr*matrix[x-1][y]/2);
				if (matrix[x-1][y] == 0) {
					sum += mean_field*curr;
				}
			}
			if (x != matrix[0].length-1 && !calculated) {
				sum += (curr*matrix[x+1][y]/2);
				if (matrix[x+1][y] == 0) {
					sum += mean_field*curr;
				}
			}
			if (y != 0 && !calculated) {
				sum += (curr*matrix[x][y-1]/2);
				if (matrix[x][y-1] == 0) {
					sum += mean_field*curr;
				}
			}
			if (y != matrix.length-1 && !calculated) {
				sum += (curr*matrix[x][y+1]/2);
				if (matrix[x][y+1] == 0) {
					sum += mean_field*curr;
				}
			}
			if (x != 0 && y != matrix[0].length-1 && !calculated) {
				sum += (curr*matrix[x-1][y+1]/2);
				if (matrix[x-1][y+1] == 0) {
					sum += mean_field*curr;
				}
			}
			if (x != matrix.length-1 && y != 0 && !calculated) {
				sum += (curr*matrix[x+1][y-1]/2);
				if (matrix[x+1][y-1] == 0) {
					sum += mean_field*curr;
				}
			}

			
			if (entropy != 0) {
				sum -= temperature*entropy;
			}
		}

		return sum;
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
		System.out.println(Arrays.toString(muval.toArray()));
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
