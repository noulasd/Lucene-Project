package luceneproject;
import java.io.IOException;

public class LuceneTester {
	
   String indexDir = "/home/dimitris/IndexData";
   String dataDir = "/home/dimitris/LuceneData";
   Indexer indexer;
   
   public static void main(String[] args) {
      LuceneTester tester;
      try {
         tester = new LuceneTester();
         tester.createIndex();
      } catch (IOException e) {
         e.printStackTrace();
      } 
   }

   private void createIndex() throws IOException {
      indexer = new Indexer(indexDir);
      int numIndexed;
      long startTime = System.currentTimeMillis();	
      numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
      long endTime = System.currentTimeMillis();
      indexer.close();
      System.out.println(numIndexed+" Files indexed, time taken: "
         +(endTime-startTime)+" ms");		
   }
}