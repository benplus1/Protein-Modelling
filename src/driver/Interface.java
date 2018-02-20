package driver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

class encoding_score{
	String encoding;
	int score;
	
	public encoding_score(String enc, int sc) {
		encoding = enc;
		score = sc;
	}
	
	public String toString() {
		return encoding + "," + score + "\n";
	}
}

public class Interface {
	
	public static void main(String args[]) throws IOException, InterruptedException {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("enter 0 to analyze the value for one specific encoding, 1 to run a top 10 list of matrices with the highest value, 2 to run a top 10 list for 3D matricies, 3 to read a score results file");
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
			
			File encodingScores = new File("encoding scores.csv");
			encodingScores.createNewFile();
			FileWriter writer = new FileWriter(encodingScores); 
			
			//reading the file
			System.out.println("please print your filename");
			String fileName = sc.nextLine();
			fileName = "C:/Users/yangb/workspace/Protein/test";
			
			//System.out.println("please specify the amount of divisions for the histogram");
			//int numDivision = sc.nextInt();
			
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
			for (int currentEncodingNum = 0; currentEncodingNum < (findMaxDigits(5, size-2) + 1); currentEncodingNum++) {
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
					encoding_score temp = new encoding_score(current.encoding, current.score);
					writer.write(temp.toString());
					writer.flush();
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
			
			System.out.println("\nmax score is: " + top10List.get(0).score + "\n");
			
			writer.close();
			
			int max = top10List.get(0).score;
			
			System.out.println("please enter the file path of your csv file");
			String tempLine = sc.nextLine();
			tempLine = "encoding scores.csv";
			System.out.println("please enter the number of divisions you would like");
			int num_divisions = Integer.parseInt(sc.nextLine());
			
			num_divisions = 100;
			
	    	float quotient = (float) max / num_divisions;
			
			int[] aggregateScores = new int[num_divisions];
			
			File read_this = new File(tempLine);
			
			try(BufferedReader br2 = new BufferedReader(new FileReader(read_this))) {
			    for(String line2; (line2 = br2.readLine()) != null; ) {
			    	String[] temp = line2.split(",");
			    	float temp_score = Float.parseFloat(temp[1]);
			    	
			    	float curr = quotient;
			    	int aggregate_slot = 0;
			    	
			    	while (curr < temp_score) {
			    		curr += quotient;
			    		aggregate_slot++;
			    	}
			    	
			    	aggregateScores[aggregate_slot]++;
			    }
			}
			
			File output_aggregate = new File("output aggregate.csv");
			
			FileWriter aggregate_writer = new FileWriter(output_aggregate);
			
			aggregate_writer.write("minimum score,number of encodings\n");
			aggregate_writer.flush();
			
			for (int i = 0; i < num_divisions; i++) {
				aggregate_writer.write((i*quotient) + "," + aggregateScores[i] + "\n");
			}
			
			aggregate_writer.close();
		}
		else if (selection.equals("2")) {
			
		}
		else if (selection.equals("3")) {
			System.out.println("please print out the maximum score of your file");
			int max = Integer.parseInt(sc.nextLine());
			
			max = 588950;
			
			System.out.println("please enter the file path of your csv file");
			String tempLine = sc.nextLine();
			tempLine = "encoding scores.csv";
			System.out.println("please enter the number of divisions you would like");
			int num_divisions = Integer.parseInt(sc.nextLine());
			
			num_divisions = 100;
			
	    	float quotient = (float) max / num_divisions;
			
			int[] aggregateScores = new int[num_divisions];
			
			File read_this = new File(tempLine);
			
			int counter = 0;
			
			try(BufferedReader br2 = new BufferedReader(new FileReader(read_this))) {
			    for(String line2; (line2 = br2.readLine()) != null; ) {
			    	String[] temp = line2.split(",");
			    	float temp_score = Float.parseFloat(temp[1]);
			    	
			    	float curr = quotient;
			    	int aggregate_slot = 0;
			    	
			    	while (curr < temp_score) {
			    		curr += quotient;
			    		aggregate_slot++;
			    	}
			    	
			    	aggregateScores[aggregate_slot]++;
			    	
			    	System.out.println("reading line " + counter);
			    	counter++;
			    }
			}
			
			File output_aggregate = new File("output aggregate.csv");
			
			FileWriter aggregate_writer = new FileWriter(output_aggregate);
			
			aggregate_writer.write("minimum score,number of encodings\n");
			aggregate_writer.flush();
			
			for (int i = 0; i < num_divisions; i++) {
				aggregate_writer.write((i*quotient) + "," + aggregateScores[i] + "\n");
			}
			
			aggregate_writer.close();
		}
		else if (selection.equals("10")) {
			Coor3D tempCoor = new Coor3D(0,0,0);
			System.out.println(tempCoor.xCoor + " " + tempCoor.yCoor + " " + tempCoor.zCoor);
			tempCoor.goULevel();
			System.out.println(tempCoor.xCoor + " " + tempCoor.yCoor + " " + tempCoor.zCoor);
			tempCoor.goDownLeft();
			System.out.println(tempCoor.xCoor + " " + tempCoor.yCoor + " " + tempCoor.zCoor);
		}
		else if (selection.equals("11")) {
			System.out.println(findMaxDigits(5, 11));
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
	
	public static int findMaxDigits(int base, int length) {
		int amount_in_base_10 = 0;
		
		int current_power = 0;
		
		for (current_power = 0; current_power < length; current_power++) {
			amount_in_base_10 += ((base-1)*Math.pow(base, current_power));
		}
		
		return amount_in_base_10;
	}
}