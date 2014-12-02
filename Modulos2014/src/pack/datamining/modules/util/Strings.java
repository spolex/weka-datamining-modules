package pack.datamining.modules.util;
/**
 * 
 * @author spolex
 * 
 * Esta clase sirve para recoger todos los mensajes de la aplicación.El objetivo es facilitar la implementación 
 * multi-idioma 
 *
 */
public class Strings 
{
	public static final String MSG_FILE_NOTFOUND = "El archivo indicado no se encuentra en el sistema";
	public static final String MSG_IO_FAIL = "Error en el flujo de entrada/salida";
	public static final String MSG_ERROR_OUTLIERS = "Error desconocido al filtrar Outliers y Extreme Values";
	public static final String MSG_SCAN_PARAMS = "Scan Params";
	public static final String MSG_EJECUCION_INTERRUMPIDA = "El thread en background ha sido interrumpido";
	public static final String MSG_ERROR_EVALUACION = "Error al crear cargar los datos de entrenamiento en el evaluador";
	public static final String MSG_ERROR_EVALUACION_MODELO = "Error al crear cargar los datos de entrenamiento en el evaluador";
	public static final String MSG_V_CHANGE_FMEASURE = "Parámetros reajustados";
	public static final String MSG_V_NO_CHANGE_FMEASURE = "Los parámetros no mejoran la Fmeasure de la clase positiva";
	public static final String MSG_ERROR_ENTRENAR_MODELO = "Error al entrenar el modelo";
	public static final String MSG_ERROR_CREAR_ARCHIVO = "Error al crear el directorio";
	public static final String MSG_ERROR_NUMS_ARGS = "El número de argumentos de entrada es incorrecto";
	public static final String MSG_ERROR_CLASS_NOT_DEFINED = "No se ha definido el atributo clase";
	public static final String MSG_ERROR_ARGS = "Asegurese de haber introducido correctamente los argumentos: <train path> <dev path> <test path> ";
	public static final String MSG_SVM_OPTIMIZED = "Modelo SVM optimizado";
	public static final String MSG_DIRECTROIO_CREADO = "El directorio ha sido creado con éxito";
	public static final String MSG_INSTANCES_RANDOM = "Instancias barajadas...";
	public static final String MSG_ERROR_INSTANCES_RANDOM = "Error al barajar las instancias";
	public static final String MSG_ERROR_NUM_FORMATO = "Error en el formato, no es numérico";
	public static final String MSG_SCAN_MODEL = "Comienza el barrido de parámetros con el kernel: ";
	public static final String MSG_SCAN_COST = "Barriendo el parámetro cost........";
	public static final String MSG_SCAN_GAMMA = "Barriendo el parámetro gamma........";
	public static final String MSG_SCAN_DEGREE = "Barriendo el parámetro degree........";
	public static final String MSG_FIN_SCAN_COST = "Fin del barrido cost";
	public static final String MSG_FIN_SCAN_GAMMA = "Fin del barrido gamma";
	public static final String MSG_FIN_SCAN_DEGREE = "Fin del barrido degree";
	public static final String MSG_FIN_SVM_SCAN = "Barrido ah-hoc finalizado";
	public static final String LABEL_ARGUMENTO = "Argumento";
	public static final String LABEL_OBJETIVO = "El OBJETIVO:\nBarrido de parámetros para el clasificador SVM con "
			+ "el kernel indicado.\n"
			+ "Argumentos: <path_train.arff><path_dev.arff><costMAX><gammaMAX><degreeMAX>";
	public static final String MSG_ARITM = "Operación aritmética inválida";
	public static final String MSG_SERIALIZANDO = "Serializando el modelo....";
	public static final String MSG_CLF_OPT_CONF = "Configurando el modelo óptimo....";
	public static final String MSG_FIN = "Fin del algoritmo";
}

