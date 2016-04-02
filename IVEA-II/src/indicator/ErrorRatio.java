package indicator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ErrorRatio {

	
	
	private static BufferedReader paretoFrontBR;
	private static BufferedReader approximateBR;

//	public static void main(String[] args) throws IOException
//	{
//		String approximateFile = "";
//		String exactFile = "D:\\360тфел\\myPapers\\featureSelection\\SANERJournal\\exactSolutions\\OptimalFileVar";
//		doCoverage(approximateFile, exactFile);
//	}
	
	String approximateFile= "";
	String exactFile = "";
	
	public ErrorRatio(String approximateFile,String exactFile)
	{
		this.approximateFile = approximateFile;
		this.exactFile = exactFile;
	}
	
	public double doErrorRatio() throws IOException
	{
		double value = 0.0;
		paretoFrontBR = new BufferedReader(new FileReader(exactFile));
		approximateBR = new BufferedReader(new FileReader(approximateFile));
		double overlayNum = 0;
		double approximateScale = 0;
		String paretoFront;
		String approximate;
		while((approximate = approximateBR.readLine())!= null)
		{
			while((paretoFront = paretoFrontBR.readLine()) != null)
			{
				if(approximate.equals(paretoFront))
				{
					overlayNum++;
					break;
				}
			}
			approximateScale++;
		}
		
		System.out.println("errorRatio:" +overlayNum + ":" + approximateScale);
		value = (approximateScale-overlayNum)/approximateScale;

		
		return value;
	}
}
