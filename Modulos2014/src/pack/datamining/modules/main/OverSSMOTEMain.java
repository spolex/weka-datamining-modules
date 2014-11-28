package pack.datamining.modules.main;
import pack.datamining.modules.filters.OverSSMOTE;
import pack.datamining.modules.io.LoaderSaver;
import weka.core.Instances;

public class OverSSMOTEMain {

	public static void main(String[] args) throws Exception {
		
		Instances instances = LoaderSaver.getMyLoader().loadArff(args[0]);
		double percentage = Double.parseDouble(args[1]);
		int knn= Integer.parseInt(args[2]);
		
		OverSSMOTE.getOverSSMOTE().applySMOTE(instances,percentage,knn);
		LoaderSaver.getMyLoader().saveInstances(instances, args[0]+"SMOTEd.arff");
		
	}

}
