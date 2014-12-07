package pack.datamining.modules.scans;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.util.Strings;
import pack.datamining.modules.util.VerboseCutter;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.SerializationHelper;

public class OneClassAlgorithm {
	private Instances dev, train, test;
	private LibSVM model;
	private Evaluation evaluator;
	private File models;
	
	//Variable para Logger
	public static final String LOG_TAG = OneClassAlgorithm.class.getSimpleName().toString();
	
	public OneClassAlgorithm(Instances trainData, Instances devData, Instances testData) {
		this.train = trainData;
		this.dev = devData;
		this.test = testData;
		this.model = new LibSVM();
		this.models = new File("Modelos");
	}
	
	public OneClassAlgorithm(Instances trainData, Instances devData) {
		this.train = trainData;
		this.dev = devData;
		this.model = new LibSVM();
		this.models = new File("Modelos");
	}
	
	public OneClassAlgorithm(Instances trainData) {
		this.train = trainData;
		this.model = new LibSVM();
	}
	
	/**
	 * Realiza el barrido de parámetros, crea un modelo evaluador.
	 * @return Instances: conjunto de instancias reconocidas como positivas.
	 */
	public void getModelTrained() {
		this.scanParams();
		this.evaluateModel();		
		this.createFiles();
	}
	
	/**
	 * Realiza el barrido de parámetros y realiza el test final en busca de las instancias positivas (los outliers).
	 * @return Instances: conjunto de instancias reconocidas como positivas.
	 */
	public Instances performTest() {
		Instances data = null;
		this.scanParams();
		this.doFinalTest();
		this.createFiles();
		try {
			data = this.getIncorrectDataset();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Elimina aquellas posibles instancias que no pertenecen al conjunto de instancias principal.
	 * @return Instances: conjunto de instancias con los outliers eliminados.
	 */
	public Instances removeOutliers() {
		System.out.println("\n--- Calculando clasificador One-class. ---");
		Instances data = null;
		this.resetModel(0.01);			
		this.evaluateDishonestModel();
		try {
			data = this.getNewDataset();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * Crea los ficheros .model y los datos acerca de la evaluación.
	 */
	private void createFiles() {
		if(!this.models.exists())
			this.models.mkdirs();
		
		Calendar calendar = new GregorianCalendar(); // Fecha y hora actuales.
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm"); // Formato de la fecha.
			String dateS = dateFormat.format(calendar.getTime()); // Fecha y hora actuales formateadas.	
		
		try {
			String text = evaluator.toSummaryString();
			
			SerializationHelper.write(this.models + "/" + dateS + "_OneClass.model", this.model);
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(this.models + "/" + dateS + "_OneClassPredictions.txt"));
			
			writer.write(text);
			if (writer != null)
				writer.close();
			
		} catch (Exception e) {
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_CREAR_ARCHIVO+" .model");
		}
		
		System.out.println("\nFichero de modelo creado en: " + this.models + "/" + dateS + "_OneClass.model.");
		System.out.println("Fichero de predicciones creado en: " + this.models + "/" + dateS + "_OneClassPredictions.txt.");
	}
	
	/**
	 * Barrido ad-hoc del parámetro nu para One-class.
	 */
	private void scanParams() {
		/* Variables para comparar el número de instancias correctas en curso con la mejor y la del barrido anterior. */
		double nCorrectInstancesAct = 0.0;
		double nCorrectInstancesBest = 0.0;
		double nCorrectInstancesPrev = 0.0;
		
		double bestN = 0.0; // Guardará la mejor nu.
		
		int sweep = 1; // Número para contar los barridos llevados hasta el momento.
		int exit = 0; // Salida forzada, si llegamos a 3 ocasiones en los que no se mejora el barrido anterior, saldremos del bucle.
		
		// http://www.researchgate.net/post/Increasing_nu_parameter_in_One-Class_SVM_Im_using_LIBSVM_causes_underfitting_and_a_small_value_for_nu_causes_overfitting_Am_I_right
		// http://scikit-learn.org/stable/modules/generated/sklearn.svm.OneClassSVM.html#sklearn.svm.OneClassSVM
		// http://scholar.google.nl/citations?view_op=view_citation&hl=en&user=DZ-fHPgAAAAJ&cstart=400&pagesize=100&sortby=pubdate&citation_for_view=DZ-fHPgAAAAJ:GFxP56DSvIMC
		// http://rvlasveld.github.io/blog/2013/07/12/introduction-to-one-class-support-vector-machines/
		
		System.out.println("\n--- Barrido del parámetro nu del clasificador One-class. ---");
		
		for (double nu = 0.001; nu <= 0.1; nu += 0.001) { // No recuerdo dónde leí que la mejor nu se encuentra entre 10^-3 y 10^-1.
			System.out.println("\nBarrido " + sweep + " de 100...");
			System.out.printf(" - Nu: %.3f.\n", nu);
			
			this.resetModel(nu);			
			this.evaluateModel();			
			
			nCorrectInstancesAct = this.evaluator.correct();
			
			System.out.println(" * Instancias correctamente clasificadas: " + (int)nCorrectInstancesAct + ".");
			
			if (nCorrectInstancesAct > nCorrectInstancesBest) { // El número actual de correctos es la mejor hasta ahora.
				nCorrectInstancesBest = nCorrectInstancesAct;
				bestN = nu;
				exit = 0; // Reseteamos el contador de salida forzada.
			} else {
				if (nCorrectInstancesPrev > nCorrectInstancesAct) { // Comprobamos el número de correctos actual con el anterior.
					if (++exit == 3) // Si en 3 ocasiones no se mejora el número de instancias correctas previas, paramos el barrido.
						break;
				} else // Si lo mejora, reseteamos el contador de salida forzada.
					exit = 0;
			}
			
			nCorrectInstancesPrev = nCorrectInstancesAct; // Asignamos el número de correctos actual en el anterior.
			sweep++; // Incrementamos el número de barrido.
		}
		
		if (exit == 3)
			System.out.println("\n--- Parada forzada de barrido. Obteniendo datos a guardar. ---");
		else
			System.out.println("\n--- Barrido terminado. Obteniendo datos a guardar. ---");
		
		this.resetModel(bestN);
	}
	
	/**
	 * Inicializa el modelo con el parámetro nu.
	 * @param nu: parámetro nu para poder usar el modelo One-class con SVM.
	 */
	private void resetModel(double nu) {
		this.model = new LibSVM();
		
		/* svm_type: set type of SVM
			0 -- C-SVC
			1 -- nu-SVC
			2 -- one-class SVM
			3 -- epsilon-SVR
			4 -- nu-SVR
		
			Por lo tanto, pondremos el tipo 2 para usar One-class.
		*/
		this.model.setSVMType(new SelectedTag(2, LibSVM.TAGS_SVMTYPE));
		
		/* Establecemos el kernel RBF justiificado en el informe práctica 3 SAD. */
		this.model.setKernelType(new SelectedTag(2, LibSVM.TAGS_KERNELTYPE));
		
		this.model.setDegree(0);
		this.model.setGamma(0);
		this.model.setNu(nu);
	}
	
	/**
	 * Evalúa el modelo con un conjunto de instancias Dev.
	 */
	private void evaluateModel() {
		try {
			VerboseCutter.getVerboseCutter().cutVerbose();
			this.model.buildClassifier(train);
			VerboseCutter.getVerboseCutter().activateVerbose();
			this.evaluator = new Evaluation(this.train);
			this.evaluator.evaluateModel(this.model, this.dev);
		} catch (Exception e) {
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_EVALUACION_MODELO + " - " + e.toString());
		}
	}
	
	/**
	 * Realiza el test final con el modelo entrenado.
	 */
	private void doFinalTest() {
		Instances data = this.mergeDataSets(train, dev);
		try {
			VerboseCutter.getVerboseCutter().cutVerbose();
			this.model.buildClassifier(data);
			VerboseCutter.getVerboseCutter().activateVerbose();
			this.evaluator = new Evaluation(data);
			this.evaluator.evaluateModel(this.model, this.test);
		} catch (Exception e) {
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_EVALUACION_MODELO + " - " + e.toString());
		}
	}
	
