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

/**
 * 
 * @author Iñigo Sánchez Méndez (spolex)
 *
 */
public class LibSVMEvaluation {

	public static void main(String[] args) {
		if(args.length>2)
		{
			System.out.println("Configurando el algoritmo");
			Instances pTrainData = LoaderSaver.getMyLoader().loadArff(args[0]);
			Instances pDevData = LoaderSaver.getMyLoader().loadArff(args[1]);
			pTrainData.setClassIndex(0);
			pDevData.setClassIndex(0);
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

					String dir = "EvaluationsDirectory";
					File evalDir = new File(dir);
					if(!evalDir.exists() || !evalDir.isDirectory())evalDir.mkdirs();
					Calendar calendar = new GregorianCalendar(); // Fecha y hora actuales.
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm"); // Formato de la fecha.
					String dateS = dateFormat.format(calendar.getTime()); // Fecha y hora actuales formateadas.	
					String evalName = dateS+"rbf_svm.eval";
					
					dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm"); // Formato de la fecha.
					dateS = dateFormat.format(calendar.getTime());

					String experimento = "Evaluaciones para el modelo"+model.getClass().getSimpleName()+"\t\t"+dateS;

					pTrainData.setClassIndex(pTrainData.numAttributes()-1);
					pDevData.setClassIndex(pDevData.numAttributes()-1);
					
					Multibounds evaluation = new Multibounds(pTrainData);
					evaluation.dishonestEvaluator(model, pTrainData);
					
					String title=("\n\n\nEvaluación no-honesta \n");
					String bar=("======================================== \n");	
					String FileContent = experimento+title+bar;
					String summary = evaluation.toSummaryString() + 
							"Recall:\t " + evaluation.weightedRecall() + "\nPrecision:\t " + 
							evaluation.weightedPrecision() + "\n\n" + evaluation.toMatrixString()
							+"\nC: "+model.getCost()+
							"\ngamma: "+model.getGamma()
							+evaluation.toClassDetailsString();;


							FileContent=FileContent+summary;
							LoaderSaver.getMyLoader().SaveFile(dir+"/"+evalName, FileContent, false);
							
							evaluation.evaluateModel(model,pDevData);
							
							title=("\n\nEvaluación hold-out con dev \n");					
							FileContent = title+bar;				
							summary = evaluation.toSummaryString() + 
									"Recall:\t " + evaluation.weightedRecall() + "\nPrecision:\t " + 
									evaluation.weightedPrecision() + "\n\n" + evaluation.toMatrixString()
									+"\nC: "+model.getCost()+
									"\ngamma: "+model.getGamma()
									+evaluation.toClassDetailsString();;
									FileContent=FileContent+summary;
									LoaderSaver.getMyLoader().SaveFile(dir+"/"+evalName, FileContent, false);

									Instances allData = pTrainData;
									allData.addAll(pDevData);
									Randomize.randomize(allData, 1);
									model.buildClassifier(allData);
									
									evaluation.crossValidateModel(model, allData, 10, new Random(1));
									
									title=("\n\nEvaluación 10FCV con dev+train \n");					
									FileContent = title+bar;				
									summary = evaluation.toSummaryString() + 
											"Recall:\t " + evaluation.weightedRecall() + "\nPrecision:\t " + 
											evaluation.weightedPrecision() + "\n\n" + evaluation.toMatrixString()
											+"\nC: "+model.getCost()+
											"\ngamma: "+model.getGamma()
											+evaluation.toClassDetailsString();;
											FileContent=FileContent+summary;
											LoaderSaver.getMyLoader().SaveFile(dir+"/"+evalName, FileContent, false);
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
