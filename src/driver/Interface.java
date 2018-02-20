package driver;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class encoding_score{
	String encoding;
	int score;
	
	public encoding_score(String enc, int sc) {
		encoding = enc;
		score = sc;
	}
	
	public String toString() {
		return "(" + encoding + "," + score + ")";
	}
}

public class Interface {
	
	public static void main(String args[]) throws IOException, InterruptedException {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("enter 0 to analyze the value for one specific encoding, 1 to run a top 10 list of matrices with the highest value, 2 to run a top 10 list for 3D matricies");
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
			
			result.score = result.calculate();
			result.encoding = encoding;
			
			result.printProteinMatrix();

		}
		else if (selection.equals("1")) {
			//highest number of base 5 11digits in base 10 48828124
			
			//reading the file
			System.out.println("please print your filename");
			String fileName = sc.nextLine();
			fileName = "C:/Users/yangb/workspace/Protein/test";
			
			System.out.println("please specify the amount of divisions for the histogram");
			int numDivision = sc.nextInt();
			ArrayList<encoding_score> listOfES = new ArrayList<encoding_score>();
			
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
					current.score = current.calculate();
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
				top10List.get(k).printProteinMatrix();
			}
		}
		else if (selection.equals("2")) {
			
		}
		else if (selection.equals("10")) {
			Coor3D tempCoor = new Coor3D(0,0,0);
			System.out.println(tempCoor.xCoor + " " + tempCoor.yCoor + " " + tempCoor.zCoor);
			tempCoor.goULevel();
			System.out.println(tempCoor.xCoor + " " + tempCoor.yCoor + " " + tempCoor.zCoor);
			tempCoor.goDownLeft();
			System.out.println(tempCoor.xCoor + " " + tempCoor.yCoor + " " + tempCoor.zCoor);
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
		
		tempCoor.goRight();
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
			pastMove = tempCoor.nextMove(pastMove, currEncodeValue);
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
	
	public static String addZeroes(String encoding, int size) {
		size -= 2;
		int diff = size - encoding.length();
		for (int i = 0; i < diff; i++) {
			encoding = "0" + encoding;
		}
		return encoding;
	}
}