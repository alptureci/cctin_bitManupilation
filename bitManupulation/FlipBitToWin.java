package bitManupulation;

import java.util.ArrayList;

public class FlipBitToWin {

	public static void main(String[] args) {
		// TODO Auto-generated method stub		
		
		/**BRUTE FORCE approach: 
		time complexity O(b)
		space complexity O(b)
		b=length of the sequence
		This is pretty good. It's O(b) time and O(b) memory, where b is the length of the sequence.		
		*/
		System.out.println(longestSequence(1775));
		
		/*
		 * CAN WE DO BETTER??
		 * Recall the concept of Best Conceivable Runtime. 
		 * The B.C.R. for this algorithm is O(b) (since we'll always have to read through the sequence), so we know we can't optimize the time.
		 * We can however, reduce the memory usage.
		 */
		
		/**
		 * OPTIMAL ALGORITHM
		 * starting with line 110.
		 * time complexity O(b)
		 * space complexity O(1)
		 * 
		 * The runtime of this algorithm is still O(b), but we use only O(1) additional memory.
		 */
		System.out.println(flipBit(1775));
		
	}
	/**
	 * 5.3 Flip Bit to Win
	 * You have an integer and you can flip exactly one bit from a 0 to a 1. 
	 * Write Code to find the length of the longest sequence of 1's you could create.
	 * 
	 * Example:
	 * Input 1775 --> (110_11101111)
	 * Outout = 8 --> (110_11111111)
	 * 
	 * SOLUTION
	 * We can think about each integer as being an alternating sequence if 0s and 1s.
	 * Whenever a 0s sequence has length one, we can potentially merge the adjacent 1s sequences.
	 * 
	 * BRUTE FORCE Solution
	 * One approach is to convert an integer to an array that reflects the lengths of the 0s and 1s sequences.
	 * For example; 11011101111 would be (reading from right to left) [0->0, 4->1, 1->0, 3->1, 1->0, 2->1, 21->0].
	 * The subscript reflects whether the integer corresponds to a 0s sequence or a 1s sequence, but the actual solution doesn't need this.
	 * It's a alternating sequence, always starting with the 0s sequence.
	 * 
	 * Once we have this; we just walk through the array. 
	 * At each 0s sequence, then we consider merging the adjacent 1s sequences if the 0s sequence has length 1.
	 */
	static int longestSequence(int n){
		if(n==-1)
			return Integer.BYTES*8;
		ArrayList<Integer> sequences = getAlternatingSequences(n);
		return findLongestSequence(sequences);
	}
	
	/*
	 * Return a list of the sizes of the sequences. 
	 * The sequence starts off with the number of 0s (which might be 0) and then alternates with the counts of each value.
	 * */
	static ArrayList<Integer> getAlternatingSequences(int n){
		ArrayList<Integer> sequences = new ArrayList<Integer>();
		
		int searchingFor = 0;
		int counter = 0;
		
		for(int i = 0; i < Integer.BYTES * 8 ; i++){
			if((n & 1) != searchingFor){
				sequences.add(counter);
				searchingFor = n & 1; // flip 1 to 0 or 0 to 1
				counter = 0;
			}
			counter++;
			n >>>= 1;
		}
		sequences.add(counter);
		return sequences;
	}
	
	/*
	 * Given the lengths of alternating sequences of 0s and 1s, find the longest one we can build. 
	 * */
	static int findLongestSequence(ArrayList<Integer> seq){
		int maxSeq = 1;
		
		for (int i = 0; i < seq.size(); i+=2){
			int zerosSeq = seq.get(i);
			int onesSeqRight = i - 1 >= 0 ? seq.get(i-1) : 0;
			int onesSeqLeft = i + 1 < seq.size() ? seq.get(i+1) : 0;
			
			int thisSeq = 0;
			if (zerosSeq == 1){ //can merge
				thisSeq = onesSeqLeft + 1 + onesSeqRight;
			}
			if (zerosSeq > 1){ // just add a zero to either side
				thisSeq = 1 + Math.max(onesSeqRight, onesSeqLeft);
			}
			else if (zerosSeq == 0) { //No zero; but take either side
				thisSeq = Math.max(onesSeqRight, onesSeqLeft);
			}
			maxSeq = Math.max(thisSeq, maxSeq);			
		}
		return maxSeq;
	}
	
	/**
	 * OPTIMAL ALGORITHM
	 * To reduce the space usage, note that we don't need to hang on to the length of each sequence the entire time.
	 * We only need it long enough to compare each 1s sequence to the immediately preceding 1s sequence.
	 * 
	 * Therefore, we can just walk through the integer doing this, tracking the current 1s sequence length and the previous 1s sequence length.
	 * When we see a zero, update previous length:
	 * 
	 * -> If the next bit is a 1, previousLength should be set to the currentLength.
	 * -> If the next bit is a 0, then we can't merge these sequences together. So, set previousLength to 0.
	 * 
	 * Update maxLength as we go. 
	 */
	static int flipBit(int a){
		if (~a == 0)
			return Integer.BYTES * 8;
		
		int currentLength = 0;
		int previousLength = 0;
		int maxLength = 1; // We can always have a sequence of at least one 1.
		while (a != 0){
			if ((a & 1) == 1){ // Current bit is a 1.
				currentLength++;
			}
			else if ((a & 1) == 0){ //Current bit is a 0.
				/*
				 * Update to 0 (if next bit is 0) OR update to currentLength (if next bit is 1).
				 */
				previousLength = (a & 2) == 0 ? 0 : currentLength;
				currentLength = 0;
			}
			maxLength = Math.max(previousLength + currentLength + 1, maxLength);
			a >>>= 1;
		}
		return maxLength;
	}

}
