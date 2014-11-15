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
	private static String LOG_TAG = LoaderSaver.class.getSimpleName().toString();
	
	/**
	 * Constructora del objeto LoaderSaver
	 * @param pOrPath Ruta del fichero a cargar
	 */
	private LoaderSaver()
	{
	}
	
	/**
	 * Permite acceder al objeto LoaderSaver y establecer el fichero que 
	 * se podra cargar;
	 * @param pOrPath Ruta del fichero a cargar
	 * @return El objeto LoaderSaver
	 */
	public static LoaderSaver getMyLoader()
	{
		if (myLoader == null)
			myLoader = new LoaderSaver();
		return myLoader;
	}
	
	/**
	 * Carga las instacias de un fichero Arff
	 * @param pPath Ruta del fichero a cargar
	 * @return Las Instancias cargadas
	 */
	public Instances loadArff (String pPath)
	{
		BufferedReader reader= null;
		try 
		{
			reader = new BufferedReader(new FileReader(pPath));
		} 
		catch (FileNotFoundException e) {
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_FILE_NOTFOUND);
		}
		
		Instances instances = null;
		try 
		{
			instances = new Instances(reader);
		} 
		catch (IOException e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_IO_FAIL);
		}
		return instances;
	}
	
	/**
	 * Guarda las instancias en un fichero Arff
	 * @param pInstances Instancias a guardar (si es null guardamos las
	 *  instancias del propio objeto)
	 * @param pPath Ruta donde se guardaran las insntacias
	 */
	public void saveInstances (Instances pInstances, String pPath)
	{
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(pInstances);
		try 
		{
			saver.setFile(new File(pPath));
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
