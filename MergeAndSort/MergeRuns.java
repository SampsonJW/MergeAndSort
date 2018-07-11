//Name: Sampson John Ward
//ID: sjw91

//Name: Andy Shen
//ID: as382


import java.io.*;
import java.util.*;

class MergeRuns{
	
	static int count = 1;
	static PrintWriter writer;
	static BufferedReader[] bufArray;
	static PrintWriter[] pwArray;
	static int reduce = 1;
	static int kCount;
	static int numRuns;
	static int parses;
	static Tracker[] heapArray;
	static boolean donzo;
	static String file;
	static String sortedFile;
	public static void main(String[] args) {


		/*--------------------------------------*/
		//CHECKS COMMAND LINE FOR CORRECT PARAMS
		/*--------------------------------------*/
		try{

			if (args.length != 2) {
				System.err.println("Invalid Args, use: 'Length' 'text.txt'");
			}

			Integer.parseInt(args[0]);
		}catch(NumberFormatException e){ 
			System.err.println("Please use integer as length");
			return;
		}
		/*--------------------------------------*/
		//END OF COMMAND LINE ARGUEMENT CHECKS
		/*--------------------------------------*/
		kCount = Integer.parseInt(args[0]);
		file = args[1];
		sortedFile = file.replace(".runs", ".sorted");
		heapArray = new Tracker[Integer.parseInt(args[0]) + 1];
		
		try{
			//our merge run file
			writer = new PrintWriter(new FileOutputStream(sortedFile, false));
			//Arrays of readers and writers
			bufArray = new BufferedReader[kCount];
			pwArray = new PrintWriter[kCount];
			//make k many print writer and buffered readers

			DistRuns(file);
			//change filename to proper one
			Merge(kCount);
			
		}
		catch(Exception e){
			System.out.println(e);
		}
		
	}
	/*#########################################*/
	/*#########################################*/
	/*	DISTRUBUTE RUNS TO TMP FILES	   */
	/*#########################################*/
	/*#########################################*/
	public static void DistRuns(String fileName){
		try{
			for(int i = 0; i < kCount; i++){
				String tmpName = "tmp" + i + ".txt";

				pwArray[i] = new PrintWriter(new FileOutputStream(tmpName, false));
				bufArray[i] = new BufferedReader(new FileReader(tmpName));

			}
			int next = 0;
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String s = reader.readLine();
			//Reads in from the run file 
			while(s !=null){
				//if the we have written to the last tmp file loop back to the first tmp file
				if(next == kCount){next = 0;}
				//when the reader reads in a delim character it is the end of a run
				if(s.equals("~(*v*)~")){
					numRuns++;
					//write a delim character to the tmp file
					pwArray[next].println("~(*v*)~");
					pwArray[next].flush();
					//changes the tmp file
					next ++;
					s = reader.readLine();
				}
				else{
					//if its not the end of the run keep on reading a new line to pwArray[next]
					pwArray[next].println(s);
					pwArray[next].flush();
					s = reader.readLine();
				}

			}
			if(numRuns == 1){

				//When merge is finished delete tmp files
				for(int i = 0; i<kCount; i++){
					File f = new File("tmp"+i+".txt");
					f.delete();
				}
				donzo = true;

			}

		}catch(Exception e){
			System.out.println(e);
		}
	}

	/*#########################################*/
	/*#########################################*/
	/*   METHOD TO MERGE RUNS CALLS REPLACE    */
	/*#########################################*/
	/*#########################################*/
	public static void Merge(int kCount){
		String delim = "~(*v*)~";
		String line;

		Tracker swap;
		int runDone;
		int rootFile;//keeps a track of which temp file the root node came from
        boolean done[] = new boolean[kCount];
		try{

				// read a line from each tmp file and put it in the heap
				for(int i = 0; i < kCount; i++){

					line = bufArray[i].readLine();

					if(heapArray.length != count) {

						Insert(i, line);

					}
				}
            rootFile = heapArray[1].getFileNum();
            line = bufArray[rootFile].readLine();
            while(true){
                // reads in from the root element and which temp file is came from

                Replace(rootFile, line);

                if(heapArray.length - reduce == 0){



		    //if there are no more runs to merge 
                    if(numRuns==0){
					parses ++;
						//if it's all sorted 
						if(donzo==true){
							System.err.println("Amount of passes: " + parses);
							return;
						}
						writer.println("~(*v*)~");
						writer.flush();
						reduce=1;
                    	DistRuns(sortedFile);


						writer = new PrintWriter(new FileOutputStream(sortedFile, false));

					}
					else{
						writer.println("~(*v*)~");
						writer.flush();
						reduce=1;
					}

		    //Clears the heap
                    for(int j = 1; j <=kCount; j++){
                        heapArray[j] = null;
                      }
                      count = 1;
		   //Reads from runs and if it is null then reduce size of heap
                   for(int n = 0; n <kCount; n++){

                        line = bufArray[n].readLine();
                        if(line == null){

                            runDone = kCount - n;
                            if(runDone == 0){
								System.out.println(" comon");
							}

                            reduce+=runDone;

                            break;



                        }else {

                            Insert(n, line);
                        }


                    }

                }

                //keeps checking what file the root node came from
                rootFile = heapArray[1].getFileNum();
                //reads in the from that file
                line = bufArray[rootFile].readLine();


            }

			
		}catch(Exception e){

		}
		
	}


