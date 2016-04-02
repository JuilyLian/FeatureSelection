package indicator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class A12 {
	
	private static BufferedReader reader;

	public static void main(String[] args) throws NumberFormatException, IOException
	{
		
		//////////////////////////////////////////////////////
		//single indicator compare
		/////////////////////////////////////////////////////
//		String myFile = "D:\\360云盘\\myPapers\\featureSelection\\SANERJournal\\CompareResult\\FullcompareRepeatDominate\\MinevsICSE\\ICSE2015-uClinux\\IGD";
//		String other = "D:\\360云盘\\myPapers\\featureSelection\\SANERJournal\\CompareResult\\FullcompareRepeatDominate\\MinevsICSE\\Mine-uClinux\\IGD";
//		
		String myFile = "D:\\360云盘\\myPapers\\featureSelection\\SANERJournal\\full+30seeds\\2.6.28.6-icse11-Mine30Seeds\\indicators\\IGD";
		String other = 	"D:\\360云盘\\myPapers\\featureSelection\\SANERJournal\\2dfitness+cd+oneSeed\\2.6.28.6-icse11-OneSeed\\indicators\\IGD";
		ArrayList<Double> myData = ReadFile(myFile);
		ArrayList<Double> otherData = ReadFile(other);
		double a12 = A12(myData,otherData);
		System.out.println(a12);
		//////////////////////////////over single indicator compare
		
		/////////////////////5 objectives compare
//		String ourFile = "D:\\360云盘\\myPapers\\featureSelection\\SANERJournal\\mine-2dfitness+cd+30seeds\\uClinux-Mine\\overallObjs.txt";
//		String otherFile = "D:\\360云盘\\myPapers\\featureSelection\\SANERJournal\\Baselines\\uClinux-ICSE2015\\overallObjs.txt";
		
//		ArrayList<ArrayList<Double>> ourObjectives = ReadSourceFile(ourFile);
//		ArrayList<ArrayList<Double>> otherObjectives = ReadSourceFile(otherFile);
//		double A12_1 = A12(ourObjectives.get(0),otherObjectives.get(0));
//		double A12_2 = A12(ourObjectives.get(1),otherObjectives.get(1));
//		double A12_3 = A12(ourObjectives.get(1),otherObjectives.get(2));
//		double A12_4 = A12(ourObjectives.get(1),otherObjectives.get(3));
//		System.out.println("Obj1:" + (1- A12_1));
//		System.out.println("Obj2:" + (1- A12_2) );
//		System.out.println("Obj3:" + (1- A12_3));
//		System.out.println("Obj4:" + (1- A12_4));
	}

	//only compare the valid solutions
	private static ArrayList<ArrayList<Double>> ReadSourceFile(String filePath) throws IOException
	{
		ArrayList<ArrayList<Double>> objectives = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> obj1 = new ArrayList<Double>();
		ArrayList<Double> obj2 = new ArrayList<Double>();
		ArrayList<Double> obj3 = new ArrayList<Double>();
		ArrayList<Double> obj4 = new ArrayList<Double>();
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = "";
		while((line = br.readLine()) != null)
		{
			if(line.length() == 0)
			{
				continue;
			}
			String[] terms = line.split(" ");
			String term0 = terms[0];
			String term1 = terms[1];
			String term2 = terms[2];
			String term3 = terms[3];
			
			double term0_v = Double.parseDouble(term0);
			double term1_v = Double.parseDouble(term1);
			double term2_v = Double.parseDouble(term2);
			double term3_v = Double.parseDouble(term3);
			obj1.add(term0_v);
			obj2.add(term1_v);
			obj3.add(term2_v);
			obj4.add(term3_v);
		}
		objectives.add(obj1);
		objectives.add(obj2);
		objectives.add(obj3);
		objectives.add(obj4);
		return objectives;
	}
	//A12>0.5时，表明前一个算法具有更高的机会获得比第二个算法高的fitness value
	//A12=0.5时，表明两个算法是等效的
	private static double A12(ArrayList<Double> a, ArrayList<Double> b)
	{
		double greatNum = 0;
		double equalNum = 0;
		int a_len = a.size();
		int b_len = b.size();
		System.out.println(a_len + ":" + b_len);
		for(int i = 0; i < a_len; i++)
		{
			double a_value = a.get(i);
			for(int j = 0; j < b_len; j++)
			{
				double b_value = b.get(j);
				if(a_value > b_value)
				{
					greatNum++;
				}
				else if(a_value == b_value)
				{
					equalNum++;
				}
			}
		}
		double temp = a_len * b_len;
		//int 
		double A12 = (greatNum/temp) + 0.5 * (equalNum/temp);
		return A12;
	}
	
	private static ArrayList<Double> ReadFile(String filePath) throws NumberFormatException, IOException
	{
		ArrayList<Double> list = new ArrayList<Double>();
		InputStream is = new FileInputStream(filePath);
		reader = new BufferedReader(new InputStreamReader(is));
		String line;
		while((line = reader.readLine()) != null)
		{
			if(!line.equals(""))
			{
				double curValue = Double.parseDouble(line);
				list.add(curValue);
			}
		}
		return list;
	}
}
