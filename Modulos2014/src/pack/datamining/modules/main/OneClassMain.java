package pack.datamining.modules.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import pack.datamining.modules.filters.OneClassFilters;
import pack.datamining.modules.filters.Randomize;
import pack.datamining.modules.io.LoaderSaver;
import pack.datamining.modules.scans.OneClassAlgorithm;
import pack.datamining.modules.util.Strings;
import weka.core.Instances;

public class OneClassMain {
	/* Variable para el Logger. */
	public static final String LOG_TAG = OneClassAlgorithm.class.getSimpleName().toString();
		
	public static void main(String[] args) {
		Instances train = null;
		Instances dev = null;
		Instances test = null;
		int nParamters = args.length;
		
		System.out.println("+++ One-class. +++");
		
		if (nParamters == 2) { // Calcular parámetros óptimos de One-class y exportar modelo.
			train = LoaderSaver.getMyLoader().loadArff(args[0]);
			dev = LoaderSaver.getMyLoader().loadArff(args[1]);
			System.out.println("\n--- Instancias leídas. ---");
		} else if (nParamters == 1) { // Outliers usando One-class.
			train = LoaderSaver.getMyLoader().loadArff(args[0]);
			System.out.println("\n--- Instancias leídas. ---");
		} else if (nParamters == 3) { // OCalcular parámetros óptimos de One-class y realizar test.
			train = LoaderSaver.getMyLoader().loadArff(args[0]);
			dev = LoaderSaver.getMyLoader().loadArff(args[1]);
			test = LoaderSaver.getMyLoader().loadArff(args[2]);
			System.out.println("\n--- Instancias leídas. ---");
		} else { // Error.
			System.out.println(Strings.MSG_ERROR_NUMS_ARGS + ".");
			System.out.println("1. Para buscar outliers: parámetro único, ruta con el fichero con las instancias.");
			System.out.println("2. Para realizar modelo: dos parámetros, ruta de las instancias de entrenamiento y ruta de las instancias Dev.");
			System.out.println("3. Para buscar outliers: tres parámetros, ruta de las instancias de entrenamiento, ruta de las instancias Dev y ruta de instancias de test.");
			return;
		}
		
		if (nParamters == 2) {
			System.out.println("\n+++ Optimizar modelo óptimo. +++");
			if (dev != null && train != null) {
				System.out.println("\n--- Instancias barajadas. ---");
				try {
					dev = Randomize.randomize(dev, 1);
					train = Randomize.randomize(train, 1);
					System.out.println("\n--- Instancias barajadas. ---");
				} catch (Exception e) {
					System.out.println(Strings.MSG_ERROR_INSTANCES_RANDOM);
					return;
				}
				
				try {
					train = OneClassFilters.getInstance().prepareNegativeInstances(train);
					dev = OneClassFilters.getInstance().prepareNegativeInstances(dev);
				} catch (Exception e) {
					Logger.getLogger(LOG_TAG).log(Level.SEVERE, "¡¡¡ Error al filtrar la clase !!! - " + e.toString() + ".");
					System.exit(0);
				}
				
				OneClassAlgorithm svmScan = new OneClassAlgorithm(train, dev);
				Instances data = svmScan.getModelTrained();

				try {
					data = OneClassFilters.getInstance().removeArtificialAttribute(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				LoaderSaver.getMyLoader().saveInstances(data, args[0].substring(0, args[0].length() - 5) + "-Positivos.arff");
				
				System.out.println("\nNuevo fichero: " + args[0].substring(0, args[0].length() - 5) + "-Positivos.arff");
				System.out.println("\n--- Modelo One-class optimizado. ---");
			} else {
				System.out.println("\n¡¡¡ Error al crear el modelo !!!");
			}
		} else if (nParamters == 1) {
			if (train != null) {
				System.out.println("\n+++ Obtención de outliers. +++");
				try {
					train = Randomize.randomize(train, 1);
					System.out.println("\n--- Instancias barajadas. ---");
				} catch (Exception e) {
					System.out.println(Strings.MSG_ERROR_INSTANCES_RANDOM);
					return;
				}
				
				try {
					train = OneClassFilters.getInstance().prepareInstances(train);
				} catch (Exception e) {
					Logger.getLogger(LOG_TAG).log(Level.SEVERE, "¡¡¡ Error al filtrar la clase !!! - " + e.toString() + ".");
					System.exit(0);
				}
				
				OneClassAlgorithm svmScan = new OneClassAlgorithm(train);
				Instances newData = svmScan.removeOutliers();
				
				try {
					newData = OneClassFilters.getInstance().removeArtificialAttribute(newData);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				System.out.println("\nNuevo fichero: " + args[0].substring(0, args[0].length() - 5) + "-SinOutliers.arff");
				
				LoaderSaver.getMyLoader().saveInstances(newData, args[0].substring(0, args[0].length() - 5) + "-SinOutliers.arff");

				System.out.println("\n--- Fichero con las instancias sin outliers creado. ---");
			} else {
				System.out.println("\n¡¡¡ Error al crear el modelo !!!");
			}
		} else if (nParamters == 3) {
			if (dev != null && train != null && test != null) {
				System.out.println("\n+++ Realización del test completo de One-class. +++");
				try {
					train = Randomize.randomize(train, 1);
					dev = Randomize.randomize(dev, 1);
					test = Randomize.randomize(test, 1);
					System.out.println("\n--- Instancias barajadas. ---");
				} catch (Exception e) {
					System.out.println(Strings.MSG_ERROR_INSTANCES_RANDOM);
					return;
				}
				
				try {
					train = OneClassFilters.getInstance().prepareNegativeInstances(train);
					dev = OneClassFilters.getInstance().prepareNegativeInstances(dev);
					test = OneClassFilters.getInstance().prepareTestIntances(test);
					
				} catch (Exception e) {
					Logger.getLogger(LOG_TAG).log(Level.SEVERE, "¡¡¡ Error al filtrar la clase !!! - " + e.toString() + ".");
					System.exit(0);
				}
				
				OneClassAlgorithm svmScan = new OneClassAlgorithm(train, dev, test);
				Instances data = svmScan.performTest();
				
				try {
					data = OneClassFilters.getInstance().removeArtificialAttribute(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				LoaderSaver.getMyLoader().saveInstances(data, args[0].substring(0, args[0].length() - 5) + "-Positivos.arff");
				
				System.out.println("\nNuevo fichero: " + args[0].substring(0, args[0].length() - 5) + "-Positivos.arff");
				System.out.println("\n--- Test del modelo One-clas realizado. ---");
			} else {
				System.out.println("\n¡¡¡ Error al crear el modelo !!!");
			}
		}
	} 
}
