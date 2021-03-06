package pack.datamining.modules.main;
import pack.datamining.modules.filters.OverSSMOTE;
import pack.datamining.modules.io.LoaderSaver;
import weka.core.Instances;

public class OverSSMOTEMain {

	public static void main(String[] args) throws Exception {
		
		Instances instances = LoaderSaver.getMyLoader().loadArff(args[0]);
		instances.setClassIndex(instances.numAttributes()-1);
		double percentage = Double.parseDouble(args[1]);
		int knn= Integer.parseInt(args[2]);
		
		Instances newInstances = OverSSMOTE.getOverSSMOTE().applySMOTE(instances,percentage,knn);
		LoaderSaver.getMyLoader().saveInstances(newInstances, args[0]+"SMOTEd.arff");
		
	}

}
