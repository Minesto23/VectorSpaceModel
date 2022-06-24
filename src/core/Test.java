package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Test {

	public static void main(String[] args) {
		
		// Reading File information
		String[] data1 = readFile("WordCount1.txt").split(" ");
		String[] data2 = readFile("WordCount2.txt").split(" ");
		String[] data3 = readFile("WordCount3.txt").split(" ");
		
		
		// merge all the data

		ArrayList <String> data = new ArrayList<String>();
		
		addData(data, data1);
		addData(data, data2);
		addData(data, data3);
		
		// cleaning duplicate data
		
		Set <String> dataset = new HashSet<String>(data);
		
		
		// Step 1: compute term frequency matrix:
		
		Map<String, Integer> map1 = new HashMap<String, Integer>();
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		Map<String, Integer> map3 = new HashMap<String, Integer>();
		
		
		int frecuency = 0;
		
		frecuencyMatrix(data1, dataset, map1);
		frecuencyMatrix(data2, dataset, map2);
		frecuencyMatrix(data3, dataset, map3);
		
		
		niceprint(dataset, map1, map2, map3);
		
		
		// Step 2: Compute the inverse document frequency IDF 
		
		Map<String, Integer> IDFmap = new HashMap<String, Integer>();
		

		int value = 0;
		
		for(String key : map1.keySet()){
			
			value = map1.get(key) + map2.get(key) + map3.get(key);
			IDFmap.put(key, value);
		}
		
		
		double valueLog = 0;
		
		Map<String, Double> IDFmaplog = new HashMap<String, Double>();
		Map<String, Double> IDFmap1 = new HashMap<String, Double>();
		Map<String, Double> IDFmap2 = new HashMap<String, Double>();
		Map<String, Double> IDFmap3 = new HashMap<String, Double>();
		
		computeLog(IDFmap, IDFmaplog);
		computeLog(map1, IDFmap1);
		computeLog(map2, IDFmap2);
		computeLog(map3, IDFmap3);
		
		niceprint(IDFmaplog);
		
		niceprint2(dataset, IDFmap1, IDFmap2, IDFmap3);
		
		/// Step 3: Compute the length of the document vector 
		
		
		double d1 =0;
		double d2 =0;
		double d3 =0;
		
		for(String key : IDFmap1.keySet())
			d1 += Math.pow(IDFmap1.get(key), 2) ;
		
		for(String key : IDFmap1.keySet())
			d2 += Math.pow(IDFmap2.get(key), 2) ;
		
		for(String key : IDFmap1.keySet())
			d3 += Math.pow(IDFmap3.get(key), 2) ;
		
		d1 = Math.sqrt(d1);
		d2 = Math.sqrt(d2);
		d3 = Math.sqrt(d3);
		
		System.out.println("Length of the document vector: \n\nd1 = " + d1 + "\nd2 = " + d2 + "\nd3 = " + d3 );
		
	
		// query search 
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Please insert your query:");
		String query = scan.nextLine();
		
		String[] Query = query.split(" ");
		
		scan.close();
		
		ArrayList <String> queryList = new ArrayList<String>();
		
		for(String item : Query)
			queryList.add(item.toLowerCase());
		
		Set <String> querySet = new HashSet <String>(queryList);
		
		
		Map <String, Integer> queryMap = new HashMap<String, Integer>();
		
		double q = 0;
		
		int queryFrecuency = 0;
		for(String itemset : querySet) {
			queryFrecuency = 0;
			for(String item : queryList)
				if(item.equals(itemset))
					queryFrecuency++;
			queryMap.put(itemset, queryFrecuency);
		}
		
		queryFrecuency = 0;
		
		for(String key : queryMap.keySet())
			if (queryFrecuency < queryMap.get(key))
				queryFrecuency = queryMap.get(key);
		
		for(String key : queryMap.keySet())
			for(String IDFKey : IDFmaplog.keySet()) 
				if(IDFKey.equals(key)) 
					q += Math.pow((double) (queryMap.get(key)/queryFrecuency) * IDFmaplog.get(IDFKey), 2);
			
			
		q = Math.sqrt(q);
		
		System.out.println("\nVector lenght of your query:\n q = " + q);
		
		// cosine funtion
		
		double cosine1 = cosine(queryMap, IDFmap1, d1, q);
		double cosine2 = cosine(queryMap, IDFmap2, d2, q);
		double cosine3 = cosine(queryMap, IDFmap3, d3, q);
		
		System.out.println("\nCosine Function:");
		System.out.println("Cosine d1 = " + cosine1);
		System.out.println("Cosine d2 = " + cosine2);
		System.out.println("Cosine d3 = " + cosine3);
		
		
		
		result(q, cosine1, cosine2, cosine3);
		
		
		
			
		
		

		
		
	}
	
	public static String readFile(String filename) {
		
		String data="";
		try {
		      File file = new File(filename);
		      Scanner myReader = new Scanner(file);
		      while (myReader.hasNextLine()) {
		        data += " " + myReader.nextLine();
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		return data;
	}
	
	public static void addData(ArrayList<String> list, String [] data) {
		
		for(int i = 0 ; i < data.length; i++)
			if(data[i] != "") {
				if(data[i].endsWith("!"))
					list.add(data[i].replace("!", "").toLowerCase());
				else if(data[i].endsWith(","))
					list.add(data[i].replace(",", "").toLowerCase());
				else if(data[i].endsWith("."))
					list.add(data[i].replace(".", "").toLowerCase());
				else	
					list.add(data[i].toLowerCase());
			}
				
	}
	
	public static void frecuencyMatrix(String [] data, Set <String> dataset, Map <String, Integer> map) {
		int frecuency = 0;
		
		for(String item : dataset) {
			frecuency = 0;
			for(int i = 1; i < data.length; i++) {
				if (item.equals(data[i].toLowerCase()))
					frecuency ++;
				else if(item.equals(data[i].replace("!", "").toLowerCase()))
					frecuency ++;
				else if(item.equals(data[i].replace(".", "").toLowerCase()))
					frecuency ++;
				else if(item.equals(data[i].replace(",", "").toLowerCase()))
					frecuency ++;
				
			}
			map.put(item, frecuency);
		}
	}
	
	public static void niceprint(Set <String> dataset, Map <String, Integer>  map1, Map <String, Integer>  map2, Map <String, Integer>  map3) {
		
		
		System.out.println("term frequency matrix:");
		System.out.println();
		System.out.print("   |");
		for(String item : dataset)
			System.out.print(item + "|");
		System.out.println();
		for(int i =0; i<285; i ++)
			System.out.print("=");
		System.out.println();
		String space = "";
		System.out.print("D1 |");
		for(String key : map1.keySet()) {
			for(int i = 0; i < key.length()-1; i++)
				space += " ";
			System.out.print(space + map1.get(key) + "|");
			space ="";
		}
		System.out.println();
		for(int i =0; i<285; i ++)
			System.out.print("=");
		System.out.println();
		System.out.print("D2 |");
		for(String key : map2.keySet()) {
			for(int i = 0; i < key.length()-1; i++)
				space += " ";
			System.out.print(space + map2.get(key) + "|");
			space ="";
		}
		System.out.println();
		for(int i =0; i<285; i ++)
			System.out.print("=");
		
		System.out.println();
		System.out.print("D3 |");
		for(String key : map3.keySet()) {
			for(int i = 0; i < key.length()-1; i++)
				space += " ";
			System.out.print(space + map3.get(key) + "|");
			space ="";
		}
		System.out.println();
		for(int i =0; i<285; i ++)
			System.out.print("=");
		System.out.println();
		System.out.println();
		
	}
	
	public static void computeLog(Map <String,Integer> IDFmap, Map <String,Double> IDFmaplog) {
		
		double valueLog;
		
		for(String key : IDFmap.keySet()){
			
			if(IDFmap.get(key)!=0)
				valueLog = Math.log((double) 3/IDFmap.get(key)) / Math.log(2);
			else
				valueLog = 0;
			
			IDFmaplog.put(key, valueLog);
		}
		
	}
	
	public static void niceprint(Map<String, Double> IDFmaplog ) {
		
		System.out.println("Compute the IDF");
		System.out.println();
		for(String key : IDFmaplog.keySet())
			System.out.println(key + " = " + IDFmaplog.get(key));
		
		System.out.println();
		System.out.println();
	}
	
	public static void niceprint2(Set <String> dataset, Map <String, Double>  map1, Map <String, Double>  map2, Map <String, Double>  map3) {
		
		System.out.println("Inverse document frequency:");
		System.out.println();
		System.out.print("   |");
		for(String item : dataset)
			System.out.print(item + "|");
		System.out.println();
		for(int i =0; i<285; i ++)
			System.out.print("=");
		System.out.println();
		String space = "";
		System.out.print("D1 |");
		for(String key : map1.keySet()) {
			for(int i = 0; i < key.length()-5; i++)
				space += " ";
			System.out.print(space + String.format("%.02f", map1.get(key)) + "|");
			space ="";
		}
		System.out.println();
		for(int i =0; i<285; i ++)
			System.out.print("=");
		System.out.println();
		System.out.print("D2 |");
		for(String key : map2.keySet()) {
			for(int i = 0; i < key.length()-5; i++)
				space += " ";
			System.out.print(space + String.format("%.02f", map2.get(key)) + "|");
			space ="";
		}
		System.out.println();
		for(int i =0; i<285; i ++)
			System.out.print("=");
		
		System.out.println();
		System.out.print("D3 |");
		for(String key : map3.keySet()) {
			for(int i = 0; i < key.length()-5; i++)
				space += " ";
			System.out.print(space + String.format("%.02f", map3.get(key)) + "|");
			space ="";
		}
		System.out.println();
		for(int i =0; i<285; i ++)
			System.out.print("=");
		System.out.println();
		System.out.println();
		System.out.println();
	}

	public static double cosine(Map <String,Integer> queryMap, Map <String,Double> IDFmap, double d, double q) {
		
		double cosine=0;
		
		for(String key : queryMap.keySet())
			for(String IDFKey : IDFmap.keySet())
				if(IDFKey.equals(key))
					cosine += (double) queryMap.get(key) * IDFmap.get(IDFKey);
		cosine = (double) cosine / (d*q);
		return cosine;
	}
	
	public static void result(double q, double cosine1, double cosine2, double cosine3) {
		double d1 = Math.abs(q - cosine1);
		double d2 = Math.abs(q - cosine2);
		double d3 = Math.abs(q - cosine3);
		
		System.out.println("\nCompute the more near value: ");
		System.out.println("d1 = " + d1);
		System.out.println("d2 = " + d2);
		System.out.println("d3 = " + d3);
		
		double lower = d1;
		
		if(lower > d2)
			lower = d2;
		if(lower > d3)
			lower = d3;
		
		System.out.println("more near value: " + lower);
		String result="";
		
		if(lower == d1) {
			
			result += "WordCount1.txt,";
			
			if( d2 < d3)
				result += " WordCount2.txt, WordCount3.txt,";
			else
				result += " WordCount3.txt, WordCount2.txt,";
		}else if(lower == d2) {
			
			result += "WordCount2.txt,";
			
			if( d1 < d3)
				result += " WordCount1.txt, WordCount3.txt,";
			else
				result += " WordCount3.txt, WordCount1.txt,";
		}else if(lower == d3) {
			
			result += "WordCount3.txt,";
			
			if( d1 < d2)
				result += " WordCount1.txt, WordCount2.txt,";
			else
				result += " WordCount2.txt, WordCount1.txt,";
		}else {
			result += "your query it's not inside the files";
		}
		
		System.out.println();
		System.out.println("Your index search is: ");
		System.out.println(result);
	}
}