	/*#########################################*/
	/*#########################################*/
	/*	THIS IS THE INSERT METHOD	   */
	/*#########################################*/
	/*#########################################*/
	public static void Insert(int fileNum, String line){

		int parent;
		int temp;
		Tracker swap;

		//if array is empty
		if(heapArray[1]==null){

			heapArray[1]= new Tracker(fileNum, line);
			count++;
			return;
		}

		heapArray[count] = new Tracker(fileNum, line);
		temp = count;

		count++;
			//if the current node is smaller than its parent swap wth it
		while(heapArray[temp].getLine().compareTo(heapArray[temp/2].getLine())<0){

			parent = temp/2;
			swap = heapArray[parent];
			heapArray[parent] = heapArray[temp];
			heapArray[temp] = swap;
			temp = parent;

			//if the current node is head of heap break
			if(temp == 1){
				break;
			}
		}
		


	}

	/*#########################################*/
	/*#########################################*/
	/*	THIS IS THE REPLACE METHOD	   */
	/*#########################################*/
	/*#########################################*/
	public static void Replace(int fileNum, String line){

		int index = 1;
		Tracker swap;
		int smaller;

		try{

		
            if(line.equals("~(*v*)~")){

				numRuns--;
                writer.println(heapArray[1].getLine());
                writer.flush();
                swap = heapArray[1];
                heapArray[1] = heapArray[heapArray.length-reduce];
                heapArray[heapArray.length-reduce] = swap;


                reduce++;

            }else {

                writer.println(heapArray[1].getLine());
                writer.flush();
                //replace the root node with new line
                heapArray[1] = new Tracker(fileNum, line);
            }



            //down heap the heap
            DownHeap(index);


		 }catch(Exception e){
		}
		
	}

	/*#########################################*/
	/*#########################################*/
	/*		DOWNHEAP METHOD		   */
	/*#########################################*/
	/*#########################################*/

	
	public static void DownHeap(int index){
			try{
			Tracker curr;
			int isSmaller;


			while(heapArray[index].getLine().compareTo(heapArray[Smaller(index)].getLine())>0 && Smaller(index)<=heapArray.length-reduce) {

					isSmaller = Smaller(index);


					curr = heapArray[index];
				
					heapArray[index] = heapArray[isSmaller];
				
					heapArray[isSmaller] = curr;
				
					index = isSmaller;
				
				}

		}catch(Exception e){
	}
	

	}

	/*#########################################*/
	/*#########################################*/
	/*		SMALLER METHOD		   */
	/*#########################################*/
	/*#########################################*/
	//returns the smaller of the two children 
	public static int Smaller(int index){

		int smaller = 0;

			if(index * 2 + 1 <= heapArray.length - reduce  ){

				if (heapArray[index * 2].getLine().compareTo(heapArray[index * 2 + 1].getLine()) <= 0){

				 	smaller = index * 2;

				}
				else if(heapArray[index * 2].getLine().compareTo(heapArray[index * 2 + 1].getLine()) > 0){

					smaller = index * 2 +1 ;
					
				}
				if(smaller == index * 2 +1 && smaller > heapArray.length-reduce && index*2<= heapArray.length - reduce){

					smaller = index * 2;
				}

			}
			else if(index * 2 <= heapArray.length - reduce){
				smaller = index * 2;
			}

		
		return smaller;
		
	}
	/*#########################################*/
	/*#########################################*/
	/*	UPHEAP METHOD TO SORT HEAP	   */
	/*#########################################*/
	/*#########################################*/

	public static void Heapify(){
	
	int parent = (heapArray.length)/2;
	
		try{
			for(int i = parent; i>=1; i--){ 
			
				DownHeap(i);

			}
		
		}catch(Exception e){

		}
				
	}

	/*#########################################*/
	/*#########################################*/
	/*	CLASS TO TRACK FILENAME AND LINE   */
	/*#########################################*/
	/*#########################################*/
	
	private static class Tracker{

		private int fileNum_;
		private String line_;
		//constructor for the line and file number
		public Tracker(int fileNum, String line){

			fileNum_ = fileNum;
			line_ = line;

		}
		//returns file number and line
		public int getFileNum(){
			return fileNum_;
		}

		public String getLine(){
			return line_;
		}

	}
	
}
