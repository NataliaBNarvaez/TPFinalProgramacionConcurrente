package TPOConcurrente;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

public class Colectivo {
    private int capacidadColectivo, subieronAlCole;
    private Lock lock;
    private Condition esperaColectivo, colectivero, cole;
    private Boolean empezo, sigueAbierto;

    public Colectivo() {
        this.capacidadColectivo = 25;
        this.subieronAlCole = 0;
        this.lock = new ReentrantLock(true);
        this.esperaColectivo = lock.newCondition();
        this.colectivero = lock.newCondition();
        this.cole = lock.newCondition();
        this.empezo = false;
        this.sigueAbierto = true;
    }

    // Metodos para los visitantes
    public boolean irEnColectivo() throws InterruptedException {
        lock.lock();
        try {
            while (subieronAlCole >= capacidadColectivo || empezo) {
                System.out.println(
                        "El visitante " + Thread.currentThread().getName()
                                + " esta esperando al colectivo folklorico.");
                esperaColectivo.await();
            }
            if (sigueAbierto) {
                subieronAlCole++;
                System.out.println(ColoresSout.PASTEL_MINT + "El visitante " + Thread.currentThread().getName()
                        + " se ha subido al tour en el colectivo folklorico." + ColoresSout.RESET);
                if (subieronAlCole == capacidadColectivo) {
                    colectivero.signal();
                }
                cole.await();
            } else {
                System.out.println(ColoresSout.PASTEL_MINT + "El visitante " + Thread.currentThread().getName()
                        + " NO puede subir al tour en el colectivo folklorico ya que cerro el ingreso."
                        + ColoresSout.RESET);
            }
            return sigueAbierto;

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

    // Metodos para el hilo Colectivero
    public void arrancarTourColectivo() {
        lock.lock();
        try {
            colectivero.await(3, TimeUnit.SECONDS);
            empezo = true;
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
            empezo = false;
            esperaColectivo.signalAll();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void cerrarColectivo() {
        lock.lock();
        try {
            System.out.println(ColoresSout.BOLD + ColoresSout.PASTEL_GREEN
                    + "HA CERRADO EL RECORRIDO EN COLECTIVO FOLKLORICO"
                    + ColoresSout.RESET);
            empezo = false;
            subieronAlCole = 0;
            cole.signalAll();
            esperaColectivo.signalAll();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }
}
