package pack.datamining.modules.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.filters.Discretization;
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
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_FILE_NOTFOUND+"-"+e.getMessage());
		}
		
		Instances instances = null;
		try 
		{
			instances = new Instances(reader);
		} 
		catch (IOException e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_IO_FAIL+"-"+e.getMessage());
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
	/**
	 * 
	 * @param FilePath = String, indica la ruta del archivo.
	 * @param FileContent = String, indica el contenido del archivo
	 * @param CleanFileContent = boolean.Si true y existe el archivo borra el contenido, 
	 * 			si false, añade el contenido al final del archivo.
	 * @return true si se guarda con éxito, false en caso contrario.
	 * 
	 * fuente : http://www.creatusoftware.com/index.php?option=com_content&view=article&id=142:funcion-para-guardar-un-archivo-en-java&catid=62:fuentes-java&Itemid=41
	 */
	
	public boolean SaveFile(String FilePath, String FileContent, boolean CleanFileContent)
	{
	 
	    FileWriter file;
	    BufferedWriter writer;
	     
	    try
	    {
	        file = new FileWriter(FilePath, !CleanFileContent);
	        writer = new BufferedWriter(file);
	        writer.write(FileContent, 0, FileContent.length());
	         
	        writer.close();
	        file.close();
	 
	        return true;
	    } 
	    catch (IOException ex) 
	    {
	        ex.printStackTrace();
	        return false;
	   }
	}
	
	/**
	 * @param instances
	 * @param desde
	 * @param hasta
	 * @param pathFile
	 * @throws Exception
	 */
	public static void extractFileInDateDir(Instances instances, int desde,
			int hasta, String pathFile,String filter) throws Exception {
		Calendar calendar = new GregorianCalendar(); // Fecha y hora actuales.
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm"); // Formato de la fecha.
		String dateS = dateFormat.format(calendar.getTime()); // Fecha y hora actuales formateadas.
		Instances filteredInstances = Discretization.getDiscretized(instances,desde ,hasta);
		dateFormat = new SimpleDateFormat("yyyyMMddHH");
		String dateD =  dateFormat.format(calendar.getTime());
		String filePath="data/"+filter+"_"+dateD+"/"+pathFile.substring(4, pathFile.length()-5)+"_"+dateS+"_"+filter+"Class.arff";
		LoaderSaver.getMyLoader().saveInstances(filteredInstances, filePath );
	}
}
