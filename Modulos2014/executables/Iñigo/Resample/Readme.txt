==================================================================================================================
Asignatura: Miner�a de Datos
N�mero de Pr�ctica: 5.
Autores: Jose Ignacio S�nchez.
Fecha: 25 de noviembre de 2014.
==================================================================================================================
Recursos utilizados:
	- Linux Mint 16 con JDK 7 y eclipse Kepler.
	- Librer�a de Weka 3.7.11.

* Es posible que d� errores a la hora de ejecutar el ejecutable .jar en Windows 7.
==================================================================================================================
Objetivos:

El objetivo principal del sistema es balancear el n�mero de apariciones de las diferentes clases. Para ello aumenta 
el n�mero de apariciones de la clase minoritaria hasta lograr una distribuci�n uniforme.

==================================================================================================================
Argumentos:
	- Fichero .arff de entrenamiento.
	
	- Ejemplo: java -jar Resample-<versi�n>.jar ScatteringNeutrons_Train.arff
==================================================================================================================
Precondiciones:
	- La clase en los ficheros .arff debe encontrarse en �ltima posici�n.

Postcondiciones:
	- Se obtiene un fichero .arff NUEVO con las instancias balanceadas.
		- Se crear� un directorio con la fecha y la hora actual sin minutos, en el se guardar� el fichero con el 
		patr�n <nombre_fichero_de_entrenamiento>_<fecha-hora>_Resampled.arff
==================================================================================================================
Ejemplo de ejecuci�n en GNU/Linux:

Ejecuci�n del programa mediante la l�nea de comandos:

1. Nos situamos en el directorio donde est� situado el ejecutable, supondremos que est� en nuestro directorio personal,
dentro del directorio Filtros:

		$cd /home/usuario/Filtros

2. Le damos los permisos necesarios de ejecuci�n, en caso de que no los tenga ya, para asegurarnos
podemos introducir el siguiente comando:

		$ls -la

En caso de que no tenga los permisos suficientes para poder ejecutarlo se los asignamos:

		$sudo chmod 755 Resample-<versi�n>.jar

Nota: se necesita la contrase�a de super usuario para realizar esta tarea.

3. Volvemos al directorio Filtros e introducimos el comando para ejecutar Outliers-<versi�n>.jar:

		cd ..
		$java -jar -Xmx512m Resample-<versi�n>.jar "path_train" 

Donde le asignaremos suficiente memoria para poder realizar el c�mputo sin problemas con el par�metro -Xmx
de la m�quina virtual de java.

El par�metro path_train, que es obligatorio, es el path donde se encuentra el archivo .arff con los datos 
que ser�n preprocesador.

En la carpeta examples se encuentran algunos archivos .arff con los que realizar el experimento, siendo uno de
ellos ionosphere.arff, adem�s de los resultados de los experimentos ya realizados. Para �sto, una vez situados
en el directorio, como se ha descrito en los puntos anteriores, introducimos el siguiente comando:

		$java -jar -Xmx512m Resample-<versi�n>.jar examples/ionosphere.arff 

Los resultados con los que cotejar se encuentran en el mismo directorio.
==================================================================================================================
