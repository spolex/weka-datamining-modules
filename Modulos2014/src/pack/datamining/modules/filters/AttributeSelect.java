package pack.datamining.modules.filters;

import java.io.File;
import java.util.BitSet;
import java.util.Random;

import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.ClassifierSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;



public class AttributeSelect 
{	
	public static Instances getAttributeSelection(Instances originalInstances) throws Exception
	{
	//	originalInstances.setClassIndex(originalInstances.numAttributes()-1);
		
		 
		 int numValuesClass = originalInstances.numClasses();
		 Instances j48Inst = selectAttributes(originalInstances, new J48());
		 Instances RFInst = selectAttributes(originalInstances, new RandomForest());
		 
		 double j48Fm, RFFm;
		 
		 j48Fm = calculateFmeasure(j48Inst, numValuesClass);
		 RFFm = calculateFmeasure(RFInst, numValuesClass);
		 
		 if (j48Fm > RFFm)
			{System.out.println("J48"); 
			return j48Inst;
			}
		 else
			 {
			 System.out.println("RF");
			 return RFInst;
			 }
		
	}

	private static Instances selectAttributes(Instances data, Classifier classifier) 
	{
				
		AttributeSelection filter = new AttributeSelection();
		  ClassifierSubsetEval eval = new ClassifierSubsetEval();
		  GreedyStepwise search = new GreedyStepwise();
		  eval.setClassifier(classifier);
		  search.setSearchBackwards(true);
		  filter.setEvaluator(eval);
		  filter.setSearch(search);
		  try {
			filter.setInputFormat(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  // generate new data
		  Instances newData = null;
		try {
			newData = Filter.useFilter(data, filter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  System.out.println(newData.numAttributes());
		  
		  return newData;
	}

	private static double calculateFmeasure(Instances originalInstances, int numValuesClass)
			throws Exception {
		 Classifier cls = new RandomForest();
		 Evaluation eval = new Evaluation(originalInstances);
		 Random rand = new Random(1);  // using seed = 1
		 int folds = 10;
		 eval.crossValidateModel(cls, originalInstances, folds, rand);
		 return evaluateFmeasure(eval, numValuesClass);
		 
	}

	private static double evaluateFmeasure(Evaluation eval, int numValuesClass) 
	{
		double sum = 0.0;
		
		for (int i = 0 ; i < numValuesClass; i++)
			sum += eval.fMeasure(i);
		
		
		return sum;
	}

}
