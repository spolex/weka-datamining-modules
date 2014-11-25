package pack.datamining.modules.main;

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
			dateFormat = new SimpleDateFormat("yyyyMMddHH");
			String dateD =  dateFormat.format(calendar);
			String filePath=dateD+"/"+args[0].substring(0, args[0].length()-5)+"_"+dateS+"_WithoutOutliers.arff";
			LoaderSaver.getMyLoader().saveInstances(filteredInstances, filePath );
		} 
		catch (Exception e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_OUTLIERS);
		}
	}
}
