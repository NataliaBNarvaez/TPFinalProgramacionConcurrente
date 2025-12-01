package TPOConcurrente;

import java.util.Random;

public class Snorkel {
    private int equiposDeSnorkel;
    private Random asistente;
    private Boolean sigueAbierto;

    public Snorkel(int cantEquipos) {
        this.equiposDeSnorkel = cantEquipos;
        this.asistente = new Random();
        this.sigueAbierto = true;
    }

    public synchronized boolean adquirirEquipoSnorkel() throws InterruptedException {
        while (equiposDeSnorkel <= 0 && sigueAbierto) {
            System.out.println("El visitante " + Thread.currentThread().getName()
                    + " esta esperando en el stand por un equipo completo.");
            wait();
        }
        if (sigueAbierto) {
            equiposDeSnorkel--;
            System.out.println("El asistente " + (asistente.nextInt(2) + 1) + " le entrego al visitante "
                    + Thread.currentThread().getName() + " el equipo completo de snorkel y esta listo para comenzar!");
        } else {
            System.out.println(Thread.currentThread().getName()
                    + " No puede hacer snorkel ya que cerraron las actividades del parque.");
        }
        return sigueAbierto;
    }

    public synchronized void devolverEquipoSnorkel() {
        equiposDeSnorkel++;
        System.out.println("El visitante " + Thread.currentThread().getName() + " ha devuelto el equipo de snorkel.");
        notifyAll();
    }

    public synchronized void cerrarSnorkel() {
        System.out.println(ColoresSout.BOLD + ColoresSout.BLUE + "HA CERRADO EL STAND DE SNORKEL" + ColoresSout.RESET);
        sigueAbierto = false;
        notifyAll();
    }
}
