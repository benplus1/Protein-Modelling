package driver;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

//C:\Users\yangb\workspace\Protein\test
//22222222222

public class ProteinNode {
	
	public static int[][] matrix;
	public static ArrayList<Coor> coordinates = new ArrayList<Coor>();
	public static ArrayList<Integer> values = new ArrayList<Integer>();
	public static int size;
	
	public static void main(String args[]) throws IOException {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("please print your filename");
		String fileName = sc.nextLine();
		fileName = "C:/Users/yangb/workspace/Protein/test";
		
		FileReader fr =  new FileReader(fileName);
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		line = br.readLine();
		
		size = Integer.parseInt(line);
		
		for (int i = 0; i < size; i++) {
			line = br.readLine();
			int tempValue = Integer.parseInt(line);
			values.add(tempValue);
		}
		
		matrix = new int[size*2][size*2];
		
		System.out.println("please enter your string encoding in base 5: ");
		String encoding = sc.nextLine();
		encoding = "22222222222";
		
		if (!populate(encoding)) {
			System.out.println("the encoding was not valid");
			return;
		}
		
		for (int[] row : matrix)
		{
		    System.out.println(Arrays.toString(row));
		}
		System.out.println(Arrays.toString(values.toArray()));
		printCoordinates();
		System.out.println(size);
		
		float solution = calculate();
		
		System.out.println("your value is " + solution);
	}
	
	public static boolean populate(String encoding) {
		int tempX = size;
		int tempY = size;
		int valueCounter = 0;
		
		Coor tempCoor = new Coor(tempX, tempY);
		
		Coor tempCoor1 = new Coor(tempCoor.xCoor, tempCoor.yCoor);
		coordinates.add(tempCoor1);
		matrix[tempCoor.xCoor][tempCoor.yCoor] = values.get(valueCounter);
		System.out.println(matrix[tempCoor.xCoor][tempCoor.yCoor]);
		System.out.println(tempCoor.xCoor + ", " + tempCoor.yCoor);
		
		goRight(tempCoor);
		//goRight(tempX, tempY);
		valueCounter++;
		Coor tempCoor2 = new Coor(tempCoor.xCoor, tempCoor.yCoor);
		coordinates.add(tempCoor2);
		matrix[tempCoor.xCoor][tempCoor.yCoor] = values.get(valueCounter);
		System.out.println(matrix[tempCoor.xCoor][tempCoor.yCoor]);
		System.out.println(tempCoor.xCoor + ", " + tempCoor.yCoor);
		
		valueCounter++;
		int pastMove = 2;
		int encodingCounter = 0;
		
		for (; valueCounter < values.size(); valueCounter++) {
			int currEncodeValue = Character.getNumericValue(encoding.charAt(encodingCounter));
			pastMove = nextMove(pastMove, tempCoor, currEncodeValue);
			if (matrix[tempCoor.xCoor][tempCoor.yCoor] != 0) {
				for (int[] row : matrix)
				{
				    System.out.println(Arrays.toString(row));
				}
				System.out.println(Arrays.toString(values.toArray()));
				printCoordinates();
				System.out.println(size);
				return false;
			}
			matrix[tempCoor.xCoor][tempCoor.yCoor] = values.get(valueCounter);
			System.out.println(matrix[tempCoor.xCoor][tempCoor.yCoor]);
			System.out.println(tempCoor.xCoor + ", " + tempCoor.yCoor);
			Coor tempCoor3 = new Coor(tempCoor.xCoor, tempCoor.yCoor);
			coordinates.add(tempCoor3);
			encodingCounter++;
		}
		
		return true;
	}
	
	public static void printCoordinates() {
		for (int i = 0; i < coordinates.size(); i++) {
			System.out.println(coordinates.get(i).xCoor + ", " + coordinates.get(i).yCoor);
		}
	}
	
	public static int calculate() {
		int sum = 0;

		int x;
		int y;

		int curr = 0;

		for(Coor coor : coordinates){
			x = coor.xCoor;
			y = coor.yCoor;

			curr = matrix[x][y];

			if (x != 0 && x != matrix[0].length-1) {
				sum += curr*matrix[x-1][y];
				sum += curr*matrix[x+1][y];
			}
			if (y != 0 && y != matrix.length-1) {
				sum += curr*matrix[x][y+1];
				sum += curr*matrix[x][y-1];
			}
			if (x != 0 && x != matrix.length-1 && y != 0 && y != matrix[0].length-1) {
				sum += curr*matrix[x-1][y+1];
				sum += curr*matrix[x+1][y-1];
			}

		}

		return sum;
	}
	
