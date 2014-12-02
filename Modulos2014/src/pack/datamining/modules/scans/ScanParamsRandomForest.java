package pack.datamining.modules.scans;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


import pack.datamining.modules.evaluation.Multibounds;
import pack.datamining.modules.util.Strings;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class ScanParamsRandomForest {
	
	
	private Instances mTrain;
	private Instances mDev;
	private RandomForest mModel;
	private Multibounds mEvaluator;
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
	
	
	public RandomForest scanParams(int maxI, int maxK)
	{
		/*
		 * Parámetros a barrer:
		 * 	-I: número de árboles (por defecto 10)
		 *  -K: número de atributos a considerar
		 */
		int minI = 0;
		int minK = 0;
		if (maxI==0)
		{
			maxI = 10;
			//TODO ver cuál podría ser el max
		}
		if (maxK == 0)
		{
			maxK = Math.sqrt(mTrain.numAttributes()).intValue();
			
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
					this.mEvaluator=new Multibounds(mTrain);
					mEvaluator.evaluateModel(mModel, mTrain, mDev);
				}
				catch (Exception e) {}
				
				mFmeasureAux = mEvaluator.fMeasure(0);
				
				if (mFmeasureAux > mFmeasureBest)
				{
					 bestI = i;
					 bestK = k;
				}
			}
			
			//si estamos en un múltiplo de 10 de nº de árboles (se va comprobar cada 10 en 10)
			
			if (i % 10 == 0 && previousBestFmeasure != 0)
			{
				if (shouldStop(mFmeasureBest, previousBestFmeasure))
				{
					i = maxI;
				}
				previousBestFmeasure = mFmeasureBest;
			}
			else if (previousBestFmeasure == 0)
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
		//TODO no sé si está todo
	
	
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
			this.mEvaluator=new Multibounds(mTrain);
			
		} 
		catch (Exception e)
		{
			System.out.println(Strings.MSG_ERROR_EVALUACION+e.getMessage());
		}
		
		try {
			mEvaluator.evaluateModel(mModel, mTrain, mDev);
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
		 *  
		 */
		
		if (fMeasNew - fMeasPrev < 0.05)
			return true;
		else
			return false;
	}
	
	
}
