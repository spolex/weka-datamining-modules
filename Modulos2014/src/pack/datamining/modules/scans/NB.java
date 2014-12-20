package pack.datamining.modules.scans;

import java.io.File;
import java.util.AbstractCollection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import pack.datamining.modules.evaluation.Multibounds;
import pack.datamining.modules.io.LoaderSaver;
import pack.datamining.modules.util.Strings;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class NB {
	private Instances train;
	private Instances dev;
	private NaiveBayes clasif;
	private Multibounds evaluator;
	private File model;
	
	public NB(Instances vTrain, Instances vDev){
		this.train=vTrain;
		this.dev=vDev;
		this.clasif= new NaiveBayes();
		this.model = new File("Modelos");
		if(!this.model.exists())
		{
			this.model.mkdirs();
			System.out.println(this.model.getAbsolutePath()+": "+Strings.MSG_DIRECTROIO_CREADO);
		}
	}
	public NaiveBayes getClasif(){
		return this.clasif;
	}
	public Evaluation getEvaluator(){
		return this.evaluator;
	}
	public void evaluarNB(String path) throws Exception{
		this.train.setClassIndex(this.train.numAttributes()-1);
		this.dev.setClassIndex(this.dev.numAttributes()-1);
		this.evaluator = new Multibounds(this.train);
		String tipo = "No honesto:";
		this.evaluator.evaluateNB(this.clasif,this.train, this.train);
		imprimirDatos(path,this.evaluator, tipo);
		
		tipo = "Hold-out:";
		this.evaluator = new Multibounds(train);
		this.evaluator.evaluateNB(this.clasif,this.train,dev);
		imprimirDatos(path, this.evaluator, tipo);
		
		tipo = "10 fold cross validation:";
		Instances newTrain = new Instances(this.train);
		for(int i = 0;i<this.dev.numInstances();i++){
			Instance ins = this.dev.instance(i);
			if(!newTrain.checkInstance(ins)){
				newTrain.add(ins);
			}
		}
		this.evaluator = new Multibounds(newTrain);
		this.clasif.buildClassifier(newTrain);
		this.evaluator.assesPerformanceNFCV(this.clasif, 10, newTrain);
		imprimirDatos(path, this.evaluator, tipo);
		serializeModel();
		
	}
	/**
	 * Escribe los datos de las evaluaciones en un fichero de texto.
	 * @param path
	 * @param evaluator
	 * @param tipo
	 */
	public void imprimirDatos(String path,Evaluation evaluator, String tipo)
	{
			
			String datos;
			try {
				datos =tipo+"\n"+ evaluator.toClassDetailsString()+evaluator.toMatrixString();
				LoaderSaver.getMyLoader().SaveFile(path,datos,false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
			
	}
	private void serializeModel() throws Exception {
		if(!this.model.exists())this.model.mkdirs();
		Calendar calendar = new GregorianCalendar(); // Fecha y hora actuales.
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm"); // Formato de la fecha.
		String dateS = dateFormat.format(calendar.getTime()); // Fecha y hora actuales formateadas.		
		//String filePath = "Resultados/"+dateS+"resultados.txt";				
		SerializationHelper.write(this.model+"/"+dateS+"naivebayes.model", clasif);
	}

}
