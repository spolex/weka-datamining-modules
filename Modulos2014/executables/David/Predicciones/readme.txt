************************************README MDP5_predicciones_prob.jar************************************

Objetivo:
	Obtener las predicciones de un conjunto de test obteniendo por cada instancia la probabilidad de pertenencia a cada clase posible y la clase que estima para la misma el clasificador proporcionado.
	

Ejecución:

	Comando: java -jar MDP5_predicciones_prob.jar Fichero_Test Fichero_Modelo [-h]
	
	
	Argumentos:
		Fichero_Test
			Fichero arff conteniendo las instancias a predecir. El atributo clase es el último de los especificados.
		
		Fichero_Modelo
			Fichero .model que contiene un modelo ya entrenado.
		
		[-h]
			imprime la ayuda para la ejecución del programa. Si este parámetro se especifica, finaliza el programa tras mostrar la ayuda, independientemente del resto de parámetros especificados
		

Precondiciones:

	Disponer de la máquina virtual JAVA v7
	
	El fichero de test debe estar en formato ARFF, la clase debe ser el último de los atributos y debe estar preparado para ser usado por el modelo proporcionado.
	
	El modelo debe estar previamente entrenado.

Postcondiciones:

	Se generará un fichero con extensión "nombretest-pred-prob.txt" que contendrá una fila por cada instancia de test en la que se especifica el valor de la probabilidad de pertenencia a cada una de las clases posibles.
	Se generará un fichero con extensión "nombretest-predicciones.txt" que contendrá la clase estimada por el clasificador para cada instancia del conjunto de test.
	