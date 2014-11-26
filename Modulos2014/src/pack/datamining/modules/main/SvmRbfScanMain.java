package pack.datamining.modules.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.filters.Randomize;
import pack.datamining.modules.io.LoaderSaver;
import pack.datamining.modules.scans.ScanParamsRBFSvmAlgoritm;
import pack.datamining.modules.util.Strings;
import weka.core.Instances;
/**
 * 
 * @author spolex
 *
 */
public class SvmRbfScanMain {

	//For logs
	public static String LOG_TAG=SvmRbfScanMain.class.getSimpleName().toString();
	
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
						 System.out.println("Argumento 3: "+Strings.MSG_ERROR_NUM_FORMATO);
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
						 System.out.println("Argumento 4: "+Strings.MSG_ERROR_NUM_FORMATO);
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
			ScanParamsRBFSvmAlgoritm svmScan =new ScanParamsRBFSvmAlgoritm(pTrainData, pDevData);
			svmScan.scanParams(pCMax,pGMax);
			System.out.println(Strings.MSG_SVM_OPTIMIZED);
		}
		else
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_ARGS);
		}		
	}
}
