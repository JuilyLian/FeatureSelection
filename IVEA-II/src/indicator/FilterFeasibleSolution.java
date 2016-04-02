package indicator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import util.FileHandle;
import util.SeedsGen;

public class FilterFeasibleSolution {
	//this class is used to discard the unfeasible solutions
	// only feasible solutions are left to generate the HV, IGD,Spread and epsilon
	
	//originalData: the data before filter,objective file
	//filteredPath: all feasible solutions of a feature model by an algorithm are written in a single file
	
	//do a small change, any single obj file also includes the objectives (delete the duplication)
	public static void FilterSolution(String originalData, String binarySolution,String filteredPath, String filteredBinaryFile) throws IOException
	{
		FileHandle fileHandler = new FileHandle();
		File originalDir = new File(originalData);
		
		 
		if(originalDir.isDirectory())
		{
			File[] funs = originalDir.listFiles();
			for(int i = 0; i < funs.length; i++)
			{
				File curFun = funs[i];
				String curFunPath = curFun.getAbsolutePath();
				String curFunName = curFun.getName();
				//保证无重复的正确解
				ArrayList<String> binarySolutions = new ArrayList<String>();
				
				String binarySolutionPath = binarySolution + "/" + curFunName;
				String filteredSingleFile = filteredPath + "/" + curFunName;
				String binarySingleFile = filteredBinaryFile + "/" + curFunName;
				
				InputStream is = new FileInputStream(curFunPath);
				String line;
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				
				InputStream binaryIs = new FileInputStream(binarySolutionPath);
				String binaryLine;
				BufferedReader BinaryReader = new BufferedReader(new InputStreamReader(binaryIs));
				
				while((line = reader.readLine()) != null && (binaryLine = BinaryReader.readLine()) != null)
				{
				   int spaceIndex = line.indexOf(" ");
				  
				   if(line.length() == 0)
				   {
					   continue;
				   }
				   String firstObj = line.substring(0, spaceIndex);
				   if(firstObj.equals("0.0"))
				   {
					  // System.out.println(curFunPath);
					   if(binarySolutions.contains(binaryLine))
					   {
						   continue;
					   }
					   else
					   {
						   binarySolutions.add(binaryLine);
						 //  line = line.substring(4, line.length());
						   fileHandler.AppendWrite(filteredSingleFile, line);
						   fileHandler.AppendWrite(binarySingleFile, binaryLine);
						//   System.out.println(binaryLine);
					   }
					   
					   //can repeat
					   //fileHandler.AppendWrite(filteredPath, line);
				   }
				}
			}
		}
	}

	//同时获取有效解的基因串
	private static void FilterSolutionFile(String original, String storeFile) throws IOException
	{
		String varDir = original + "//binarySolution";
		String funDir = original + "//objectivesSplit";
		
		String filteredFunDir = storeFile + "//feasibleObjectives";
		String filteredVarDir = storeFile + "//feasibleVar";
		
		FileHandle fileHandler = new FileHandle();
		File varFileDir = new File(varDir);
		File funFileDir = new File(funDir);
		if(funFileDir.isDirectory())
		{
			File[] funs = funFileDir.listFiles();
			for(int i = 0; i < funs.length; i++)
			{
				File curFun = funs[i];
				String curFunName = curFun.getName();
				String curFunPath = curFun.getAbsolutePath();
				String curVarPath = varDir + "//" + curFunName;
				
//				String filteredFunPath = filteredFunDir + "//" + curFunName;
//				String filteredVarPath = filteredVarDir + "//" + curFunName;
				BufferedReader varBR = new BufferedReader(new FileReader(curVarPath));
				BufferedReader funBR = new BufferedReader(new FileReader(curFunPath));
				String varLine;
				String funLine;
				while((funLine = funBR.readLine()) != null && (varLine = varBR.readLine()) != null)
				{
					 int spaceIndex = funLine.indexOf(" ");
					  
					   if(funLine.length() == 0)
					   {
						   continue;
					   }
					   String firstObj = funLine.substring(0, spaceIndex);
					   if(firstObj.equals("0.0"))
					   {
						   String filteredFunLine = funLine.substring(spaceIndex+1, funLine.length());
						   fileHandler.AppendWrite(filteredFunDir,curFunName, filteredFunLine);
						   fileHandler.AppendWrite(filteredVarDir,curFunName, varLine);
					   }
				}
			}
		}
				
		
//		if(originalDir.isDirectory())
//		{
//			File[] funs = originalDir.listFiles();
//			for(int i = 0; i < funs.length; i++)
//			{
//				File curFun = funs[i];
//				String curFunPath = curFun.getAbsolutePath();
//				String curFunName = curFun.getName();
//				
//				String filteredPath = storeFile + "/" + curFunName;
//				
//				InputStream is = new FileInputStream(curFunPath);
//				String line;
//				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//				while((line = reader.readLine()) != null)
//				{
//					if(line.length() == 0)
//					{
//						continue;
//					}
//				   int spaceIndex = line.indexOf(" ");
//				  
//				   fileHandler.AppendWrite(filteredPath, line.substring(spaceIndex + 1, line.length()));
//				}
//			}
//		}
	}
	
