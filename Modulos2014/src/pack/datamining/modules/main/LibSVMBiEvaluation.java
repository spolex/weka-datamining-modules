package pack.datamining.modules.main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import pack.datamining.modules.evaluation.Multibounds;
import pack.datamining.modules.filters.Randomize;
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
					File evalDir = new File("EvaluationsDirectory");
					if(!evalDir.exists() || !evalDir.isDirectory())evalDir.mkdirs();
					Calendar calendar = new GregorianCalendar(); // Fecha y hora actuales.
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm"); // Formato de la fecha.
					String dateS = dateFormat.format(calendar.getTime()); // Fecha y hora actuales formateadas.	
					
					String evalName = dateS+"rbf_svm_evaluations.eval";
					File evalFile = new File(evalName);
					
					pTrainData.setClassIndex(pTrainData.numAttributes()-1);
					pDevData.setClassIndex(pDevData.numAttributes()-1);
					
					Multibounds evaluation = new Multibounds(pTrainData);
					String title=("Evaluación no-honesta");
					String bar=("========================================");
					
					LoaderSaver.getMyLoader().s
					evaluation.dishonestEvaluator(model, pTrainData);
					
					evaluation.evaluateModel(model,pDevData);
					
					Instances allData = pTrainData;
					allData.addAll(pDevData);
					Randomize.randomize(allData, 1);
					model.buildClassifier(allData);
					evaluation.crossValidateModel(model, allData, 10, new Random(1));
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
