package driver;
public class Coor {
		int xCoor;
		int yCoor;
		
		public Coor(int x, int y) {
			xCoor = x;
			yCoor = y;
		}
		
		public int nextMove(int pastMove, int currEncodeValue) {
			switch (pastMove) {
				case 0: {
					if (currEncodeValue == 0) {
						goDownLeft();
						return 4;
					}
					else if (currEncodeValue == 1) {
						goLeft();
						return 5;
					}
					else if (currEncodeValue == 2) {
						goUpLeft();
						return 0;
					}
					else if (currEncodeValue == 3) {
						goUpRight();
						return 1;
					}
					else if (currEncodeValue == 4) {
						goRight();
						return 2;
					}
					break;
				}
				case 1: {
					if (currEncodeValue == 0) {
						goLeft();
						return 5;
					}
					else if (currEncodeValue == 1) {
						goUpLeft();
						return 0;
					}
					else if (currEncodeValue == 2) {
						goUpRight();
						return 1;
					}
					else if (currEncodeValue == 3) {
						goRight();
						return 2;
					}
					else if (currEncodeValue == 4) {
						goDownRight();
						return 3;
					}
					break;
				}
				case 2: {
					if (currEncodeValue == 0) {
						goUpLeft();
						return 0;
					}
					else if (currEncodeValue == 1) {
						goUpRight();
						return 1;
					}
					else if (currEncodeValue == 2) {
						goRight();
						return 2;
					}
					else if (currEncodeValue == 3) {
						goDownRight();
						return 3;
					}
					else if (currEncodeValue == 4) {
						goDownLeft();
						return 4;
					}
					break;
				}
				case 3: {
					if (currEncodeValue == 0) {
						goUpRight();
						return 1;
					}
					else if (currEncodeValue == 1) {
						goRight();
						return 2;
					}
					else if (currEncodeValue == 2) {
						goDownRight();
						return 3;
					}
					else if (currEncodeValue == 3) {
						goDownLeft();
						return 4;
					}
					else if (currEncodeValue == 4) {
						goLeft();
						return 5;
					}
					break;
				}
				case 4: {
					if (currEncodeValue == 0) {
						goRight();
						return 2;
					}
					else if (currEncodeValue == 1) {
						goDownRight();
						return 3;
					}
					else if (currEncodeValue == 2) {
						goDownLeft();
						return 4;
					}
					else if (currEncodeValue == 3) {
						goLeft();
						return 5;
					}
					else if (currEncodeValue == 4) {
						goUpLeft();
						return 0;
					}
					break;
				}
				case 5: {
					if (currEncodeValue == 0) {
						goDownRight();
						return 3;
					}
					else if (currEncodeValue == 1) {
						goDownLeft();
						return 4;
					}
					else if (currEncodeValue == 2) {
						goLeft();
						return 5;
					}
					else if (currEncodeValue == 3) {
						goUpLeft();
						return 0;
					}
					else if (currEncodeValue == 4) {
						goUpRight();
						return 1;
					}
					break;
				}
				default: System.out.println("invalid past move");
						break; 
			}
			return -1;
		}
		
		public void goUpLeft() {
			yCoor--;
		}
		
		public void goUpRight() {
			xCoor++;
			yCoor--;
		}
		
		public void goLeft() {
			xCoor--;
		}
		
		public void goRight() {
			xCoor++;
		}
		
		public void goDownLeft() {
			xCoor--;
			yCoor++;
		}
		
		public void goDownRight() {
			yCoor++;
		}
}