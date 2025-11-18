package TPOConcurrente;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tren {
    private int capacidadTren, subieronAlTren;
    private Lock lock;
    private Condition esperaTren, maquinista, tren;

    public Tren() {
        this.capacidadTren = 15;
        this.subieronAlTren = 0;
        this.lock = new ReentrantLock(true);
        this.esperaTren = lock.newCondition();
        this.maquinista = lock.newCondition();
        this.tren = lock.newCondition();
    }

    public void irEnTren() {
        lock.lock();
        try {
            while (subieronAlTren >= capacidadTren) {
                System.out.println("El visitante " + Thread.currentThread().getName() + " esta esperando al tren.");
                esperaTren.await();
            }
            subieronAlTren++;
            System.out.println("El visitante " + Thread.currentThread().getName() + " se ha subido al tren.");
            if (subieronAlTren == capacidadTren) {
                maquinista.signal();
            }
            tren.await();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void bajarseDelTren() {
        lock.lock();
        try {
            if (subieronAlTren > 1) {
                subieronAlTren--;
            } else { // el ultimo en bajar avisa al resto q ya puede subir
                subieronAlTren = 0;
                esperaTren.signalAll();
            }
            System.out.println(ColoresSout.PURPLE
                    + "El visitante " + Thread.currentThread().getName() + " se ha bajado del tren."
                    + ColoresSout.RESET);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void arrancarRecorridoTren() {
        lock.lock();
        try {
            maquinista.await(7, TimeUnit.SECONDS);
            System.out.println(
                    ColoresSout.UNDERLINE + ColoresSout.PASTEL_PURPLE + "¡Ha comenzado el viaje en tren!"
                            + ColoresSout.RESET);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void terminarRecorridoTren() {
        lock.lock();
        try {
            System.out.println(ColoresSout.UNDERLINE + ColoresSout.PASTEL_PURPLE + "¡Ha terminado el viaje en tren!"
                    + ColoresSout.RESET);
            tren.signalAll();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }
}
