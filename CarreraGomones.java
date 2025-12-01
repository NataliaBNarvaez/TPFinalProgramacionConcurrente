package TPOConcurrente;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class CarreraGomones {
    private int individuales, individualesEnUso, dobles, doblesEnUso, esperandoCompa, terminaron, gomonesParaLargada,
            totalVisitParaCarrera;
    private Lock lock;
    private Condition esperaIndiv, esperaDoble, esperaCompa, largada, esperaProxLargada, esperaAdminCarrera;
    private CyclicBarrier salida;
    private Semaphore controlLargada, semIndividual, semDoble, semAdminCarrera;
    private Boolean carreraComenzo, sigueAbierto, faltaCompaniero, aux;

    public CarreraGomones(int indiv, int dob, int h) {
        this.individuales = indiv;
        this.individualesEnUso = 0;
        this.dobles = dob;
        this.doblesEnUso = 0;
        this.gomonesParaLargada = h;
        this.lock = new ReentrantLock(true);
        this.esperaIndiv = lock.newCondition();
        this.esperaDoble = lock.newCondition();
        this.esperaCompa = lock.newCondition();
        this.largada = lock.newCondition();
        this.esperaProxLargada = lock.newCondition();
        this.esperaAdminCarrera = lock.newCondition();
        this.esperandoCompa = 0;
        this.terminaron = 0;
        this.totalVisitParaCarrera = 0;
        this.salida = new CyclicBarrier(gomonesParaLargada, () -> {
            System.out.println(
                    ColoresSout.UNDERLINE + ColoresSout.RED
                            + "'''''''''''''''' ¡Comienza la carrera! ''''''''''''''''"
                            + ColoresSout.RESET);
        });
        this.carreraComenzo = false;
        this.controlLargada = new Semaphore(0, true);
        this.semIndividual = new Semaphore(0);
        this.semDoble = new Semaphore(0);
        this.semAdminCarrera = new Semaphore(0);
        this.sigueAbierto = true;
        this.faltaCompaniero = true;
        this.aux = true;
    }

    // -------------------------------------------------------------------------------------------------//
    // Metodos que controlan la dsitribucion de gomones a los visitantes y la
    // finalizacion de la carrera (pedir sus pertenencias al bajar)
    public boolean pedirGomon(int tipo) throws InterruptedException {
        lock.lock();
        try {
            if (tipo == 1) { // Individual
                pedirGomonIndividual();
            } else { // Doble
                pedirGomonDoble();
            }
            return aux;

        } finally {
            lock.unlock();
        }
    }

    private void pedirGomonIndividual() {
        lock.lock();
        try {
            while (individualesEnUso >= individuales && sigueAbierto) {
                System.out.println(Thread.currentThread().getName() + " esta esperando un gomon INDIVIDUAL");
                esperaIndiv.await();
            }
            if (sigueAbierto) {
                while ((individualesEnUso + doblesEnUso >= gomonesParaLargada || carreraComenzo) && sigueAbierto) {
                    System.out.println("ESPERA PROXIMA LARGADA " + Thread.currentThread().getName());
                    esperaProxLargada.await();
                }
                if (sigueAbierto) {
                    individualesEnUso++;
                    System.out.println(
                            Thread.currentThread().getName() + " SUBIO a un gomon INDIVIDUAL, indiv en uso: "
                                    + individualesEnUso);
                    semIndividual.release();

                    System.out.println(ColoresSout.RED + Thread.currentThread().getName() + " EN LARGADA INDIVIDUAL"
                            + ColoresSout.RESET);
                    totalVisitParaCarrera++;
                    esperaAdminCarrera.signal();
                    largada.await();
                    aux = true;

                } else {
                    System.out.println("NO PUEDE ESPERAR A LA PROXIMA LARGADA YA QUE CERRO LA ACTIVIDAD "
                            + Thread.currentThread().getName());
                    aux = false;
                }

            } else {
                System.out.println("NO PUEDE ESPERAR MAS UN GOMON YA QUE CERRO LA ACTIVIDAD "
                        + Thread.currentThread().getName());
                aux = false;
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    private void pedirGomonDoble() {
        lock.lock();
        try {
            while (doblesEnUso >= dobles && sigueAbierto) {
                System.out.println(Thread.currentThread().getName() + " esta esperando un gomon DOBLE");
                esperaDoble.await();
            }
            if (sigueAbierto) {
                while ((individualesEnUso + doblesEnUso >= gomonesParaLargada || carreraComenzo) && sigueAbierto) {
                    System.out.println("ESPERA PROXIMA LARGADA " + Thread.currentThread().getName());
                    esperaProxLargada.await();
                }
                if (sigueAbierto) {
                    esperandoCompa++;
                    if (esperandoCompa == 1) {
                        faltaCompaniero = true;
                        while (esperandoCompa == 1 && faltaCompaniero) {
                            System.out.println(Thread.currentThread().getName()
                                    + " SUBIO a un gomon DOBLE Y ESPERA COMPA, dobles en uso: "
                                    + doblesEnUso);
                            esperaCompa.await();
                            System.out.println(ColoresSout.YELLOW + Thread.currentThread().getName()
                                    + " SE DESPERTO XQ CONSIGUIO COMPA " + ColoresSout.RESET);
                            faltaCompaniero = false;
                        }
                    } else {
                        esperandoCompa = 0;
                        doblesEnUso++;
                        System.out.println(Thread.currentThread().getName()
                                + " SUBIO a un gomon DOBLE Y COMPLETO PAREJA, dobles en uso: "
                                + doblesEnUso);
                        esperaCompa.signal();
                        semDoble.release();
                    }
                    if (sigueAbierto) {
                        System.out.println(
                                ColoresSout.RED + Thread.currentThread().getName() + " EN LARGADA DOBLE"
                                        + ColoresSout.RESET);
                        totalVisitParaCarrera++;
                        esperaAdminCarrera.signal();
                        largada.await();
                        aux = true;

                    } else {
                        System.out.println("YA QUE CERRO LA ACTIVIDAD "
                                + Thread.currentThread().getName());
                        aux = false;
                    }

                } else {
                    System.out.println("NO PUEDE ESPERAR A LA PROXIMA LARGADA YA QUE CERRO LA ACTIVIDAD "
                            + Thread.currentThread().getName());
                    aux = false;
                }

            } else {
                System.out.println("NO PUEDE ESPERAR A LA PROXIMA LARGADA YA QUE CERRO LA ACTIVIDAD "
                        + Thread.currentThread().getName());
                aux = false;
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void pedirPertenencias() {
        System.out.println(ColoresSout.PASTEL_BLUE + Thread.currentThread().getName()
                + " pide sus pertenencias" + ColoresSout.RESET);
    }

    // -------------------------------------------------------------------------------------------------//
    // Metodos para el control de los gomones (espera a ser llamados y comienzo de
    // la carrera)
    public void esperaIndiv() throws InterruptedException {
        System.out.println(
                ColoresSout.BLUE + "--- GOMON " + Thread.currentThread().getName() + " ESTA ESPERANDO "
                        + ColoresSout.RESET);
        semIndividual.acquire();
        System.out.println(
                ColoresSout.PURPLE + "\"--- GOMON " + Thread.currentThread().getName() + " FUE LLAMADO "
                        + ColoresSout.RESET);
    }

    public void esperaDoble() throws InterruptedException {
        System.out.println(
                ColoresSout.BLUE + "--- GOMON DOBLE" + Thread.currentThread().getName() + " ESTA ESPERANDO "
                        + ColoresSout.RESET);
        semDoble.acquire();
        System.out.println(
                ColoresSout.PURPLE + "--- GOMON DOBLE" + Thread.currentThread().getName() + " FUE LLAMADO "
                        + ColoresSout.RESET);
    }

    public void carrera(int t) throws Exception {
        controlLargada.acquire();
        System.out.println(Thread.currentThread().getName() + " PERMISO PARA IR A LA CARRERA");

        salida.await();
        carreraComenzo = true;
        System.out.println(ColoresSout.CYAN + "<< El GOMON de " + t + " : " + Thread.currentThread().getName()
                + " esta participando en la carrera! >> " + ColoresSout.RESET);
    }

    public void terminarCarrera(int t) {
        lock.lock();
        try {
            terminaron++;
            if (t == 1) {
                individualesEnUso--;
            } else {
                doblesEnUso--;
            }
            if (terminaron == 1) {
                System.out.println(ColoresSout.GREEN + Thread.currentThread().getName()
                        + " EL GOMON GANO LA CARRERA!!!" + ColoresSout.RESET);

            } else if (terminaron < gomonesParaLargada) {
                System.out.println(
                        ColoresSout.PASTEL_MINT + Thread.currentThread().getName()
                                + " EL GOMON TERMINO LA CARRERA EN POSICION " + terminaron + ColoresSout.RESET);

            } else {// ultimo en llegar
                System.out.println(ColoresSout.PASTEL_ORANGE
                        + Thread.currentThread().getName()
                        + " EL GOMON ES EL ULTIMO EN TERMINAR LA CARRERA :(" + ColoresSout.RESET);

                semAdminCarrera.release();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    // -------------------------------------------------------------------------------------------------//
    // Metodos para AdminCarreraGomones y cierre de la actividad
    public void iniciarCarrera() {
        lock.lock();
        try {
            while (totalVisitParaCarrera < gomonesParaLargada
                    || totalVisitParaCarrera < individualesEnUso + doblesEnUso * 2) {
                System.out.println(ColoresSout.BLUE + Thread.currentThread().getName()
                        + " ADMIN ESPERA QUE LLEGUEN TODOS: visitantes listos para largar = " + totalVisitParaCarrera
                        + " total de visitantes para largar = "
                        + (individualesEnUso
                                + doblesEnUso * 2)
                        + ColoresSout.RESET);
                esperaAdminCarrera.await();
            }
            totalVisitParaCarrera = 0;
            System.out.println(ColoresSout.BLUE + Thread.currentThread().getName()
                    + " ADMIN PERMITE Q SALGAN " + ColoresSout.RESET);
            controlLargada.release(gomonesParaLargada);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void esperaFinalizarCarrera() {
        try {
            semAdminCarrera.acquire();
            System.out.println(ColoresSout.BLUE + Thread.currentThread().getName()
                    + " TERMINA LA CARRERA Y PUEDEN BAJAR DE LOS GOMONES" + ColoresSout.RESET);
            finalizarCarrera();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finalizarCarrera() {
        lock.lock();
        try {
            largada.signalAll();
            carreraComenzo = false;
            esperaProxLargada.signalAll();
            esperaIndiv.signalAll();
            System.out.println(ColoresSout.CYAN + "  INDIVIDUALES EN USO LUEGO DE LA CARRERA: " + individualesEnUso
                    + ColoresSout.RESET);
            esperaDoble.signalAll();
            System.out.println(ColoresSout.CYAN + "  DOBLES EN USO LUEGO DE LA CARRERA: " + doblesEnUso
                    + ColoresSout.RESET);
            terminaron = 0;

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void cerrarCarrera() {
        lock.lock();
        try {
            System.out.println(ColoresSout.BOLD + ColoresSout.RED + "HA CERRADO LA CARRERA DE GOMONES POR EL RIO"
                    + ColoresSout.RESET);
            sigueAbierto = false;
            carreraComenzo = false;
            esperaIndiv.signalAll();
            esperaDoble.signalAll();
            esperaCompa.signalAll();
            esperaProxLargada.signalAll();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }
}
