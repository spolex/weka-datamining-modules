package pack.datamining.modules.filters;

import weka.core.AttributeStats;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.AddNoise;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.RemoveWithValues;

public class OneClassFilters {
	private static OneClassFilters instance = null;
	
	private OneClassFilters() {}
	
	public static OneClassFilters getInstance() {
		if(instance == null)
			instance = new OneClassFilters();
		return instance;
	}

	/**
	 * Elimina las instancias positivas del conjunto .
	 * @param data: conjunto de datos a preparar.
	 * @return Instances: conjunto con las instancias borradas.
	 * @throws Exception
	 */
	public Instances prepareNegativeInstances(Instances data) throws Exception {
		data = this.classNegFiltering(data);
		data = this.removeClass(data);
		data = this.setUniqueClass(data);
		return this.setClass(data);
	}
	
	/**
	 * Elimina las instancias negativas del conjunto .
	 * @param data: conjunto de datos a preparar.
	 * @return Instances: conjunto con las instancias borradas.
	 * @throws Exception
	 */
	public Instances preparePositiveInstances(Instances data) throws Exception {
		data = this.classPosFiltering(data);
		data = this.removeClass(data);
		data = this.setUniqueClass(data);
		return this.setClass(data);
	}
	
	/**
	 * Prepara el conjunto de instancias añadiendo una clase ficticia.
	 * @param data: conjunto de instancias.
	 * @return Instances: conjunto de instancias con la clase ficticia añadida.
	 */
	public Instances prepareInstances(Instances data) throws Exception {
		data = this.setUniqueClass(data);
		return this.setClass(data);
	}
	
	/**
	 * Elimina del conjunto de instancias la clase ficticia.
	 * @param data: conjunto de instancias.
	 * @return Instances: conjunto de instancias con la clase ficticia eliminada.
	 * @throws Exception
	 */
	public Instances removeArtificialAttribute(Instances data) throws Exception {
		Remove remove = new Remove();
		remove.setOptions(weka.core.Utils.splitOptions("-R last"));
		remove.setInputFormat(data);
		data = Filter.useFilter(data, remove);
		return data;
	}
	
	/**
	 * Prepara el conjunto de test para poder usarlo.
	 * @param data: conjunto de instancias.
	 * @return Instances: conjunto de instancias con la clase eliminada y añadida clase artificial.
	 * @throws Exception
	 */
	public Instances prepareTestIntances(Instances data) throws Exception {
		data = this.removeClass(data);
		data = this.setUniqueClass(data);
		return this.setClass(data);
	}
	
	/**
	 * Elimina la clase de un conjunto de instancias.
	 * @param data: conjunto de instancias.
	 * @return Instances: conjunto de instancias con la clase eliminada.
	 * @throws Exception
	 */
	private Instances removeClass(Instances data) throws Exception {
		Remove remove = new Remove();
		remove.setOptions(weka.core.Utils.splitOptions("-R last"));
		remove.setInputFormat(data);
		return Filter.useFilter(data, remove);
	}
	
	/**
	 * Crea una clase ficticia en un conjunto de instancias.
	 * @param data: conjunto de instancias.
	 * @return Instances: conjunto de instancias con la clase ficticia añadida.
	 * @throws Exception
	 */
	private Instances setUniqueClass(Instances data) throws Exception {
		Add add = new Add();
		AddNoise addNoise = new AddNoise();
		
		add.setOptions(weka.core.Utils.splitOptions("-T NOM -N aux -L V1 -C last"));
		addNoise.setOptions(weka.core.Utils.splitOptions("-C last -M -P 100 -S 1"));
		
		add.setInputFormat(data);
		data = Filter.useFilter(data, add);
		
		addNoise.setInputFormat(data);
		return Filter.useFilter(data, addNoise);
	}
	
	/**
	 * Elimina la clase mayoritaria (instancias y cabecera) del fichero que se le haya introducido al instanciar la clase.
	 * @return Instances: conjunto de instancias con la clase minoritaria eliminada.
	 * @throws Exception
	 */
	private Instances classPosFiltering(Instances data) throws Exception {
		int numberAttributes = data.numAttributes(); // Número de atributos totales.
		AttributeStats classStats = data.attributeStats(numberAttributes - 1); // Cogemos los datos de la clase.
		int[] classValues = classStats.nominalCounts; // Array que nos guarda el número de instancias por clase.
		RemoveWithValues removeWithValues = new RemoveWithValues(); // Filtro a usar.
		
		if (classValues.length != 1) { // Si la clase ya es unaria, no hacemos nada.
			/* Configuramos el filtro. */
			if (classValues[0] < classValues[1]) // Clase mayoritaria la primera.
				removeWithValues.setOptions(weka.core.Utils.splitOptions("-S 0.0 -C " + numberAttributes + " -L 1 -H")); // Eliminamos todas las instancia con la clase mayoritaria y reescribimos la cabecera.
			else
				removeWithValues.setOptions(weka.core.Utils.splitOptions("-S 0.0 -C " + numberAttributes + " -L 0 -H"));
			
			/* Decimos al filtro cómo será el formato de entrada de los datos. */
			removeWithValues.setInputFormat(data);
			
			/* Aplicamos el filtro. */
			data = Filter.useFilter(data, removeWithValues);
		}
		
		return data;
	}
	
	/**
	 * Elimina la clase minoritaria (instancias y cabecera) del fichero que se le haya introducido al instanciar la clase.
	 * @return Instances: conjunto de instancias con la clase minoritaria eliminada.
	 * @throws Exception
	 */
	private Instances classNegFiltering(Instances data) throws Exception {
		int numberAttributes = data.numAttributes(); // Número de atributos totales.
		AttributeStats classStats = data.attributeStats(numberAttributes - 1); // Cogemos los datos de la clase.
		int[] classValues = classStats.nominalCounts; // Array que nos guarda el número de instancias por clase.
		RemoveWithValues removeWithValues = new RemoveWithValues(); // Filtro a usar.
		
		if (classValues.length != 1) { // Si la clase ya es unaria, no hacemos nada.
			/* Configuramos el filtro. */
			if (classValues[0] > classValues[1])
				removeWithValues.setOptions(weka.core.Utils.splitOptions("-S 0.0 -C " + (numberAttributes) + " -L 0 -H")); // Eliminamos todas las instancia con la clase minoritaria y reescribimos la cabecera.
			else
				removeWithValues.setOptions(weka.core.Utils.splitOptions("-S 0.0 -C " + (numberAttributes) + " -L 1 -H"));
			
			/* Decimos al filtro cómo será el formato de entrada de los datos. */
			removeWithValues.setInputFormat(data);
			
			/* Aplicamos el filtro. */
			data = Filter.useFilter(data, removeWithValues);
		}
		
		return data;
	}
	
	/**
	 * Establece la clase de un conjunto de instancias. En nuestro caso, el último atributo.
	 * @param data: conjunto de instancias.
	 * @return Instances: conjunto de instancias con la clase establecida.
	 */
	private Instances setClass(Instances data) {
		/* Establecemos la posición de la clase de los conjuntos de datos. */
		data.setClassIndex(data.numAttributes() - 1);
		return data;
	}
}
