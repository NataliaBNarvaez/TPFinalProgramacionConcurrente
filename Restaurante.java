package TPOConcurrente;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.Semaphore;

public class Restaurante {
    private int nroRestaurante, capacidad;
    private BlockingQueue<String> restaurante;
    private Boolean sigueAbierto, aux;
    // private Semaphore comer;

    public Restaurante(int nro, int cap) {
        this.nroRestaurante = nro;
        this.capacidad = cap;
        this.restaurante = new ArrayBlockingQueue(capacidad);
        this.sigueAbierto = true;
        // this.comer = new Semaphore(capacidad);
    }

    // Metodos para los visitantes
    public boolean entrarAComer(String consumo) throws InterruptedException {
        System.out.println(
                Thread.currentThread().getName() + " esta esperando para entrar al restaurante " + nroRestaurante);
        restaurante.put("entro");
        if (aux = puedeEntrar()) {
            // comer.acquire();
            System.out.println(ColoresSout.PURPLE +
                    Thread.currentThread().getName() + " entro al restaurante " + nroRestaurante + " para "
                    + consumo + ColoresSout.RESET);
        } else {
            System.out.println(
                    Thread.currentThread().getName() + " NO puede entrar al restaurante " + nroRestaurante + " para "
                            + consumo + " porque cerro." + ColoresSout.RESET);
            restaurante.take();
        }
        return aux;
    }

    public void salirDelRestaurante() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " salio del restaurante " + nroRestaurante);
        // comer.release();
        restaurante.take();
    }

    public synchronized boolean puedeEntrar() {
        return sigueAbierto;
    }

    // Metodo para el controladorParque
    public synchronized void cerrarRestaurante() throws InterruptedException {
        System.out.println(ColoresSout.BOLD + ColoresSout.PURPLE
                + "HA CERRADO EL RESTAURANTE " + nroRestaurante + ColoresSout.RESET);
        sigueAbierto = false;
    }
}
