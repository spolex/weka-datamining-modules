package pack.datamining.modules.main;


import pack.datamining.modules.filters.Randomize;
import pack.datamining.modules.io.LoaderSaver;
import pack.datamining.modules.scans.ScanParamsRandomForest;
import pack.datamining.modules.util.Strings;
import weka.core.Instances;

public class RandomForestScanMain {
	
	
	
	
	public static void main(String[] args) throws Exception 
	{
		
		Instances pTrainData=null;
		Instances pDevData=null;
		int nMaxTrees = 0;
		int nMaxFeat = 0;
		
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
						 nMaxTrees=Integer.valueOf(args[2]);
					 }
					 catch (NumberFormatException e) 
					 {
						 System.out.println("Argumento 3: "+Strings.MSG_ERROR_NUM_FORMATO+" "+e.getMessage());
					 }
				 }
				 if(args[3]!=null)
				 {
					 try
					 {
						 nMaxFeat=Integer.valueOf(args[3]);
					 }
					 catch (NumberFormatException e) 
					 { 
						 System.out.println("Argumento 4: "+Strings.MSG_ERROR_NUM_FORMATO+" "+e.getMessage());
					 }
				 }
				 else
					 nMaxFeat=pTrainData.numAttributes();
			 }
			 
		}
		else
		{
			System.out.println(Strings.MSG_ERROR_NUMS_ARGS);
			return;
		}
		
		if(pDevData!=null && pTrainData!=null){
			try 
			{
				pDevData = Randomize.randomize(pDevData, 1);
				pTrainData = Randomize.randomize(pTrainData, 1);
			} 
			catch (Exception e) 
			{
				return;
			}			
			ScanParamsRandomForest rfScan =new ScanParamsRandomForest(pTrainData, pDevData);
			rfScan.scanParams(nMaxTrees, nMaxFeat);
			System.out.println("Modelo Random Forest optimizado");
		}
		else
		{
			System.out.println(Strings.MSG_ERROR_ARGS);
		}	
	}
}
