package pack.datamining.modules.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
				System.out.println("Balanceando instancias....");
				
				Instances instances = LoaderSaver.getMyLoader().loadArff(args[0]);
				Instances filteredInstances=null;
				try 
				{
					instances.setClassIndex(instances.numAttributes()-1);
					filteredInstances = Balance.getBalancedInstances(instances);					
				} 
				catch (Exception e) 
				{
					Logger.getLogger(LOG_TAG).log(Level.SEVERE, e.getMessage());
					return;
				}
				
				Calendar calendar = new GregorianCalendar(); // Fecha y hora actuales.
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm"); // Formato de la fecha.
				String dateS = dateFormat.format(calendar.getTime()); // Fecha y hora actuales formateadas.	
				dateFormat = new SimpleDateFormat("yyyyMMddHH");
				String dateD =  dateFormat.format(calendar);
				String filePath=dateD+"/"+args[0].substring(0, args[0].length()-5)+"_"+dateS+ "_resampled.arff";
				LoaderSaver.getMyLoader().saveInstances(filteredInstances, args[0].substring(0, args[0].length()-5)+"_"+dateS+ "_resampled.arff");
				System.out.println("Instancias balanceadas....");
				System.out.println("Archivo guardado: "+filePath);
	}

}
