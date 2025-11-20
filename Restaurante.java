package TPOConcurrente;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/*
Restaurante: en el pago del acceso al Parque se encuentra incluido el almuerzo y la 
merienda. Existen tres restaurantes, pero solamente se puede consumir un almuerzo 
y una merienda en cualquiera de ellos. Puede tomar el almuerzo en un restaurante y 
la  merienda  en  otro.  Los  restaurantes  tienen  capacidad  limitada.  Las  personas  son 
atendidas en orden de llegada. Los restaurantes tienen habilitada una cola de espera. 
--->BLOCKING QUEUE
*/
public class Restaurante {
    private int nroRestaurante;
    private BlockingQueue<String> restaurante;
    private Boolean sigueAbierto, aux;

    public Restaurante(int nro, int capacidad) {
        this.nroRestaurante = nro;
        this.restaurante = new ArrayBlockingQueue(capacidad);
        this.sigueAbierto = true;
    }

    public boolean entrarAComer(String consumo) throws InterruptedException {
        System.out.println(
                Thread.currentThread().getName() + " esta esperando para entrar al restaurante " + nroRestaurante);
        if (aux = puedeEntrar()) {
            restaurante.put("entro");
            System.out.println(ColoresSout.PURPLE +
                    Thread.currentThread().getName() + " entro al restaurante " + nroRestaurante + " para "
                    + consumo + ColoresSout.RESET);
        } else {
            System.out.println(
                    Thread.currentThread().getName() + " NO puede entrar al restaurante " + nroRestaurante + " para "
                            + consumo + " porque cerro." + ColoresSout.RESET);
        }
        return aux;
    }

    public void salirDelRestaurante() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " salio del restaurante " + nroRestaurante);
        restaurante.take();
    }

    public synchronized boolean puedeEntrar() {
        return sigueAbierto;
    }

    public void cerrarRestaurante() {
        System.out.println(ColoresSout.BOLD + ColoresSout.PURPLE
                + "HA CERRADO EL RESTAURANTE " + nroRestaurante + ColoresSout.RESET);
        restaurante.clear();
        sigueAbierto = false;
    }
}
