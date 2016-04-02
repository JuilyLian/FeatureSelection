package indicator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MediateAverage {

	public static void main(String[] args) throws NumberFormatException, IOException
	{
		String filePath = "D:\\360тфел\\myPapers\\featureSelection\\SANERJournal\\Baselines\\ecos-icse11-ICSE2015\\indicators\\HV";
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = "";
		int count = 0;
		float sum = 0;
		ArrayList<Float> values = new ArrayList<Float>();
		while((line = br.readLine()) != null)
		{
			float value = Float.parseFloat(line);
			System.out.println(value);
			values.add(value);
			sum += value;
			count++;
		}
		float average = sum/count;
		System.out.println("average:" + average);
		float mediateValue = mediate(values);
		System.out.println("mediate:" + mediateValue);
		
	}
	
	private static float mediate(ArrayList<Float> valueList)
	{
		float mediate = 0;
		//sort first
		ArrayList<Float> sortedList = new ArrayList<Float>();
//		sortedList.add(valueList.get(0));
//		sortedList.a
		int leastIndex = 0;
		int count = 1;
		int listSize = valueList.size();
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		float leastValue = 0;
		while(count <= listSize)
		{
			leastValue = valueList.get(0);
			for(int i = 1; i < valueList.size(); i++)
			{
				float curValue = valueList.get(i);
				
				if(curValue <= leastValue && !indexList.contains(i))
				{
					leastIndex = i;
					leastValue = curValue;
				}
			}
			
			sortedList.add(leastValue);
			indexList.add(leastIndex);
//			valueList.remove(leastIndex);
			count++;
		}
		
		//calculate mediate
		if(listSize % 2 == 0)
		{
			int middle_left = listSize / 2;
			int middle_right = middle_left + 1;
			mediate = (sortedList.get(middle_right) + sortedList.get(middle_left)) / 2;
		}
		else
		{
			int middleIndex = (listSize + 1)/2;
			mediate = sortedList.get(middleIndex);
		}
		return mediate;
	}
}
