 ==================================================================================================================
Asignatura: Mineria de datos.
Número de Práctica: 5.
Autores: Jose Ignacio Sánchez.
Fecha: 1 de diciembre.
==================================================================================================================
Recursos utilizados:
	- Linux Mint 16, 17 con JDK 7 y eclipse Kepler.
	- Librería de Weka 3.7.11.
	- Librería de LibSVM 3.17.

* Es posible que dé errores a la hora de ejecutar el ejecutable .jar en Windows 7.
==================================================================================================================
Objetivos:

El objetivo principal del sistema es optimizar los parámetros de un modelo basado en las máquinas de soporte
vectorial (SVM), utilizando un barrido de parámetros ad-hoc observando como figura de mérito comparativa la
F-Measure de la clase positiva, esta debe estar colocada en primera posición.

Se obtiene el clasificador ajustado con los hiperparámetros que maximizan la F-measure de la clase considerada
positiva. Los parámetros que se optimizan son cost y gamma para ub kernel rnf.

==================================================================================================================
Argumentos:
	- Fichero .arff de entrenamiento.
	- Fichero .arff dev.
	- (Opcional) cotas superiores del barrido para cost y gamma. Aedmás se puede incluir degree>=2
		-Si degree == 2 entonces kernel RBF
		-Si degree >  2 entonces kernel polinomial. Barrido desde 2 hasta degree.
	- Ejemplo: java -jar ScanSvmParams<version>.jar ScatteringNeutrons_Train.arff ScatteringNeutrons_dev.arff 11 4 2
==================================================================================================================
Precondiciones:
	- La clase en los ficheros .arff debe encontrarse en última posición.
	- En caso de decidir introducir los parámetros serán ambos, en caso 
	contrario unicamente se acotará cost.
	-En caso de introducir degree que será tras cost y gamma, degree>=2.
	-Default:
				-cost=		11
				-gamma=		4
				-degree=	2

Postcondiciones:
	- Se obtiene un fichero .model con los mejores resultados del modelo
	  obtenidos en el barrido ad-hoc, guardándose en la misma ruta:
		 <directorio_donde_se_encuentra_jar>/Modelos/LibSVM/<kernel>/<yyyyMMdd>/HH<nombre>.model
==================================================================================================================
Ejemplo de ejecución en GNU/Linux:

Ejecución del programa mediante la línea de comandos:

1. Nos situamos en el directorio donde está situado el ejecutable en el, ScanSvmRbfParams<version>.jar, 
supondremos que está en nuestro directorio personal:

		$cd /home/usuario

2. Le damos los permisos necesarios de ejecución, en caso de que no los tenga ya, para asegurarnos
podemos introducir el siguiente comando:

		$ls -la

En caso de que no tenga los permisos suficientes para poder ejecutarlo se los asignamos:

		$sudo chmod 755 ScanSvmParams<version>.jar

Nota: se necesita la contraseña de super usuario para realizar esta tarea.

3. Volvemos al directorio home de nuestro usuario e introducimos el comando para ejecutar ScanSvmRbfParams<version>.jar:

		cd ..
		$java -jar -Xmx2048m ScanSvmParams<version>.jar "path_train" "path_dev" <argumentos>

Donde le asignaremos suficiente memoria para poder realizar el cómputo sin problemas con el parámetro -Xmx
de la máquina virtual de java.

El parámetro <argumentos> es óptativo. El parámetro path_train y path_dev, que son obligatorios, es el path
donde se encuentran los archivos .arff con los datos que se utilizará para el experimento.

En la carpeta examples se encuentran varios archivos .arff con los que realizar el experimento, siendo uno de
ellos train.arff, además de los resultados de los experimentos ya realizados. Para ésto, una vez situados
en el directorio bin, como se ha descrito en los puntos anteriores, introducimos el siguiente comando:

		$java -jar -Xmx2048m ScanParams<version>.jar data/train.arff data/dev.arff -14 -2 5

Los resultados con los que cotejar se encuentran en el mismo directorio.
==================================================================================================================
