package indicator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jmetal.experiments.Experiment;
import jmetal.qualityIndicator.Epsilon;
import jmetal.qualityIndicator.GeneralizedSpread;
import jmetal.qualityIndicator.GenerationalDistance;
import jmetal.qualityIndicator.Hypervolume;
import jmetal.qualityIndicator.InvertedGenerationalDistance;
import jmetal.qualityIndicator.R2;
import jmetal.qualityIndicator.Spread;

public class CalculateIndicator {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String qualityIndicatorFile = "D:\\360‘∆≈Ã\\myPapers\\featureSelection\\SANERJournal\\full+30seeds\\uClinux-Mine30Seeds\\indicators";
		
		//see the implements in jmetal.Experiment.java
		//make sure the meanings of solutionFrontFile and paretoFrontPath
		String solutionFrontDir = "D:\\360‘∆≈Ã\\myPapers\\featureSelection\\SANERJournal\\full+30seeds\\uClinux-Mine30Seeds\\objectives";
//		String binaryDir = "D:\\360‘∆≈Ã\\myPapers\\featureSelection\\SANERJournal\\mine-2dfitness+cd+30seeds\\fiasco-Mine\\binarySolution";
		
		String paretoFrontPath = "D:\\360‘∆≈Ã\\myPapers\\featureSelection\\SANERJournal\\full+30seeds\\uClinux-Mine30Seeds\\sum.txt";
//		String paretoFrontVar = "D:\\360‘∆≈Ã\\myPapers\\featureSelection\\SANERJournal\\exactSolutions\\exactParetoFronts\\fiasco-OptimalFilevarNoIndex";
		//String paretoFrontPath = solutionFrontDir;
		String HVContent = "";
		String IGDContent = "";
		String EpsilonContent = "";
		String SpreadContent = "";
		String GSpreadContent = "";
//		String errorRatioContent = "";
//		String coverageContent = "";
//		String dominateCoverageContent = "";
//		String nodominatedCoverageContent = "";
		for(int runNum = 0; runNum < 30; runNum++)
		{
			String solutionFrontFile = solutionFrontDir + "/" + runNum + ".txt";
//			String binaryFile = binaryDir + "/" + runNum + ".txt";
			File curSolutionFrontFile = new File(solutionFrontFile);
			if(!curSolutionFrontFile.exists())
			{
				continue;
			}
		//    String solutionFrontFile = solutionFrontDir;
			double HV = 0;
			Hypervolume HVIndicators = new Hypervolume();
			double[][] solutionFront =
					HVIndicators.utils_.readFront(solutionFrontFile);
			double[][] trueFront =
					HVIndicators.utils_.readFront(paretoFrontPath);
			HV = HVIndicators.hypervolume(solutionFront, trueFront, trueFront[0].length);
			HVContent += HV + "\r\n";
			
			double SpreadValue = 0;
			Spread SpeadIndicators = new Spread();
		//	int len = trueFront[0].length;
			SpreadValue = SpeadIndicators.spread(solutionFront, trueFront, trueFront[0].length);
			SpreadContent += SpreadValue + "\r\n";
			
			
			double IGDValue = 0;
			InvertedGenerationalDistance IGDIndicators = new InvertedGenerationalDistance();
			IGDValue = IGDIndicators.invertedGenerationalDistance(solutionFront, trueFront, trueFront[0].length);
			IGDContent += IGDValue + "\r\n";
			


			double EpsilonValue = 0;
			Epsilon EpsilonIndicators = new Epsilon();
			EpsilonValue = EpsilonIndicators.epsilon(solutionFront, trueFront, trueFront[0].length);
			EpsilonContent += EpsilonValue + "\r\n";
			
			double GSpread = 0;
			GeneralizedSpread GSpreadIndicators = new GeneralizedSpread();
			GSpread = GSpreadIndicators.generalizedSpread(solutionFront, trueFront, trueFront[0].length);
			GSpreadContent += GSpread + "\r\n";
		

//			//ª˘”⁄binaryŒƒº˛Ω¯––º∆À„
//			double errorRatio = 0;
//			ErrorRatio errorRatioIndicator = new ErrorRatio(binaryFile,paretoFrontVar);
//			errorRatio = errorRatioIndicator.doErrorRatio();
//			errorRatioContent += errorRatio + "\r\n";
//			
//			//String approximateFile,String exactFile, String approximateFun, String exactFun
//			double coverageValue = 0;
//			double dominateCoverage = 0;
//			double nodominatedCoverage = 0;
//			Coverage coverage = new Coverage(binaryFile,paretoFrontVar,solutionFrontFile,paretoFrontPath);
//			coverageValue = coverage.doCoverage();
//			nodominatedCoverage = coverage.nodominatedNum();
//			dominateCoverage = coverage.dominateNum();
//			
//			coverageContent += coverageValue + "\r\n";
//			dominateCoverageContent += dominateCoverage + "\r\n";
//		    nodominatedCoverageContent += nodominatedCoverage + "\r\n";
		}

		WriteFile(qualityIndicatorFile, "HV", HVContent);
		WriteFile(qualityIndicatorFile, "SPREAD",SpreadContent);
		WriteFile(qualityIndicatorFile, "IGD", IGDContent);
		WriteFile(qualityIndicatorFile, "EPSILON", EpsilonContent);
		WriteFile(qualityIndicatorFile,"GSpread",GSpreadContent);
//		WriteFile(qualityIndicatorFile,"ErrorRatio",errorRatioContent);
//		WriteFile(qualityIndicatorFile,"Coverage", coverageContent);
//		WriteFile(qualityIndicatorFile,"noDominated", nodominatedCoverageContent);
//		WriteFile(qualityIndicatorFile,"DominateCoverage",dominateCoverageContent);
	}
		
	private static void WriteFile(String storeDir,String fileName, String content)
	{
		//write
		File storeFile = new File(storeDir);
		if(!storeFile.exists())
		{
			storeFile.mkdirs();
		}
		FileWriter os;
		try {
			String filePath = storeFile + "/" + fileName;
			os = new FileWriter(filePath);
			os.write(content);
			os.close();
		} catch (IOException ex) {
			Logger.getLogger(Experiment.class.getName()).log(Level.SEVERE, null, ex);
		}		
	}

}
