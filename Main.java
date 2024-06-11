package tpe;

public class Main {

	public static void main(String args[]) {
		Servicios servicios = new Servicios("./src/datasets/Procesadores.csv", "./src/datasets/Tareas.csv");
		
		servicios.Backtracking(120);
		servicios.Greedy(120);
	}
}