package pack.datamining.modules.main;

import pack.datamining.modules.evaluation.Multibounds;
import pack.datamining.modules.io.LoaderSaver;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class LibSVMBiEvaluation {

	public static void main(String[] args) {
		if(args.length>2)
		{
			System.out.println("Configurando el algoritmo");
			Instances pTrainData = LoaderSaver.getMyLoader().loadArff(args[0]);
			Instances pDevData = LoaderSaver.getMyLoader().loadArff(args[1]);
			if(args[2]!=null)
			{
				LibSVM model = null;
				try 
				{
					model = (LibSVM) SerializationHelper.read(args[2]);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				try 
				{
					pTrainData.setClassIndex(pTrainData.numAttributes()-1);
					pDevData.setClassIndex(pDevData.numAttributes()-1);
					Multibounds evaluation = new Multibounds(pTrainData);
					evaluation.biEvaluatorSVM(model, pTrainData, pDevData);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}				 
			}
			else
			{
				System.out.println("Son necesarios tres parámetros de entrada <train.arff> <dev.arff> <svm.model>");
			}
		}
	}
}
