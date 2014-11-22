package pack.datamining.modules.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.filters.Discretization;
import pack.datamining.modules.io.LoaderSaver;
import pack.datamining.modules.util.Strings;
import weka.core.*;

public class DiscretizeMain {

	public static void main(String[] args) 
	{
		String LOG_TAG= DiscretizeMain.class.getSimpleName().toString();
		
		Instances instances = LoaderSaver.getMyLoader().loadArff(args[0]);
		int intervalos = 10;
		int pos;
		
		if (args[1] != null)
		{
			intervalos = Integer.valueOf(args[1]);

		}
		
		if (args[2] == null)
		{
			pos = instances.numAttributes()-1;
		}
		else
		{
			pos = Integer.valueOf(args[2]);
		}

		try 
		{
			Instances filteredInstances = Discretization.getDiscretized(instances,intervalos ,pos);
			LoaderSaver.getMyLoader().saveInstances(filteredInstances, args[0] + ".DiscretizedClass.arff" );
		} 
		catch (Exception e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_OUTLIERS+e.getMessage());
		}
	

	}

}
