package pack.datamining.modules.scans;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import pack.datamining.modules.evaluation.Multibounds;
import pack.datamining.modules.io.LoaderSaver;
import pack.datamining.modules.util.Strings;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.ChebyshevDistance;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.NormalizableDistance;
import weka.core.SelectedTag;
import weka.core.SerializationHelper;
import weka.core.neighboursearch.LinearNNSearch;

	public class Knn {
		private Instances train;
		private Instances dev;
		private IBk clasif;
		private NormalizableDistance[] lista;
		private Multibounds evaluator;
		private File model;
		
		public Knn(Instances vTrain, Instances vDev)
		{
			this.train=vTrain;
			this.dev=vDev;
			this.clasif= new IBk();
			this.lista = createDistances();
			this.model = new File("Modelos");
			if(!this.model.exists())
			{
				this.model.mkdirs();
				System.out.println(this.model.getAbsolutePath()+": "+Strings.MSG_DIRECTROIO_CREADO);
			}
		}
		public IBk getClasif(){
			return this.clasif;
		}
		public Evaluation getEvaluator(){
			return this.evaluator;
		}
		/**
		 * Barrido de pámetros del algoritmo knn con los parámtros k, distancia y poderación
		 * Devuelve .model con el modelo optimizado y un informe con las figuras de mérito usando la evaluación no honesta, hold out y cross validation.
		 * @param path
		 * @throws Exception
		 */

		public void barridoKnn(String path) throws Exception {
				this.train.setClassIndex(this.train.numAttributes()-1);
				this.dev.setClassIndex(this.dev.numAttributes()-1);
				LinearNNSearch nns= new LinearNNSearch(); 
				double mejorfm=0;
				int num= 0;
				//Limitamos el número de iteraciones de la k en el caso de que el número de instancias sea muy grande
				if (this.train.numInstances()>50){
					num=51;
				}
				else{
					num = this.train.numInstances()+1;
				}
				
				//Evaluation evaluate = new Evaluation(train);
				for(int i=1;i<num;i++){	//bucle para k
					for(int j=0;j<3;j++){//bucle para ponderaci�n
						double selectorValue=Math.pow(2, j);
						if(selectorValue==4){//normalizamos las distancias con -F
							lista[0].setDontNormalize(false);
							lista[1].setDontNormalize(false);
							lista[2].setDontNormalize(false);
						}
							for(int k=0;k<3;k++){
								this.evaluator=new Multibounds(train);
								IBk classifier = new IBk();//creamos el clasificador
								classifier.setKNN(i);//le asignamos la k al clasificador
								classifier.setDistanceWeighting(new SelectedTag((int)selectorValue, IBk.TAGS_WEIGHTING));// asignamos la ponderaci�n al clasificador
								// asignamos la distancoas al clasificador
								nns.setDistanceFunction(this.lista[k]);
								classifier.setNearestNeighbourSearchAlgorithm(nns);
								this.evaluator.evaluateKnn(this.clasif, train, dev);
								double fmeasure=this.evaluator.fMeasure(0);
								System.out.println("Valor de k:"+classifier.getKNN()+"\t"+"Ponderacion:"+classifier.getDistanceWeighting()+"\t"+"Distancia"+classifier.getNearestNeighbourSearchAlgorithm().getDistanceFunction().getClass().getName()+"\t"+"F-Measure:"+fmeasure);
								//Buscamos el mejor clasificador
								if(mejorfm<fmeasure){
									mejorfm=fmeasure;
									this.clasif=classifier;
								}
							}	
					}

				
		}
				String tipo = "No honesto:";
				this.evaluator.evaluateModel(this.clasif,train);
				imprimirDatos(path,this.evaluator, tipo);
				tipo = "Hold-out:";
				this.evaluator.evaluateKnn(this.clasif,train,dev);
				imprimirDatos(path, this.evaluator, tipo);
				tipo = "10 fold cross validation:";
				Instances newTrain = new Instances(this.train);
				for(int i = 0;i<this.dev.numInstances();i++){
					Instance ins = this.dev.instance(i);
					if(!newTrain.checkInstance(ins)){
						newTrain.add(ins);
					}
				}
				this.clasif.buildClassifier(newTrain);
				this.evaluator.assesPerformanceNFCV(this.clasif, 10, train);
				imprimirDatos(path, this.evaluator, tipo);
				serializeModel();
		}
		
		/**
		 *Devuelve una lista con las distancias que vayamos a utilizar, todas las distancias estar�n desnormalizadas 
		 * @return
		 */
		private static NormalizableDistance[] createDistances(){
			ManhattanDistance mD=new ManhattanDistance();
			EuclideanDistance eD= new EuclideanDistance();
			ChebyshevDistance cD= new ChebyshevDistance();
			mD.setDontNormalize(true);
			eD.setDontNormalize(true);
			cD.setDontNormalize(true);
			NormalizableDistance[] lista = new NormalizableDistance[3];
			lista[0]=mD;
			lista[1]=eD;
			lista[2]=cD;
			return lista;
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
		/**
		 * 
		 * @throws Exception
		 */
		private void serializeModel() throws Exception {
			if(!this.model.exists())this.model.mkdirs();
			Calendar calendar = new GregorianCalendar(); // Fecha y hora actuales.
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm"); // Formato de la fecha.
			String dateS = dateFormat.format(calendar.getTime()); // Fecha y hora actuales formateadas.		
			//String filePath = "Resultados/"+dateS+"resultados.txt";				
			SerializationHelper.write(this.model+"/"+dateS+"knn.model", clasif);
		}
		

	}
