package pack.datamining.modules.evaluation;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.util.VerboseCutter;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;

@SuppressWarnings("serial")
public class Multibounds extends Evaluation
{
	public static final String LOG_TAG = Multibounds.class.getSimpleName().toString();
	private Instances data;	
	public Multibounds(Instances pData) throws Exception
	{
		super(pData);
		this.data = pData;
	}
	@SuppressWarnings("unused")
	private Instances getData() {
		return data;
	}


	@SuppressWarnings("unused")
	private void setData(Instances pData) 
	{
		this.data = pData;
	}
	/**
	 * @param newData
	 * @param estimador
	 * @param pFold
	 * @return evaluator
	 * 
	 * pre:Recibe el conjunto de instancias con las que se evalúa, además el clasificador
	 * debe estar previamente configurado y recibido como parametro en la constructora
	 * de la presente clase.
	 * 
	 * pos:Devuelve un Evaluator del clasificador recibido en la constructora.
	 * Evalua desempeño del clasificador utilizando el método pFold-HCV
	 * @throws Exception 
	 */
	public  void assesPerformanceNFCV(Classifier estimador, int pFold, Instances pData) throws Exception
	{
		try 
		{			
			estimador.buildClassifier(pData);
		} 
		catch (Exception e2) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, e2.getMessage());
		}
		this.crossValidateModel(estimador, pData,pFold, new Random(1));		
	}
	/**
	 *  pre: Recibe el evaluador con la evaluacion hecha
	 * fuente:http://weka.wikispaces.com/Programmatic+Use 
	 * @param evaluator
	 * @throws Exception
	 *  post: imprime por pantalla los resultados de la evaluacion
	 */
	public void printStatistics(Evaluation evaluator)
	{
		try {
			System.out.println(evaluator.toClassDetailsString());
			System.out.println(evaluator.toMatrixString());
			System.out.println("weightedFMeasure: "+evaluator.weightedFMeasure());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * pre : recibe el modelo ya entrenado
	 * @palo  estimador
	 * @param unlabeled
	 * @return Instances
	 * @throws Exception
	 *	post: devuelve el conjunto de instancias con las predicciones hechas
	 */
	public Instances predictionsMaker(LibSVM pEstimador,
			Instances pUnlabeled) throws Exception {
		// Se crea una copia de las instancias sin clasificar para obtener después la clase estimada
		 Instances labeled = new Instances(pUnlabeled);
		 
		 // Estimar las clases
		 for (int i = 0; i < pUnlabeled.numInstances(); i++) {
		   double clsLabel = pEstimador.classifyInstance(pUnlabeled.instance(i));
		   labeled.instance(i).setClassValue(clsLabel);
		 }
		return labeled;
	}
	/**
	 * @param pEstimador
	 * @param pUnlabeled
	 * @return Instances
	 * @throws Exception
	 */
	public  Instances predictionsMakerGeneric(Classifier pEstimador,
			Instances pUnlabeled) throws Exception {
		// Se crea una copia de las instancias sin clasificar para obtener después la clase estimada
		 Instances labeled = new Instances(pUnlabeled);
		 
		 // Estimar las clases
		 for (int i = 0; i < pUnlabeled.numInstances(); i++) {
		   double clsLabel = pEstimador.classifyInstance(pUnlabeled.instance(i));
		   labeled.instance(i).setClassValue(clsLabel);
		 }
		return labeled;
	}
	/**
	 * fuente:http://weka.wikispaces.com/Programmatic+Use 
	 * @param estimador
	 * @param pData
	 * @param pRuta
	 * @throws Exception
	 */
	public double[] dishonestEvaluator(Classifier estimador, Instances pData) throws Exception
	{		
		return this.evaluateModel(estimador, pData);	 
	}
	
	/**
	 * fuente:http://weka.wikispaces.com/Programmatic+Use 
	 * @param estimador
	 * @param pData
	 * @param pRuta
	 * @throws Exception
	 */
	public double[] dishonestEvaluatorSVM(LibSVM estimador, Instances pData) throws Exception
	{		
		return this.evaluateModel(estimador, pData);		 
	}
	/**
	 * 
	 * @param estimador
	 * @param pTrain
	 * @param pDev
	 */
	public void evaluateModel(LibSVM estimador,Instances pTrain,Instances pDev){			
		try 
		{	
			VerboseCutter.getVerboseCutter().cutVerbose();
			estimador.buildClassifier(pTrain);
			VerboseCutter.getVerboseCutter().activateVerbose();
		} 
		catch (Exception e2) 
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, e2.getMessage());
		}
		try 
		{
			VerboseCutter.getVerboseCutter().cutVerbose();
			this.evaluateModel(estimador, pDev);
			VerboseCutter.getVerboseCutter().activateVerbose();
		} 
		catch (Exception e)
		{
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, e.getMessage());
		}		
	}
}
