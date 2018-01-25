package driver;

import java.util.Comparator;

public class ProteinMatrixComparator implements Comparator<ProteinMatrix> {
	public int compare(ProteinMatrix p1, ProteinMatrix p2) {
		int retVal;
		if (p1.score < p2.score) {
			retVal = 1;
		}
		else if (p1.score > p2.score) {
			retVal = -1;
		}
		else {
			retVal = 0;
		}
		return retVal;
	}
}