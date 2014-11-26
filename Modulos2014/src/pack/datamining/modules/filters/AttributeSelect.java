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
	/**
	 * 
	 * @param originalInstances Instancias sobre las que se crea el filtro
	 * @return El filtro para seleccionar atributos
	 * @throws Exception
	 */
	private static Filter getAttributeSelectionFilter(Instances originalInstances) throws Exception
	{		
		 J48 classJ48 = new J48();
		 classJ48.setOptions(weka.core.Utils.splitOptions("-C 0.25 -M 2"));
		 
		 RandomForest classRF = new RandomForest();
		 classRF.setOptions(weka.core.Utils.splitOptions("-I 10 -K 0 -S 1"));
		 
		 Filter j48Fil = createFilter(originalInstances, classJ48);
		 Filter rfFil = createFilter(originalInstances, classRF);
		 
		 int numValuesClass = originalInstances.numClasses();
		 Instances j48Inst = selecAttributesWithFilter(j48Fil, originalInstances);
		 Instances RFInst = selecAttributesWithFilter(rfFil, originalInstances);
		 
		 double j48Fm, RFFm;
		 j48Fm = calculateFmeasure(j48Inst, numValuesClass);
		 RFFm = calculateFmeasure(RFInst, numValuesClass);
		 
		 if (j48Fm > RFFm)
			{
			return j48Fil;
			}
		 else
			 {
			 return rfFil;
			 }
		
	}

	/**
	 * 
	 * @param data Instancias sobre las que se crea el filtro
	 * @param classifier Clasificador empleado para crear el filtro
	 * @return El filtro para seleccionar atributos
	 */
	private static Filter createFilter(Instances data, Classifier classifier) 
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
		  return filter;
	}

	/**
	 * 
	 * @param originalInstances Instancias sobre las que se realiza la evaluación
	 * @param numValuesClass Numero de valores que toma la clase
	 * @return
	 * @throws Exception
	 */
	private static double calculateFmeasure(Instances originalInstances, int numValuesClass)
			throws Exception {
		 Classifier cls = new RandomForest();
		 Evaluation eval = new Evaluation(originalInstances);
		 Random rand = new Random(1);  
		 int folds = 10;
		 eval.crossValidateModel(cls, originalInstances, folds, rand);
		 return evaluateFmeasure(eval, numValuesClass);
		 
	}
	
	/**
	 * 
	 * @param eval La evaluación que se emplea para realizar el calculo de la fmeasure
	 * @param numValuesClass Numero de valores que toma la clase
	 * @return
	 */
	private static double evaluateFmeasure(Evaluation eval, int numValuesClass) 
	{
		double sum = 0.0;
		
		for (int i = 0 ; i < numValuesClass; i++)
			{
			sum += eval.fMeasure(i);
			}
		
		return sum;
	}
	
	/**
	 * 
	 * @param train 1º conjunto de instancias (el que crea el filtro)
	 * @param dev 2º conjunto de instancias
	 * @param test 3º conjunto de instancias
	 * @return
	 */
	public static Instances[] getFilteredData (Instances train, Instances dev, Instances test)
	{
		Instances[] out = new Instances[3];
		out[0] = null;
		out[1] = null;
		out[2] = null;
		
		Filter filter = null;
		try {
			filter = getAttributeSelectionFilter(train);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	
		try {	
			if (train != null)
			out[0] = Filter.useFilter(train, filter);
			if (dev != null)
			out[1] = Filter.useFilter(dev, filter);
			if (test != null)
			out[2] = Filter.useFilter(test, filter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
		
	}
	/**
	 * 
	 * @param filter El filtro para seleccionar atributos	
	 * @param instances Las instancias a las que se aplicara la selección de atributos
	 * @return
	 */
	private static Instances selecAttributesWithFilter(Filter filter, Instances instances)
	{
		
		try {
			instances = Filter.useFilter(instances, filter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return instances;
	}

}
