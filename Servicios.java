package tpe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import tpe.utils.CSVReader;

/**
 * NO modificar la interfaz de esta clase ni sus métodos públicos. Sólo se
 * podrá adaptar el nombre de la clase "Tarea" según sus decisiones de
 * implementación.
 */
public class Servicios {

    private HashMap<String, Tarea> tareasCriticas;
    private HashMap<String, Tarea> tareasNoCriticas;
    private ArrayList<Procesador> procesadores;

    /*
	 * La complejidad temporal del constructor es O(n+m) siendo n la cantidad de
	 * tareas y m la cantidad de procesadores.
     */
    public Servicios(String pathProcesadores, String pathTareas) {
        this.tareasCriticas = new HashMap<>();
        this.tareasNoCriticas = new HashMap<>();
        this.procesadores = new ArrayList<>();
        recibirTareas(pathTareas);
        this.procesadores = recibirProcesadores(pathProcesadores);
    }

    private void recibirTareas(String tareas) {
        CSVReader reader = new CSVReader();
        ArrayList<Tarea> arrTareas = reader.readTasks(tareas);
        for (Tarea tarea : arrTareas) {
            if (tarea.isCritic()) {
                tareasCriticas.put(tarea.getId(), tarea);
            } else {
                tareasNoCriticas.put(tarea.getId(), tarea);
            }
        }
    }

    private ArrayList<Procesador> recibirProcesadores(String procesadores) {
        CSVReader reader = new CSVReader();
        ArrayList<Procesador> arrProcesadores = reader.readProcessors(procesadores);
        return arrProcesadores;
    }

    /*
	 * La complejidad temporal de devolver una tarea segun su identificador es
	 * O(1).
     */
    public Tarea servicio1(String ID) {
        if (tareasCriticas.containsKey(ID)) {
            return tareasCriticas.get(ID);
        } else if (tareasNoCriticas.containsKey(ID)) {
            return tareasNoCriticas.get(ID);
        }

        return null;
    }

    /*
	 * La complejidad temporal de este servicio es O(n) siendo n la cantidad de
	 * tareas criticas o no criticas, dependiendo de lo que pide el usuario.
     */
    public List<Tarea> servicio2(boolean esCritica) {
        ArrayList<Tarea> tareas = new ArrayList<Tarea>();
        if (esCritica) {
            for (String tarea : tareasCriticas.keySet()) {
                tareas.add(tareasCriticas.get(tarea));
            }
        } else {
            for (String tarea : tareasNoCriticas.keySet()) {
                tareas.add(tareasNoCriticas.get(tarea));
            }
        }

        return tareas;
    }

    /*
	 * La complejidad temoral de este servicio es O(n) siendo N la cantidad
	 * total de tareas, decidimos hacerlo asi ya que nos parecio que era un
	 * servicio muy especifico, poco utilizado en un proyecto real. por eso no
	 * fue prioridad a la hora de elegir una estructura que se adapte mejor.
     */
    public ArrayList<Tarea> servicio3(int prioridadInferior,
            int prioridadSuperior) {
        ArrayList<Tarea> tareas = new ArrayList<>();
        for (String tarea : tareasCriticas.keySet()) {
            if (tareasCriticas.get(tarea).getPriority() >= prioridadInferior
                    && tareasCriticas.get(tarea).getPriority() <= prioridadSuperior) {
                tareas.add(tareasCriticas.get(tarea));
            }
        }
        for (String tarea : tareasNoCriticas.keySet()) {
            if (tareasNoCriticas.get(tarea).getPriority() >= prioridadInferior
                    && tareasNoCriticas.get(tarea).getPriority() <= prioridadSuperior) {
                tareas.add(tareasNoCriticas.get(tarea));
            }
        }

        return tareas;
    }

    
    /*
    *La complejidad temporal del algoritmo Backtracking que analizamos es O(n^b*m!), donde:
    *
	*	n es la cantidad de tareas a asignar.
	*	b es la cantidad m�xima de tareas cr�ticas que un procesador puede tener asignadas.
	*	m es la cantidad de procesadores disponibles.
    *
    *La estrategia que elegimos en este caso, fue agregar una poda extra (ademas de las mencionadas en la consigna)
    *con el objetivo de reducir la cantidad de llamados a la funcion recursiva backtracking().
    *
    *Esta poda extra consiste en chequear la solucion parcial al problema antes de seguir agregando estados a la solucion,
    *si la solucion parcial ya supera en tiempo a la mejor solucion obtenida hasta el momento, no tiene sentido seguir
    *agregando estados a esa posible solucion final.
    *
    */
    public String Backtracking(int tiempoX) {
        ArrayList<Tarea> tareas = combinarTareas();
        Backtracking algoritmoBacktracking = new Backtracking(tareas, this.procesadores);
        algoritmoBacktracking.BackTracking(tiempoX);
        String solucion = algoritmoBacktracking.resultadoBacktracking();

        return solucion;
    }

    /*
    *La complejidad temporal del algoritmo Greedy en el peor de los casos es 
    *O(Log n + n*p*m) donde n es el numero de tareas, p el numero de procesadores
    *y m el numero de tareas asignadas al procesador.

    *La estrategia para el algoritmo geedy que usamos es ordenar las tareas de forma descendente, con 
    *las que mas tardan primero y se las asignamos al procesador que menos tiempo max tenga�en�ese�momento,
    *respetando en cada tarea las restricciones que representan, siendo estas, 
    *en el caso de las criticas que no se asignen mas de 2 por cada procesador y en el caso de los procesadores
    *no refrigerados que la suma del tiempo total no supere el tiempoX cargado por el usuario.
    */
    public String Greedy(int tiempoX) {
        ArrayList<Tarea> tareas = combinarTareas();
        Greedy algoritmoGreedy = new Greedy(tareas, this.procesadores);
        algoritmoGreedy.asignarTareasGreedy(tiempoX);
        String solucion = algoritmoGreedy.resultadoGreedy();

        return solucion;
    }

    private ArrayList<Tarea> combinarTareas() {
        ArrayList<Tarea> tareasCombinadas = new ArrayList<>();

        for (Map.Entry<String, Tarea> entry : tareasCriticas.entrySet()) {
            tareasCombinadas.add(entry.getValue());
        }

        for (Map.Entry<String, Tarea> entry : tareasNoCriticas.entrySet()) {
            tareasCombinadas.add(entry.getValue());
        }

        return tareasCombinadas;
    }
}