	public static int nextMove(int pastMove, Coor coordinate, int currEncodeValue) {
		switch (pastMove) {
			case 0: {
				if (currEncodeValue == 0) {
					goDownLeft(coordinate);
					return 4;
				}
				else if (currEncodeValue == 1) {
					goLeft(coordinate);
					return 5;
				}
				else if (currEncodeValue == 2) {
					goUpLeft(coordinate);
					return 0;
				}
				else if (currEncodeValue == 3) {
					goUpRight(coordinate);
					return 1;
				}
				else if (currEncodeValue == 4) {
					goRight(coordinate);
					return 2;
				}
				break;
			}
			case 1: {
				if (currEncodeValue == 0) {
					goLeft(coordinate);
					return 5;
				}
				else if (currEncodeValue == 1) {
					goUpLeft(coordinate);
					return 0;
				}
				else if (currEncodeValue == 2) {
					goUpRight(coordinate);
					return 1;
				}
				else if (currEncodeValue == 3) {
					goRight(coordinate);
					return 2;
				}
				else if (currEncodeValue == 4) {
					goDownRight(coordinate);
					return 3;
				}
				break;
			}
			case 2: {
				if (currEncodeValue == 0) {
					goUpLeft(coordinate);
					return 0;
				}
				else if (currEncodeValue == 1) {
					goUpRight(coordinate);
					return 1;
				}
				else if (currEncodeValue == 2) {
					goRight(coordinate);
					return 2;
				}
				else if (currEncodeValue == 3) {
					goDownRight(coordinate);
					return 3;
				}
				else if (currEncodeValue == 4) {
					goDownLeft(coordinate);
					return 4;
				}
				break;
			}
			case 3: {
				if (currEncodeValue == 0) {
					goUpRight(coordinate);
					return 1;
				}
				else if (currEncodeValue == 1) {
					goRight(coordinate);
					return 2;
				}
				else if (currEncodeValue == 2) {
					goDownRight(coordinate);
					return 3;
				}
				else if (currEncodeValue == 3) {
					goDownLeft(coordinate);
					return 4;
				}
				else if (currEncodeValue == 4) {
					goLeft(coordinate);
					return 5;
				}
				break;
			}
			case 4: {
				if (currEncodeValue == 0) {
					goRight(coordinate);
					return 2;
				}
				else if (currEncodeValue == 1) {
					goDownRight(coordinate);
					return 3;
				}
				else if (currEncodeValue == 2) {
					goDownLeft(coordinate);
					return 4;
				}
				else if (currEncodeValue == 3) {
					goLeft(coordinate);
					return 5;
				}
				else if (currEncodeValue == 4) {
					goUpLeft(coordinate);
					return 0;
				}
				break;
			}
			case 5: {
				if (currEncodeValue == 0) {
					goDownRight(coordinate);
					return 3;
				}
				else if (currEncodeValue == 1) {
					goDownLeft(coordinate);
					return 4;
				}
				else if (currEncodeValue == 2) {
					goLeft(coordinate);
					return 5;
				}
				else if (currEncodeValue == 3) {
					goUpLeft(coordinate);
					return 0;
				}
				else if (currEncodeValue == 4) {
					goUpRight(coordinate);
					return 1;
				}
				break;
			}
			default: System.out.println("invalid past move");
					break; 
		}
		return -1;
	}
	
	
	
	public static Coor goUpLeft(Coor coordinate) {
		coordinate.yCoor--;
		return coordinate;
	}
	
	public static Coor goUpRight(Coor coordinate) {
		coordinate.xCoor++;
		coordinate.yCoor--;
		return coordinate;
	}
	
	public static Coor goLeft(Coor coordinate) {
		coordinate.xCoor--;
		return coordinate;
	}
	
	public static Coor goRight(Coor coordinate) {
		coordinate.xCoor++;
		return coordinate;
	}
	
	public static Coor goDownLeft(Coor coordinate) {
		coordinate.xCoor--;
		coordinate.yCoor++;
		return coordinate;
	}
	
	public static Coor goDownRight(Coor coordinate) {
		coordinate.yCoor++;
		return coordinate;
	}
}