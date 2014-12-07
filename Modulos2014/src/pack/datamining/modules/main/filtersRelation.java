package pack.datamining.modules.main;

import pack.datamining.modules.filters.Balance;
import pack.datamining.modules.filters.OverSSMOTE;
import pack.datamining.modules.io.LoaderSaver;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class filtersRelation {

	public static void main(String[] args) throws Exception {
		Instances originalInstances=LoaderSaver.getMyLoader().loadArff(args[0]);
		Instances testInstances=LoaderSaver.getMyLoader().loadArff(args[1]);
		double precision;
		
		originalInstances.setClassIndex(originalInstances.numAttributes()-1);
		testInstances.setClassIndex(testInstances.numAttributes()-1);
		int scaleFactor=10;
		int maxiumScale=4000;
		J48 tree = new J48();
		double area=0;
		int smoteFactor;
		Instances modifiedInstances;
		Evaluation evaluator = new Evaluation(originalInstances);
		double partialROC;
		double correct;
		for(int i=2000;i<maxiumScale;i+=scaleFactor)
		{
			modifiedInstances=new Instances(originalInstances);
			modifiedInstances.setClassIndex(modifiedInstances.numAttributes()-1);
			modifiedInstances=OverSSMOTE.getOverSSMOTE().applySMOTE(modifiedInstances, i, 5);
			modifiedInstances=Balance.getBalancedInstances(modifiedInstances);
			tree.buildClassifier(modifiedInstances);
			
			
			evaluator.evaluateModel(tree, testInstances);
			partialROC=evaluator.areaUnderROC(0);
			precision=evaluator.precision(0);
			correct=evaluator.correct();
			if(partialROC>area)
			{
				area=partialROC;
				smoteFactor=i;
			}
				System.out.println(partialROC +"\t"+ i +"\t"+precision+"\t"+correct);
			
			
		}	
	}

}
