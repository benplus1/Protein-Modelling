package driver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class encoding_score{
	String encoding;
	float score;
	
	public encoding_score(String enc, float score2) {
		encoding = enc;
		score = score2;
	}
	
	public String toString() {
		return encoding + "," + score + "\n";
	}
}

public class Interface {
	
	public static void main(String args[]) throws IOException, InterruptedException {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("enter 0 to analyze the value for one specific encoding, 1 to run a top 10 list of matrices with the highest value, 2 to run a top 10 list for 3D matricies, 3 to read a score results file, 4 randomize and run 8");
		String selection = sc.nextLine();
		
		if (selection.equals("0")) {
			System.out.println("please print your filename");
			String fileName = sc.nextLine();
			fileName = "C:/Users/yangb/workspace/Protein/test";
			
			System.out.println("please specify your temperature");
			String tem = sc.nextLine();
			float temperature = Integer.parseInt(tem);
			
			temperature = 0;
			
			System.out.println("specify the entropy of each residue");
			String res = sc.nextLine();
			float entropy = Integer.parseInt(res);
			
			entropy = 0;
			
			System.out.println("specify the value of the meanfield");
			String mf = sc.nextLine();
			float mean_field = Integer.parseInt(mf);
			
			mean_field = 0;
			
			ProteinMatrix result = new ProteinMatrix(fileName);
			
			System.out.println("please enter your string encoding in base 5: ");
			String encoding = sc.nextLine();
			encoding = "22222222222";
			
			if (!populate(encoding, result)) {
				System.out.println("the encoding was not valid");
				return;
			}
			
			result.score = result.calculate(mean_field, entropy, temperature);
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
			fileName = "C:/Users/yangb/workspace/Protein/test2_0.txt";
			
			System.out.println("please specify your temperature");
			String tem = sc.nextLine();
			float temperature = Integer.parseInt(tem);
			
			temperature = 0;
			
			System.out.println("specify the entropy of each residue");
			String res = sc.nextLine();
			float entropy = Integer.parseInt(res);
			
			entropy = 0;
			
			System.out.println("specify the value of the meanfield");
			String mf = sc.nextLine();
			float mean_field = Integer.parseInt(mf);
			
			mean_field = 0;
			
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
					current.score = current.calculate(mean_field, entropy, temperature);
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
			
			float max = top10List.get(0).score;
			
			System.out.println("please enter the file path of your csv file");
			String tempLine = sc.nextLine();
			tempLine = "encoding scores.csv";
			System.out.println("please enter the number of divisions you would like");
			int num_divisions = Integer.parseInt(sc.nextLine());
			
			num_divisions = 135;
			max = 135*6000;
			
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
			
			max = 589970;
			
			System.out.println("please enter the file path of your csv file");
			String tempLine = sc.nextLine();
			tempLine = "encoding_scores_1_1.txt";
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
			    	
			    	if (temp_score == max) {
			    		aggregate_slot = 99;
			    	}
			    	else {
				    	while (curr < temp_score) {
				    		//System.out.println(curr);
				    		curr += quotient;
				    		//System.out.println(aggregate_slot);
				    		aggregate_slot++;
				    	}
			    	}
			    	
			    	aggregateScores[aggregate_slot]++;
			    	
			    	System.out.println("reading line " + counter);
			    	counter++;
			    }
			}
			
			File output_aggregate = new File("output aggregate test.csv");
			
			FileWriter aggregate_writer = new FileWriter(output_aggregate);
			
			aggregate_writer.write("minimum score,number of encodings\n");
			aggregate_writer.flush();
			
			for (int i = 0; i < num_divisions; i++) {
				aggregate_writer.write((i*quotient) + "," + aggregateScores[i] + "\n");
			}
			
