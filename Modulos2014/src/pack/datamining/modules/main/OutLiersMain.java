package pack.datamining.modules.main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.filters.Outliers;
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
			Calendar calendar = new GregorianCalendar(); // Fecha y hora actuales.
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm"); // Formato de la fecha.
			String dateS = dateFormat.format(calendar.getTime()); // Fecha y hora actuales formateadas.
			Instances filteredInstances = Outliers.getFilterInstancesWithoutOutliers(instances);
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd-HH");
			String dateD =  dateFormat1.format(new GregorianCalendar().getTime());
			String filePath="data/Outliers_"+dateD+"/"+args[0].substring(4, args[0].length()-5)+"_"+dateS+"_WithoutOutliers.arff";
			LoaderSaver.getMyLoader().saveInstances(filteredInstances, filePath );
		} 
		catch (Exception e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_OUTLIERS+e.getMessage());
		}
	}
}
