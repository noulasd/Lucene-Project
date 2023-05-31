package luceneproject;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {
	IndexReader ir;
	IndexSearcher indexSearcher;
	QueryParser queryParser;
	Query query;
	MultiFieldQueryParser mfqp;
	String[] multiq={LuceneConstants.TITLE,LuceneConstants.AUTHOR,LuceneConstants.SYNOPSIS}; //fields to search
	
	public Searcher(String indexDirectoryPath) throws IOException{
		Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
		ir = IndexReader.open(indexDirectory); //use indexes from index directory to search
	
		indexSearcher = new IndexSearcher(ir); //indexSearcher object
		mfqp = new MultiFieldQueryParser(Version.LUCENE_36,multiq,new MyAnalyzer()); //create query type based on lucenes version, fields and analyze it with Porter Stemmer
		
		
		
		
	}
	
	public TopDocs search(String searchQuery,Sort sort) throws IOException, ParseException{
		query = mfqp.parse(searchQuery); //Parse query
		return indexSearcher.search(query, LuceneConstants.MAX_SEARCH,sort); //return top docs using built-in search() respecting the query
	}																		//the number of hits we allow, sorted by relevance
	
	
	public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException{
		
		return indexSearcher.doc(scoreDoc.doc); //returns document from indexSearcher object
	}
	
	public void setDefaultFieldSortScoring(boolean doTrackScores, 
		      boolean doMaxScores) {
		      indexSearcher.setDefaultFieldSortScoring(
		         doTrackScores,doMaxScores);
		   }
	

	public void close() throws IOException{
		
		indexSearcher.close();
	}
	
}
