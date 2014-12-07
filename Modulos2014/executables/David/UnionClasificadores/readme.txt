************************************README MDP5_union_PV.jar************************************

Objetivo:
	Obtener las predicciones de un test uniendo las predicciones de varios clasificadores. Se implementa mediante la unión de clasificadores en paralelo por votación.
	

Ejecución:

	Comando: java -jar MDP5_union_PV.jar Fichero_Parametros [-h]
	
	
	Argumentos:
		Fichero_Parametros
			Fichero de texto que contiene los parámetros que cargará el programa.
			El formato es: 	-En cada línea se representa información correspondiente a un clasificador
							-Por cada clasificador se especifica la ruta del fichero de predicciones correspondiente y su ponderación al realizar la unión (separadas por ;)
							
							Ejemplo:
								ruta/predmod1.txt;0.2
								ruta/predmod2.txt;0.5
								...
		
		[-h]
			imprime la ayuda para la ejecución del programa. Si este parámetro se especifica, finaliza el programa tras mostrar la ayuda, independientemente del resto de parámetros especificados
		

Precondiciones:

	Disponer de la máquina virtual JAVA v7
	
	El fichero de parámetros debe estar especificado de forma correcta
	
	Los ficheros de predicciones deben cumplir el siguiente formato, donde las probabilidades de pertenencia a cada clase se especifican para cada instancia en una línea:
			Fichero_Predicciones (separador = ;):
				Clase1;Clase2;ClaseN
				p1;p2;pn
				...
			
			En todos los ficheros a usar, una instancia concreta ocupa la misma línea.

Postcondiciones:

	Se generará un fichero con extensión "unionPV-pred-prob.txt" que contendrá una fila por cada instancia de test en la que se especifica el valor de la probabilidad de pertenencia a cada una de las clases posibles.
	
	Se generará un fichero "prediccion-union.txt" que contendrá por cada fila la clase predicha para una instancia.
	