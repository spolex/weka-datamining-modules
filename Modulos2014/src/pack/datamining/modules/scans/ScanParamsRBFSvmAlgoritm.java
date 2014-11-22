package pack.datamining.modules.scans;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.evaluation.Multibounds;
import pack.datamining.modules.util.Strings;
import pack.datamining.modules.util.VerboseCutter;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.SerializationHelper;
/**
 * 
 * @author Iñigo Sánchez Méndez (spolex)
 *libsvm Version=1.0.5
 */
public class ScanParamsRBFSvmAlgoritm
{
	private Instances mTrain;
	private Instances mDev;
	private LibSVM mModel;
	private Multibounds mEvaluator;
	private File mModels;
	
	// Variables para comparar la f-measure en curso con la f-measure de la vuelta anterior.
	private double mFmeasureAux = 0.0;
	private double mFmeasureBest = 0.0;
	private	int maxOfCSearch; //Hasta  que valor es óptimo C "barrer"??11
	private	int maxOfGSearch;  //Hasta  que valor es óptimo	4
	
	private double bestG;
	private double bestC;
	
	//Variable para Logger y la salida por pantalla
	String mSoNombre;
	Boolean mUnix=true;
	public static final String LOG_TAG = ScanParamsRBFSvmAlgoritm.class.getSimpleName().toString();
	
	public ScanParamsRBFSvmAlgoritm(Instances pTrainData, Instances pDevData)
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
	public LibSVM scanParams(int pCmax,int pGMax){
		
		configureModel(pCmax, pGMax);
		
		for (int c = -15; c <= maxOfCSearch; c++) {
			int percent = (c+15)*100/(maxOfGSearch+15);
			System.out.println("Barriendo el parámetro cost........"+percent+"%");
			VerboseCutter.getVerboseCutter().deleteConsole(mUnix);
			for (int g = -3; g <= maxOfGSearch; g++)
			{
				percent = (g+3)*100/(maxOfGSearch+3);
				System.out.println("Barriendo el parámetro gamma........"+percent+"%");
				VerboseCutter.getVerboseCutter().deleteConsole(mUnix);
								
				//La justificación del uso de potencias de dos se encuentra en el informe practica 3 SAD.				
				double cost = (Math.pow(2, c));
				double gamma = (Math.pow(2, g));
				extractEvaluatedModel(cost, gamma);

				mFmeasureAux = mEvaluator.fMeasure(0);
				if (mFmeasureAux > mFmeasureBest)
				{
					updateParameters(cost, gamma);
				}
			}
			System.out.println("Fin barrido para cost="+c);
		}
		System.out.println("Barrido ah-hoc finalizado");

		this.mModel= new LibSVM();
		System.out.println("Configurando el modelo a serializar....");
		
		//La justificación del uso de potencias de dos se encuentra en el informe practica 3 SAD.				
		double cost = (Math.pow(2, bestC));
		double gamma = (Math.pow(2, bestG));
		extractEvaluatedModel(cost, gamma);
		
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

	/**
	 * @throws Exception
	 */
	private void serializeModel() throws Exception {
		if(!mModels.exists())mModels.mkdirs();
		Calendar calendar = new GregorianCalendar(); // Fecha y hora actuales.
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm"); // Formato de la fecha.
		String dateS = dateFormat.format(calendar.getTime()); // Fecha y hora actuales formateadas.		
		//String filePath = "Resultados/"+dateS+"resultados.txt";				
		SerializationHelper.write(mModels+"/"+dateS+"_RBFsvm.model",mModel );
	}

	/**
	 * @param cost
	 * @param gamma
	 */
	private void updateParameters(double cost, double gamma) {
		mFmeasureBest = mFmeasureAux;
		bestC = cost;
		bestG = gamma;
		System.out.println(Strings.MSG_V_CHANGE_FMEASURE);
	}

	/**
	 * @param cost
	 * @param gamma
	 */
	private LibSVM extractEvaluatedModel(double cost, double gamma) 
	{
		this.mModel.setGamma(gamma);
		this.mModel.setCost(cost);
		this.mModel.setDegree(0);
		//Utilizamos C-SVC
		mModel.setSVMType(new SelectedTag(0, LibSVM.TAGS_SVMTYPE));
		
		//Establecemos el kernel RBF justiificado en el informe práctica 3 SAD
		mModel.setKernelType(new SelectedTag(2,LibSVM.TAGS_KERNELTYPE));
		
		
		try 
		{
			this.mEvaluator=new Multibounds(mTrain);
			mModel.buildClassifier(mTrain);
		} 
		catch (Exception e)
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_EVALUACION+e.getMessage());
		}
		mEvaluator.evaluateModel(mModel, mTrain, mDev);		
		return this.mModel;
	}
	

	/**
	 * configura el modelo y las clases necesarias para el barrido ad hoc,
	 * por el momento además asigna al atributo en la última posición la función de la clase.
	 */
	private void configureModel(int pCmax, int pGMax)
	{
		//Creamos el directorio necesario para serializar los modelos.
		mModels = new File("Modelos");
		if(!mModels.exists())
		{
			mModels.mkdirs();
			System.out.println(mModels.getAbsolutePath()+": "+Strings.MSG_DIRECTROIO_CREADO);
		}
		
		//obtenemos el sistema operativo.
		mSoNombre=System.getProperty("os.name");
		if(mSoNombre.toUpperCase().contains("WINDOWS"))
		{
			mUnix=false;
		}
		//establecemos la posición de la clase de los conjuntos de datos.
		mDev.setClassIndex(mDev.numAttributes()-1);
		mTrain.setClassIndex(mTrain.numAttributes()-1);		
		
		//Inicializar figuras de mérito
		mFmeasureAux = 0.0;
		mFmeasureBest = 0.0;
		
		//barrido del parámetro c = 2^C, justificado en el guión porqué se utilizan potencias de 2
		System.out.println("Starting C and Gamma optimization with svm model");
		
		// Utilizamos el kernel por defecto RBF
		//Inicialización de límite de barrido de parámetros.Utilizamos aconsejados por los autores de la librería LibSVM.
		maxOfCSearch = pCmax; //Hasta  que valor es óptimo C "barrer"??11
		maxOfGSearch = pGMax;  //Hasta  que valor es óptimo	4
		
		bestC=-15;
		bestG=-3;		
	}
	
	
}
