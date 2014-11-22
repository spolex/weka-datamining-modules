package pack.datamining.modules.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.filters.Randomize;
import pack.datamining.modules.io.LoaderSaver;
import pack.datamining.modules.scans.ScanParamsRBFSvmAlgoritm;
import pack.datamining.modules.util.Strings;
import weka.core.Instances;

public class SvmScanMain {

	//For logs
	public static String LOG_TAG=SvmScanMain.class.getSimpleName().toString();
	
	public static void main(String[] args) 
	{
		
		Instances pTrainData=null;
		Instances pDevData=null;
		if(args.length>1)
		{
			 pTrainData = LoaderSaver.getMyLoader().loadArff(args[0]);
			 pDevData = LoaderSaver.getMyLoader().loadArff(args[1]);			
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
				System.out.println(Strings.MSG_INSTANCES_RANDOM);
			} 
			catch (Exception e) 
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_INSTANCES_RANDOM);
				return;
			}
			
			ScanParamsRBFSvmAlgoritm svmScan =new ScanParamsRBFSvmAlgoritm(pTrainData, pDevData);
			svmScan.scanParams();
			System.out.println(Strings.MSG_SVM_OPTIMIZED);
		}
		else
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_ARGS);
		}
		
	}

}
