//COMP317
//ASSIGNMENT 1
//ANDY SHEN, ID: AS392
//SAMPSON JOHN WARD, ID: SJW91

import java.io.*;

class MakeRuns {


	static int count = 1;
	static PrintWriter writer;
	static String size;
	static String[] heapArray;
	static String fileName;
	static int reduce = 1;
	static int numRuns;
	public static void main(String[] args) {

		/*########################*/
		/*COMMAND LINE ARGS CHECKS*/
		/*########################*/
		try{

			if (args.length != 2) {
				System.err.println("Invalid Args, use: 'Length' 'text.txt'");
			}

  			Integer.parseInt(args[0]);
		}catch(NumberFormatException e){
			System.err.println("Please use integer as length");
			return;
		}
		/*##########################*/
		/*END OF COMMAND LINE CHECKS*/
		/*##########################*/
		
		fileName = new String(args[1]);			//MAKE FILENAME EQUAL THE CMD ARGS							
		fileName = fileName.replace(".txt", ".runs");	//REPLACE .TXT WITH .RUNS  

		heapArray = new String[Integer.parseInt(args[0]) + 1];		//MAKE HEAP EQUAL TO CMD ARGS -1 AS WE START HEAP AT INDEX 1

		try {	

			writer = new PrintWriter(new FileOutputStream(fileName, false));	//NEW PRINTWRITER WHICH IS APPENDED THE FILENAME AND CAN BE OVERWRITTEN 
			BufferedReader br = new BufferedReader(new FileReader(args[1]));	//BUFFERED READER WHICH IS APPENDED TO THE .TXT FILE SPECIFIED IN CMD LINE

			String s = br.readLine();		
			MakeRuns runs = new MakeRuns();		//MAKING INSTANCE OF OUR MAKERUNS AS OUR METHODS ARE NOT STATIC


			//WHILE NOT FINISHED READING FILE 
			while(s !=null) {


				if(count != heapArray.length) {
					runs.insert(s);
				}
				else {
					runs.Replace(s);

				}
				s = br.readLine();

			}
			
			//FINAL HEAP
			if(s == null){
				
				int aLength = heapArray.length;
				String swap;
				br.close();
				
				//PRINT DELIMETER WHEN RUN IS FINISHED
				if(reduce != 1){

					writer.println("~(*v*)~");

				}

				numRuns++;
				System.err.println("Number of runs = " + (numRuns + 1));
				reduce = 1;
				runs.Heapify();

				//WHILE HEAP IS NOT REDUCED TO ONLY HEAD
				while(aLength > 1){
					
					writer.println(heapArray[1]);
					writer.flush();

					swap = heapArray[1];
					heapArray[1] = heapArray[heapArray.length - reduce];
					heapArray[heapArray.length - 1] = swap;

					reduce ++;
					runs.Heapify();
					
					aLength--;
				}
			writer.println("~(*v*)~");
			writer.flush();

			}

		}catch (Exception e){
			
		}
	}


	public void insert(String line){
		int parent;
		int temp;
		String swap;

		//IF ARRAY IS EMPTY
		if(heapArray[1]==null){

			heapArray[1]=line;
			count++;
			return;
		}

		heapArray[count] = line;
		temp = count;

		count++;


		//SWAP IF CHILD IS SMALLER THAN PARENT
		while(heapArray[temp].compareTo(heapArray[temp/2])<0){

			parent = temp/2;
			swap = heapArray[parent];
			heapArray[parent] = heapArray[temp];
			heapArray[temp] = swap;
			temp = parent;

			//BREAK IF CURRENT NODE IS HEAD
			if(temp == 1){
				break;
			}
		}		
	}

	
	public void Replace(String line){

		int index = 1;
		String swap;
		int smaller;
		
		
		 
		try{
			//IF LINE IS GREATER THAN OR EQUAL TO HEAD
			if(line.compareTo(heapArray[1])>=0){

				//FLUSH HEAD
				writer.println(heapArray[1]);
				writer.flush();

				//HEAD EQUALS NEW LINE
				heapArray[1] = line;
				//DOWNHEAP
				DownHeap(index);
			}
			//ELSE IF LINE IS SMALLER
			else if(line.compareTo(heapArray[1])<0){

				//FLUSH HEAD
				writer.println(heapArray[1]);
				writer.flush();

				//NEW LINE IS HEAD
				heapArray[1] = line;

				//SWAP HEAD WITH LOWEST CHILD WHICH IS IN BOUNDS
				swap = heapArray[1];
				heapArray[1] = heapArray[heapArray.length-reduce];
				heapArray[heapArray.length-reduce] = swap;
				
				//REDUCE AND DOWNHEAP
				reduce++;
				DownHeap(index);
					
				//IF REDUCED TO TOP OF HEAP MAKE NEW RUN
				if(heapArray.length - reduce == 0){
					numRuns++;
					writer.println("~(*v*)~");
					writer.flush();
					reduce = 1;
					Heapify();
					
				}
				
			}
				
								
		 }catch(Exception e){}
		
	}

		public void DownHeap(int index){
		
			try{
			String curr;
			int isSmaller;

			//WHILE CURRENT IS BIGGER THAN IT'S SMALLER CHILD AND SMALLER EXISTS E.G NOT OUT OF BOUNDS
			while(heapArray[index].compareTo(heapArray[Smaller(index)])>0 && Smaller(index)<=heapArray.length-reduce){
				 	
					//DOWNHEAP
					isSmaller = Smaller(index);
	
					curr = heapArray[index];
				
					heapArray[index] = heapArray[isSmaller];
				
					heapArray[isSmaller] = curr;
				
					index = isSmaller;
				
				}

		}catch(Exception e){}

	}

	public int Smaller(int index){

		int smaller = 0;
			//IF RIGHT CHILD EXISTS
			if(index * 2 + 1 <= heapArray.length - 1){
				//IF LEFT CHILD IS LESS THAN OR EQUAL TO RIGHT CHILD
				if (heapArray[index * 2].compareTo(heapArray[index * 2 + 1]) <= 0){
					//SMALLER CHILD IS ON THE LEFT
				 	smaller = index * 2;

				}
				//ELSE IF LEFT CHILD IS GREATER THAN RIGHT
				else if(heapArray[index * 2].compareTo(heapArray[index * 2 + 1]) > 0){
					//SMALLER IS ON THE RIGHT
					smaller = index * 2 +1 ;
					
				}
				//IF SMALLER IS THE RIGHT CHILD AND IS CUT OFF FROM REDUCE AND LEFT INDEX IS INSIDE BOUNDS 
				if(smaller == index * 2 +1 && smaller > heapArray.length-reduce && index * 2 <= heapArray.length - reduce){
					//SMALLER IS LEFT CHILD
					smaller = index * 2;
				}

			}
			//ELSE IF LEFT EXISTS AND RIGHT DOES NOT 
			else if(index * 2 <= heapArray.length - 1){
				//THEN SMALLER IS ON THE LEFT
				smaller = index * 2;
			}

		return smaller;
	
	}


	public void Heapify(){
	
	
	int parent = (heapArray.length)/2;
	
	try{
		//FOR EACH PARENT STARTING AT THE BOTTOM
		for(int i = parent; i >= 1; i--){ 
			//DOWNHEAP WITH THAT PARENT
			DownHeap(i);

		}
		
	}
	catch(Exception e){}
			
	}
}
