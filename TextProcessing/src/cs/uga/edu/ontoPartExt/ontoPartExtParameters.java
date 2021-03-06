package cs.uga.edu.ontoPartExt;

/**
 * 
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author mehdi
 *
 * updated for ontoPart
 *
 */
public class ontoPartExtParameters {
	
	public int fileNumOfLines = 150000;
	public int [] w = null;
	public int [] d = null;
	public int [] docIds = null;
	public int [] wordIds = null;
	public int [] wordsCounts = null;
	public int D = 0; //numberOfEntity /Documents
	public int W = 0; //sizeOfVocabulary(Object)
	public int T = 0; //number of topics
	public int T1 = 57;
	public int T2 = 133;
	public int P = 30; 
	public int C = 0;//Number of all classes 
	public int N = 0; //numberOfAllObjects(Words)
	public int nIterations = 1000;
	int burnIn = 200;
	public double ALPHA = 0.1;//50.0 / 20; //Alpha=50/K  K is the number of topics
	public final double BETA  = 0.01;
	public final double ZETA  = 0.01;
//	public final double TAU = 50.0 / T;
//	public final double EPSILON = 0.9;
	public final double gamma = 0.01	;
	public String corpusFilename = "/home/mehdi/ontoPartExt/evaluation/corpus.txt";
	public String entitiesFilename = "/home/mehdi/ontoPartExt/evaluation/entNameOnly.txt";
	public String corpusStatFilename = "/home/mehdi/ontoPartExt/evaluation/corpusStatistics.txt";
//	public String predicateFilename = "/home/mehdi/ontoPart/evaluation/predicateToId.txt";
	public String classFilename = "/home/mehdi/ontoPartExt/evaluation/classListandID.txt";

//	public String corpusFilename = "/home/mehdi/taxonomyProject/preprocessedFiles/corpus.txt";
//	public String entitiesFilename = "/home/mehdi/taxonomyProject/preprocessedFiles/corpusConceptsSr.txt";
//	public String corpusStatFilename = "/home/mehdi/taxonomyProject/preprocessedFiles/corpusStatistics.txt";
	
	public ontoPartExtParameters() {
		initializeParameters();
		fillArrays();
	}
	
	public void initializeParameters() {
		System.out.print("Reading corous file...");
		List<String> corpus = readFile(corpusFilename);
		System.out.println("done!");
		docIds	= new int [corpus.size()];
		wordIds = new int [corpus.size()];
		wordsCounts = new int [corpus.size()];
		Set<Integer> uniqueWordIds = new HashSet<Integer>();
		Set<Integer> uniqueDocIds = new HashSet<Integer>();
		Set<Integer> classIds = new HashSet<Integer>(); //Set of classIds
		
		
		System.out.println("corpos size:"+corpus.size());
		
		for (int i = 0;i < corpus.size();i++) {
			String [] tokens = corpus.get(i).split(" ");
			docIds [i] = Integer.parseInt(tokens [0]);
			wordIds [i] = Integer.parseInt(tokens [1]);
			wordsCounts [i] = Integer.parseInt(tokens [2]);
			uniqueDocIds.add(docIds [i]);
			uniqueWordIds.add(wordIds [i]);
			N += wordsCounts [i];
		} // end of for
//		for (int i: td){
//			if (!uniqueDocIds.contains(i)) System.out.println(i);
//		}

		D = uniqueDocIds.size() ;
		W = uniqueWordIds.size();
		System.out.println("D:"+D +"         W:"+W);
		
		//Counting number of classes C, reading text file and extract number of classes. 

		BufferedReader reader=null;
		try {
			reader = new BufferedReader(new FileReader(classFilename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			while (reader.readLine() != null) {
				classIds.add(C); //Filling classIds Set. 
				C++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Number of Classes =" + C);
		
		
		
		
		BufferedWriter bw1 = null;
		try {
			bw1 = new BufferedWriter(new FileWriter(corpusStatFilename));
			bw1.write(  "numberOfEntity, sizeOfVocabulary(Object), numberOfAllObjects(Words), number of classes");
			bw1.newLine();
			bw1.write(  D + " " + W + " " + N + " " + C);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bw1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("D: " + D + " W: " + W + " N: " + N + " C: " + C);

		
	} // end of initializeParameters
	
	public void fillArrays() {
		System.out.print("Filling arrays...");
//		long st = System.currentTimeMillis();
		d = new int [N];   // matrix holding all the documents of words
		w = new int [N];   // matrix keeping all the words of all the documents
		int count = 0;
		for (int j = 0; j < wordsCounts.length; j++) {
			for (int i = count; i < count + wordsCounts[j]; i++) {
				d [i] = docIds [j];
				w [i] = wordIds [j];
				//System.out.println(i);
			} // end of for i
			count += wordsCounts[j];
//			System.out.println(count);
		} // end of for j
//		double dt = (System.currentTimeMillis() - st);// / 1000.;
//		System.out.println("time: " + dt);
		System.out.println("done!");
		System.out.println("doc!" + d.length);
		System.out.println("w!" + w.length);
		
	} // end of fillArrays
	
	public List<String> readFile(String filename) {
		List<String> corpus = new ArrayList<String>(fileNumOfLines);
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = "";
			while ((line = br.readLine()) != null) {
			//	System.out.println(line);
				corpus.add(line);
			} // end of while
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return corpus;
	} // end of readFile

}
