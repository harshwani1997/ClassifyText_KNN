import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;

class Pair
{
	String label;
	double dis;
	Pair(double dis, String train_label)
	{
		label = train_label;
		this.dis = dis;
	}
}

public class KNN {
	
	  double[] idfs;
	  int K;
	public KNN(double[] idfs, int K)
	{
		this.K = K;  
		this.idfs = idfs;
	}
	
	
	PreProcessing pp = new PreProcessing();
	DocumentMatrix dm = new DocumentMatrix();
	
	public ArrayList<String> KNN_Clustering(File testFile, double[][] train_tfidf, ArrayList<String> train_labels)
	{
		double[] test_tfidf = getTestTf_idf(testFile, Main.uniqueWords_train);
		ArrayList<String> topLabels = getDistancesLabels(test_tfidf, train_tfidf, train_labels, K);
		
		HashMap<String, Integer> KNN = new HashMap<>();
		for(String s:topLabels)
		{
			if(KNN.containsKey(s))
			{
				KNN.put(s, KNN.get(s)+1);
			}
			else
			{
				KNN.put(s, 1);
			}
		}
		
		String bestLabel = "";
		int max =  Integer.MIN_VALUE;
		for(String key:KNN.keySet())
		{
			if(KNN.get(key)>max)
			{
				max = KNN.get(key);
				bestLabel = key;
			}
		}
		
		String fuzzyKNN = getfuzzyKNN(KNN, train_labels);
		
		System.out.print("This document belongs to category: " + bestLabel);
		System.out.println();
		System.out.print(fuzzyKNN);
		System.out.println();
		return topLabels;
	}
	
	public String getPredictions(File testFile, double[][] train_tfidf, ArrayList<String> train_labels)
	{
		double[] test_tfidf = getTestTf_idf(testFile, Main.uniqueWords_train);
		ArrayList<String> topLabels = getDistancesLabels(test_tfidf, train_tfidf, train_labels, K);
		
		HashMap<String, Integer> KNN = new HashMap<>();
		for(String s:topLabels)
		{
			if(KNN.containsKey(s))
			{
				KNN.put(s, KNN.get(s)+1);
			}
			else
			{
				KNN.put(s, 1);
			}
		}
		
		String bestLabel = "";
		int max =  Integer.MIN_VALUE;
		for(String key:KNN.keySet())
		{
			if(KNN.get(key)>max)
			{
				max = KNN.get(key);
				bestLabel = key;
			}
		}
		
		return bestLabel;
	}
	
	//This is the bonus part fuzzyKNN
	private String getfuzzyKNN(HashMap<String, Integer> KNN, ArrayList<String> labels) 
	{
	   String ans = "";
	   HashSet<String> uniqueLabels = new HashSet<>();
	   
	   for(String s:labels)
	   {
		   uniqueLabels.add(s);
	   }
	   
	   for(String s:uniqueLabels)
	   {
		   if(!KNN.containsKey(s))
		   {
			   ans = ans + "Document is 0% of " + s + ", ";
		   }
	   }
	   
	   int totalCount=0;
	   for(String label:KNN.keySet())
	   {
		   totalCount += KNN.get(label);
	   }
	   
	   for(String label:KNN.keySet())
	   {
		   double percentage = ((double)KNN.get(label)/totalCount)*100;
		   ans = ans + "Document is " + percentage + "% of " + label + ", ";
	   }
	   
	   ans = ans.substring(0,ans.length()-2);
	   ans = ans + ".";
	   return ans;
	}

	private ArrayList<String> getDistancesLabels(double[] test_tfidf, double[][] train_tfidf, ArrayList<String> train_labels, int K) 
	{
		 PriorityQueue<Pair> dislabels = new PriorityQueue<>((p1,p2)->Double.compare(p2.dis, p1.dis));
       int labels=0;
       for(double[] row:train_tfidf)
       {
          double dis = getCosineDis(row, test_tfidf); 
          //System.out.println(dis + " " + train_labels.get(labels));
          Pair p = new Pair(dis, train_labels.get(labels));
      
          dislabels.add(p);
          labels++;
       }
       
       ArrayList<String> topLabels = new ArrayList<>();
       while(!dislabels.isEmpty())
       {
    	 Pair p = dislabels.poll();
    	 topLabels.add(p.label);
    	 if(topLabels.size()==K)
    	 {
    		 break;
    	 }
       }
       
       //
		return topLabels;
	}
	
	public double getEuclideanDis(double[] a1, double[] a2)
    {
  	 double ans=0.0;
  	 for(int i=0;i<a1.length;i++)
  	 {
  		 double diff = a1[i]-a2[i];
  		 ans = ans + diff * diff;
  	 }
  	 
  	 return Math.sqrt(ans);
    }
	
	public double getCosineDis(double[] a1, double[] a2)
	{
		double dotProduct = 0.0;
		double a1Denom = 0.0;
		double a2Denom = 0.0;
		for(int i=0;i<a1.length;i++)
		{
			dotProduct = dotProduct + a1[i]*a2[i];
			a1Denom = a1Denom + a1[i]*a1[i];
			a2Denom = a2Denom + a2[i]*a2[i];
		}
		double sqrta1 = Math.sqrt(a1Denom);
		double sqrta2 = Math.sqrt(a2Denom);
		
		return dotProduct / (sqrta1*sqrta2);
	}

	public double[] getTestTf_idf(File testFile, ArrayList<String> uniqueWords_train)
	{
		int[] test_freqCount = new int[uniqueWords_train.size()];
		double[] test_tfidf = new double[uniqueWords_train.size()];
		HashSet<String> stopwords = pp.getStopWords();
		ArrayList<String> test_list = pp.readFromFile(stopwords, testFile);
		HashMap<String, Integer> test_map = dm.getFreq_test(test_list);
       /// You are also including uniquewords from this file,,thats why uniquewords isgoing frm 2431 to 2446
		int c=0;
		for(String s:uniqueWords_train)
		{	
			if(!test_map.containsKey(s))
			{
				test_freqCount[c]=0;
			}
			else
			{
				test_freqCount[c]=test_map.get(s);
			}
			c++;
		}
		
		double[] tf = new double[test_tfidf.length];
		
		int totalWords=0;
		for(int i=0;i<test_tfidf.length;i++)
		{
			totalWords = totalWords + test_freqCount[i];
		}
		
		for(int i=0;i<test_tfidf.length;i++)
		{
			tf[i] = (double)test_freqCount[i]/totalWords;
		}
		
		
		for(int i=0;i<uniqueWords_train.size();i++)
		{
			test_tfidf[i] = tf[i]*Math.log(24/idfs[i]);
		}

	   return test_tfidf;	
		
	}
	
}
