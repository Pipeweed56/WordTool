package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class LearningTool {
	static HashMap<String, Word> wordTable;
	static PriorityQueue<Word> wordQueue;
	
	public static void main(String[] args) throws FileNotFoundException
	{
		initialize();
	}
	
	static void initialize() throws FileNotFoundException
	{
		wordTable = new HashMap<String, Word>();
		wordQueue = new PriorityQueue<Word>(new WordComp());
		
		URL url = LearningTool.class.getResource("../resource/ToolDict.txt");
		if(url!=null) {
			File file = new File(url.getFile());
			Scanner sc = new Scanner(file); 
//			sc.useDelimiter("@");
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] tokens = line.split("@");
				String pWrite = tokens[0];
				int pMaster = Integer.valueOf(tokens[1]);
				String[] temp = tokens[2].split("#");
				String[][] pMean = new String[temp.length/2][2];
				for(int n=0; n<temp.length/2; n++) {
					pMean[n][0] = temp[n*2];
					pMean[n][1] = temp[n*2+1];
				}
				
				Word word1 = new Word(pWrite, pMaster, pMean);
				for(int n=4; n<tokens.length; n++) {
					if(tokens[n].length()>0)
						for(String term : tokens[n].split("#")) {
//							System.out.println(term);
//							System.out.println("-------------");
							word1.connections[n-4].add(term);
						}
				}
				wordTable.put(word1.writing, word1);
				wordQueue.add(word1);
			}
			sc.close();
		}
		
		
		
	}
	
	static void readData()
	{
		
	}
	
	static private class Word
	{
		String writing;
		ArrayList<String[]> meanings;
		int masterLevel;
		// arcs: 
		// 0 - structural connection
		// 1 - similar writing
		// 2 - similar meaning
		// 3 - similar spelling
		ArrayList<String>[] connections;
		
		@SuppressWarnings("unchecked")
		public Word(String pWrite, int pMaster, String[][] pMean)
		{
			writing = pWrite;
			meanings = new ArrayList<String[]>();
			masterLevel = pMaster;
			connections = new ArrayList[4];
			for(int n=0; n<4; n++)
				connections[n] = new ArrayList<String>();
			meanings.addAll(Arrays.asList(pMean));
		}
	}
	
	static private class WordComp implements Comparator<Word>
	{
		public int compare(Word w1, Word w2)
		{
			return w1.masterLevel-w2.masterLevel;
		}
	}

}
