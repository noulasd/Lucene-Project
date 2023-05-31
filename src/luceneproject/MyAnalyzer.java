package luceneproject;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.util.Version;


class MyAnalyzer extends Analyzer { //Porter Stemmer with stop words from common_words
	  public final TokenStream tokenStream(String fieldName, Reader reader) {
		  TokenStream stream = null;
		  
		  try {
		  
			FileReader reader2 = new FileReader("/home/dimitris/common_words");
		  
			stream = new StopFilter(Version.LUCENE_36,new LowerCaseTokenizer(Version.LUCENE_36,reader),
				        WordlistLoader.getWordSet(reader2, Version.LUCENE_36));
		  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
		  return new PorterStemFilter(stream);
	  }

}