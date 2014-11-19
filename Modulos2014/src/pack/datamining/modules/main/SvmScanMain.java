package pack.datamining.modules.main;

import java.util.logging.Level;
import java.util.logging.Logger;

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
			ScanParamsRBFSvmAlgoritm svmScan =new ScanParamsRBFSvmAlgoritm(pTrainData, pDevData);
			svmScan.scanParams();
		}
	}

}
