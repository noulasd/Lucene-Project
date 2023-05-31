package luceneproject;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;

public class LuceneTester {
	
   static String indexDir = "/home/dimitris/IndexData";
   static String dataDir = "/home/dimitris/LuceneData";
   Indexer indexer;
   static String query="";
   
   
   public static void main(String[] args) {
      LuceneTester tester;
      tester = new LuceneTester();
      try {									//check if index directory is empty, then create indexes, continue
         
         File fil = new File(indexDir);
         if(fil.isDirectory()){
        	
        	if(fil.list().length<=1) {
        		
        		tester.createIndex();
        	}
         }
         
         
      } catch (IOException e) {
         e.printStackTrace();
      }
      
      SwingUtilities.invokeLater(new Runnable(){

          @Override
          public void run() {
              // TODO Auto-generated method stub								//gui
              JFrame frame = new JFrame("Search Engine");
              JPanel panel = new JPanel();
              JButton btnLoad = new JButton("Load File");
              JButton btnLoad2 = new JButton("Search");
              JButton btnLoad3 = new JButton("Relevance Feedback");
              
              SpinnerModel model = new SpinnerNumberModel(1, 1, 20, 1);     
              JSpinner spinner1 = new JSpinner(model);
              SpinnerModel model2 = new SpinnerNumberModel(1, 1, 10, 1);
              JSpinner spinner2 = new JSpinner(model2);
              
              ((JSpinner.DefaultEditor) spinner1.getEditor()).getTextField().setEditable(false);
              ((JSpinner.DefaultEditor) spinner2.getEditor()).getTextField().setEditable(false);
              
              
              final JTextArea textArea = new JTextArea(10,20);
              final JTextArea textArea2 = new JTextArea(5,10);
              
              
              
              //button1
              btnLoad.addActionListener(new ActionListener(){ //load action button1

                  @Override
                  public void actionPerformed(ActionEvent e) {
                      // TODO Auto-generated method stub
                      JFileChooser fileChooser = new JFileChooser();
                      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                      fileChooser.setCurrentDirectory(new File(dataDir));
                      fileChooser.setFileFilter(new FileNameExtensionFilter
                                ("Text File", "txt"));
                      fileChooser.setAcceptAllFileFilterUsed(false);
                      while(true)
                      {
                          if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) {

                              if(!fileChooser.getSelectedFile().exists()) {
                                  JOptionPane.showMessageDialog(fileChooser, 
                                          "You must select an existing file!","open File",
                                          JOptionPane.ERROR_MESSAGE);//continue
                              }else {
                                  //read file to the textArea
                                  File file = fileChooser.getSelectedFile();
                                  try {
                                      Scanner scanner = new Scanner(new  BufferedReader
                                                 (new FileReader( file)));
                                       textArea.setText("");
                                       while(scanner.hasNext()) {
                                           textArea.append(scanner.nextLine()+"\n");
                                        }
                                       scanner.close();
                                  } catch (FileNotFoundException e1) {
                                      // TODO Auto-generated catch block
                                      e1.printStackTrace();
                                  }
                                  break;//successfully open break
                              }
                          }else {
                              break;//user cancel open break
                          }
                      }
                  }
              });
              
              btnLoad2.addActionListener(new ActionListener() {  //search action button2
            	  @Override
                  public void actionPerformed(ActionEvent e){
            		  try {
            			if(!textArea2.getText().equals("")) {  
            				tester.search(textArea2.getText().replaceAll("[\\-\\+\\^\\(\\)\\(\\)\\{\\}\\$\\[\\]\\?\\;\\*\\!\\%\\&]",""));
            			}
					} catch (IOException | ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            	  }
              });
              
              
              btnLoad3.addActionListener(new ActionListener() { //relevance feedback action button3
            	  @Override
                  public void actionPerformed(ActionEvent e) {
            		  int k = Integer.parseInt(spinner1.getValue().toString()); //parse k,l from jspinner
            		  int l = Integer.parseInt(spinner2.getValue().toString());
            		  try {
            			  if(!textArea2.getText().equals("")) { //append the frequent words to the search textbox and search
            				  String s = tester.RelFeedback(textArea2.getText().replaceAll("[\\-\\+\\^\\(\\)\\(\\)\\{\\}\\$\\[\\]\\?\\;\\*\\!\\%\\&]",""), k, l);
            				  textArea2.append(" "+ s);
            				  tester.search(textArea2.getText().replaceAll("[\\-\\+\\^\\(\\)\\(\\)\\{\\}\\$\\[\\]\\?\\;\\*\\!\\%\\&]",""));
            			  }
					} catch (IOException | ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            	  }
              });
              
              JScrollPane scrollPane = new JScrollPane(textArea); 					//gui
              JScrollPane scrollPane2 = new JScrollPane(textArea2);
              panel.add(btnLoad);
              panel.add(btnLoad2);
              panel.add(btnLoad3);
              panel.add(scrollPane2);
              panel.add(spinner1);
              panel.add(spinner2);
              frame.add(panel,BorderLayout.NORTH);
              frame.add(scrollPane,BorderLayout.CENTER);
              frame.setSize(700, 400);
              frame.setResizable(false);
              frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
              frame.setVisible(true);
          }
      });
      
      
      
      
      
   }//main

   private void createIndex() throws IOException {
      indexer = new Indexer(indexDir); // new indexer object
      int numIndexed;
      long startTime = System.currentTimeMillis();	
      numIndexed = indexer.createIndex(dataDir, new TextFileFilter()); //creates indexes using createIndex from Indexer class
      long endTime = System.currentTimeMillis();
      indexer.close();
      
      JOptionPane.showMessageDialog(null,numIndexed+" Files indexed, time taken: "
    	         +(endTime-startTime)+" ms" , "Results", JOptionPane.PLAIN_MESSAGE );
   }
   
   
   private void search(String searchQuery) throws IOException, ParseException{
	   Searcher searcher = new Searcher(indexDir); //new searcher object
	   long startTime = System.currentTimeMillis();
	   searcher.setDefaultFieldSortScoring(true, false);
	   TopDocs hits = searcher.search(searchQuery,Sort.RELEVANCE); 	//gets results respecting the query on the textbox 
	   long endTime = System.currentTimeMillis();					//using search from Searcher class
	   
	   
	   String s="";
	   
	   
	   
	   for(ScoreDoc scoreDoc: hits.scoreDocs) {
		   Document doc = searcher.getDocument(scoreDoc); //creating the string to print the records
		   s+=doc.get(LuceneConstants.FILE_NAME) + "\n";
	   }
	   
	   
	   JOptionPane p = new JOptionPane(hits.totalHits + " documents found. Time :"+(endTime-startTime) + "\n"+s , JOptionPane.PLAIN_MESSAGE);
	   JDialog k = p.createDialog(null, "Search");
	   k.setModal(false);
	   k.setVisible(true);
	   searcher.close();
   }
   
   public static void quickSort(int[] arr, String[] arr2, int low, int high) { //quicksorting two parallel arrays
		if (arr == null || arr.length == 0)										// based on the values of the first one
																				//decreasing order
			return;

		if (low >= high)
			return;

		// pick the pivot
		int middle = low + (high - low) / 2;
		int pivot = arr[middle];

		// make left > pivot and right < pivot
		int i = low, j = high;
		while (i <= j) {
			while (arr[i] > pivot) {
				i++;
			}

			while (arr[j] < pivot) {
				j--;
			}

			if (i <= j) {
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
				
				String temp2 = arr2[i];
				arr2[i] = arr2[j];
				arr2[j] = temp2;
				
				
				
				i++;
				j--;
			}
		}

		// recursively sort two sub parts
		if (low < j)
			quickSort(arr,arr2, low, j);
		if (high > i)
			quickSort(arr,arr2, i, high);
	}//quicksort
   
   
   
   private String RelFeedback(String searchQuery, int k, int l) throws IOException, ParseException{
	   Searcher searcher = new Searcher(indexDir);								//gets results respecting the query on the textbox 
	   searcher.setDefaultFieldSortScoring(true, false);
	   TopDocs hits = searcher.search(searchQuery,Sort.RELEVANCE);				//using search from Searcher class
	   if(k<=hits.scoreDocs.length) {
		   int[] freq;
		   String[] trm = {};
		   String s="";
		   for (int i=0; i<k; i++){
		     TermFreqVector termVector = searcher.ir.getTermFreqVector(hits.scoreDocs[i].doc, LuceneConstants.SYNOPSIS); //gets term frequencies for each record
		     freq = termVector.getTermFrequencies();																	//based on the synopsis and the docid of each document in hits
		     trm = termVector.getTerms(); //parallel arrays
		     quickSort(freq,trm,0,freq.length -1);
		     if(l<=freq.length) {
		    	 for (int j=0; j<l;j++) {
		    		 if(!searchQuery.contains(trm[j])) //add l most frequent terms to the returning string if they are not contained in the search query
		    			 s+=trm[j] + " ";
		    	 }
		     }
		}
	   
	   
		   return s;
	   }else return "";
	   
   }//relfeedback
   
   
   
}//class
   
   
   
   
 

