package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class SeedsGen {

	//public static void main(String[] args) throws IOException {
	public void GenSeedsFile(String binaryFilePath, String storeSeedPath) throws IOException
	{
		// TODO Auto-generated method stub

//		String timePath = "D:\\360тфел\\myPapers\\featureSelection\\SANERJournal\\DataSet\\fiasco-Mine\\time.txt";
//		FilterEmptyLine(timePath);
		
//		String binaryFilePath = "D:\\360тфел\\myPapers\\featureSelection\\SANERJournal\\DataSet\\2.6.28.6-icse11-ICSE2015\\feasibleBinary";
//		String storeSeedPath = "D:\\360тфел\\myPapers\\featureSelection\\SANERJournal\\DataSet\\2.6.28.6-icse11-ICSE2015\\seeds.txt";
//		
		File binaryFileDir = new File(binaryFilePath);
		File[] binaryFiles = binaryFileDir.listFiles();
		ArrayList<String> binaryList = new ArrayList<String>();
		
		int count = 0;
		for(int i = 0; i < binaryFiles.length; i++)
		{
			File curFile = binaryFiles[i];
			String curFilePath = curFile.getAbsolutePath();
			InputStream is = new FileInputStream(curFilePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			FileHandle fileHandler = new FileHandle();
			String line;
			while((line = br.readLine()) != null)
			{
				if(binaryList.contains(line))
				{
					continue;
				}
				else
				{
					String integerStr = BinaryToInteger(line);
					fileHandler.AppendWrite(storeSeedPath, integerStr);
					binaryList.add(line);
					count++;
					if(count == 30)
					{
						return;
					}
				}
				
				
			}
		}
		
	}
	
	private String BinaryToInteger(String line)
	{
		char[] chars = line.toCharArray();
		String integerStr = "";
		for(int i = 0; i < chars.length; i++)
		{
			char curBit = chars[i];
			int featureID = i+1;
			String curInteger = featureID + "";
			String curStr = curBit == '0' ? "-" + curInteger: curInteger;
			integerStr += curStr + " ";
		}
		return integerStr;
	}
	
	private static void FilterEmptyLine(String fileName) throws IOException
	{
		String filteredContent = "";
		double aveElapsed = 0;
		String elapsed = "";
		InputStream is = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		while((line = br.readLine()) != null)
		{
			if(!line.equals(""))
			{
				//filteredContent += line + "\r\n";
			}
			if(line.startsWith("Evaluations"))
			{
				int commaIndex = line.indexOf(":")+1;
				String curElapse = line.substring(commaIndex, line.length());
				aveElapsed += Double.parseDouble(curElapse);
				elapsed += curElapse + " ";
			}
		}
		filteredContent += elapsed;
		filteredContent += aveElapsed/30;
		System.out.println(aveElapsed/30);
		fileName = fileName.substring(0, fileName.length()-4);
		String storePath = fileName + "filtered.txt";
		FileHandle fileHandler = new FileHandle();
		fileHandler.WriteFile(storePath,"" , filteredContent);
	}

}
