package pack.datamining.modules.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.filters.Balance;
import pack.datamining.modules.io.LoaderSaver;
import weka.core.Instances;

public class ResampleMain {

	public static void main(String[] args) 
	{
				//For logs
				String LOG_TAG=ResampleMain.class.getSimpleName().toString();
				
				LoaderSaver.getMyLoader(args[0]).loadArff();
				Instances instances = LoaderSaver.getMyLoader(null).getInstances();
				Instances filteredInstances=null;
				try 
				{
					instances.setClassIndex(instances.numAttributes()-1);
					filteredInstances = Balance.getBalancedInstances(instances);					
				} 
				catch (Exception e) 
				{
					Logger.getLogger(LOG_TAG).log(Level.SEVERE, e.getMessage());
				}
				LoaderSaver.getMyLoader(null).saveInstances(filteredInstances);
	}

}
