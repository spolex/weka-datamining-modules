package pack.datamining.modules.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import pack.datamining.modules.evaluation.PrediccionProbabilidad;
import pack.datamining.modules.io.LoaderSaver;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class GeneracionPrediccionesP {

	public static void main(String[] args) {
		if(args.length!=0 && args.length != 2)
		{
			/*Comprobar si se ha pedido la ayuda*/
			if(buscarParametro("-h", args) != -1)
			{
				//Se imprime y sale el programa
				help();
				System.exit(0);
			}
			
			//Objetos necesarios para el algoritmo y algunos de sus valores por defecto
			String rutaIntancias = null;
			String rutaModelo = null;
			
			
			/******************** Bloque de carga de instancias de test ********************/
			
			rutaIntancias = args[0];
			Instances test = LoaderSaver.getMyLoader().loadArff(rutaIntancias);
			//La clase es el último atributo
			test.setClassIndex(test.numAttributes()-1);
			
			
			/********************Ruta del clasificador***************************/
			
			rutaModelo = args[1];
			
			
			/********************Bloque de creación del fichero*****************************/
			
			PrintStream salidaPredicciones = crearFichero(rutaIntancias, "-pred-prob.txt");
			
			
			/*********************Ejecucion*************************************************/
			
			//obtenemos las predicciones
			
			PrediccionProbabilidad pred = new PrediccionProbabilidad(rutaModelo);
			System.out.println("Realizando las predicciones...");
			pred.calcularPrediccionesConProbabilidad(test, salidaPredicciones);
			System.out.println("Predicciones calculadas");
			
			
		}
		else
		{
			System.err.println("Los parámetros especificados no son correctos, el programa no puede continuar");
			help();
			System.exit(1);
		}

	}

	/**
	 * Dado un string, se busca su posición dentro del array
	 * @param pStr 
	 * @param args
	 * el array en el que buscar
	 * @return
	 * la posición del string dentro del array
	 * -1 en caso de que no se encuentre el elemento
	 */
	private static int buscarParametro(String pStr, String[] array) {
		//comprobamos que el array tenga elementos
		if(array.length != 0)
		{
			//recorremos el array buscando el elemento
			
			int i = 0;
			boolean enc = false;
			
			while(!enc && i < array.length)
			{
				if(array[i].equals(pStr))
				{
					enc = true;
				}
				else
				{
					i++;
				}
			}
			
			if(!enc)
			{
				i = -1;
			}
			
			return i;
		}
		return -1;
	}
	
	/**
	 * Imprime la ayuda del programa
	 */
	private static void help()
	{
		
	}
	
	/**
	 * Crea un nuevo fichero y devuelve su descriptor para escribir en el mismo
	 * @param pPathOriginal
	 * El path del fichero original, con extensión
	 * @param pSufijo
	 * El sufijo que tendrá el nuevo fichero
	 * @return
	 * El descriptor del fichero para comenzar su escritura. null si no se puede crear
	 */
	private static PrintStream crearFichero(String pPathOriginal, String pSufijo)
	{
		String path = pPathOriginal.substring(0, (pPathOriginal.length() - 5));
		path = path + pSufijo;
		
		File f = new File(path);
		PrintStream ps;
		try {
			ps = new PrintStream(f);
			return ps;
		} catch (FileNotFoundException e) {
			System.err.println("No se ha podido crear el fichero de guardado");
			System.exit(1);
		}
		
		return null;
	}
}
