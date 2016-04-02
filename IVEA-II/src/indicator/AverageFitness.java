package indicator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AverageFitness {
	
	public static void main(String[] args) throws IOException
	{
		String objPath = "D:\\360тфел\\myPapers\\featureSelection\\SANERJournal\\Baselines\\freebsd-icse11-ICSE2015\\5objsum.txt";
		BufferedReader br = new BufferedReader(new FileReader(objPath));
		String line ;
		int count = 0;
		double amount = 0;
		while((line = br.readLine()) != null)
		{
			if(line.length() == 0)
			{
				continue;
			}
			String[] items = line.split(" ");
			for(int i = 0; i < items.length; i++)
			{
				amount += Double.parseDouble(items[i]); 
			}
			count++;
		}
		System.out.println("amount:" + amount + "count:" + count);
		System.out.println("averageObj:" + amount/count);
		
	}

}
