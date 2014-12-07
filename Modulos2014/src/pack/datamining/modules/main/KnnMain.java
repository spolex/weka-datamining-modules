package pack.datamining.modules.main;
import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.io.LoaderSaver;
import pack.datamining.modules.scans.Knn;
import pack.datamining.modules.util.Strings;
import weka.core.Instances;




public class KnnMain {
	public static String LOG_TAG=KnnMain.class.getSimpleName().toString();
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Instances pTrainData=null;
		Instances pDevData=null;
		String path="";
		if(args.length==3)
		{
			 System.out.println("Configurando el algoritmo");
			 pTrainData = LoaderSaver.getMyLoader().loadArff(args[0]);
			 pDevData = LoaderSaver.getMyLoader().loadArff(args[1]);
			 path=args[2];
			 
			
			 
		}
		else
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_NUMS_ARGS);
			System.out.println(Strings.MSG_ERROR_NUMS_ARGS);
			return;
		}
		if(pDevData!=null && pTrainData!=null){
			System.out.println("Barrido:");
			Knn knn =new Knn(pTrainData, pDevData);
			knn.barridoKnn(path);
			
			System.out.println("Knn optimizado");
		}
		else
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_ARGS);
		}

	}

}
