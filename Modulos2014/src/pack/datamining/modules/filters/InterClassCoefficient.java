package pack.datamining.modules.filters;

import java.math.BigInteger;
import java.util.HashMap;

import pack.datamining.modules.util.Stopwatch;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;

public class InterClassCoefficient {

	private double delta;
	private DistanceFunction distanceFunction;
	private Instances instances;
	private HashMap<BigInteger, Double> distances;
	private int instancesNumber;
	private double deltaFactor;
	
	public InterClassCoefficient(Instances pInstances,DistanceFunction pDistance,double pDeltaFactor)
	{
		//Almaceno la función de distancia.
		this.distanceFunction=pDistance;
		//Almaceno la lista de instancias.
		this.instances=pInstances;
		
		//Establezco el factor para el cálculo de delta.
		if(pDeltaFactor<2)
			this.deltaFactor=2;
		else
			this.deltaFactor=pDeltaFactor;

		//Almaceno el número de instancias.
		this.instancesNumber=pInstances.numInstances();
		//Inicializo la tabla hash
		this.distances=new HashMap<BigInteger, Double>(this.instancesNumber);

	}
	
	/**
	 * Función que inicializa el hashmap de distancias
	 */
	private void initializeDistances() {
		System.out.println("Inicialización de distancias");
		Stopwatch reloj = new Stopwatch();
		//Almacena el hashcode de la instancia en evaluación.
		BigInteger temporaryInstanceHash;
		//Almacena la instancia en evaluación.
		Instance temporaryInstance;
		//Almacena la clave propuesta para estar en el hashmap.
		BigInteger temporaryKey;
		
		//Para cada instancia
		for(int i=0;i<this.instancesNumber;i++)
		{
			//Almaceno los valores temporales para no tener que hacerlo n veces.
			temporaryInstance=this.instances.instance(i);
			temporaryInstanceHash=BigInteger.valueOf(temporaryInstance.hashCode());
			//Evaluo su distancia con respecto al resto que no hayan sido evaluadas.
			for(int j=i+1;j<this.instancesNumber;j++)
			{
				//Calculo la clave que debería tener la tabla hash.
				temporaryKey=cantorPairing(temporaryInstanceHash, BigInteger.valueOf(this.instances.instance(j).hashCode()));
				//Si no existe esa clave, calculo la distancia e introduzco la clave dentro del HashMap
				if(!this.distances.containsKey(temporaryKey))
					distances.put(temporaryKey, distanceFunction.distance(temporaryInstance, this.instances.instance(j)));
			}
		}
		System.out.println("Inicialización de distancias finalizada en: "+ reloj.elapsedTime() + " segundos.");
	}

	/**
	 * Elimina los outliers mediante el método propuesto.
	 * @return
	 * Retorna la lista de instancias sin los outliers
	 */
	public Instances removeOutliers()
	{
		//Inicializo un reloj para el cálculo de tiempos de ejecución.
		Stopwatch clock=new Stopwatch();
		//Hago el cálculo de las distancias.
		this.initializeDistances();
		//Almacena la mejora conseguida.
		double distanceImprovement=Double.MAX_VALUE;
		
		int proposedInstance=0;
		//Almacena la distancia base de partida
		double baseDistance;
		//Almacena la distancia temporal en evaluación.
		double temporaryDistance=0;
		
		//Establezco que la distancia base inicial es la distancia intracluster.
		//Mientras la mejora de la distancia intracluster expresada porcentualmente no sea mayor que Delta.
		//Recalculo la distancia intracluster.
		for(baseDistance=this.internalDistance(instances,-1);distanceImprovement>delta;baseDistance=this.internalDistance(instances,-1))
		{
			//Calculo Delta
			this.computeDelta();
			//Inicializo un reloj para el cálculo de los tiempos de cada iteración.
			Stopwatch loopClock=new Stopwatch();
			int skipIndex;
			//Para cada instancia
			//La mejora de distancia en un primer momento es cero.
			distanceImprovement=0;
			for(int i=0;i<this.instancesNumber;i++)
			{
				//Marco la instancia que no se debe de evaluar.
				skipIndex=i;
				
				//Calculo la distancia interna sin la instancia.
				temporaryDistance=this.internalDistance(instances,skipIndex);
				
				//Si la mejora de la distancia es mayor a la mejora anterior.
				if(baseDistance-temporaryDistance>distanceImprovement)
				{
					//Almaceno la nueva mejora de distancia.
					distanceImprovement=baseDistance-temporaryDistance;
					//Almaceno el índice de la variable propuesta para eliminación.
					proposedInstance=i;
					//System.out.println("Mejora de distancia:\t" + distanceImprovement + "\tCon distancia base:\t" + baseDistance + "\tInstancia número:\t"+i );
					//System.out.println("Mejora porcentual:  \t"+ ((distanceImprovement/baseDistance)) + "\tPorcentaje objetivo:\t" + this.delta );
				
				}
			}
			//Calculo la mejora porcentual que supone eliminar la instancia.
			distanceImprovement=(distanceImprovement/baseDistance);
			if(distanceImprovement>this.delta)
			{
				System.out.println("La iteración para eliminar una instancia ha tardado: "+loopClock.elapsedTime());
				System.out.println("Eliminada la instancia:\t" + proposedInstance + "\t de clase: \t "+ this.instances.instance(proposedInstance).classValue()+ "\tcon una mejora porcentual de:\t"+ distanceImprovement);
				//Elimino la instancia propuesta al finalizar la evaluación.
				this.removeInstance(proposedInstance);
			}
		}
		
		System.out.println("El proceso ha tardado:" + clock.elapsedTime());
		
		return instances;
	}
	
