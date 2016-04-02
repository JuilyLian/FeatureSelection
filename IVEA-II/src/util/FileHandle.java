package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jmetal.experiments.Experiment;

public class FileHandle {

	public void WriteFile(String storePath, String filePath,String content) throws IOException// throws IOException
	{
		//File storeFile = new File(storePath);
		File targetFile = new File(storePath);
		if(!targetFile.exists())
		{
			targetFile.mkdirs();
		}
		FileWriter fw;
		try {
			String finalPath = storePath + "/" + filePath;
			fw = new FileWriter(finalPath);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	
	public void AppendWrite(String fileDir, String fileName, String appendContent )
	{
		File targetDir = new File(fileDir);
		if(!targetDir.exists())
		{
			targetDir.mkdirs();
		}
		FileWriter os;
		try {
			String storePath = fileDir + "\\" + fileName;
			File newFile = new File(storePath);
			os = new FileWriter(storePath, true);
			os.write("" + appendContent + "\r\n");
			os.close();
		} catch (IOException ex) {
			Logger.getLogger(Experiment.class.getName()).log(Level.SEVERE, null, ex);
		}		
	}
	
	//¿É×·¼ÓÐ´
	public void AppendWrite(String storePath, String appendContent)
	{
		FileWriter os;
		try {
			File newFile = new File(storePath);
			os = new FileWriter(storePath, true);
			os.write("" + appendContent + "\r\n");
			os.close();
		} catch (IOException ex) {
			Logger.getLogger(Experiment.class.getName()).log(Level.SEVERE, null, ex);
		}			
	}
}
