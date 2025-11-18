package TPOConcurrente;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class StandDeBicis {
    private int cantBicicletas, bicisEnUso;
    private Lock lock;
    private Condition biciDisponible;

    public StandDeBicis(int cBicis) {
        this.cantBicicletas = cBicis;
        this.bicisEnUso = 0;
        this.lock = new ReentrantLock(true);
        this.biciDisponible = lock.newCondition();
    }

    public void irEnBici() {
        lock.lock();
        try {
            while (bicisEnUso >= cantBicicletas) {
                System.out.println("El visitante " + Thread.currentThread().getName()
                        + " esta esperando una bicicleta.");
                biciDisponible.await();
            }
            System.out.println(ColoresSout.BLUE + "El visitante " + Thread.currentThread().getName()
                    + " se subio a una bicicleta y comienza el viaje!." + ColoresSout.RESET);
            bicisEnUso++;

        } catch (Exception e) {
            e.printStackTrace();

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
}