			aggregate_writer.close();
		}
		else if (selection.equals("4")) {
			float mean_field = 50;
			for (int i = 1; i < 3; i++) {
				for (int j = 0; j < 8; j++) {
					//if (!((i == 1) && (j == 0)) && !((i==1) && (j==1))) {
						String fileName = "test" + i + "_" + j;
						String outputLine = "mean_field_" + mean_field + "_encoding_scores_" + i + "_" + j + ".txt";
						runOneProteinMatrix(fileName, outputLine, 0, 0, mean_field, 100);
						System.out.println(mean_field + i + j + "done");
					//}
				}
			}
			mean_field = 0;
			for (int i = 1; i < 3; i++) {
				for (int j = 0; j < 8; j++) {
					//if (!((i == 1) && (j == 0)) && !((i==1) && (j==1))) {
						String fileName = "test" + i + "_" + j;
						String outputLine = "mean_field_" + mean_field + "_encoding_scores_" + i + "_" + j + ".txt";
						runOneProteinMatrix(fileName, outputLine, 0, 0, mean_field, 100);
						System.out.println(mean_field + i + j + "done");

					//}
				}
			}
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
		else if (selection.equals("12")) {
			File read_this = new File("encoding_scores_1_1.txt");
			float max = 0;
			try(BufferedReader br2 = new BufferedReader(new FileReader(read_this))) {
			    for(String line2; (line2 = br2.readLine()) != null; ) {
			    	String[] temp = line2.split(",");
			    	float temp_score = Float.parseFloat(temp[1]);
			    	
			    	if (temp_score > max) {
			    		max = temp_score;
			    	}
			    }
			}
			System.out.println(max);
		}
		else if (selection.equals("13")) {
			String fileName = "C:/Users/yangb/workspace/Protein/gammatest.txt";
			
			ProteinMatrix result = new ProteinMatrix(fileName, true);
			
			//String encoding = "22222222222";
			
			result.sigmaval[0] = 2;
			result.sigmaval[1] = 2;
			result.sigmaval[2] = 2;
			result.sigmaval[3] = 2;
			result.sigmaval[4] = 2;
			result.sigmaval[5] = 2;
			result.sigmaval[6] = 2;
			result.sigmaval[7] = 2;
			result.sigmaval[8] = 2;
			result.sigmaval[9] = 1;
			result.sigmaval[10] = 1;
			result.sigmaval[11] = 1;
			result.sigmaval[12] = 1;
			
			result.generateMu();
			
			System.out.println(Arrays.toString(result.muval.toArray()));
			System.out.println(result.size);
		}
	}
	
	public static boolean populate(String encoding, ProteinMatrix result) {
		int tempX = result.size;
		int tempY = result.size;
		int valueCounter = 0;
		
		Coor tempCoor = new Coor(tempX, tempY);
		
		Coor tempCoor1 = new Coor(tempCoor.xCoor, tempCoor.yCoor);
		result.coordinates.add(tempCoor1);
		result.matrix[tempCoor.xCoor][tempCoor.yCoor] = result.muval.get(valueCounter);
		//System.out.println(result.matrix[tempCoor.xCoor][tempCoor.yCoor]);
		//System.out.println(tempCoor.xCoor + ", " + tempCoor.yCoor);
		
		tempCoor.goRight();
		valueCounter++;
		Coor tempCoor2 = new Coor(tempCoor.xCoor, tempCoor.yCoor);
		result.coordinates.add(tempCoor2);
		result.matrix[tempCoor.xCoor][tempCoor.yCoor] = result.muval.get(valueCounter);
		//System.out.println(result.matrix[tempCoor.xCoor][tempCoor.yCoor]);
		//System.out.println(tempCoor.xCoor + ", " + tempCoor.yCoor);
		
		valueCounter++;
		int pastMove = 2;
		int encodingCounter = 0;
		
		for (; valueCounter < result.muval.size(); valueCounter++) {
			int currEncodeValue = Character.getNumericValue(encoding.charAt(encodingCounter));
			pastMove = tempCoor.nextMove(pastMove, currEncodeValue);
			if (result.matrix[tempCoor.xCoor][tempCoor.yCoor] != 0) {
				//printProteinMatrix(result);
				return false;
			}
			result.matrix[tempCoor.xCoor][tempCoor.yCoor] = result.muval.get(valueCounter);
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
	
	public static void runOneProteinMatrix(String fileName, String outputLine, float temperature, float entropy, float mean_field, int num_divisions) throws IOException {
		File encodingScores = new File(outputLine);
		encodingScores.createNewFile();
		FileWriter writer = new FileWriter(encodingScores); 
		
		String nufileName = "C:/Users/yangb/workspace/Protein/test_files/" + fileName + ".txt";
		
		FileReader fr =  new FileReader(nufileName);
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
				current.score = current.calculate(mean_field, entropy, temperature);
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
				//System.out.println("the current encoding was added successfully to the top 10 list");
			}
			//TimeUnit.SECONDS.sleep(1);
		}
		
		/*
		for (int k = 0; k < top10List.size(); k++) {
			top10List.get(k).printProteinMatrix();
		}
		*/
		
		//System.out.println("\nmax score is: " + top10List.get(0).score + "\n");
		
		writer.close();
		
		float max = top10List.get(0).score;
		
		num_divisions = 135;
		max = 6000*135;
		
    	float quotient = (float) max / num_divisions;
		
		int[] aggregateScores = new int[num_divisions];
		
		File read_this = new File(outputLine);
		
		try(BufferedReader br2 = new BufferedReader(new FileReader(read_this))) {
		    for(String line2; (line2 = br2.readLine()) != null; ) {
		    	String[] temp = line2.split(",");
		    	float temp_score = Float.parseFloat(temp[1]);
		    	
		    	float curr = quotient;
		    	int aggregate_slot = 0;
		    	
		    	if(temp_score == max) {
		    		aggregate_slot = 99;
		    	}
		    	else {
			    	while (curr < temp_score) {
			    		curr += quotient;
			    		aggregate_slot++;
			    	}
		    	}
		    	
		    	aggregateScores[aggregate_slot]++;
		    }
		}
		
		File output_aggregate = new File("mean_field_" + mean_field + "_output_aggregate_" + fileName + ".csv");
		
		FileWriter aggregate_writer = new FileWriter(output_aggregate);
		
		aggregate_writer.write("minimum score,number of encodings\n");
		aggregate_writer.flush();
		
		for (int i = 0; i < num_divisions; i++) {
			aggregate_writer.write((i*quotient) + "," + aggregateScores[i] + "\n");
		}
		
		aggregate_writer.close();
		
		//read_this.delete();
	}
}