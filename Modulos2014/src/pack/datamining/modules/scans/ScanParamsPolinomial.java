package pack.datamining.modules.scans;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.evaluation.Multibounds;
import pack.datamining.modules.util.Strings;
import pack.datamining.modules.util.VerboseCutter;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.SelectedTag;

public class ScanParamsPolinomial extends ScanParamsRBFSvmAlgoritm {
	
	private int maxOfDegreeSearch;
	private int bestD;

	public ScanParamsPolinomial(Instances pTrainData, Instances pDevData) {
		super(pTrainData, pDevData);
	}
	
	protected void configureModel(int pCmax, int pGmax, int pDegreeMax) {	
		this.mModel= new LibSVM();
		//Creamos el directorio necesario para serializar los modelos.
		mModels = new File("Modelos");
		if(!mModels.exists())
		{
			mModels.mkdirs();
			System.out.println(mModels.getAbsolutePath()+": "+Strings.MSG_DIRECTROIO_CREADO);
		}

		//establecemos la posición de la clase de los conjuntos de datos.
		mDev.setClassIndex(mDev.numAttributes()-1);
		mTrain.setClassIndex(mTrain.numAttributes()-1);	
		
		//Inicializar figuras de mérito
		mFmeasureAux = 0.0;
		mFmeasureBest = 0.0;
		
		//Inicialización de límite de barrido de parámetros.Utilizamos aconsejados por los autores de la librería LibSVM.
		maxOfCSearch = pCmax; //Hasta  que valor es óptimo C "barrer"??11
		maxOfGSearch = pGmax;  //Hasta  que valor es óptimo	4
		maxOfDegreeSearch= pDegreeMax;

		bestC=-15;
		bestG=-3;
		bestD=2;
	}

	protected LibSVM extractEvaluatedModel(double gamma, double cost, int degree) {
		 	this.mModel= new LibSVM();
			
			//Utilizamos C-SVC
			mModel.setSVMType(new SelectedTag(0, LibSVM.TAGS_SVMTYPE));
			
			//Establecemos el kernel polinomial.
			mModel.setKernelType(new SelectedTag(1,LibSVM.TAGS_KERNELTYPE));
			
			this.mModel.setDegree(degree);
			this.mModel.setGamma(gamma);
			this.mModel.setCost(cost);		
			try 
			{
				this.mEvaluator=new Multibounds(mTrain);
			} 
			catch (Exception e)
			{
				Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_EVALUACION+e.getMessage());
			}
			
			mEvaluator.evaluateModel(mModel, mTrain, mDev);		
			return this.mModel;
	}
	

	/**
	 * barrido ad-hoc de los parámetros cost & gamma del modelo svm teniendo en cuenta la fmeasure de 
	 * la clase positiva. Se considera clase positiva el valor situado en el índice 0
	 * @return LibSVM
	 */
	public LibSVM scanParams(int pCmax,int pGMax, int pDmax){
		
		configureModel(pCmax, pGMax, pDmax);

		for (int d=2;d<maxOfDegreeSearch;d++)
		{
			int percent = (d-2)*100/(maxOfDegreeSearch-2);
			for (int c = -15; c <= maxOfCSearch; c++) {
				percent = (c+15)*100/(maxOfCSearch+15);
				System.out.println("Barriendo el parámetro degree........"+percent+"%");
				System.out.println("Barriendo el parámetro cost........"+percent+"%");
				for (int g = -3; g <= maxOfGSearch; g++)
				{
					percent = (g+3)*100/(maxOfGSearch+3);
					System.out.println("Barriendo el parámetro gamma........"+percent+"%");

					//La justificación del uso de potencias de dos se encuentra en el informe practica 3 SAD.				
					double cost = (Math.pow(2, c));
					double gamma = (Math.pow(2, g));

					VerboseCutter.getVerboseCutter().cutVerbose();	
					extractEvaluatedModel(cost, gamma, d);
					VerboseCutter.getVerboseCutter().activateVerbose();

					mFmeasureAux = mEvaluator.fMeasure(0);
					if (mFmeasureAux > mFmeasureBest)
					{
						updateParameters(cost, gamma, d);
					}
				}
				System.out.println("Fin barrido para cost="+c);
			}
			System.out.println("Fin barrido para degree="+d);
		}
		System.out.println("Barrido ah-hoc finalizado");

		this.mModel= new LibSVM();
		System.out.println("Configurando el modelo a serializar....");
		
		//La justificación del uso de potencias de dos se encuentra en el informe practica 3 SAD.				
		double cost = (Math.pow(2, bestC));
		double gamma = (Math.pow(2, bestG));
		
		extractEvaluatedModel(cost, gamma, bestD);
		
		System.out.println("Serializando el modelo....");
		try 
		{
			serializeModel();
		} 
		catch (IOException e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_CREAR_ARCHIVO+" Models");
		} 
		catch (Exception e) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_CREAR_ARCHIVO+" .model");
		}

		return mModel;
	}
	
	protected void updateParameters(double cost, double gamma, int degree) {
		mFmeasureBest = mFmeasureAux;
		bestC = cost;
		bestG = gamma;
		bestD = degree;
		System.out.println(Strings.MSG_V_CHANGE_FMEASURE);
	}
	
}
