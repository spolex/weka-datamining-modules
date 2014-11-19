package pack.datamining.modules.filters;

import java.util.HashMap;

import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;

public class InterClassCoefficient {

	private double delta;
	private DistanceFunction distanceFunction;
	private Instances instances;
	private HashMap<Integer, Double> distances;
	private int instancesNumber;
	
	public InterClassCoefficient(Instances pInstances,DistanceFunction pDistance,double pDelta)
	{
		//Almaceno la función de distancia.
		this.distanceFunction=pDistance;
		//Almaceno la lista de instancias.
		this.instances=pInstances;
		
		//Si no se ha establecido una delta eligo una arbitraria.
		if(pDelta!=0)
			this.delta=pDelta;
		else
			this.delta=1/2*pInstances.numInstances();
		
		//Hago el cálculo de las distancias.
		this.inicializeDistances();
		//Almaceno el número de instancias.
		this.instancesNumber=pInstances.numInstances();
	}
	
	/**
	 * Función que inicializa el hashmap de distancias
	 */
	private void inicializeDistances() {

		//Almacena el hashcode de la instancia en evaluación.
		int temporaryInstanceHash;
		//Almacena la instancia en evaluación.
		Instance temporaryInstance;
		//Almacena la clave propuesta para estar en el hashmap.
		int temporaryKey;
		
		//Para cada instancia
		for(int i=0;i<this.instancesNumber;i++)
		{
			//Almaceno los valores temporales para no tener que hacerlo n veces.
			temporaryInstance=this.instances.instance(i);
			temporaryInstanceHash=temporaryInstance.hashCode();
			//Evaluo su distancia con respecto al resto que no hayan sido evaluadas.
			for(int j=i;j<this.instancesNumber;j++)
			{
				//Calculo la clave que debería tener la tabla hash.
				temporaryKey=cantorPairing(temporaryInstanceHash, this.instances.instance(j).hashCode());
				//Si no existe esa clave, calculo la distancia e introduzco la clave dentro del HashMap
				if(!this.distances.containsKey(temporaryKey))
					distances.put(temporaryKey, distanceFunction.distance(temporaryInstance, this.instances.instance(j)));
			}
		}	
	}

	/**
	 * Elimina los outliers mediante el método propuesto.
	 * @return
	 * Retorna la lista de instancias sin los outliers
	 */
	public Instances removeOutliers()
	{
		//Almacena la mejora conseguida.
		double distanceImprovement=Double.MAX_VALUE;
		//Almacena temporalmente la instancia que queda apartada del cálculo.
		Instance temporaryInstance;
		//Almacena la instancia propuesta para eliminación.
		int proposedInstance=0;
		//Almacena la distancia base de partida
		double baseDistance;
		//Almacena la distancia temporal en evaluación.
		double temporaryDistance=0;
		
		//Establezco que la distancia base inicial es la distancia intracluster.
		//Mientras la mejora de la distancia intracluster expresada porcentualmente no sea mayor que Delta.
		//Recalculo la distancia intracluster.
		for(baseDistance=this.internalDistance(instances);distanceImprovement>delta;baseDistance=this.internalDistance(instances))
		{
			//Para cada instancia
			for(int i=0;i<this.instancesNumber;i++)
			{
				//Almaceno la instancia en evaluación.
				temporaryInstance=instances.instance(i);
				//Elimino temporalmente la instancia en evaluación.
				this.instances.delete(i);
				//La mejora de distancia en un primer momento es cero.
				distanceImprovement=0;
				//Calculo la distancia interna sin la instancia.
				temporaryDistance=this.internalDistance(instances);
				
				//Si la mejora de la distancia es mayor a la mejora anterior.
				if(baseDistance-temporaryDistance>distanceImprovement)
				{
					//Almaceno la nueva mejora de distancia.
					distanceImprovement=baseDistance-temporaryDistance;
					//Almaceno el índice de la variable propuesta para eliminación.
					proposedInstance=i;
				}
				//Vuelvo a introducir la variable en evaluación al vector de instancias.
				instances.add(temporaryInstance);
			}
			//Calculo la mejora porcentual que supone eliminar la instancia.
			distanceImprovement=1-(distanceImprovement/baseDistance);
			//Elimino la instancia propuesta al finalizar la evaluación.
			this.removeInstance(proposedInstance);
		}
		
		return instances;
	}
	
	/**
	 * Dado un índice de instancia, la elimina da la lista de instancias y actualiza el contador de número de instancias.
	 * @param pInstance
	 */
	private void removeInstance(int pInstance)
	{
		this.instancesNumber-=1;
		this.instances.delete(pInstance);
	}
	/**
	 * Dada una lista de instancias, calcula la distancia de cada una de ellas al resto.
	 * @param pInstances
	 * @return
	 * Retorna la distancia de cada instancia al resto.
	 */
	private double internalDistance(Instances pInstances)
	{
		//Obtengo el hashcode de la instancia en evaluación.
		int temporaryHash;
		double distance=0;
		//Para cada instancia.
		for(int i=0;i<this.instancesNumber;i++)
		{
			temporaryHash=pInstances.instance(i).hashCode();
			
			//Acumulo su distancia con el resto extrayendo la distancia de la tabla de distancias
			for(int j=0;j<this.instancesNumber;j++)
				distance+=this.distances.get(cantorPairing(this.instances.instance(j).hashCode(),temporaryHash));
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
	private int cantorPairing(int pFirst,int pSecond)
	{
		int maxiumConverted;
		int miniumConverted;
		
		//Almaceno cual es el mayor de los dos.
		if(pFirst>pSecond)
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
		return ((1/2)*maxiumConverted*(maxiumConverted+1))+miniumConverted;
	}
	
	/**
	 * Función para convertir los números enteros en naturales.
	 * @param pFirst
	 * @return
	 */
	private int bijectionZtoN(int pFirst)
	{
		if(pFirst<=0) return -2*pFirst; else return 2*pFirst-1;
	}
	

	
}
