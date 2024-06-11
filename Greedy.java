package tpe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;

public class Greedy {

    private ArrayList<Tarea> tareas;
    private ArrayList<Procesador> procesadores;
    private HashMap<String, ArrayList<Tarea>> solucion;
    private int candidatos;

    public Greedy(ArrayList<Tarea> tareas, ArrayList<Procesador> procesadores) {
        this.tareas = tareas;
        this.procesadores = procesadores;
        this.candidatos = 0;
    }

    /*
        *Complejidad temporal O(Log n + n*p*m) donde n es el numero total de tareas,
        *p es el numero total de procesadores, y m es el numero de tareas asignadas al procesador.
    */
    public HashMap<String, ArrayList<Tarea>> asignarTareasGreedy(int tiempoX) {
        ArrayList<Tarea> tareasOrdenadas = this.ordenarTareas();
        this.solucion = crearHashMap();

        for (Tarea tarea : tareasOrdenadas) {
            Procesador mejorProcesador = null;
            int tiempoMejorProcesador = Integer.MAX_VALUE;
            for (Procesador procesador : procesadores) {
                if (cumpleCondiciones(tiempoX, tarea, procesador)) {
                    candidatos++;
                    int tiempoProc = sumarTiemposProcesador(procesador);
                    if (tiempoProc < tiempoMejorProcesador) {
                        mejorProcesador = procesador;
                        tiempoMejorProcesador = tiempoProc;
                    }
                }
            }
            if (mejorProcesador != null) {
                solucion.get(mejorProcesador.getId()).add(tarea);
            }
        }
        
        System.out.println(resultadoGreedy());
        return solucion;
    }

    /*
        *Complejidad temporal O(m) con m numero de tareas asignadas al procesador.
    */
    private boolean cumpleCondiciones(int tiempoX, Tarea tarea, Procesador procesador) {
        int tiempoProc = sumarTiemposProcesador(procesador) + tarea.getTime();
        if (procesador.isRefrigerado()) {
            // Si el procesador está refrigerado, no hay restricciones adicionales.
            if (tarea.isCritic()) {
                // No puede tener más de 2 tareas críticas.
                return cantidadCriticas(procesador) < 2;
            } else {
                return true;
            }
        } else {
            // Si el procesador no está refrigerado, verificar que el tiempo total no exceda tiempoX.
            if (tiempoProc <= tiempoX) {
                if (tarea.isCritic()) {
                    // No puede tener más de 2 tareas críticas.
                    return cantidadCriticas(procesador) < 2;
                } else {
                    return true;
                }
            }
        }
        return false;
    }
    
    /*
        *Complejidad temporal O(n) con n cantidad de tareas.
    */
    private int cantidadCriticas(Procesador procesador) {
        int suma = 0;
        for (Tarea tarea : solucion.get(procesador.getId())) {
            if (tarea.isCritic()) {
                suma++;
            }
        }
        return suma;
    }

    /*
        *Complejidad temporal de O(n Log n) para n tareas.
    */
    private ArrayList<Tarea> ordenarTareas() {
        Collections.sort(tareas, new Comparator<Tarea>() {
            @Override
            public int compare(Tarea t1, Tarea t2) {
                return Integer.compare(t2.getTime(), t1.getTime());
            }
        });
        return tareas;
    }

    private int sumarTiemposProcesador(Procesador procesador) {
        ArrayList<Tarea> tareasDelProcesador = solucion.get(procesador.getId());
        int suma = 0;
        for (Tarea tarea : tareasDelProcesador) {
            suma += tarea.getTime();
        }
        return suma;
    }

    private HashMap<String, ArrayList<Tarea>> crearHashMap() {
        HashMap<String, ArrayList<Tarea>> hash = new HashMap<>();
        for (Procesador procesador : this.procesadores) {
            hash.put(procesador.getId(), new ArrayList<Tarea>());
        }
        return hash;
    }

    public String resultadoGreedy() {
        String resultado = "Greedy \n Solución obtenida: \n";
        if (this.solucion != null) {
            for (String procesador : this.solucion.keySet()) {
                resultado += procesador + ": [";
                for (Tarea tarea : this.solucion.get(procesador)) {
                	resultado += tarea.getName() + ", ";
                }
                resultado += "] \n";
            }
            resultado += "\n Tiempo maximo de ejecucion: "
                    + this.procesadorMasTarda(this.solucion);
            resultado += "\n Candidatos: " + this.candidatos;
        } else {
            resultado += "No hay solucion";
        }
        return resultado;
    }

    private int procesadorMasTarda(HashMap<String, ArrayList<Tarea>> solucion) {
        int sumaTotal = 0;
        for (String procesadores : solucion.keySet()) {
            int suma = 0;
            for (Tarea tarea : solucion.get(procesadores)) {
                suma += tarea.getTime();
            }
            if (sumaTotal < suma) {
                sumaTotal = suma;
            }
        }
        return sumaTotal;
    }
}