	/**
	 * Realiza una evaluación no honesta del modelo.
	 */
	private void evaluateDishonestModel() {
		try {
			VerboseCutter.getVerboseCutter().cutVerbose();
			this.model.buildClassifier(train);
			VerboseCutter.getVerboseCutter().activateVerbose();
			this.evaluator = new Evaluation(this.train);
			this.evaluator.evaluateModel(this.model, this.train);
		} catch (Exception e) {
			Logger.getLogger(LOG_TAG).log(Level.SEVERE, Strings.MSG_ERROR_EVALUACION_MODELO + " - " + e.toString());
		}
	}
	
	/**
	 * Recupera las instancias clasificadas correctamente y las devuelve en un nuevo conjunto.
	 * @return Instances: conjunto de instancias clasificadas correctamente.
	 * @throws Exception: si no es capaz de reclasificar una instancia.
	 */
	private Instances getNewDataset() throws Exception {
		Instances newData = new Instances(train, 0);
	    for (int i = 0; i < train.numInstances(); i++) {
	    	double pred = model.classifyInstance(train.instance(i));
	    	if (pred == train.instance(i).classValue())
	    	   newData.add(train.instance(i));
	    }
	    return newData;
	}
	
	/**
	 * Recupera las instancias clasificadas incorrectamente y las devuelve en un nuevo conjunto.
	 * @return Instances: conjunto de instancias clasificadas incorrectamente.
	 * @throws Exception: si no es capaz de reclasificar una instancia.
	 */
	private Instances getIncorrectDataset() throws Exception {
		Instances newData = new Instances(train, 0);
	    for (int i = 0; i < train.numInstances(); i++) {
	    	double pred = model.classifyInstance(train.instance(i));
	    	if (pred != train.instance(i).classValue())
	    	   newData.add(train.instance(i));
	    }
	    return newData;
	}
	
	/**
	 * Combina dos conjuntos de instancias.
	 * @param data1: conjunto 1.
	 * @param data2: conjunto 2.
	 * @return Instances: conjuntos combinados.
	 */
	private Instances mergeDataSets(Instances data1, Instances data2) {
		Instances data = data1;
		for (int i = 0; i < data2.numInstances(); i++) {
			data.add(data2.instance(i));
		}
		return data;
	}
}
