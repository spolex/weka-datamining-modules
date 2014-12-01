==================================================================================================================
Asignatura: Mineria de datos.
N�mero de Pr�ctica: 5.
Autores: Jose Ignacio S�nchez.
Fecha: 26 de noviembre.
==================================================================================================================
Recursos utilizados:
	- Linux Mint 16, 17 con JDK 7 y eclipse Kepler.
	- Librer�a de Weka 3.7.11.
	- Librer�a de LibSVM 3.17.

* Es posible que d� errores a la hora de ejecutar el ejecutable .jar en Windows 7.
==================================================================================================================
Objetivos:

El objetivo principal del sistema es optimizar los par�metros de un modelo basado en las m�quinas de soporte
vectorial (SVM), utilizando un barrido de par�metros ad-hoc observando como figura de m�rito comparativa la
F-Measure de la clase positiva, esta debe estar colocada en primera posici�n.

Se obtiene el clasificador ajustado con los hiperpar�metros que maximizan la F-measure de la clase considerada
positiva. Los par�metros que se optimizan son cost y gamma para ub kernel rnf.

==================================================================================================================
Argumentos:
	- Fichero .arff de entrenamiento.
	- Fichero .arff dev.
	- (Opcional) cotas superiores del barrido para cost y gamma
	- Ejemplo: java -jar ScanSvmRbfParams<version>.jar ScatteringNeutrons_Train.arff ScatteringNeutrons_dev.arff 11 4
==================================================================================================================
Precondiciones:
	- La clase en los ficheros .arff debe encontrarse en �ltima posici�n.
	- En caso de decidir introducir los par�metros ser�n ambos, en caso 
	contrario unicamente se acotar� cost.

Postcondiciones:
	- Se obtiene un fichero .model con los mejores resultados del modelo
	  obtenidos en el barrido ad-hoc, guard�ndose en la misma ruta <directorio_donde_se_encuentra_jar>/Modelos/<nombre>.model
==================================================================================================================
Ejemplo de ejecuci�n en GNU/Linux:

Ejecuci�n del programa mediante la l�nea de comandos:

1. Nos situamos en el directorio donde est� situado el ejecutable en el, ScanSvmRbfParams<version>.jar, 
supondremos que est� en nuestro directorio personal:

		$cd /home/usuario

2. Le damos los permisos necesarios de ejecuci�n, en caso de que no los tenga ya, para asegurarnos
podemos introducir el siguiente comando:

		$ls -la

En caso de que no tenga los permisos suficientes para poder ejecutarlo se los asignamos:

		$sudo chmod 755 ScanSvmRbfParams<version>.jar

Nota: se necesita la contrase�a de super usuario para realizar esta tarea.

3. Volvemos al directorio home de nuestro usuario e introducimos el comando para ejecutar ScanSvmRbfParams<version>.jar:

		cd ..
		$java -jar -Xmx2048m ScanSvmRbfParams<version>.jar "path_train" "path_dev" <argumentos>

Donde le asignaremos suficiente memoria para poder realizar el c�mputo sin problemas con el par�metro -Xmx
de la m�quina virtual de java.

El par�metro <argumentos> es �ptativo. El par�metro path_train y path_dev, que son obligatorios, es el path
donde se encuentran los archivos .arff con los datos que se utilizar� para el experimento.

En la carpeta examples se encuentran varios archivos .arff con los que realizar el experimento, siendo uno de
ellos train.arff, adem�s de los resultados de los experimentos ya realizados. Para �sto, una vez situados
en el directorio bin, como se ha descrito en los puntos anteriores, introducimos el siguiente comando:

		$java -jar -Xmx2048m ScanSvmRbfParams<version>.jar data/train.arff data/dev.arff -14 -2

Los resultados con los que cotejar se encuentran en el mismo directorio.
==================================================================================================================
