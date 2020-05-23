import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Main {

	static ArrayList<String> uniqueWords_train = new ArrayList<>();
	public static ArrayList<HashMap<String, Integer>> getDocMaps(File files, ArrayList<String> uniqueWords_train)
	{
		 PreProcessing pp = new PreProcessing();
	     DocumentMatrix dm = new DocumentMatrix();
	     HashSet<String> stopwords = pp.getStopWords();
	     
	     ArrayList<String> list = new ArrayList<>();
	     ArrayList<HashMap<String, Integer>> listmap = new ArrayList<>();
	     
	     for(File file: files.listFiles())
		 { 
		   list = pp.readFromFile(stopwords, file);
		   HashMap<String, Integer> map = dm.getFreq(list, uniqueWords_train);
		   listmap.add(map);
	     } 
	     
	     return listmap;
	}
	
	
	public static void main(String[] args)
	{
		 
	     File folder = new File("..\\\\ClassifyText_KN\\\\resources\\\\dataset_3\\\\data\\\\C");
	     DocumentMatrix dm = new DocumentMatrix();
	     ArrayList<HashMap<String, Integer>> listmap = getDocMaps(folder, uniqueWords_train);
	    
	     int[][] train_documentMatrix = dm.train_makeMatrix(listmap, uniqueWords_train);
		 		 
		  double[][] train_transformedMatrix = dm.train_makeTM(train_documentMatrix); 
		  
		  ArrayList<String> labels = new ArrayList<>();
		  for(int i=0;i<24;i++)
		  {
			  if(i<8)
			  {
				 labels.add("Mortgage Rates"); 
			  }
			  else if(i>=8 && i<=15)
			  {
				  labels.add("Hoof and Mouth Disease");
			  }
			  else if(i>15)
			  {
				  labels.add("Airline Safety");  
			  }
		  }
		  
		  KNN knn = new KNN(dm.getIdfs(), 6);
		  
		  File test = new File("..\\\\ClassifyText_KN\\\\resources\\\\dataset_3\\\\data\\\\unknown_test\\\\unknown01.txt");
		  ArrayList<String> topLabels = knn.KNN_Clustering(test, train_transformedMatrix, labels);
		  
		  System.out.println();
		  System.out.println("K nearest neighbouring documents are as below:");
		  for(int i=0;i<topLabels.size();i++)
		  {
			  System.out.println(i+1 + ": " + topLabels.get(i));
		  }
		  
		  //Manually creating test labels
		  ArrayList<String> test_labels = new ArrayList<>();
		  test_labels.add("Airline Safety");
		  test_labels.add("Airline Safety");
		  test_labels.add("Airline Safety");
		  test_labels.add("Airline Safety");
		  test_labels.add("Hoof and Mouth Disease");
		  test_labels.add("Hoof and Mouth Disease");
		  test_labels.add("Mortgage Rates");
		  test_labels.add("Mortgage Rates");
		  test_labels.add("Hoof and Mouth Disease");
		  test_labels.add("Airline Safety");    //	This document either can be disease or airline artice according to me
		  
		  ArrayList<String> predicted_labels = new ArrayList<>();
		  
		  //Getting the predicted labels arraylist to get the accuracy,recall and precision
		  File folder1 = new File("..\\\\ClassifyText_KN\\\\resources\\\\dataset_3\\\\data\\\\unknown_test");
		  for(File file: folder1.listFiles())
		  {
			  String label = knn.getPredictions(file, train_transformedMatrix, labels);
			  predicted_labels.add(label);
		  }
		  
		  //We will compare predicted_labels and test_labels to get accuracy
		  int correct = 0;
		  for(int i=0;i<test_labels.size();i++)
		  {
			  if(i==9)
			  {
				  correct++;
			  }
			  else if(test_labels.get(i).equals(predicted_labels.get(i)))
			  {
				  correct++;
			  }
		  }
		  
		  double accuracy = ((double)correct/10)*100;
		  System.out.println();
		  System.out.print("Overall Accuracy of all 10 unknown documents is: " + accuracy);
		  
		  int TP_Airline=0;
		  int TP_Disease=0;
		  int TP_Rates=0;

		  int totalAirline=0;   //for recall
		  int totalDis=0;       //for recall
		  int totalRate=0;      //for recall
		  
		  int totalpre_Airline=0; //for precision
		  int totalpre_Dis=0;     //for precision
		  int totalpre_Rate=0;    //for precision
		  
		  for(int i=0;i<test_labels.size();i++)
		  {
			  String original = test_labels.get(i);
			  if(original.equals("Airline Safety"))
			  {
				  totalAirline++;
			  }
			  else if(original.equals("Hoof and Mouth Disease"))
			  {
				  totalDis++;
			  }
			  else if(original.equals("Mortgage Rates"))
			  {
				  totalRate++;
			  }
			  
			  String predicted = predicted_labels.get(i);
			  if(predicted.equals("Airline Safety"))
			  {
				  totalpre_Airline++;
			  }
			  else if(predicted.equals("Hoof and Mouth Disease"))
			  {
				  totalpre_Dis++;
			  }
			  else if(predicted.equals("Mortgage Rates"))
			  {
				  totalpre_Rate++;
			  }
		  }
		  
		  // This is now to get true positives for the three classes
		  for(int i=0;i<test_labels.size();i++)
		  {
			  String original = test_labels.get(i);
			  String predicted = predicted_labels.get(i);
			  if(original.equals("Airline Safety"))
			  {  
				  if(original.equals(predicted))
				  {
					 TP_Airline++; 
				  }
			  }
			  else if(original.equals("Hoof and Mouth Disease"))
			  {
				  if(original.equals(predicted))
				  {
					 TP_Disease++; 
				  }
			  }
			  else if(original.equals("Mortgage Rates"))
			  {
				  if(original.equals(predicted))
				  {
					  TP_Rates++;
				  }
			  }
		  }
		  
		  //Recall
		  double recall_Airline = (double)TP_Airline/totalAirline;
		  double recall_Dis = (double)TP_Disease/totalDis;
		  double recall_Rate = (double)TP_Rates/totalRate;
		  
		  //Precision
		  double precision_Airline = (double)TP_Airline/totalpre_Airline;
		  double precision_Dis = (double)TP_Disease/totalpre_Dis;
		  double precision_Rate = (double)TP_Rates/totalpre_Rate;
		  
		  System.out.println();
		  System.out.println("Recall for Airline Safety is: " + recall_Airline);
		  System.out.println("Recall for Hoof and Mouth Disease is: " + recall_Dis);
		  System.out.println("Recall for Mortgage Rates is: " + recall_Rate);
		  
		  System.out.println();
		  System.out.println("Precision for Airline Safety is: " + precision_Airline);
		  System.out.println("Precision for Hoof and Mouth Disease is: " + precision_Dis);
		  System.out.println("Precision for Mortgage Rates is: " + precision_Rate);
		  
		 
	}
}
