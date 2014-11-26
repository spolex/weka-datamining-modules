package pack.datamining.modules.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.io.LoaderSaver;
import pack.datamining.modules.util.Strings;
import weka.core.Instances;


public class OutLiersMain 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		//For logs
		String LOG_TAG=OutLiersMain.class.getSimpleName().toString();
		
		Instances instances = LoaderSaver.getMyLoader().loadArff(args[0]);
		try 
		{
			String pathFile=args[0];
			LoaderSaver.extractFileInDateDir(instances, 0, pathFile.length(), pathFile,"Outlier");
		} 
		catch (Exception e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_OUTLIERS+e.getMessage());
		}
	}
}
