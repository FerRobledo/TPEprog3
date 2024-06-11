package tpe;

import java.util.ArrayList;
import java.util.HashMap;

public class Backtracking {

    private ArrayList<Tarea> tareas;
    private ArrayList<Procesador> procesadores;
    private HashMap<String, ArrayList<Tarea>> mejorSolucion;
    private int metrica;
    private int sumaMejorSolucion;

    public Backtracking(ArrayList<Tarea> tareas, ArrayList<Procesador> procesadores) {
        this.tareas = tareas;
        this.procesadores = procesadores;
        this.mejorSolucion = null;
    }

    public HashMap<String, ArrayList<Tarea>> BackTracking(int tiempoX) {
        HashMap<String, ArrayList<Tarea>> solucionActual = new HashMap<>();
        ArrayList<Tarea> asignadas = new ArrayList<>();
        for (Procesador procesadores : this.procesadores) {
            solucionActual.put(procesadores.getId(), new ArrayList<Tarea>());
        }

        int indiceTareas = 0;
        this.metrica = 0;
        backtracking(solucionActual, asignadas, tiempoX, indiceTareas);
        System.out.println(resultadoBacktracking());
        return mejorSolucion;
    }

    public String resultadoBacktracking() {
        String resultado = "Backtracing \n Solucion obtenida: \n";
        if (this.mejorSolucion != null) {
            for (String procesador : this.mejorSolucion.keySet()) {
                resultado += procesador + ": [";
                for (Tarea tarea : this.mejorSolucion.get(procesador)) {
                    resultado += tarea.getName() + ", ";
                }
                resultado += "] \n";
            }
            resultado += "\n Tiempo maximo de ejecucion: "
                    + this.procesadorMasTarda(this.mejorSolucion);
            resultado += "\n Metrica: " + this.metrica;
        } else {
            resultado += "No hay solucion";
        }

        return resultado;

    }

    private void backtracking(HashMap<String, ArrayList<Tarea>> solucionActual,
            ArrayList<Tarea> asignadas, int tiempoX, int index) {

        if (asignadas.size() == this.tareas.size()) {
            elegirMejorSolucion(solucionActual);
        } else {
            // PODA EXTRA: CUANDO SE SUPERA EL TIEMPO DE LA MEJOR SOLUCION
            // OBTENIDA HASTA EL MOMENTO, SE DESCARTA LA POSIBLE SOLUCION
            // ACTUAL.
            if (comprobarSolucionParcial(solucionActual)) {

                int indexTarea = index;
                while (indexTarea < tareas.size()) {
                    Tarea tarea = this.tareas.get(indexTarea);

                    if (!asignadas.contains(tarea)) {

                        for (Procesador procesador : this.procesadores) {

                            // 1ER PODA: COMPRUEBA PRIMERO SI LA TAREA ES
                            // CRITICA,
                            // COMPRUEBA QUE EL PROCESADOR NO TENGA 2 TAREAS
                            // CRITICAS.
                            if (comprobarSiTareaCritica(procesador, tarea,
                                    solucionActual)) {

                                // 2DA PODA: COMPRUEBA PRIMERO SI EL PROCESADOR
                                // ESTA REFRIGERADO, SI ES ASI COMPRUEBA QUE NO
                                // SE SUPERE EL TIEMPO LIMITE INGRESADO POR EL
                                // USUARIO.
                                if (!procesador.isRefrigerado()
                                        && comprobarTiempoLimite(procesador,
                                                tarea, solucionActual, tiempoX)) {
                                    // Se puede agregar tarea
                                    solucionActual.get(procesador.getId()).add(
                                            tarea);
                                    asignadas.add(tarea);
                                    index += 1;

                                    metrica += 1;
                                    backtracking(solucionActual, asignadas,
                                            tiempoX, index);

                                    solucionActual.get(procesador.getId())
                                            .remove(tarea);
                                    asignadas.remove(tarea);
                                    index--;

                                } else if (procesador.isRefrigerado()) {
                                    // Se puede agregar Tarea
                                    solucionActual.get(procesador.getId()).add(
                                            tarea);
                                    asignadas.add(tarea);
                                    index += 1;

                                    metrica += 1;
                                    backtracking(solucionActual, asignadas,
                                            tiempoX, index);

                                    solucionActual.get(procesador.getId())
                                            .remove(tarea);
                                    asignadas.remove(tarea);
                                    index--;
                                }

                            }
                        }
                    }
                    indexTarea += 1;
                }
           }
        }
    }

    private boolean comprobarSolucionParcial(
            HashMap<String, ArrayList<Tarea>> solucionActual) {

        if (this.mejorSolucion == null) { // Si no hay mejor solucion entonces
            // puede seguir con la posible
            // solucion.
            return true;
        } else {
            int sumaActual = procesadorMasTarda(solucionActual);
            if (sumaActual < this.sumaMejorSolucion) { // Si la solucion actual es
                // menor a la mejor solucion
                // entonces puede seguir con
                // la posible solucion.
                return true;
            }
        }
        return false;
    }

    private boolean comprobarTiempoLimite(Procesador procesador, Tarea tarea,
            HashMap<String, ArrayList<Tarea>> solucionActual, int tiempoX) {
        int sumaParcial = 0;

        for (Tarea t : solucionActual.get(procesador.getId())) {
            sumaParcial += t.getTime();
        }

        if (sumaParcial + tarea.getTime() <= tiempoX) {
            return true;
        }

        return false;
    }

    private boolean comprobarSiTareaCritica(Procesador procesador, Tarea tarea,
            HashMap<String, ArrayList<Tarea>> solucionActual) {
        if (!tarea.isCritic()) {
            return true;
        } else {
            if (this.getCantidadCritica(procesador, solucionActual) < 2) {
                return true;
            }
            return false;
        }
    }

    private int getCantidadCritica(Procesador procesador,
            HashMap<String, ArrayList<Tarea>> solucionActual) {
        int tareasCriticas = 0;
        for (Tarea task : solucionActual.get(procesador.getId())) {
            if (task.isCritic()) {
                tareasCriticas++;
            }
        }
        return tareasCriticas;
    }

    private void elegirMejorSolucion(
            HashMap<String, ArrayList<Tarea>> solucionActual) {
        int sumaActual = procesadorMasTarda(solucionActual);
        if (sumaActual != 0) {
            if (this.mejorSolucion == null) {
                this.mejorSolucion = new HashMap<>();
                for (String key : solucionActual.keySet()) {
                    this.mejorSolucion.put(key,
                            new ArrayList<>(solucionActual.get(key)));
                }
                this.sumaMejorSolucion = sumaActual;
            } else {
                if (procesadorMasTarda(this.mejorSolucion) > procesadorMasTarda(solucionActual)) {
                    this.mejorSolucion = new HashMap<>();
                    for (String key : solucionActual.keySet()) {
                        this.mejorSolucion.put(key, new ArrayList<>(
                                solucionActual.get(key)));
                    }
                    this.sumaMejorSolucion = sumaActual;
                }
            }
        }
    }

    private int procesadorMasTarda(HashMap<String, ArrayList<Tarea>> solucion) {
        int sumaTotal = 0;
        for (String procesadores : solucion.keySet()) {
            int suma = 0;
            for (Tarea tareas : solucion.get(procesadores)) {
                suma += tareas.getTime();
            }
            if (sumaTotal < suma) {
                sumaTotal = suma;
            }
        }
        return sumaTotal;
    }

}