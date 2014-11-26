package pack.datamining.modules.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.filters.Discretization;
import pack.datamining.modules.filters.Outliers;
import pack.datamining.modules.io.LoaderSaver;
import pack.datamining.modules.util.Strings;
import weka.core.*;

public class DiscretizeMain {

	public static void main(String[] args) 
	{
		String LOG_TAG= DiscretizeMain.class.getSimpleName().toString();
		
		Instances instances = LoaderSaver.getMyLoader().loadArff(args[0]);
		int intervalos = 10;
		int pos = instances.numAttributes() - 1;
		
		if (args.length > 1 && args.length < 4)
		{ 
			if (args[1] != null && args[2] != null)
			{
				if (args[1].equals("-I"))
				{
					System.out.println("I");
					intervalos = Integer.valueOf(args[2]);
				}
				
				if (args[1].equals("-C"))
				{System.out.println("C");
					pos = Integer.valueOf(args[2]);
				}
			}
		}
		if (args.length > 4 && args.length < 6)
		{
				if (args[1].equals("-I"))
				{
					intervalos = Integer.valueOf(args[2]);
				}
				
				if (args[1].equals("-C"))
				{
					pos = Integer.valueOf(args[2]);
				}
				if (args[3] != null && args[4] != null)
				{
					if (args[3].equals("-I"))
					{
						intervalos = Integer.valueOf(args[4]);
					}
					if (args[3].equals("-C"))
					{
						pos = Integer.valueOf(args[4]);
					}
				}
				
		/*	try {
				instances = Discretization.getDiscretized(instances, intervalos, pos);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LoaderSaver.getMyLoader().saveInstances(instances, args[0] + "discretized.arff");*/
		}
		
		try 
		{
			
			Calendar calendar = new GregorianCalendar(); // Fecha y hora actuales.
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm"); // Formato de la fecha.
			String dateS = dateFormat.format(calendar.getTime()); // Fecha y hora actuales formateadas.
			Instances filteredInstances = Discretization.getDiscretized(instances,intervalos ,pos);
			dateFormat = new SimpleDateFormat("yyyyMMddHH");
			String dateD =  dateFormat.format(calendar.getTime());
			String filePath="Discretized_"+dateD+"/"+args[0].substring(4, args[0].length()-5)+"_"+dateS+"_DiscretizedClass.arff";
			LoaderSaver.getMyLoader().saveInstances(filteredInstances, filePath );
		} 
		catch (Exception e) 
		{
		Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_OUTLIERS+e.getMessage());
		}
	}
	}
