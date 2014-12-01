package pack.datamining.modules.main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.filters.Randomize;
import pack.datamining.modules.io.LoaderSaver;
import pack.datamining.modules.scans.ScanSvmParams;
import pack.datamining.modules.util.Strings;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.SerializationHelper;
/**
 * 
 * @author spolex
 *
 */
public class SvmScanMain {

	//For logs
	public static String LOG_TAG=SvmScanMain.class.getSimpleName().toString();
	
	/**
	 * Serializa el modelo .model para la máquina de soporte vectorial con el kernel rbf.
	 *Los parámetros que optimiza son gamma y cost.
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception 
	{
		
		Instances pTrainData=null;
		Instances pDevData=null;
		int pCMax=11;
		int pGMax=4;
		int pDMax=2;
		System.out.println(Strings.LABEL_OBJETIVO);
		if(args.length>1)
		{
			 System.out.println("Configurando el algoritmo");
			 pTrainData = LoaderSaver.getMyLoader().loadArff(args[0]);
			 pDevData = LoaderSaver.getMyLoader().loadArff(args[1]);
			 
			 if(args.length>2)
			 {
				 if(args[2]!=null)
				 {
					 try
					 {
						 pCMax=Integer.valueOf(args[2]);
					 }
					 catch (NumberFormatException e) 
					 {
						 System.out.println(Strings.LABEL_ARGUMENTO +"3: "+Strings.MSG_ERROR_NUM_FORMATO);
					 }
				 }
				 if(args[3]!=null)
				 {
					 try
					 {
						 pGMax=Integer.valueOf(args[3]);
					 }
					 catch (NumberFormatException e) 
					 {
						 System.out.println(Strings.LABEL_ARGUMENTO +"4: "+Strings.MSG_ERROR_NUM_FORMATO);
					 }
				 }
				 if(args[4]!=null)
				 {
					 try
					 {
						 pDMax=Integer.valueOf(args[4]);
					 }
					 catch (NumberFormatException e) 
					 {
						 System.out.println(Strings.LABEL_ARGUMENTO+ "5: "+Strings.MSG_ERROR_NUM_FORMATO);
					 }
				 }
			 }
			 
		}
		else
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_NUMS_ARGS);
			System.out.println(Strings.MSG_ERROR_NUMS_ARGS);
			return;
		}
		
		if(pDevData!=null && pTrainData!=null){
			try 
			{
				pDevData = Randomize.randomize(pDevData, 1);
				pTrainData = Randomize.randomize(pTrainData, 1);
				Logger.getLogger(LOG_TAG).log(Level.INFO, Strings.MSG_INSTANCES_RANDOM);
			} 
			catch (Exception e) 
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_INSTANCES_RANDOM);
				return;
			}			
			ScanSvmParams svmScan =new ScanSvmParams(pTrainData, pDevData);
			LibSVM svm =svmScan.scanParams(pCMax,pGMax,pDMax);
			System.out.println(Strings.MSG_SVM_OPTIMIZED);
			System.out.println(Strings.MSG_SERIALIZANDO);
			try 
			{
				serializeModel(createDirForModel(),svm);
				System.out.println(Strings.MSG_FIN);
			} 
			catch (IOException e) 
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_CREAR_ARCHIVO+" Models");
			} 
			catch (Exception e) 
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_CREAR_ARCHIVO+" .model");
			}

		}
		else
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_ARGS);
		}		
	}
	/**
	 * @throws Exception
	 */
	protected static void serializeModel(File pDir, LibSVM clf) throws Exception {
		
		if(!pDir.exists())pDir.mkdirs();
		Calendar calendar = new GregorianCalendar(); // Fecha y hora actuales.
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); // Formato de la fecha.
		String dateD = dateFormat.format(calendar.getTime()); // Fecha y hora actuales formateadas.	
		dateFormat = new SimpleDateFormat("HHmm");
		String dateH = dateFormat.format(calendar.getTime());
		String kernel = ((LibSVM) clf).getKernelType().getSelectedTag().getIDStr();
		if(kernel.equals("1"))
		{
			kernel="poly";
		}
		else
		{
			kernel="rbf";

		}
		
		//String filePath = "Resultados/"+dateS+"resultados.txt";	
		String path = pDir.getPath()+"/"+clf.getClass().getSimpleName().toString()+"/"+kernel+"/"+dateD;
		File dir = new File(path);
		if(!dir.exists())dir.mkdirs();
		SerializationHelper.write(dir+"/"+dateH+"svm.model",clf );
	}
	
	/**
	 * 
	 */
	protected static  File createDirForModel() {
		File modelDir = new File("Modelos");
		if(!modelDir.exists())
		{
			modelDir.mkdirs();
			System.out.println(modelDir.getAbsolutePath()+": "+Strings.MSG_DIRECTROIO_CREADO);
		}
		
		return modelDir;
	}
}
