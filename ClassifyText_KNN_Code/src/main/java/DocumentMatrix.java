import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DocumentMatrix {

	ArrayList<String> ordered_uniqueWords = new ArrayList<>();
	double[] idfs = new double[2431];
	public HashMap<String, Integer> getFreq(ArrayList<String> list, ArrayList<String> uniqueWords_train)
	{
		for(String s:list)
		   {
			   if(!uniqueWords_train.contains(s))
			   {
				   //System.out.print(s);
				   uniqueWords_train.add(s);
			   }
		   }
		
		HashMap<String, Integer> freqCount = new HashMap<>();
		for(String s:list)
		{
			//System.out.println(s);
			if(freqCount.containsKey(s))
			{
				freqCount.put(s, freqCount.get(s)+1);
			}
			else
			{
				freqCount.put(s,1);
			}
		}
		
		return freqCount;
	}
	
	public HashMap<String, Integer> getFreq_test(ArrayList<String> test_list) {
		HashMap<String, Integer> freqCount = new HashMap<>();
		for(String s:test_list)
		{
			//System.out.println(s);
			if(freqCount.containsKey(s))
			{
				freqCount.put(s, freqCount.get(s)+1);
			}
			else
			{
				freqCount.put(s,1);
			}
		}
		return freqCount;
	}
	
	public int[][] train_makeMatrix(ArrayList<HashMap<String, Integer>> listmap, ArrayList<String> set)
	{
	   int[][] train_documentMatrix = new int[listmap.size()][set.size()];
	   
		for(int i=0;i<listmap.size();i++)
	   {
		   int j=0;
			HashMap<String, Integer> map = listmap.get(i);
			for(String check:set)
		   {
				ordered_uniqueWords.add(check);
				if(!map.containsKey(check))
			   {
				   train_documentMatrix[i][j]=0; 
			   }
			   else
			   {
				   train_documentMatrix[i][j]=map.get(check); 
			   }
				j++;
		   }
	   }
		return train_documentMatrix;
	}
	
	public double[][] train_makeTM(int[][] train_documentMatrix)
	{
		double tf=0.0;
		double idf=0.0;
		int sum;
		int i=0;
		int j=0;
		int count;
		int no_of_docs = train_documentMatrix.length;
		//idfs = new double[train_documentMatrix[0].length];
	    double[][] train_transformedMatrix = new double[train_documentMatrix.length][train_documentMatrix[0].length];
		
		for(j=0;j<train_documentMatrix[0].length;j++)
		{
			count=0;
			for(i=0;i<train_documentMatrix.length;i++)
			{
				if(train_documentMatrix[i][j]!=0)
				{
					count++;
				}
			}
			idfs[j]=count;
		}
		
		for(i=0;i<train_documentMatrix.length;i++)
		{
			sum=0;
			for(j=0;j<train_documentMatrix[0].length;j++)
			{
			   sum = sum + train_documentMatrix[i][j]; 
			}
			for(j=0;j<train_documentMatrix[0].length;j++)
			{
				 tf = (double)train_documentMatrix[i][j]/sum;
				 idf = Math.log(no_of_docs/idfs[j]);
				 train_transformedMatrix[i][j] = tf*idf;
			}
		}
		
		return train_transformedMatrix;
	}

	// To access this is KNN class while computing tf-idf row for a raw test document
	public double[] getIdfs() {
		return idfs;
	}

	

}
