package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import jmetal.experiments.Experiment;

public class FormatTransfor {
	
	//temp
	//由于误操作，ICSE2015, ASE2013产生的数据中一个选择方案的数据没有单独成行
	public static void main(String[] args) throws IOException
	{
//		String filePath = "D:/360云盘/myPapers/featureSelection/SANERJournal/DataSet/2.6.28.6-icse11-ICSE2015/objectives";
//		String storePath = "D:/360云盘/myPapers/featureSelection/SANERJournal/DataSet/2.6.28.6-icse11-ICSE2015/objectivesSplit";
//		TransferFormat(filePath, storePath);
		String objPath = "F:\\program\\eclipse\\MOEAs\\fmFile\\ParetoFronts\\uClinux-OptimalFileobj";
		String binaryPath = "F:\\program\\eclipse\\MOEAs\\fmFile\\ParetoFronts\\uClinux-OptimalFilevar";
		FilterObjIndex(objPath);
		FilterVarIndex(binaryPath);
	}
	
	private static void TransferFormat(String filePath, String storePath) throws IOException
	{
		File storeDir = new File(storePath);
		if(!storeDir.exists())
		{
			storeDir.mkdirs();
		}
		
		FileHandle fileHandler = new FileHandle();
		String fileStorePath;
		File dir = new File(filePath);
		File[] files = dir.listFiles();
		for(int i = 0; i < files.length; i++)
		{
			File curFile = files[i];
			String curFileName = curFile.getName();
			String curFilePath = curFile.getAbsolutePath();
			
			fileStorePath = storePath;
			fileStorePath += "/" + curFileName;
			
			InputStream is = new FileInputStream(curFilePath);
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			while((line = reader.readLine()) != null)
			{
				//System.out.println(line);
				
				String[] strs = line.split(" ");

				String newLine = "";
				int lenAmount = strs.length;
				if(lenAmount%5 != 0)
				{
					System.out.println("check the fun file!");
				}
				
				for(int numCount = 0; numCount < lenAmount; numCount++)
				{
					newLine += strs[numCount] + " ";
					if((numCount+1) % 5==0)
					{
						fileHandler.AppendWrite(fileStorePath,newLine);
						newLine = "";
					}
				}
			}		
			
			while(line != null)
			{
				String[] strs = line.split(" ");						
				
				String newLine = "";
				int lenAmount = strs.length;
				if(lenAmount % 5 != 0)
				{
					System.out.println("check the fun file!");
				}
				
				for(int numCount = 0; numCount < lenAmount; numCount++)
				{
					newLine += strs[numCount] + " ";
					if((numCount+1) % 5==0)
					{
						fileHandler.AppendWrite(fileStorePath,newLine);
						newLine = "";
					}
				}
			}
		}
	}	
	
	private static void FilterObjIndex(String filePath) throws IOException
	{
		String storePath = filePath + "NoIndex";
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line;
		while((line = br.readLine()) != null)
		{
			int spaceIndex = line.indexOf(" ") + 1;
			String filteredLine = line.substring(spaceIndex, line.length());
			FileHandle fileHandler = new FileHandle();
			fileHandler.AppendWrite(storePath, filteredLine);
		}
	}
	
	private static void FilterVarIndex(String filePath) throws IOException
	{
		String storePath = filePath + "NoIndex";
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line;
		while((line = br.readLine()) != null)
		{
			if(line.contains(" "))
			{
				int spaceIndex = line.indexOf(" ") + 1;
				String filteredLine = line.substring(spaceIndex, line.length());
				FileHandle fileHandler = new FileHandle();
				fileHandler.AppendWrite(storePath, filteredLine);
			}
		}
	}
}
