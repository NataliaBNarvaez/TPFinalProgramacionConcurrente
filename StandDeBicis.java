package TPOConcurrente;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class StandDeBicis {
    private int cantBicicletas, bicisEnUso;
    private Lock lock;
    private Condition biciDisponible;
    private Boolean sigueAbierto;

    public StandDeBicis(int cBicis) {
        this.cantBicicletas = cBicis;
        this.bicisEnUso = 0;
        this.lock = new ReentrantLock(true);
        this.biciDisponible = lock.newCondition();
        this.sigueAbierto = true;
    }

    // Metodos para los visitantes
    public boolean irEnBici() throws InterruptedException {
        lock.lock();
        try {
            while (bicisEnUso >= cantBicicletas) {
                System.out.println("El visitante " + Thread.currentThread().getName()
                        + " esta esperando una bicicleta.");
                biciDisponible.await();
            }
            if (sigueAbierto) {
                System.out.println(ColoresSout.PINK + "El visitante " + Thread.currentThread().getName()
                        + " se subio a una bicicleta y comienza el viaje!" + ColoresSout.RESET);
                bicisEnUso++;
            } else {
                System.out.println("El visitante " + Thread.currentThread().getName()
                        + " NO puede agarrar una bicicleta dado que cerro el stand.");
            }
            return sigueAbierto;

        } finally {
            lock.unlock();
        }
    }

    public void dejarBicicleta() {
        lock.lock();
        try {
            bicisEnUso--;
            System.out.println(ColoresSout.PASTEL_PINK + "El visitante " + Thread.currentThread().getName()
                    + " llego al inicio del recorrido y devolvio la bicicleta." + ColoresSout.RESET);
            biciDisponible.signalAll();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    // Metodo para el controladorParque
    public void cerrarStandBicis() {
        lock.lock();
        try {
            System.out.println(ColoresSout.BOLD + ColoresSout.PINK + "HA CERRADO EL STAND DE BICICLETAS"
                    + ColoresSout.RESET);
            sigueAbierto = false;
            bicisEnUso = 0;
            biciDisponible.signalAll();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }
}
