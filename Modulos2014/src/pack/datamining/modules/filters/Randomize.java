package pack.datamining.modules.filters;

import java.util.Random;

import weka.core.Instances;

public class Randomize
{
	/**
	 * 
	 * @param data
	 * @param seed
	 * @return desordenadas las instancias recibidas por parámetro con la semilla indicada.
	 * @throws Exception
	 */
	public static Instances randomize (Instances data, int seed) throws Exception
	{
		 Random rand = new Random(seed);   // create seeded number generator
		 Instances randData = new Instances(data);   // create copy of original data
		 randData.randomize(rand);		 
		 return randData;
	}
}
