package luceneproject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;








public class Indexer {

	   private IndexWriter writer;
	   
	   @SuppressWarnings("deprecation")
	public Indexer(String indexDirectoryPath) throws IOException {
	      //this directory will contain the indexes
	      Directory indexDirectory = 
	         FSDirectory.open(new File(indexDirectoryPath));

	      //create the indexer
	      writer = new IndexWriter(indexDirectory, //using our index directory and Porter Stemmer
	         new MyAnalyzer(),true,
	         IndexWriter.MaxFieldLength.UNLIMITED);
	   }

	   public void close() throws CorruptIndexException, IOException {
	      writer.close();
	   }

	   private Document getDocument(File file) throws IOException {
		  Document document = new Document();
		  FileReader fr = new FileReader(file);
	      
	      
	      
	      
	      String title ="";
	      String author="";
	      String synopsis="";
			
	      try {														//parsing Title, Author, Synopsis from records
				
				BufferedReader br = new BufferedReader(fr);
				String brs=br.readLine();
				
				while(brs!=null) {
					if(brs.startsWith(".T")) {
						brs=br.readLine();
						while(!brs.startsWith(".")) {
							title+=brs;
							brs=br.readLine();
						}
					}
					
					if(brs.startsWith(".A")) {
						brs=br.readLine();
						while(!brs.startsWith(".")) {
							author+=brs;
							brs=br.readLine();
						}
					}
					
					if(brs.startsWith(".W")) {
						brs=br.readLine();
						while(!brs.startsWith(".")) {
							synopsis+=brs;
							brs=br.readLine();
						}
					}
					
					
					
					
					brs=br.readLine();
					
					
				
						
					
				}//while brs null
				br.close();
			} 
			catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
				}
	      
	      
	      
	      
	      
	      
	      
	      
	      
	      
	      
	      
	      
	     
	      
	  
	      //index file name
	      Field fileNameField = new Field(LuceneConstants.FILE_NAME, 				//creating the fields for filename and filepath
	         file.getName(),
	         Field.Store.YES,Field.Index.NOT_ANALYZED);
	      
	      //index file path
	      Field filePathField = new Field(LuceneConstants.FILE_PATH,
	         file.getCanonicalPath(),
	         Field.Store.YES,Field.Index.NOT_ANALYZED);

	      //creating fields for author,title,synopsis
	      //if not empty add fields to document object
	      if(title!="") {
	    	  Field titlefield = new Field(LuceneConstants.TITLE,title,Field.Store.YES,Field.Index.NOT_ANALYZED);
	    	  document.add(titlefield);

	      }
	      
	      //no need to analyze author and title
	      
	      
	      if(author!="") {
	    	  Field authorfield = new Field(LuceneConstants.AUTHOR,author,Field.Store.YES,Field.Index.NOT_ANALYZED);
	    	  document.add(authorfield);
	      }
	      
	      
	      if(synopsis!="") {
	    	  Field synopsisfield = new Field(LuceneConstants.SYNOPSIS,synopsis,Field.Store.YES,Field.Index.ANALYZED,Field.TermVector.YES);
	    	  document.add(synopsisfield); //analyzing synopsis and creating term vector in order to find term frequencies
	      }
	      
	      
	      document.add(fileNameField);
	      document.add(filePathField);
	      
	      
	      
	      
	      
	      return document;
	   }   

	   private void indexFile(File file) throws IOException {
	      Document document = getDocument(file); //index document object based on its fields
	      writer.addDocument(document);
	   }

	   public int createIndex(String dataDirPath, FileFilter filter) //index all files in data directory with indexFile
	      throws IOException {
	      //get all files in the data directory
	      File[] files = new File(dataDirPath).listFiles();

	      for (File file : files) {
	         if(!file.isDirectory()
	            && !file.isHidden()
	            && file.exists()
	            && file.canRead()
	            && filter.accept(file)
	         ){
	            indexFile(file);
	         }
	      }
	      return writer.numDocs();
	   }
	   
		
	   
	}

