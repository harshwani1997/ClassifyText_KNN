How to run the program:
1) This project can be imported in eclipse as existing maven project or exported as jar to be executed in command line as java -jar Main.jar.

2) Hyperparameter for KNN can be specified in the constructor at line 77 at Main.java

3) Paths of input files:
   - in the Main class, in the main method, the variable folder of type File takes the path of the all 24 train documents. The folder is located as such:ClassifyText_KN\\resources\\dataset_3\\data\\C
   - in the Main class, in the main method, the variable test of type File takes the path of one of the unknown test documents. The file is located as such:ClassifyText_KN\\resources\\dataset_3\\data\\unknown_test\\unknown01.txt
   - in the Main class, in the main method, the variable folder1 of type File takes the path of all the 10 unknown test files. The folder is located as such:ClassifyText_KN\\resources\\dataset_3\\data\\unknown_test

4) Accuracy, recall, precision and everything else will be printed when the program is run. Only confusion matrix is present in the report pdf.