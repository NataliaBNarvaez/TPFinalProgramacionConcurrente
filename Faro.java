package TPOConcurrente;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Faro {
    private int capacidad, enEscalera, toboganAsignado, aux;
    private Lock lock;
    private Condition esperaSubir, esperaTirarse, adminTobogan, hayToboganLibre;
    private boolean tobogan1Libre, tobogan2Libre, sigueAbierto;

    public Faro(int cap) {
        this.capacidad = cap;
        this.enEscalera = 0;
        this.toboganAsignado = 0;
        this.aux = 0;
        this.lock = new ReentrantLock(true);
        this.esperaSubir = lock.newCondition();
        this.esperaTirarse = lock.newCondition();
        this.adminTobogan = lock.newCondition();
        this.hayToboganLibre = lock.newCondition();
        this.tobogan1Libre = true;
        this.tobogan2Libre = true;
        this.sigueAbierto = true;
    }

    // Metodos para los visitantes
    public int subirEscalera() throws InterruptedException {
        lock.lock();
        try {
            while (enEscalera >= capacidad) {
                System.out.println("El visitante " + Thread.currentThread().getName()
                        + " esta esperando subir a la escalera del Faro-Mirador");
                esperaSubir.await();
            }
            if (sigueAbierto) {
                enEscalera++;
                System.out.println("El visitante " + Thread.currentThread().getName()
                        + " se subio a la escalera y espera un tobogan, VISITANTES ESPERANDO en escalera: "
                        + enEscalera);
                if (enEscalera == 1) { // unicamente avisa el primero que desea tirarse
                    adminTobogan.signal();
                }
                while (toboganAsignado == 0) {
                    esperaTirarse.await();
                }
                if (sigueAbierto) {
                    System.out
                            .println("Tobogan asignado " + toboganAsignado + " a " + Thread.currentThread().getName());
                    aux = toboganAsignado;
                    toboganAsignado = 0;
                } else {
                    aux = 0; // al no ser 1 ó 2 no continua con la ejecucion de los metodos del faro
                    System.out.println(Thread.currentThread().getName() + " No se puede tirar porque cerro el Tobogan");
                }
            } else {
                aux = 0;
                System.out
                        .println(Thread.currentThread().getName() + " No se puede tirar porque cerro el Faro-Mirador");
            }
            return aux;

        } finally {
            lock.unlock();
        }
    }

    public void tirarseDelTobogan(int tobogan) {
        lock.lock();
        try {
            if (tobogan == 1) {
                tobogan1Libre = true;
            } else {
                tobogan2Libre = true;
            }
            System.out.println("El visitante " + Thread.currentThread().getName()
                    + " libero un tobogan " + enEscalera);
            hayToboganLibre.signal();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    // Metodo para el adminTobogan
    public void asignarTobogan() {
        lock.lock();
        try {
            while (enEscalera == 0) {
                adminTobogan.await();
            }
            while (!tobogan1Libre && !tobogan2Libre) {
                hayToboganLibre.await();
            }
            if (tobogan1Libre) {
                tobogan1Libre = false;
                toboganAsignado = 1;
            } else {
                tobogan2Libre = false;
                toboganAsignado = 2;
            }
            esperaTirarse.signal();
            enEscalera--;
            esperaSubir.signal();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    // Metodo para el controladorParque
    public void cerrarFaro() {
        lock.lock();
        try {
            System.out.println(ColoresSout.BOLD + ColoresSout.YELLOW
                    + "HA CERRADO EL FARO-MIRADOR CON DESCENSO EN TOBOGAN" + ColoresSout.RESET);
            sigueAbierto = false;
            toboganAsignado = 7; // numero aleatorio para que salgan del while de toboganAsignado
            enEscalera = 0; // permite que salgan de los while de espera
            esperaSubir.signalAll();
            esperaTirarse.signalAll();
            adminTobogan.signal();
            tobogan1Libre = true;
            hayToboganLibre.signal();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }
}
