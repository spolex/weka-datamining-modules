package pack.datamining.modules.filters;

import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;

public class InterClassCoefficient {

	private double delta;
	private DistanceFunction distanceFunction;
	private Instances instances;
	
	private double[][] distancias;//las distancias que hay entre las instancias
	
	public InterClassCoefficient(Instances pInstances,DistanceFunction pDistance,double pDelta)
	{
		this.distanceFunction=pDistance;
		this.instances=pInstances;
		
		if(pDelta!=0)
			this.delta=pDelta;
		else
			this.delta=1/2*pInstances.numInstances();
		
		distancias = new double[pInstances.numInstances][pInstances.numInstances];
		//inicializo todas las posiciones a -1
		for(int i = 0;i<distancias.length;i++)
		{
			for(int j = 0;j<distancias[i].length;j++) distancias[i][j]=-1;
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
			for(int i=0;i<instances.numInstances();i++)
			{
				//Almaceno la instancia en evaluación.
				temporaryInstance=instances.instance(i);
				//Elimino temporalmente la instancia en evaluación.
				instances.delete(i);
				//La mejora de distancia en un primer momento es cero.
				distanceImprovement=0;
				//Calculo la distancia intracluster sin la instancia.
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
			instances.delete(proposedInstance);
		}
		
		return instances;
	}
	
	/**
	 * Dada una lista de instancias, calcula la distancia de cada una de ellas al resto.
	 * @param pInstances
	 * @return
	 * Retorna la distancia de cada instancia al resto.
	 */
	private double internalDistance(Instances pInstances)
	{
		//Obtengo el número de instancias en evaluación.
		int numInstances=pInstances.numInstances();
		
		double distance=0;
		//Para cada instancia.
		for(int i=0;i<numInstances;i++)
		{
			//Acumulo su distancia con el resto.
			for(int j=0;j<numInstances;j++)
			{
				//si la distancia era conocida no la vuelvo a calcular, además la matriz es simétrica
				if(this.distancias[i][j] != -1)
				{
					distance+=this.distancias[i][j];
				}
				else if(this.distancias[j][i] != -1)
				{
					distance+=this.distancias[j][i];
				}
				else
				{
					//No conozco la distancia, la calculo y almaceno
					double distancia = this.distanceFunction.distance(pInstances.instance(i),pInstances.instance(j))
					distance+=distancia;
					this.distancias[i][j] = distancia;
				}
			}
		}
		//Retorno la suma de las distancias acumuladas.
		return distance;
	}
	

	
}
