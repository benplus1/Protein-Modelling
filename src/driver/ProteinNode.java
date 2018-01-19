package driver;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
		
		if (!populate(encoding)) {
			System.out.println("the encoding was not valid");
			return;
		}
		
		float solution = calculate();
		
		System.out.println("your value is " + solution);
	}
	
	public static boolean populate(String encoding) {
		int tempX = size;
		int tempY = size;
		int valueCounter = 0;
		
		Coor tempCoor = new Coor(tempX, tempY);
		
		coordinates.add(tempCoor);
		matrix[tempX][tempY] = values.get(valueCounter);
		
		goRight(tempX, tempY);
		valueCounter++;
		Coor tempCoor2 = new Coor(tempX, tempY);
		coordinates.add(tempCoor2);
		matrix[tempX][tempY] = values.get(valueCounter);
		
		valueCounter++;
		int pastMove = 2;
		int encodingCounter = 0;
		
		for (; valueCounter < values.size(); valueCounter++) {
			int currEncodeValue = Character.getNumericValue(encoding.charAt(encodingCounter));
			pastMove = nextMove(pastMove, tempX, tempY, currEncodeValue);
			if (matrix[tempX][tempY] != 0) {
				return false;
			}
			matrix[tempX][tempY] = values.get(valueCounter);
			Coor tempCoor3 = new Coor(tempX, tempY);
			coordinates.add(tempCoor3);
			encodingCounter++;
		}
		
		return true;
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

			sum += curr*matrix[x-1][y];
			sum += curr*matrix[x+1][y];
			sum += curr*matrix[x-1][y+1];
			sum += curr*matrix[x+1][y-1];
			sum += curr*matrix[x][y+1];
			sum += curr*matrix[x][y-1];

		}

		return sum;
	}
	
	public static int nextMove(int pastMove, int runningXCoor, int runningYCoor, int currEncodeValue) {
		switch (pastMove) {
			case 0: {
				if (currEncodeValue == 0) {
					goDownLeft(runningXCoor, runningYCoor);
					return 4;
				}
				else if (currEncodeValue == 1) {
					goLeft(runningXCoor, runningYCoor);
					return 5;
				}
				else if (currEncodeValue == 2) {
					goUpLeft(runningXCoor, runningYCoor);
					return 0;
				}
				else if (currEncodeValue == 3) {
					goUpRight(runningXCoor, runningYCoor);
					return 1;
				}
				else if (currEncodeValue == 4) {
					goRight(runningXCoor, runningYCoor);
					return 2;
				}
				break;
			}
			case 1: {
				if (currEncodeValue == 0) {
					goLeft(runningXCoor, runningYCoor);
					return 5;
				}
				else if (currEncodeValue == 1) {
					goUpLeft(runningXCoor, runningYCoor);
					return 0;
				}
				else if (currEncodeValue == 2) {
					goUpRight(runningXCoor, runningYCoor);
					return 1;
				}
				else if (currEncodeValue == 3) {
					goRight(runningXCoor, runningYCoor);
					return 2;
				}
				else if (currEncodeValue == 4) {
					goDownRight(runningXCoor, runningYCoor);
					return 3;
				}
				break;
			}
			case 2: {
				if (currEncodeValue == 0) {
					goUpLeft(runningXCoor, runningYCoor);
					return 0;
				}
				else if (currEncodeValue == 1) {
					goUpRight(runningXCoor, runningYCoor);
					return 1;
				}
				else if (currEncodeValue == 2) {
					goRight(runningXCoor, runningYCoor);
					return 2;
				}
				else if (currEncodeValue == 3) {
					goDownRight(runningXCoor, runningYCoor);
					return 3;
				}
				else if (currEncodeValue == 4) {
					goDownLeft(runningXCoor, runningYCoor);
					return 4;
				}
				break;
			}
			case 3: {
				if (currEncodeValue == 0) {
					goUpRight(runningXCoor, runningYCoor);
					return 1;
				}
				else if (currEncodeValue == 1) {
					goRight(runningXCoor, runningYCoor);
					return 2;
				}
				else if (currEncodeValue == 2) {
					goDownRight(runningXCoor, runningYCoor);
					return 3;
				}
				else if (currEncodeValue == 3) {
					goDownLeft(runningXCoor, runningYCoor);
					return 4;
				}
				else if (currEncodeValue == 4) {
					goLeft(runningXCoor, runningYCoor);
					return 5;
				}
				break;
			}
			case 4: {
				if (currEncodeValue == 0) {
					goRight(runningXCoor, runningYCoor);
					return 2;
				}
				else if (currEncodeValue == 1) {
					goDownRight(runningXCoor, runningYCoor);
					return 3;
				}
				else if (currEncodeValue == 2) {
					goDownLeft(runningXCoor, runningYCoor);
					return 4;
				}
				else if (currEncodeValue == 3) {
					goLeft(runningXCoor, runningYCoor);
					return 5;
				}
				else if (currEncodeValue == 4) {
					goUpLeft(runningXCoor, runningYCoor);
					return 0;
				}
				break;
			}
			case 5: {
				if (currEncodeValue == 0) {
					goDownRight(runningXCoor, runningYCoor);
					return 3;
				}
				else if (currEncodeValue == 1) {
					goDownLeft(runningXCoor, runningYCoor);
					return 4;
				}
				else if (currEncodeValue == 2) {
					goLeft(runningXCoor, runningYCoor);
					return 5;
				}
				else if (currEncodeValue == 3) {
					goUpLeft(runningXCoor, runningYCoor);
					return 0;
				}
				else if (currEncodeValue == 4) {
					goUpRight(runningXCoor, runningYCoor);
					return 1;
				}
				break;
			}
			default: System.out.println("invalid past move");
					break; 
		}
		return -1;
	}
	
	
	
	public static void goUpLeft(int runningXCoor, int runningYCoor) {
		runningYCoor--;
	}
	
	public static void goUpRight(int runningXCoor, int runningYCoor) {
		runningXCoor++;
		runningYCoor--;
	}
	
	public static void goLeft(int runningXCoor, int runningYCoor) {
		runningXCoor--;
	}
	
	public static void goRight(int runningXCoor, int runningYCoor) {
		runningXCoor++;
	}
	
	public static void goDownLeft(int runningXCoor, int runningYCoor) {
		runningXCoor--;
		runningYCoor++;
	}
	
	public static void goDownRight(int runningXCoor, int runningYCoor) {
		runningYCoor++;
	}
}