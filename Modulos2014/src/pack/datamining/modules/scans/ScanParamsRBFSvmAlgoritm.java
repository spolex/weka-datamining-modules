package pack.datamining.modules.scans;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JLabel;

import pack.datamining.modules.frames.ProgressDialog;
import pack.datamining.modules.temp.prueba;
import pack.datamining.modules.util.Strings;
import pack.datamining.modules.util.VerboseCutter;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.SerializationHelper;
/**
 * 
 * @author Iñigo Sánchez Méndez (spolex)
 *
 */
public class ScanParamsRBFSvmAlgoritm
{
	private Instances mTrain;
	private Instances mDev;
	private LibSVM mModel;
	private Evaluation evaluator;
	
	// Variables para comparar la f-measure en curso con la f-measure de la vuelta anterior.
	private double mFmeasureAux = 0.0;
	private double mFmeasureBest = 0.0;
	private	int maxOfCSearch = 11; //Hasta  que valor es óptimo C "barrer"??
	private	int maxOfGSearch = 4;  //Hasta  que valor es óptimo	
	
	private double bestG;
	private double bestC;
	
	//Variable para Logger
	public static final String LOG_TAG = ScanParamsRBFSvmAlgoritm.class.getSimpleName().toString();
	
	public ScanParamsRBFSvmAlgoritm(Instances pTrainData, Instances pDevData)
	{
		this.mTrain=pTrainData;
		this.mDev=pDevData;
		this.mModel= new LibSVM();
		
	}
	
	public void ScanParams(){
		//Utilizamos C-SVC
		configureModel();
	}
	
	/**
	 * configura el modelo y las clases necesarias para el barrido ad hoc,
	 * por el momento además asigna al atributo en la última posición la función de la clase.
	 */
	private void configureModel()
	{
		//establecemos la posición de la clase de los conjuntos de datos.
		mDev.setClassIndex(mDev.numAttributes()-1);
		mTrain.setClassIndex(mTrain.numAttributes()-1);
		try 
		{
			this.evaluator=new Evaluation(mTrain);
		} 
		catch (Exception e)
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_EVALUACION);
			e.printStackTrace();
		}
		//Utilizamos C-SVC
		mModel.setSVMType(new SelectedTag(0, LibSVM.TAGS_SVMTYPE));
		
		//Establecemos el kernel RBF justiificado en el informe práctica 3 SAD
		mModel.setKernelType(new SelectedTag(2,LibSVM.TAGS_KERNELTYPE));
		
		mModel.setDegree(0);
		
		
		//Inicializar figuras de mérito
		mFmeasureAux = 0.0;
		mFmeasureBest = 0.0;
		
		//barrido del parámetro c = 2^C, justificado en el guión porqué se utilizan potencias de 2
		//Lo activamos para evitar el modo verbose de libSVM 
		System.out.println("Starting C and Gamma optimization with svm model");
		
		// Utilizamos el kernel por defecto RBF
		//Inicio de barrido de parámetros
		maxOfCSearch = 11; //Hasta  que valor es óptimo C "barrer"??
		maxOfGSearch = 4;  //Hasta  que valor es óptimo	
		
		bestC=-15;
		bestG=-3;
		
		
	}
	
	public LibSVM scanParams(){
		configureModel();
		
		for (int c = -15; c <= -14; c++) {
			
			for (int g = -3; g <= -2; g++)
			{
				//La justificación del uso de potencias de dos se encuentra en el informe practica 3 SAD.
				double cost = (Math.pow(2, c));
				double gamma = (Math.pow(2, g));
				mModel.setGamma(gamma);
				mModel.setCost(cost);
				try 
				{
					try 
					{
						mModel.buildClassifier(mTrain);
						
					} 
					catch (Exception e1) 
					{
						Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_ENTRENAR_MODELO+" - "+e1.toString());
					}
					//VerboseCutter.getVerboseCutter().cutVerbose();
					evaluator.evaluateModel(mModel, mDev);
					//Si no lo reactivamos dejan de funcionar las salidas por pantalla 
					//VerboseCutter.getVerboseCutter().activateVerbose();
					
				} 
				catch (Exception e) 
				{
					Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_EVALUACION_MODELO+" - "+e.toString());
				}
				
				mFmeasureAux = evaluator.fMeasure(0);
				if (mFmeasureAux > mFmeasureBest)
				{
					mFmeasureBest = mFmeasureAux;
					bestC = cost;
					bestG = gamma;
					Logger.getLogger(LOG_TAG).log(Level.INFO, Strings.MSG_V_CHANGE_FMEASURE);
				}
				else
				{
					Logger.getLogger(LOG_TAG).log(Level.INFO, Strings.MSG_V_NO_CHANGE_FMEASURE);;
				}
			}
		}
			mModel.setGamma(bestG);
			mModel.setCost(bestC);
			File directory = null;
			try 
			{
				directory = new File("/Models");
				if(!directory.exists())directory.mkdirs();
				SerializationHelper.write("/Models/RBFsvm.model",mModel );
			} 
			catch (IOException e) 
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_CREAR_ARCHIVO+" Models");
			} catch (Exception e) 
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_CREAR_ARCHIVO+" .model");
			}
			return mModel;
	}
		
		
		
	
	public Instances randomize (Instances data, int seed) throws Exception
	{
		 Random rand = new Random(seed);   // create seeded number generator
		 Instances randData = new Instances(data);   // create copy of original data
		 randData.randomize(rand);		 
		 return randData;
	}
}
