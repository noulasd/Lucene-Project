package luceneproject;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
/*public class Indexer {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if (args.length !=2) {
			throw new Exception("Usage: java " + Indexer.class.getName()
						+ " <index dir> <data dir>");
		}
		String indexDir = args[0];
		String dataDir = args[1];
		
		long start = System.currentTimeMillis();
		Indexer indexer = new Indexer(indexDir);
		int numIndexed = indexer.index(dataDir);
		indexer.close();
		long end = System.currentTimeMillis();
		
		System.out.print("indexing "+ numIndexed + "files took "+ (end - start) + "milliseconds");
	}//main
	
	private IndexWriter writer;
	public Indexer(String indexDir) throws IOException{
		
		Directory dir = new FSDirectory.open(new File(indexDir));
		writer = new IndexWriter(dir, new StandardAnalyzer(), true, IndexWriter.MAX_);
	}
	public void close() throws IOException{
		writer.close();
	}
	public int index(String dataDir) throws Exception{
		File[] files = new File(dataDir).listFiles();
		
		for (int i = 0; i< files.length; i++) {
			File f = files[i];
			
			if (!f.isDirectory()  &&
					!f.isHidden() &&
					f.exists() &&
					f.canRead() &&
					acceptFile(f)) {
				indexFile(f);
			}
		}
		return writer.numDocs();
	}//index
	
	protected boolean acceptFile(File f) {
		return f.getName().endsWith(".txt");
	}
	protected Document getDocument(File f) throws Exception{
		Document doc = new Document();
		doc.add(new Field("contents", new FileReader(f)));
		doc.add(new Field("filename",f.getCanonicalPath(),Field.Store.YES, Field.Index.NOT_ANALYZED));
		return doc;
	}
	private void indexFile(File f) throws Exception{
		System.out.println("Indexing "+ f.getCanonicalPath());
		Document doc = getDocument(f);
		if(doc!=null) {
			writer.addDocument(doc);
		}
	}
	
	
	
	
	
}//class
*/







public class Indexer {

	   private IndexWriter writer;

	   public Indexer(String indexDirectoryPath) throws IOException {
	      //this directory will contain the indexes
	      Directory indexDirectory = 
	         FSDirectory.open(new File(indexDirectoryPath));

	      //create the indexer
	      writer = new IndexWriter(indexDirectory, 
	         new StandardAnalyzer(Version.LUCENE_36),true,
	         IndexWriter.MaxFieldLength.UNLIMITED);
	   }

	   public void close() throws CorruptIndexException, IOException {
	      writer.close();
	   }

	   private Document getDocument(File file) throws IOException {
	      Document document = new Document();
	      
	      //index file contents
	      Field contentField = new Field(LuceneConstants.CONTENTS, 
	         new FileReader(file));
	      
	      //index file name
	      Field fileNameField = new Field(LuceneConstants.FILE_NAME,
	         file.getName(),
	         Field.Store.YES,Field.Index.NOT_ANALYZED);
	      
	      //index file path
	      Field filePathField = new Field(LuceneConstants.FILE_PATH,
	         file.getCanonicalPath(),
	         Field.Store.YES,Field.Index.NOT_ANALYZED);

	      document.add(contentField);
	      document.add(fileNameField);
	      document.add(filePathField);

	      return document;
	   }   

	   private void indexFile(File file) throws IOException {
	      System.out.println("Indexing "+file.getCanonicalPath());
	      Document document = getDocument(file);
	      writer.addDocument(document);
	   }

	   public int createIndex(String dataDirPath, FileFilter filter) 
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

