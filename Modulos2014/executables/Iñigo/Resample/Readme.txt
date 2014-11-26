==================================================================================================================
Asignatura: Minería de Datos
Número de Práctica: 5.
Autores: Jose Ignacio Sánchez.
Fecha: 25 de noviembre de 2014.
==================================================================================================================
Recursos utilizados:
	- Linux Mint 16 con JDK 7 y eclipse Kepler.
	- Librería de Weka 3.7.11.

* Es posible que dé errores a la hora de ejecutar el ejecutable .jar en Windows 7.
==================================================================================================================
Objetivos:

El objetivo principal del sistema es balancear el número de apariciones de las diferentes clases. Para ello aumenta 
el número de apariciones de la clase minoritaria hasta lograr una distribución uniforme.

==================================================================================================================
Argumentos:
	- Fichero .arff de entrenamiento.
	
	- Ejemplo: java -jar Resample-<versión>.jar ScatteringNeutrons_Train.arff
==================================================================================================================
Precondiciones:
	- La clase en los ficheros .arff debe encontrarse en última posición.

Postcondiciones:
	- Se obtiene un fichero .arff NUEVO con las instancias balanceadas.
		- Se creará un directorio con la fecha y la hora actual sin minutos, en el se guardará el fichero con el 
		patrón <nombre_fichero_de_entrenamiento>_<fecha-hora>_Resampled.arff
==================================================================================================================
Ejemplo de ejecución en GNU/Linux:

Ejecución del programa mediante la línea de comandos:

1. Nos situamos en el directorio donde está situado el ejecutable, supondremos que está en nuestro directorio personal,
dentro del directorio Filtros:

		$cd /home/usuario/Filtros

2. Le damos los permisos necesarios de ejecución, en caso de que no los tenga ya, para asegurarnos
podemos introducir el siguiente comando:

		$ls -la

En caso de que no tenga los permisos suficientes para poder ejecutarlo se los asignamos:

		$sudo chmod 755 Resample-<versión>.jar

Nota: se necesita la contraseña de super usuario para realizar esta tarea.

3. Volvemos al directorio Filtros e introducimos el comando para ejecutar Outliers-<versión>.jar:

		cd ..
		$java -jar -Xmx512m Resample-<versión>.jar "path_train" 

Donde le asignaremos suficiente memoria para poder realizar el cómputo sin problemas con el parámetro -Xmx
de la máquina virtual de java.

El parámetro path_train, que es obligatorio, es el path donde se encuentra el archivo .arff con los datos 
que serán preprocesador.

En la carpeta examples se encuentran algunos archivos .arff con los que realizar el experimento, siendo uno de
ellos ionosphere.arff, además de los resultados de los experimentos ya realizados. Para ésto, una vez situados
en el directorio, como se ha descrito en los puntos anteriores, introducimos el siguiente comando:

		$java -jar -Xmx512m Resample-<versión>.jar examples/ionosphere.arff 

Los resultados con los que cotejar se encuentran en el mismo directorio.
==================================================================================================================
