package luceneproject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class cacmsplit {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Scanner fileIn = new Scanner(new File("/home/dimitris/cacm.all"));
		String contents = fileIn.useDelimiter("\\Z").next();
		
		String[] dc = contents.split("@");
		//System.out.println(dc.length);
		System.out.println(dc[500]);
		//System.out.println(contents);

		for(int n=1;n<dc.length;n++) {
			FileWriter fw = new FileWriter("/home/dimitris/LuceneData/record"+n+".txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(dc[n]);
			bw.close();
			
		}
		
		
		
		
		
		
		
		
		/*
		int n = 1;
		for(int i=1;i<=3204;i++) {
			String s ="";
			BufferedWriter writer = null;
			while(!fileIn.nextLine().contains(".I "+i+1) && fileIn.hasNextLine()) {
				s+=fileIn.nextLine() + "/n";
			}
			try
			{
			    writer = new BufferedWriter( new FileWriter("record"+n+".txt"));
			    writer.write(s);

			}
			catch ( IOException e)
			{
			}
			finally
			{
			    try
			    {
			        if ( writer != null)
			        writer.close( );
			    }
			    catch ( IOException e)
			    {
			    }
			}
			n++;
			
		}*/
	}

}
