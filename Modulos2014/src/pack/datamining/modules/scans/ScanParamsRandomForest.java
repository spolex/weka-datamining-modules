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
			maxK = mTrain.numAttributes();
			
			//TODO ídem
		}
		
		configureModel();
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
	
	
}
