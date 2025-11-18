package TPOConcurrente;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/*
Carrera de gomones por el río: esta actividad permite que los visitantes bajen por 
el rio, que se encuentra rodeado de manglares, compitiendo entre ellos. Para ello es 
necesario llegar hasta el inicio de la actividad a través de bicicletas que se prestan en 
un  stand  de  bicicletas,  o  a  través  de  un  tren  interno  que  tiene  una  capacidad  de  15 
personas como máximo. 
Al llegar al inicio del recorrido cada persona dispondrá de un  bolso  con  llave,  en  donde  podrá
guardar  todas  las  pertenencias  que  no  quiera mojar. Los bolsos están identificados con un número 
al igual que la llave, los bolsos serán  llevados  en  una  camioneta,  hasta  el  final  del  recorrido
en  donde  podrán  ser retirados por el visitante. 
Para bajar se utilizan gomones, individuales o con capacidad para 2 personas. La cantidad de gomones de 
cada tipo es limitada. Para habilitar una largada es necesario que haya h gomones listos para salir, 
no importa el tipo.  
--->CYCLIC BARRIER
*/
public class CarreraGomones {
    private int individuales, individualesEnUso, dobles, doblesEnUso, visEsperandoIndiv,
            visEsperandoDoble, esperandoCompa, terminaron, gomonesParaLargada;
    private Lock lock;
    private Condition gomonIndiv, gomonDoble, esperaIndiv, esperaDoble, esperaCompa, largada, esperaLargada;
    private CyclicBarrier salida;
    private Semaphore sem;
    private Boolean carreraComenzo;

    public CarreraGomones(int indiv, int dob, int h) {
        this.individuales = indiv;
        this.individualesEnUso = 0;
        this.dobles = dob;
        this.doblesEnUso = 0;
        this.gomonesParaLargada = h;
        this.lock = new ReentrantLock(true);
        this.gomonIndiv = lock.newCondition();
        this.gomonDoble = lock.newCondition();
        this.esperaIndiv = lock.newCondition();
        this.esperaDoble = lock.newCondition();
        this.esperaCompa = lock.newCondition();
        this.largada = lock.newCondition();
        this.esperaLargada = lock.newCondition();
        this.visEsperandoIndiv = 0;
        this.visEsperandoDoble = 0;
        this.esperandoCompa = 0;
        this.terminaron = 0;
        this.salida = new CyclicBarrier(gomonesParaLargada, () -> {
            System.out.println(
                    ColoresSout.UNDERLINE + ColoresSout.PASTEL_ORANGE + "¡Comienza la carrera!" + ColoresSout.RESET);
        });
        // this.carreraComenzo = false;
        this.sem = new Semaphore(gomonesParaLargada);
    }

    public void pedirGomon(int tipo) {
        lock.lock();
        try {
            if (tipo == 1) { // Individual
                visEsperandoIndiv++;
                while (individualesEnUso >= individuales) {
                    esperaIndiv.await();
                    System.out.println(Thread.currentThread().getName() + " esta esperando un gomon INDIVIDUAL");
                }
                individualesEnUso++;
                visEsperandoIndiv--;
                System.out.println(Thread.currentThread().getName() + " SUBIO a un gomon INDIVIDUAL, indiv en uso: "
                        + individualesEnUso);
                gomonIndiv.signal();
                /*
                 * while (individualesEnUso + doblesEnUso >= gomonesParaLargada) {
                 * esperaLargada.await();
                 * }
                 */
                largada.await();

            } else { // Doble
                visEsperandoDoble++;
                while (doblesEnUso >= dobles) {
                    esperaDoble.await();
                    System.out.println(Thread.currentThread().getName() + " esta esperando un gomon DOBLE");
                }
                visEsperandoDoble--;
                esperandoCompa++;
                if (esperandoCompa == 1) {
                    while (esperandoCompa == 1) {
                        System.out.println(Thread.currentThread().getName()
                                + " SUBIO a un gomon DOBLE Y ESPERA COMPA, dobles en uso: "
                                + doblesEnUso);
                        esperaCompa.await();
                    }
                } else {
                    esperandoCompa = 0;
                    doblesEnUso++;
                    System.out.println(Thread.currentThread().getName()
                            + " SUBIO a un gomon DOBLE Y COMPLETO PAREJA, dobles en uso: "
                            + doblesEnUso);
                    esperaCompa.signal();
                    gomonDoble.signal();
                }
                /*
                 * while (individualesEnUso + doblesEnUso >= gomonesParaLargada) {
                 * esperaLargada.await();
                 * }
                 */
                largada.await();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void esperaIndiv() {
        lock.lock();
        try {
            while (visEsperandoIndiv == 0) {
                gomonIndiv.await();
            }
            System.out.println(ColoresSout.PASTEL_BLUE + "GOMON INDIVIDUAL " + Thread.currentThread().getName()
                    + " fue llamado." + ColoresSout.RESET);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void esperaDoble() {
        lock.lock();
        try {
            while (visEsperandoDoble == 0) {
                gomonDoble.await();
            }
            System.out.println(ColoresSout.PASTEL_BLUE + "GOMON DOBLE " + Thread.currentThread().getName()
                    + " fue llamado." + ColoresSout.RESET);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void carrera(int t) throws Exception {
        sem.acquire();
        System.out.println(Thread.currentThread().getName() + " PERMISO PARA IR A BARRERA");
        salida.await();
        carreraComenzo = true;
        System.out.println(ColoresSout.CYAN + "<< El GOMON de " + t + " : " + Thread.currentThread().getName()
                + " esta participando en la carrera! >> " + ColoresSout.RESET);
    }

    public void terminarCarrera(int t) {
        lock.lock();
        try {
            terminaron++;
            if (terminaron == 1) {
                System.out.println(Thread.currentThread().getName()
                        + " EL GOMON GANO LA CARRERA!!!");
            } else if (terminaron < gomonesParaLargada) {
                System.out.println(Thread.currentThread().getName()
                        + " EL GOMON TERMINO LA CARRERA");
            } else {// ultimo en llegar
                System.out.println(Thread.currentThread().getName()
                        + " EL GOMON ES EL ULTIMO EN TERMINAR LA CARRERA :(");
                largada.signalAll();
                esperaIndiv.signalAll();
                esperaDoble.signalAll();
                terminaron = 0;

                sem.release(gomonesParaLargada);

                // esperaLargada.signalAll();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (t == 1) {
                individualesEnUso--;
            } else {
                doblesEnUso--;
            }
            lock.unlock();
        }
    }

    public void pedirPertenencias() {
        System.out.println(ColoresSout.RED + Thread.currentThread().getName()
                + " pide sus pertenencias" + ColoresSout.RESET);
    }
}
