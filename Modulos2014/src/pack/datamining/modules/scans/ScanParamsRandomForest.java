package pack.datamining.modules.scans;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;




import pack.datamining.modules.evaluation.Multibounds;
import pack.datamining.modules.util.Strings;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class ScanParamsRandomForest {
	
	
	private Instances mTrain;
	private Instances mDev;
	private RandomForest mModel;
	private Evaluation mEvaluator;
	private File mModels;
	private double mFmeasureAux = 0.0;
	private double mFmeasureBest = 0.0;
	private int bestI = 0;
	private int bestK = 0;
	

	public ScanParamsRandomForest(Instances pTrainData, Instances pDevData)
	{
		this.mTrain=pTrainData;
		this.mDev=pDevData;
		this.mModel= new RandomForest();		
	}
	 
	/*
	 * maxI: número máximo de árboles a probar
	 * maxK: número máximo de atributos a probar. Puede ser conveniente darle el valor del número de atributos si no son demasiados
	 */
	public RandomForest scanParams(int maxI, int maxK)
	{
		/*
		 * Parámetros a barrer:
		 * 	-I: número de árboles (por defecto en weka 10)
		 *  -K: número de atributos a considerar
		 */
		int minI = 1;
		int minK = 1;
		if (maxI==0)
		{
			maxI = 1000;
			
			// Un número arbitrariamente grande
		}
		
		if (maxK == 0)
		{
			maxK = Double.valueOf(Math.sqrt(Double.valueOf(mTrain.numAttributes()))).intValue();
			
			// me encanta hacer casting numérico en Java :D okno
			
			/*
			 * The main parameters to adjust when using these methods is n_estimators 
			 * and max_features. The former is the number of trees in the forest. The 
			 * larger the better, but also the longer it will take to compute. In addition, 
			 * note that results will stop getting significantly better beyond a critical 
			 * number of trees. The latter is the size of the random subsets of features 
			 * to consider when splitting a node. The lower the greater the reduction of 
			 * variance, but also the greater the increase in bias. Empirical good default
			 * values are max_features=n_features for regression problems, and 
			 * max_features=sqrt(n_features) for classification tasks (where n_features is
			 * the number of features in the data). 
			 * 
			 * fuente: http://scikit-learn.org/stable/modules/ensemble.html
			 */
			
		}
	
		
		configureModel();
		
		double previousBestFmeasure = 0.0;
		
		
		for (int i = minI; i<maxI; i++)
		{
			mModel.setNumTrees(i);
			
			for (int k = minK; k<maxK; k++)
			{
				mModel.setNumFeatures(k);
				try {
					mModel.buildClassifier(mTrain);
				} catch (Exception e1) {
					
					e1.printStackTrace();
				}
			
				try {
					mEvaluator = new Evaluation(mTrain);
					mEvaluator.evaluateModel(mModel, mDev);
					
				}
				catch (Exception e) {}
				
				
				mFmeasureAux = mEvaluator.fMeasure(0);
				if (mFmeasureAux > mFmeasureBest)
				{
					mFmeasureBest = mFmeasureAux;
					 bestI = i;
					 bestK = k;
				}
			}
			System.out.println("n de i "+i);
			System.out.println(mFmeasureAux);
			
			
			System.out.println(mEvaluator.fMeasure(1));
			/*
			 * Si estamos en un múltiplo de 100 de nº de árboles (se va comprobar cada 100 en 100), para tener un límite de barrido ya que el nº de árboles
			 * no tiene un límite fijo más allá de la lógica (es decir, si no mejora o mejora muy poco, etc.). 
			 */
			
			if (i % 100 == 0 && previousBestFmeasure != 0.0)
			{
				if (shouldStop(mFmeasureBest, previousBestFmeasure))
				{
					i = maxI;
				}
				previousBestFmeasure = mFmeasureBest;
			}
			else if (i % 100 == 0 && previousBestFmeasure == 0)
			{
				previousBestFmeasure = mFmeasureBest;
			}
		}
		
		mModel = extractEvaluatedModel(bestI, bestK);
		return mModel;
	}
	
	
	private void configureModel()
	{
	

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
	
	
	private RandomForest extractEvaluatedModel(int nTrees, int nFeat) 
	{
		this.mModel= new RandomForest();
		mModel.setNumTrees(nTrees);
		mModel.setNumFeatures(nFeat);
			
		try 
		{
			mModel.buildClassifier(mTrain);
			this.mEvaluator=new Evaluation(mTrain);
			
		} 
		catch (Exception e)
		{
			System.out.println(Strings.MSG_ERROR_EVALUACION+e.getMessage());
		}
		
		try {
			mEvaluator.evaluateModel(mModel, mDev);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		System.out.println("Serializando el modelo....");
		try 
		{
			serializeModel();
		} 
		catch (IOException e) 
		{
			System.out.println(Strings.MSG_ERROR_CREAR_ARCHIVO+" Models");
		} 
		catch (Exception e) 
		{
			System.out.println(Strings.MSG_ERROR_CREAR_ARCHIVO+" .model");
		}
		
		
		
		return this.mModel;
	}
	
	private boolean shouldStop(double fMeasNew, double fMeasPrev)
	{
		/*
		 *  Si la fMeasure no ha mejorado en más de 0'05 (de nuevo, un nº arbitrariamente pequeño), paramos
		 */
		
		if (fMeasNew - fMeasPrev < 0.05)
			return true;
		else
			return false;
	}
	
	
}
