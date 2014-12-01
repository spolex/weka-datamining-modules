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
import weka.core.SelectedTag;
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
				LibSVM model = new LibSVM();
				try 
				{
					model = (LibSVM) SerializationHelper.read(args[2]);
					//model.setModelFile(new File(args[2]));
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				try 
				{

					
					Calendar calendar = new GregorianCalendar(); // Fecha y hora actuales.
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm"); // Formato de la fecha.
					String dateS = dateFormat.format(calendar.getTime()); // Fecha y hora actuales formateadas.	
					String evalName = dateS+model.getClass().getSimpleName().toString()+".eval";					
					dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm"); // Formato de la fecha.
					dateS = dateFormat.format(calendar.getTime());
					dateFormat = new SimpleDateFormat("yyyyMMddHH"); // Formato de la fecha.
					String dateD = dateFormat.format(calendar.getTime());
					
					String dir = "EvaluationsDirectory/"+dateD;
					File evalDir = new File(dir);					
					if(!evalDir.exists() || !evalDir.isDirectory())evalDir.mkdirs();

					SelectedTag svmtype=null;
					SelectedTag kernel = null;
					if(model instanceof LibSVM){
						svmtype = model.getSVMType();
						kernel = model.getKernelType();
					}
					
					String experimento = "Evaluaciones para el modelo"+model.getClass().getSimpleName().toString()+"\t\t"+svmtype+"\t\t"
					+kernel+dateS;

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
							+"Parámetros óptimos"
							+bar
							+"\n|| C: "+model.getCost()+" ||"+
							"\n|| gamma: "+model.getGamma()+" ||\n\n"
							+"\n|| degree: "+model.getDegree()+" ||\n\n"
							+bar
							+evaluation.toClassDetailsString();;


							FileContent=FileContent+summary;
							LoaderSaver.getMyLoader().SaveFile(dir+"/"+evalName, FileContent, false);
							
							evaluation.evaluateModel(model,pDevData);
							
							title=("\n\nEvaluación hold-out con dev \n");					
							FileContent = title+bar;				
							summary = evaluation.toSummaryString() + 
									"Recall:\t " + evaluation.weightedRecall() + "\nPrecision:\t " + 
									evaluation.weightedPrecision() + "\n\n" + evaluation.toMatrixString()
									+"Parámetros óptimos"
									+bar
									+"\n|| C: "+model.getCost()+" ||"+
									"\n|| gamma: "+model.getGamma()+" ||\n\n"
									+"\n|| degree: "+model.getDegree()+" ||\n\n"
									+bar
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
											+"Parámetros óptimos"
											+bar
											+"\n|| C: "+model.getCost()+" ||"+
											"\n|| gamma: "+model.getGamma()+" ||\n\n"
											+"\n|| degree: "+model.getDegree()+" ||\n\n"
											+bar
											+evaluation.toClassDetailsString();
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
