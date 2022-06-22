package core;

import java.awt.event.ItemEvent;
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
		// TODO Auto-generated method stub
		
		// Reading File information
		String[] data1 = readFile("WordCount1.txt").split(" ");
		String[] data2 = readFile("WordCount2.txt").split(" ");
		String[] data3 = readFile("WordCount3.txt").split(" ");
		
		
		// merge all the data

		ArrayList <String> data = new ArrayList<String>();
		
		for(int i = 0 ; i < data1.length; i++)
			if(data1[i] != "")
				data.add(data1[i].toLowerCase());
		
		for(int i = 0 ; i < data2.length; i++)
			if(data2[i] != "")
				data.add(data2[i].toLowerCase());
		
		for(int i = 0 ; i < data3.length; i++)
			if(data3[i] != "")
				data.add(data3[i].toLowerCase());
		
		// cleaning duplicate data
		
		Set <String> dataset = new HashSet<String>(data);
		
		System.out.println(dataset);
		
		// Step 1: compute term frequency matrix:
		
		Map<String, Integer> map1 = new HashMap<String, Integer>();
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		Map<String, Integer> map3 = new HashMap<String, Integer>();
		int frecuency = 0;
		
		for(String item : dataset) {
			frecuency = 0;
			for(int i = 1; i < data1.length; i++) {
				if (item.equals(data1[i])) {
					frecuency ++;
				}
			}
			map1.put(item, frecuency);
		}
		
		for(String item : dataset) {
			frecuency = 0;
			for(int i = 1; i < data2.length; i++) {
				if (item.equals(data2[i])) {
					frecuency ++;
				}
			}
			map2.put(item, frecuency);
		}
		
		for(String item : dataset) {
			frecuency = 0;
			for(int i = 1; i < data3.length; i++) {
				if (item.equals(data3[i])) {
					frecuency ++;
				}
			}
			map3.put(item, frecuency);
		}
		
		//System.out.println(map1);
		//System.out.println(map2);
		//System.out.println(map3);
		
		
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
		
		
		for(String key : IDFmap.keySet()){
			
			if(IDFmap.get(key)!=0)
				valueLog = Math.log((double) 3/IDFmap.get(key)) / Math.log(2);
			else
				valueLog = 0;
			
			IDFmaplog.put(key, valueLog);
		}
		
		for(String key : map1.keySet()){
			
			if(map1.get(key)!=0)
				valueLog = Math.log((double) 3/IDFmap.get(key)) / Math.log(2);
			else
				valueLog = 0;
			
			IDFmap1.put(key, valueLog);
		}
		
		for(String key : map2.keySet()){
			
			if(map3.get(key)!=0)
				valueLog = Math.log((double) 3/IDFmap.get(key)) / Math.log(2);
			else
				valueLog = 0;
			
			IDFmap2.put(key, valueLog);
		}
		
		for(String key : map3.keySet()){
			
			if(map1.get(key)!=0)
				valueLog = Math.log((double) 3/IDFmap.get(key)) / Math.log(2);
			else
				valueLog = 0;
			
			IDFmap3.put(key, valueLog);
		}
		
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
		
		//System.out.println(d1);
		//System.out.println(d2);
		//System.out.println(d3);
		
		String query = "Hadoop";
		
		String[] Query = query.split("elephant");
		
		ArrayList <String> queryList = new ArrayList<String>();
		
		for(String item : Query)
			queryList.add(item.toLowerCase());
		
		Set <String> querySet = new HashSet <String>(queryList);
		
		Map <String, Integer> queryMap = new HashMap<String, Integer>();
		
		double q =0;
		
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
					q += (double) (queryMap.get(key)/queryFrecuency) * IDFmaplog.get(IDFKey);
		
		// cosine funtion
		
		double cosine1 = 0;
		double cosine2 = 0;
		double cosine3 = 0;
		
		for(String key : queryMap.keySet())
			for(String IDFKey : IDFmap1.keySet())
				if(IDFKey.equals(key))
					cosine1 += (double) queryMap.get(key) * IDFmap1.get(IDFKey);
		cosine1 = (double) cosine1 / (d1*q);
		
		for(String key : queryMap.keySet())
			for(String IDFKey : IDFmap2.keySet())
				if(IDFKey.equals(key))
					cosine2 += (double) queryMap.get(key) * IDFmap2.get(IDFKey);
		cosine2 = (double) cosine2 / (d2*q);
		
		for(String key : queryMap.keySet())
			for(String IDFKey : IDFmap3.keySet())
				if(IDFKey.equals(key))
					cosine3 += (double) queryMap.get(key) * IDFmap3.get(IDFKey);
		cosine3 = (double) cosine3 / (d3*q);
		
		
		//System.out.println(q);
		//System.out.println(cosine1);
		//System.out.println(cosine2);
		//System.out.println(cosine3);
		
		d1 = q - cosine1;
		d2 = q - cosine2;
		d3 = q - cosine3;
		
		double lower = d1;
		
		if(lower < d2)
			lower = d2;
		if(lower < d3)
			lower = d3;
		
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
		}
		
		System.out.println(result);
		
		
		
			
		
		

		
		
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
	

}
