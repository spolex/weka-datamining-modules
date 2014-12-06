package pack.datamining.modules.main;

import pack.datamining.modules.filters.InterClassCoefficient;
import pack.datamining.modules.io.LoaderSaver;
import weka.core.EuclideanDistance;
import weka.core.Instances;

public class InterClassCoefficientMain {

	public static void main(String[] args) {
		
		
		Instances instances = LoaderSaver.getMyLoader().loadArff(args[0]);
		instances.setClassIndex(instances.numAttributes()-1);
		EuclideanDistance distance = new EuclideanDistance(instances);
		distance.setDontNormalize(true);
		InterClassCoefficient icc=new InterClassCoefficient(instances, distance,Integer.parseInt(args[1]));
		Instances newInstances=icc.removeOutliers();
		
		LoaderSaver.getMyLoader().saveInstances(newInstances, args[0]+"ICC.arff");
	}

}
