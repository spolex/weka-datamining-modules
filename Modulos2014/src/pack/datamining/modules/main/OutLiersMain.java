package pack.datamining.modules.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.filters.Outliers;
import pack.datamining.modules.io.LoaderSaver;
import pack.datamining.modules.util.Strings;
import weka.core.Instances;


public class OutLiersMain 
{
	public static void main(String[] args) 
	{
		//For logs
		String LOG_TAG=OutLiersMain.class.getSimpleName().toString();
		
		Instances instances = LoaderSaver.getMyLoader().loadArff(args[0]);
		try 
		{
			Instances filteredInstances = Outliers.getFilterInstancesWithoutOutliers(instances);
			LoaderSaver.getMyLoader().saveInstances(filteredInstances, args[0] + ".WithoutOutliers.arff" );
		} 
		catch (Exception e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_OUTLIERS);
		}
	}
}
