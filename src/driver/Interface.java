package driver;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Interface {
	
	public static void main(String args[]) throws IOException, InterruptedException {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("enter 0 to analyze the value for one specific encoding, 1 to run a top 10 list of matrices with the highest value");
		String selection = sc.nextLine();
		
		if (selection.equals("0")) {
			System.out.println("please print your filename");
			String fileName = sc.nextLine();
			fileName = "C:/Users/yangb/workspace/Protein/test";
			
			ProteinMatrix result = new ProteinMatrix(fileName);
			
			System.out.println("please enter your string encoding in base 5: ");
			String encoding = sc.nextLine();
			encoding = "22222222222";
			
			if (!populate(encoding, result)) {
				System.out.println("the encoding was not valid");
				return;
			}
			
			result.score = calculate(result);
			result.encoding = encoding;
			
			printProteinMatrix(result);

		}
		else if (selection.equals("1")) {
			//highest number of base 5 11digits in base 10 48828124
			
			//reading the file
			System.out.println("please print your filename");
			String fileName = sc.nextLine();
			fileName = "C:/Users/yangb/workspace/Protein/test";
			
			FileReader fr =  new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			line = br.readLine();
			
			int size = Integer.parseInt(line);
			ArrayList<Integer> values = new ArrayList<Integer>();
				
			for (int i = 0; i < size; i++) {
				line = br.readLine();
				int tempValue = Integer.parseInt(line);
				values.add(tempValue);
			}
			
			br.close();
			//done reading file
						
			ArrayList<ProteinMatrix> top10List = new ArrayList<ProteinMatrix>();
			ProteinMatrixComparator c = new ProteinMatrixComparator();
			for (int currentEncodingNum = 0; currentEncodingNum < 48828125; currentEncodingNum++) {
				ProteinMatrix current = new ProteinMatrix(size, values);
				String currentEncoding = Integer.toString(Integer.parseInt(Integer.toString(currentEncodingNum), 10), 5);
				currentEncoding = addZeroes(currentEncoding, size);
				boolean populateCheck = populate(currentEncoding, current);
				if (!populateCheck) {
					System.out.println("the current encoding " + currentEncoding + " was not valid");
				}
				else {
					System.out.println("the current encoding " + currentEncoding + " succeeded");
					current.encoding = currentEncoding;
					current.score = calculate(current);
					//printProteinMatrix(current);
					boolean sameScore = false;
					for (int pm = 0; pm < top10List.size(); pm++) {
						if (top10List.get(pm).score == current.score) {
							sameScore = true;
							break;
						}
					}
					if (!sameScore) {
						top10List.add(current);
					}
					Collections.sort(top10List, c);
					if (top10List.size() > 10) {
						top10List.remove(10);
					}
					System.out.println("the current encoding was added successfully to the top 10 list");
				}
				//TimeUnit.SECONDS.sleep(1);
			}
			
			for (int k = 0; k < top10List.size(); k++) {
				printProteinMatrix(top10List.get(k));
			}
		}
	}
	
	public static boolean populate(String encoding, ProteinMatrix result) {
		int tempX = result.size;
		int tempY = result.size;
		int valueCounter = 0;
		
		Coor tempCoor = new Coor(tempX, tempY);
		
		Coor tempCoor1 = new Coor(tempCoor.xCoor, tempCoor.yCoor);
		result.coordinates.add(tempCoor1);
		result.matrix[tempCoor.xCoor][tempCoor.yCoor] = result.values.get(valueCounter);
		//System.out.println(result.matrix[tempCoor.xCoor][tempCoor.yCoor]);
		//System.out.println(tempCoor.xCoor + ", " + tempCoor.yCoor);
		
		goRight(tempCoor);
		valueCounter++;
		Coor tempCoor2 = new Coor(tempCoor.xCoor, tempCoor.yCoor);
		result.coordinates.add(tempCoor2);
		result.matrix[tempCoor.xCoor][tempCoor.yCoor] = result.values.get(valueCounter);
		//System.out.println(result.matrix[tempCoor.xCoor][tempCoor.yCoor]);
		//System.out.println(tempCoor.xCoor + ", " + tempCoor.yCoor);
		
		valueCounter++;
		int pastMove = 2;
		int encodingCounter = 0;
		
		for (; valueCounter < result.values.size(); valueCounter++) {
			int currEncodeValue = Character.getNumericValue(encoding.charAt(encodingCounter));
			pastMove = nextMove(pastMove, tempCoor, currEncodeValue);
			if (result.matrix[tempCoor.xCoor][tempCoor.yCoor] != 0) {
				//printProteinMatrix(result);
				return false;
			}
			result.matrix[tempCoor.xCoor][tempCoor.yCoor] = result.values.get(valueCounter);
			//System.out.println(result.matrix[tempCoor.xCoor][tempCoor.yCoor]);
			//System.out.println(tempCoor.xCoor + ", " + tempCoor.yCoor);
			Coor tempCoor3 = new Coor(tempCoor.xCoor, tempCoor.yCoor);
			result.coordinates.add(tempCoor3);
			encodingCounter++;
		}
		
		return true;
	}
	
	public static void printCoordinates(ProteinMatrix result) {
		for (int i = 0; i < result.coordinates.size(); i++) {
			System.out.println(result.coordinates.get(i).xCoor + ", " + result.coordinates.get(i).yCoor);
		}
	}
	
	public static String addZeroes(String encoding, int size) {
		size -= 2;
		int diff = size - encoding.length();
		for (int i = 0; i < diff; i++) {
			encoding = "0" + encoding;
		}
		return encoding;
	}
	
	public static void printProteinMatrix(ProteinMatrix result) {
		System.out.println("the matrix in pictoral form: ");
		printMatrix(result.matrix);
		System.out.println("the values of the protein: ");
		System.out.println(Arrays.toString(result.values.toArray()));
		System.out.println("the coordinates of the folding: ");
		printCoordinates(result);
		System.out.println("the size for this matrix is: " + result.size);
		System.out.println("the score for this matrix is: " + result.score);
		System.out.println("the encoding for this matrix is: " + result.encoding);
	}
	
	public static void printMatrix(int[][] matrix) {
		for (int[] row : matrix)
		{
		    System.out.println(Arrays.toString(row));
		}
	}
	
	public static int calculate(ProteinMatrix result) {
		int sum = 0;

		int x;
		int y;

		int curr = 0;

		for(Coor coor : result.coordinates){
			x = coor.xCoor;
			y = coor.yCoor;

			curr = result.matrix[x][y];

			if (x != 0) {
				sum += curr*result.matrix[x-1][y];
			}
			if (x != result.matrix[0].length-1) {
				sum += curr*result.matrix[x+1][y];
			}
			if (y != 0) {
				sum += curr*result.matrix[x][y-1];
			}
			if (y != result.matrix.length-1) {
				sum += curr*result.matrix[x][y+1];
			}
			if (x != 0 && y != result.matrix[0].length-1) {
				sum += curr*result.matrix[x-1][y+1];
			}
			if (x != result.matrix.length-1 && y != 0) {
				sum += curr*result.matrix[x+1][y-1];
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