package pack.datamining.modules.scans;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.evaluation.Multibounds;
import pack.datamining.modules.util.Strings;
import pack.datamining.modules.util.VerboseCutter;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.SelectedTag;
/**
 * 
 * @author Iñigo Sánchez Méndez (spolex)
 *libsvm Version=1.0.5
 */
public class ScanSvmParams
{
	protected Instances mTrain;
	protected Instances mDev;
	protected LibSVM mModel;
	protected String mStrModel;
	protected Multibounds mEvaluator;
	protected File mModels;
	
	// Variables para comparar la f-measure en curso con la f-measure de la vuelta anterior.
	protected double mFmeasureAux = 0.0;
	protected double mFmeasureBest = 0.0;
	protected	int mMaxOfCSearch; //Hasta  que valor es óptimo C "barrer"??11
	protected	int mMaxOfGSearch;  //Hasta  que valor es óptimo	4
	protected 	int mMaxOfDSearch;
	
	protected double bestG;
	protected double bestC;
	protected double bestD;
	
	//Variable para Logger y la salida por pantalla
	public static final String LOG_TAG = ScanSvmParams.class.getSimpleName().toString();
	
	public ScanSvmParams(Instances pTrainData, Instances pDevData)
	{
		this.mTrain=pTrainData;
		this.mDev=pDevData;
		this.mModel= new LibSVM();		
	}
	
	/**
	 * barrido ad-hoc de los parámetros cost & gamma del modelo svm teniendo en cuenta la fmeasure de 
	 * la clase positiva. Se considera clase positiva el valor situado en el índice 0
	 * @return LibSVM
	 */
	public LibSVM scanParams(int pCmax,int pGMax, int pDmax){
		
		configureModel(pCmax, pGMax, pDmax);
		
		System.out.println(Strings.MSG_SCAN_MODEL+mStrModel);
		for (int d=2;d<= mMaxOfDSearch;d++){
			int percent = updateProgress(d,mMaxOfDSearch,2);
			System.out.println(Strings.MSG_SCAN_DEGREE+percent+"%");
			for (int c = -15; c <= mMaxOfCSearch; c++) {
				percent = updateProgress(c,mMaxOfCSearch,-15);
				System.out.println(Strings.MSG_SCAN_COST+percent+"%");
				for (int g = -3; g <= mMaxOfGSearch; g++)
				{
					percent = updateProgress(g,mMaxOfGSearch,-3);
					System.out.println(Strings.MSG_SCAN_GAMMA+percent+"%");

					//La justificación del uso de potencias de dos se encuentra en el informe practica 3 SAD.				
					double cost = (Math.pow(2, c));
					double gamma = (Math.pow(2, g));

					VerboseCutter.getVerboseCutter().cutVerbose();	
					extractEvaluatedModel(cost, gamma);
					VerboseCutter.getVerboseCutter().activateVerbose();

					mFmeasureAux = mEvaluator.fMeasure(0);
					if (mFmeasureAux > mFmeasureBest)
					{
						updateParameters(cost, gamma);
					}
					System.out.println(Strings.MSG_FIN_SCAN_GAMMA+g);
				}
				System.out.println(Strings.MSG_FIN_SCAN_COST+c);
			}
			System.out.println(Strings.MSG_FIN_SCAN_DEGREE+d);
		}
		System.out.println(Strings.MSG_FIN_SVM_SCAN);

		//TODO devolver LibSVM y serializar .model en main
		this.mModel= new LibSVM();
		System.out.println(Strings.MSG_CLF_OPT_CONF);
		
		//La justificación del uso de potencias de dos se encuentra en el informe practica 3 SAD.				
		double cost = (Math.pow(2, bestC));
		double gamma = (Math.pow(2, bestG));
		
		extractEvaluatedModel(cost, gamma);
		
		return mModel;		
	}

	/**
	 * @param c
	 * @return
	 */
	private int updateProgress(int c,int pMax, int pDif) {
		int percent;
		try
		{
			percent = (c-pDif)*100/(pMax-(2*pDif));
		}
		catch(ArithmeticException a)
		{
			Logger.getLogger(LOG_TAG).log(Level.WARNING, Strings.MSG_ARITM);
			a.printStackTrace();
			percent = 0;
		}
		return percent;
	}

	/**
	 * @param cost
	 * @param gamma
	 */
	protected void updateParameters(double cost, double gamma) {
		mFmeasureBest = mFmeasureAux;
		bestC = cost;
		bestG = gamma;
		System.out.println(Strings.MSG_V_CHANGE_FMEASURE);
	}

	/**
	 * @param cost
	 * @param gamma
	 */
	protected LibSVM extractEvaluatedModel(double cost, double gamma) 
	{
		this.mModel= new LibSVM();
		
		//Utilizamos C-SVC
		mModel.setSVMType(new SelectedTag(0, LibSVM.TAGS_SVMTYPE));
		
		if(mMaxOfDSearch==2)
		{
			//Establecemos el kernel RBF justiificado en el informe práctica 3 SAD
			mModel.setKernelType(new SelectedTag(2,LibSVM.TAGS_KERNELTYPE));
		}
		else
		{
			mModel.setKernelType(new SelectedTag(1, LibSVM.TAGS_KERNELTYPE));
		}
		
		this.mModel.setGamma(gamma);
		this.mModel.setCost(cost);
		this.mModel.setDegree(0);		
		try 
		{
			this.mEvaluator=new Multibounds(mTrain);
			//mModel.buildClassifier(mTrain); //TODO probar si no falla para ahorrar coste de ejecución
		} 
		catch (Exception e)
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_EVALUACION+e.getMessage());
		}
		
		mEvaluator.evaluateModel(mModel, mTrain, mDev);
		//mModel.buildClassifier(mTrain);
		return this.mModel;
	}
	

	/**
	 * configura el modelo y las clases necesarias para el barrido ad hoc,
	 * por el momento además asigna al atributo en la última posición la función de la clase.
	 */
	protected void configureModel(int pCmax, int pGMax, int pDmax)
	{
		
		//establecemos la posición de la clase de los conjuntos de datos.
		mDev.setClassIndex(mDev.numAttributes()-1);
		mTrain.setClassIndex(mTrain.numAttributes()-1);		
		
		//Inicializar figuras de mérito
		mFmeasureAux = 0.0;
		mFmeasureBest = 0.0;
		
		//barrido del parámetro c = 2^C, justificado en el guión porqué se utilizan potencias de 2
		System.out.println("Starting C and Gamma optimization with svm mmaxOfCSearchodel");
		
		// Utilizamos el kernel por defecto RBF
		//Inicialización de límite de barrido de parámetros.Utilizamos aconsejados por los autores de la librería LibSVM.
		mMaxOfCSearch = pCmax; //Hasta  que valor es óptimo C "barrer"??11
		mMaxOfGSearch = pGMax;  //Hasta  que valor es óptimo	4
		mMaxOfDSearch = pDmax; //Hasta que valor es óptimo barrer 5??		
		
		bestC=-15;
		bestG=-3;
		bestD=2;
	}

}