	public static void main(String[] args) throws IOException
	{
		//产生30个种子，从ICSE2015产生的解决方案中找
//		String tacticDir = "D:\\360云盘\\myPapers\\featureSelection\\SANERJournal\\DataSet\\WebProtal-ICSE2015";
//		String originalData = tacticDir + "\\objectivesSplit";
//		String binaryData = tacticDir + "\\binarySolution";
//		String filteredSolution = tacticDir + "\\5ObjectivesDistinct";
//		String filteredBinarySolution = tacticDir + "\\feasibleBinary";
//		String storeSeedPath = tacticDir + "\\seeds.txt";
//		//FilterSolutionFile(originalData,filteredSolution);
//		FilterSolution(originalData, binaryData, filteredSolution,filteredBinarySolution);
//		SeedsGen seedsGenerator = new SeedsGen();
//		seedsGenerator.GenSeedsFile(filteredBinarySolution, storeSeedPath);
		/////////////////////////////////////////////////////////////////////////////////////////////////
		
		//产生算法对每个特征模型的正确解，可以有重复
//		String tacticDir = "D:\\360云盘\\myPapers\\featureSelection\\SANERJournal\\DataSet\\WebProtal-ICSE2015";
//		String objectives = tacticDir + "\\objectivesSplit";
//		String feasibleObjective = tacticDir + "\\feasibleObjectives";
//		FilterSolutionFile(objectives, feasibleObjective);
		
//		String tacticDir = "D:\\360云盘\\myPapers\\featureSelection\\SANERJournal\\Baselines\\WebProtal-ICSE2015";
//		String storePath = tacticDir;
//		FilterSolutionFile(tacticDir,storePath);
		
		//String originalData, String binarySolution,String filteredPath, String filteredBinaryFile
//		String solutionDir = "D:\\360云盘\\myPapers\\featureSelection\\SANERJournal\\mine-2Dfitness+CD\\result\\ecos-icse11-Mine\\objectives";
//		String binarySolution = "D:\\360云盘\\myPapers\\featureSelection\\SANERJournal\\mine-2Dfitness+CD\\result\\ecos-icse11-Mine\\binarySolution";
//		String filteredPath = "D:\\360云盘\\myPapers\\featureSelection\\SANERJournal\\mine-2Dfitness+CD\\result\\ecos-icse11-Mine\\objOneSoluDistinct";
//		String storePath = "D:\\360云盘\\myPapers\\featureSelection\\SANERJournal\\mine-2Dfitness+CD\\result\\ecos-icse11-Mine\\varOneSoluDistinct";
//		FilterSolution(solutionDir,binarySolution,filteredPath,storePath);
		String original = "D:\\360云盘\\myPapers\\featureSelection\\SANERJournal\\Baselines\\uClinux-ICSE2015";
		String storePath = original;
		FilterSolutionFile(original,storePath);
	}
}
