package pack.datamining.modules.filters;

import java.util.Random;

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
		 J48 classJ48 = new J48();
		 classJ48.setOptions(weka.core.Utils.splitOptions("-C 0.25 -M 2"));
		 
		 RandomForest classRF = new RandomForest();
		 classRF.setOptions(weka.core.Utils.splitOptions("-I 10 -K 0 -S 1"));
		 

		 int numValuesClass = originalInstances.numClasses();
		 Instances j48Inst = selectAttributes(originalInstances, classJ48);
		 Instances RFInst = selectAttributes(originalInstances, classRF);
		 
		 double j48Fm, RFFm;
		 System.out.println("j48"+ j48Inst.numAttributes());
		 j48Fm = calculateFmeasure(j48Inst, numValuesClass);
		 System.out.println("RF"+RFInst.numAttributes());
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
		try {
			search.setOptions(weka.core.Utils.splitOptions("-T -1.7976931348623157E308 -N -1"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		eval.setClassifier(classifier);
		search.setSearchBackwards(true);
		filter.setEvaluator(eval);
		filter.setSearch(search);
		  try {
			filter.setInputFormat(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		  Instances newData = null;
		try {
			newData = Filter.useFilter(data, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		  
		  return newData;
	}

	private static double calculateFmeasure(Instances originalInstances, int numValuesClass)
			throws Exception {
		 Classifier cls = new RandomForest();
		 Evaluation eval = new Evaluation(originalInstances);
		 Random rand = new Random(1);  
		 int folds = 10;
		 eval.crossValidateModel(cls, originalInstances, folds, rand);
		 return evaluateFmeasure(eval, numValuesClass);
		 
	}

	private static double evaluateFmeasure(Evaluation eval, int numValuesClass) 
	{
		double sum = 0.0;
		
		for (int i = 0 ; i < numValuesClass; i++)
			{
			sum += eval.fMeasure(i);
			System.out.println(eval.fMeasure(i)
					);
			}
		
		return sum;
	}

}
