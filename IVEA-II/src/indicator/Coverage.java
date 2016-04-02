package indicator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Coverage {

	String approximateFile= "";
	String exactFile = "";
	String approximateFun = "";
	String exactFun = "";
	private static BufferedReader paretoFrontBR;
	private static BufferedReader approximateBR;
	
	public Coverage(String approximateFile,String exactFile, String approximateFun, String exactFun)
	{
		this.approximateFile = approximateFile;
		this.exactFile = exactFile;
		this.approximateFun = approximateFun;
		this.exactFun = exactFun;
	}
	
	//计算重叠部分
	public double doCoverage() throws IOException
	{
		double value = 0.0;
		paretoFrontBR = new BufferedReader(new FileReader(exactFile));
		approximateBR = new BufferedReader(new FileReader(approximateFile));
		double overlayNum = 0;
		double exactScale = 0;
	//	double approximateScale = 0;
		String paretoFront;
	//	String approximate;
		
		while((paretoFront = paretoFrontBR.readLine()) != null)
		{
			String approximate;
			while((approximate = approximateBR.readLine()) != null)
			{
				if(paretoFront.equals(approximate))
				{
					overlayNum++;
					break;
				}
			}
			exactScale++;
		}
		System.out.println("coverage:" + overlayNum + ":" + exactScale);
		value = overlayNum/exactScale;
		return value;
	}
	
	//计算精确解中不被优化解支配的比例
	public double nodominatedNum() throws IOException
	{
		double value = 0.0;
		paretoFrontBR = new BufferedReader(new FileReader(exactFun));
		approximateBR = new BufferedReader(new FileReader(approximateFun));
		double dominateNum = 0;
		double exactScale = 0;
		
		String exactFun;
		while((exactFun = paretoFrontBR.readLine()) != null)
		{
			String approximateFun;
			while((approximateFun = approximateBR.readLine()) != null)
			{
				if(isDominate(exactFun,approximateFun))
				{
					dominateNum++;
					break;
				}
			}
			exactScale++;
		}
		
		System.out.println("dominateNum:" + dominateNum + ":" + exactScale);
		value = (exactScale-dominateNum)/exactScale;
		return value;
	}
	
	//计算优化解中支配精确解支配的比例
	public double dominateNum() throws IOException
	{
		double value = 0.0;
		paretoFrontBR = new BufferedReader(new FileReader(exactFun));
		approximateBR = new BufferedReader(new FileReader(approximateFun));
		double dominateNum = 0;
		double exactScale = 0;
		
		String exactFun;
		while((exactFun = paretoFrontBR.readLine()) != null)
		{
			String approximateFun;
			while((approximateFun = approximateBR.readLine()) != null)
			{
				if(isDominate(approximateFun,exactFun))
				{
					dominateNum++;
					break;
				}
			}
			exactScale++;
		}
		
		System.out.println("dominateNum:" + dominateNum + ":" + exactScale);
		value = dominateNum/exactScale;
		return value;
	}
	
	//if X dominates Y, return true; else, false;
	private boolean isDominate(String X, String Y)
	{
		double[] X_obj = parseObj(X);
		double[] Y_obj = parseObj(Y);
		boolean isAbove = false;
		for(int i = 0; i < X_obj.length; i++)
		{
			if(X_obj[i] > Y_obj[i])
			{
				return false;
			}
			else if(X_obj[i] < Y_obj[i])
			{
				isAbove = true;
			}
		}
		if(isAbove)
		{
			return true;
		}
		return false;
	}
	
	private double[] parseObj(String X)
	{
		int numItem = X.split(" ").length;
		String[] items = X.split(" ");
		double[] line = new double[numItem];
		for(int i = 0; i < numItem; i++)
		{
			line[i] = Double.parseDouble(items[i]);
		}
		return line;
	}
}
