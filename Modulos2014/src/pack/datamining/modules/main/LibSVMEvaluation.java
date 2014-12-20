package pack.datamining.modules.main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import pack.datamining.modules.evaluation.Multibounds;
import pack.datamining.modules.filters.Randomize;
import pack.datamining.modules.io.LoaderSaver;
import pack.datamining.modules.util.Strings;
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
			System.out.println(Strings.MSG_CONF_ALGORITMO);
			String pTrainFile=args[0];
			String pDevFile=args[1];
			Instances pTrainData = LoaderSaver.getMyLoader().loadArff(pTrainFile);
			Instances pDevData = LoaderSaver.getMyLoader().loadArff(pDevFile);
			pTrainData.setClassIndex(0);
			pDevData.setClassIndex(0);
			if(args[2]!=null)
			{
				LibSVM model = new LibSVM();
				String pathDir="";
				try 
				{
					pathDir = configureDir(args,2);
					model = (LibSVM) SerializationHelper.read(args[2]);
					//model.setModelFile(new File(args[2]));
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					System.out.println("Le falta el modelo: tercer argumento");
				}
				try 
				{

					
					Calendar calendar = new GregorianCalendar(); // Fecha y hora actuales.
					SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm"); // Formato de la fecha.
					String dateS = dateFormat.format(calendar.getTime()); // Fecha y hora actuales formateadas.	
					String evalName = dateS+"_"+model.getClass().getSimpleName().toString()+".eval";					
					dateS = dateFormat.format(calendar.getTime());
					
					SelectedTag svmtype=null;
					SelectedTag kernel = null;
					if(model instanceof LibSVM){
						svmtype = model.getSVMType();
						kernel = model.getKernelType();
					}
					
					String files="Train dataset:\n"+pTrainFile+"\nDevelop dataset\n"+pDevFile+"\n\n";
					
					String experimento = "Evaluaciones para el modelo"+model.getClass().getSimpleName().toString()+"\t\t"+svmtype+"\t\t"
					+kernel+"\t\t"+dateS+"\n\n";

					pTrainData.setClassIndex(pTrainData.numAttributes()-1);
					pDevData.setClassIndex(pDevData.numAttributes()-1);
					
					Multibounds evaluation = new Multibounds(pTrainData);
					evaluation.dishonestEvaluator(model, pTrainData);
					
					String title=("\n\n\nEvaluación no-honesta \n");
					String bar=("======================================== \n");	
					String FileContent = experimento+files+title+bar;
					String summary = evaluation.toSummaryString() + 
							"Recall:\t " + evaluation.weightedRecall() + "\nPrecision:\t " + 
							evaluation.weightedPrecision() + "\n\n" + evaluation.toMatrixString()
							+"Parámetros óptimos"
							+bar
							+"\n|| C: "+model.getCost()+" ||"+
							"\n|| gamma: "+model.getGamma()+" ||"
							+"\n|| degree: "+model.getDegree()+" ||\n"
							+bar
							+evaluation.toClassDetailsString();;


							FileContent=FileContent+summary;
							LoaderSaver.getMyLoader().SaveFile(pathDir+"/"+evalName, FileContent, false);
							
							evaluation = new Multibounds(pTrainData);
							evaluation.evaluateModel(model,pDevData);
							
							title=("\n\nEvaluación hold-out con dev \n");					
							FileContent = title+bar;				
							summary = evaluation.toSummaryString() + 
									"Recall:\t " + evaluation.weightedRecall() + "\nPrecision:\t " + 
									evaluation.weightedPrecision() + "\n\n" + evaluation.toMatrixString()
									+"Parámetros óptimos"
									+bar
									+"\n|| C: "+model.getCost()+" ||"+
									"\n|| gamma: "+model.getGamma()+" ||"
									+"\n|| degree: "+model.getDegree()+" ||\n"
									+bar
									+evaluation.toClassDetailsString();;
									FileContent=FileContent+summary;
									LoaderSaver.getMyLoader().SaveFile(pathDir+"/"+evalName, FileContent, false);

									Instances allData = pTrainData;
									allData.addAll(pDevData);
									Randomize.randomize(allData, 1);
									model.buildClassifier(allData);
									evaluation = new Multibounds(allData);									
									evaluation.crossValidateModel(model, allData, 10, new Random(1));
									
									title=("\n\nEvaluación 10FCV con dev+train \n");					
									FileContent = title+bar;				
									summary = evaluation.toSummaryString() + 
											"Recall:\t " + evaluation.weightedRecall() + "\nPrecision:\t " + 
											evaluation.weightedPrecision() + "\n\n" + evaluation.toMatrixString()
											+"Parámetros óptimos"
											+bar
											+"\n|| C: "+model.getCost()+" ||"+
											"\n|| gamma: "+model.getGamma()+" ||"
											+"\n|| degree: "+model.getDegree()+" ||\n"
											+bar
											+evaluation.toClassDetailsString();
											FileContent=FileContent+summary;
											
											LoaderSaver.getMyLoader().SaveFile(pathDir+"/"+evalName, FileContent, false);
											
											SerializationHelper.write(pathDir+"/"+"train+dev"+"svm.model", model);									
											
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

	/**
	 * @param args
	 */
	public static String configureDir(String[] args, int index) {
		String[] modelPath = args[index].split("/");
		String modelDir = modelPath[0]+"/";
		for (int i=1;i<modelPath.length-1;i++)
		{
			modelDir = modelDir+modelPath[i]+"/";
		}
		File modelDirFile = new File(modelDir);
		if(!modelDirFile.exists())modelDirFile.mkdirs();
		return modelDirFile.getPath();
	}
}
