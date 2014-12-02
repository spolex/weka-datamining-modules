package pack.datamining.modules.evaluation;

import java.io.PrintStream;
import java.util.Iterator;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

/**
 * Clase destinada a realizar las predicciones de un test obteniendo por cada instancia la probabilidad de pertenencia 
 * a cada una de las clases posibles
 * 
 * @author david
 *
 */
public class PrediccionProbabilidad {
	
	//Guardamos el modelo como object, puede ser LibSVM o Classifier
	private Object modelo;
	
	
	/**
	 * Crea la clase para realizar las predicciones
	 * @param pModelo
	 */
	public PrediccionProbabilidad(String pModelo)
	{
		try 
		{
			this.modelo = SerializationHelper.read(pModelo);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println("Error al cargar el modelo");
			System.exit(1);
		}
	}
	
	/**
	 * Realiza las predicciones de las instancias de test dadas y las almacena en el fichero dado.
	 * <br><br>
	 * Por cada instancia se calcula la probabilidad de pertenencia a cada uno de las diferentes clases posibles.
	 * <br><br>
	 * La primera línea del fichero de salida contiene los valores posibles de la clase.
	 * 
	 * @param pTest
	 * Las instancias de las que calcular su probabilidad de pertenencia.
	 * @param ficheroSalida
	 * El ficherro en que se imprimirán los resultados
	 */
	public void calcularPrediccionesConProbabilidad(Instances pTest, PrintStream ficheroSalida)
	{
		//Comprobamos de que tipo es el modelo dado
		
		if(this.modelo instanceof Classifier)
		{
			this.calcularPrediccionesConProbabilidadClassifier((Classifier) this.modelo, pTest, ficheroSalida);
		}
	}
	
	private void calcularPrediccionesConProbabilidadClassifier(Classifier pModelo, Instances pTest, PrintStream ficheroSalida)
	{
		//Imprimir los valores posibles de la clase en el fichero
		ficheroSalida.print("%");
		Attribute clase = pTest.classAttribute();
		for(int val=0; val < clase.numValues(); val++)
		{
			ficheroSalida.print(" "+ clase.value(val));
		}


		//Calcular las probabilidades para cada instancia

		Iterator<Instance> it = pTest.iterator();
		Instance instanciaActual;

		while(it.hasNext())
		{
			instanciaActual = it.next();

			try {
				ficheroSalida.println(pModelo.distributionForInstance(instanciaActual).toString());
			} catch (Exception e) {
				//fallo
			}
		}

		//termina, cerramos el fichero
		ficheroSalida.close();
	}
	
	private void calcularPrediccionesConProbabilidadLibSVM(Classifier modelo, Instances pTest, PrintStream ficheroSalida)
	{
		
	}

}
