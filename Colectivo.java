package TPOConcurrente;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

public class Colectivo {
    private int capacidadColectivo, subieronAlCole;
    private Lock lock;
    private Condition esperaColectivo, colectivero, cole;

    public Colectivo() {
        this.capacidadColectivo = 25;
        this.subieronAlCole = 0;
        this.lock = new ReentrantLock(true);
        this.esperaColectivo = lock.newCondition();
        this.colectivero = lock.newCondition();
        this.cole = lock.newCondition();
    }

    public void irEnColectivo() {
        lock.lock();
        try {
            while (subieronAlCole >= capacidadColectivo) {
                System.out.println(
                        "El visitante " + Thread.currentThread().getName()
                                + " esta esperando al colectivo folklorico.");
                esperaColectivo.await();
            }
            subieronAlCole++;
            System.out.println("El visitante " + Thread.currentThread().getName()
                    + " se ha subido al tour en el colectivo folklorico.");
            if (subieronAlCole == capacidadColectivo) {
                colectivero.signal();
            }
            cole.await();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void bajarseDelColectivo() {
        lock.lock();
        try {
            if (subieronAlCole > 1) {
                subieronAlCole--;
            } else {
                subieronAlCole = 0;
                esperaColectivo.signalAll();
            }
            System.out.println(ColoresSout.PASTEL_MINT
                    + "El visitante " + Thread.currentThread().getName()
                    + " se ha bajado del colectivo del tour folklorico."
                    + ColoresSout.RESET);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void arrancarTourColectivo() {
        lock.lock();
        try {
            colectivero.await(4, TimeUnit.SECONDS);
            System.out.println(
                    ColoresSout.UNDERLINE + ColoresSout.PASTEL_GREEN + "¡Ha comenzado el tour folklorico!"
                            + ColoresSout.RESET);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void terminarTourColectivo() {
        lock.lock();
        try {
            System.out
                    .println(ColoresSout.UNDERLINE + ColoresSout.PASTEL_GREEN + "¡Ha terminado el tour folklorico!"
                            + ColoresSout.RESET);
            cole.signalAll();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }
}
