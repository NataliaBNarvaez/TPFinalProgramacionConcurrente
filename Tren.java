package TPOConcurrente;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

public class Tren {
    private int capacidadTren, subieronAlTren;
    private Lock lock;
    private Condition esperaTren, maquinista, tren;
    private Boolean empezo, sigueAbierto;

    public Tren() {
        this.capacidadTren = 15;
        this.subieronAlTren = 0;
        this.lock = new ReentrantLock(true);
        this.esperaTren = lock.newCondition();
        this.maquinista = lock.newCondition();
        this.tren = lock.newCondition();
        this.empezo = false;
        this.sigueAbierto = true;
    }

    public boolean irEnTren() throws InterruptedException {
        lock.lock();
        try {
            while (subieronAlTren >= capacidadTren || empezo) {
                System.out.println("El visitante " + Thread.currentThread().getName()
                        + " esta esperando al tren para ir al Faro-Mirador.");
                esperaTren.await();
            }
            if (sigueAbierto) {
                subieronAlTren++;
                System.out.println("El visitante " + Thread.currentThread().getName()
                        + " se ha subido al tren para ir al Faro-Mirador.");
                if (subieronAlTren == capacidadTren) {
                    maquinista.signal();
                }
                tren.await();
            } else {
                System.out.println(
                        "El visitante " + Thread.currentThread().getName() + " NO puede subir al tren dado que cerro.");
            }
            return sigueAbierto;

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
            empezo = true;
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
            empezo = false;
            esperaTren.signalAll();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void cerrarTren() {
        lock.lock();
        try {
            System.out.println(ColoresSout.BOLD + ColoresSout.PASTEL_PURPLE + "HA CERRADO EL RECORRIDO EN TREN"
                    + ColoresSout.RESET);
            sigueAbierto = false;
            subieronAlTren = 0;
            tren.signalAll();
            esperaTren.signalAll();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }
}
