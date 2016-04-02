package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CalculateFeatureNum {

	public static void main(String[] args) throws IOException
	{
		String filePath = "D:\\360тфел\\myPapers\\featureSelection\\SANERJournal\\FM-30Seeds\\uClinux.dimacs.richseed";
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = "";
		int lineNum = 0;
		String countContent = "";
		while((line = br.readLine()) != null)
		{
			String[] terms = line.split(" ");
			int count = 0;
			for(int i = 0; i < terms.length; i++)
			{
				String curTerm = terms[i];
				if(!curTerm.startsWith("-"))
				{
					count++;
				}
			}
			lineNum++;
			System.out.println("lineNum:" + lineNum + "count:" + count);
			countContent += count + ",";
		}
		System.out.println(countContent);
	}
}