	private void computeDelta() {
		this.delta=(double)this.deltaFactor/this.instancesNumber;
		
	}

	/**
	 * Dado un índice de instancia, la elimina da la lista de instancias y actualiza el contador de número de instancias.
	 * @param pInstance
	 */
	private void removeInstance(int pInstance)
	{
		System.out.println(this.instancesNumber);
		this.instancesNumber-=1;
		this.instances.delete(pInstance);
	}
	
	@SuppressWarnings("unused")
	private void addInstance(Instance pInstance)
	{
		System.out.println(this.instancesNumber);
		this.instancesNumber+=1;
		this.instances.add(pInstance);
	}
	
	/**
	 * Dada una lista de instancias, calcula la distancia de cada una de ellas al resto.
	 * @param pInstances
	 * @param pSkipIndex 
	 * @return
	 * Retorna la distancia de cada instancia al resto.
	 */
	private double internalDistance(Instances pInstances, int pSkipIndex)
	{
		//Obtengo el hashcode de la instancia en evaluación.
		BigInteger temporaryHash;
		double distance=0;
		//Para cada instancia.
		for(int i=0;i<this.instancesNumber;i++)
		{
			if(i!=pSkipIndex)
			{
				temporaryHash=BigInteger.valueOf(pInstances.instance(i).hashCode());
				
				//Acumulo su distancia con el resto extrayendo la distancia de la tabla de distancias. (i+1, la distancia de una instancia a si misma es 0)
				for(int j=i+1;j<this.instancesNumber;j++)
					distance+=this.distances.get(cantorPairing(BigInteger.valueOf(this.instances.instance(j).hashCode()),temporaryHash));
			}
		}
		//Retorno la suma de las distancias acumuladas.
		return distance;
	}
	
	/**
	 * Dados dos enteros, devuelven un tercero combinación de los anteriores.
	 * @param pFirst
	 * @param pSecond
	 * @return
	 */
	private BigInteger cantorPairing(BigInteger pFirst,BigInteger pSecond)
	{
		BigInteger maxiumConverted;
		BigInteger miniumConverted;
		
		//Almaceno cual es el mayor de los dos.
		if(pFirst.subtract(pSecond).compareTo(BigInteger.valueOf(0))==1)
		{
			maxiumConverted=pFirst;
			miniumConverted=pSecond;
		}else
		{
			maxiumConverted=pSecond;
			miniumConverted=pFirst;
					
		}
		//Los convierto a números naturales.
		maxiumConverted=bijectionZtoN(maxiumConverted);
		miniumConverted=bijectionZtoN(miniumConverted);
		
		//Retorno el valor utilizando la función cantorPairing.
		BigInteger buffer=BigInteger.ZERO;
		
		buffer=maxiumConverted.add(miniumConverted);
		buffer=buffer.divide(BigInteger.valueOf(2));
		buffer=buffer.add(maxiumConverted.add(miniumConverted).add(BigInteger.valueOf(1)));
		buffer.add(miniumConverted);
		
		return buffer;
	}
	
	/**
	 * Función para convertir los números enteros en naturales.
	 * @param pFirst
	 * @return
	 */
	private BigInteger bijectionZtoN(BigInteger pFirst)
	{
		if(pFirst.signum()==-1) return pFirst.multiply(BigInteger.valueOf(-2)); else return pFirst.multiply(BigInteger.valueOf(2).subtract(BigInteger.valueOf(1)));
	}
	

	
}
