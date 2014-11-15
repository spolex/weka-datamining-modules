package pack.datamining.modules.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.util.Strings;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class LoaderSaver 
{
	private static LoaderSaver myLoader= null;
	private Instances instances;
	private String orPath;
	private String desPath;
	private static String LOG_TAG = LoaderSaver.class.getSimpleName().toString();
	
	/**
	 * Constructora del objeto LoaderSaver
	 * @param pOrPath Ruta del fichero a cargar
	 */
	private LoaderSaver(String pOrPath)
	{
		orPath = pOrPath;
		// El fichero arff de salida se guardar en la 
		// misma ruta que el original añadidendo modified al 
		// nombre y un codigo temporal.
		desPath = pOrPath + "modified"+new Date ().getTime() + ".arff";
	}
	
	/**
	 * Permite acceder al objeto LoaderSaver y establecer el fichero que 
	 * se podra cargar;
	 * @param pOrPath Ruta del fichero a cargar
	 * @return El objeto LoaderSaver
	 */
	public static LoaderSaver getMyLoader(String pOrPath)
	{
		if (myLoader == null)
			myLoader = new LoaderSaver(pOrPath);
		return myLoader;
	}
	
	/**
	 * Carga el fichero arff de la ruta orPath
	 */
	public void loadArff ()
	{
		BufferedReader reader= null;
		try 
		{
			reader = new BufferedReader(new FileReader(orPath));
		} 
		catch (FileNotFoundException e) {
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_FILE_NOTFOUND);
		}
		
		instances = null;
		try 
		{
			instances = new Instances(reader);
		} 
		catch (IOException e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_IO_FAIL);
		}
	}
	
	/**
	 * Devuelve las instacias del fichero de OrPath
	 * @return
	 */
	public Instances getInstances ()
	{
		return instances;
	}

	/**
	 * Guarda las instancias en un fichero Arff
	 * @param pInstances Instancias a guardar (si es null guardamos las
	 *  instancias del propio objeto)
	 */
	public void saveInstances (Instances pInstances)
	{
		Instances instancesToSave = null;
		
		if (pInstances == null)
		{
			instancesToSave = instances;
		}
		else
		{
			instancesToSave = pInstances;
		}
		ArffSaver saver = new ArffSaver();
		saver.setInstances(instancesToSave);
		try 
		{
			saver.setFile(new File(desPath));
		} 
		catch (IOException e1) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_IO_FAIL);
		}
		try 
		{
			saver.writeBatch();
		}
		catch (IOException e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_IO_FAIL);
		}
	}
}
