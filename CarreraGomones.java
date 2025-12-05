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
        this.sigueAbierto = true;
        this.lock = new ReentrantLock(true);
        this.controlLargada = new Semaphore(0, true);
        this.salida = new CyclicBarrier(gomonesParaLargada, () -> {
            System.out.println(
                    ColoresSout.UNDERLINE + ColoresSout.RED
                            + "'''''''''''''''' ¡Comienza la carrera! ''''''''''''''''"
                            + ColoresSout.RESET);
        });
        // Condiciones de espera de los visitantes y variables asociadas
        this.esperaIndiv = lock.newCondition();
        this.esperaDoble = lock.newCondition();
        this.esperaCompa = lock.newCondition();
        this.largada = lock.newCondition();
        this.esperaProxLargada = lock.newCondition();
        this.esperandoCompa = 0;
        this.terminaron = 0;
        this.totalVisitParaCarrera = 0;
        this.carreraComenzo = false;
        this.faltaCompaniero = true;
        this.aux = true;
        // Variables asociadas al adminCarrera
        this.esperaAdminCarrera = lock.newCondition();
        this.semAdminCarrera = new Semaphore(0);
        // Variables asociadas a los gomones
        this.semIndividual = new Semaphore(0);
        this.semDoble = new Semaphore(0);
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
                    if (totalVisitParaCarrera == individualesEnUso + doblesEnUso * 2
                            && individualesEnUso + doblesEnUso == gomonesParaLargada) {
                        // Unicamente avisará al admin de carrera el ultimo visitante que faltaba subir
                        // al gomon
                        esperaAdminCarrera.signal();
                    }
                    largada.await();
                    aux = true;

                } else {
                    System.out.println(Thread.currentThread().getName()
                            + " No puede seguir esperando ya que cerro la actividad de Carrera de Gomones.");
                    aux = false;
                }

            } else {
                System.out.println(Thread.currentThread().getName()
                        + " No puede seguir esperando un gomon ya que cerro la actividad de Carrera de Gomones.");
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
                        if (totalVisitParaCarrera == individualesEnUso + doblesEnUso * 2
                                && individualesEnUso + doblesEnUso == gomonesParaLargada) {
                            // Unicamente avisará al admin de carrera el ultimo visitante que faltaba subir
                            // al gomon
                            esperaAdminCarrera.signal();
                        }
                        largada.await();
                        aux = true;
                    }

                } else {
                    System.out.println(Thread.currentThread().getName()
                            + " No puede seguir esperando ya que cerro la actividad de Carrera de Gomones.");
                    aux = false;
                }

            } else {
                System.out.println(Thread.currentThread().getName()
                        + " No puede seguir esperando un gomon ya que cerro la actividad de Carrera de Gomones.");
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
        semIndividual.acquire();
        /*
         * Sout para verificar que el gomon que se llama es efectivamente uno de los que
         * va a la carrera
         * System.out.println( ColoresSout.PURPLE + "\"--- GOMON INDIVIDUAL " +
         * Thread.currentThread().getName() + " FUE LLAMADO " + ColoresSout.RESET);
         */
    }

    public void esperaDoble() throws InterruptedException {
        semDoble.acquire();
    }

    public void carrera(int t) throws Exception {
        // El admin carrera solo deja que lleguen a la barrera una vez estan todos los
        // visitantes listos
        controlLargada.acquire();
        if (verificarSigueAbierto()) {
            salida.await();
            carreraComenzo = true;
            System.out.println(ColoresSout.CYAN + "<< El GOMON de " + t + " : " + Thread.currentThread().getName()
                    + " esta participando en la carrera! >> " + ColoresSout.RESET);
        }
    }

    private boolean verificarSigueAbierto() throws InterruptedException {
        lock.lock();
        try {
            return sigueAbierto;
        } finally {
            lock.unlock();
        }
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
    // Metodos para AdminCarreraGomones
    public boolean iniciarCarrera() throws InterruptedException {
        lock.lock();
        try {
            while ((totalVisitParaCarrera <= gomonesParaLargada) && sigueAbierto) {
                esperaAdminCarrera.await();
                System.out.println(ColoresSout.BLUE + Thread.currentThread().getName()
                        + " ADMIN CHEQUEA QUE HAYAN LLEGADO TODOS: visitantes listos para largar = "
                        + totalVisitParaCarrera
                        + " total de visitantes para largar = "
                        + (individualesEnUso
                                + doblesEnUso * 2)
                        + ColoresSout.RESET);
            }
            // si ya cerro el parque no debe ejecutar el resto de codigo
            if (sigueAbierto) {
                totalVisitParaCarrera = 0;
                System.out.println(ColoresSout.BLUE + Thread.currentThread().getName()
                        + " ADMIN PERMITE Q SALGAN " + ColoresSout.RESET);
                controlLargada.release(gomonesParaLargada);
            }

            return sigueAbierto;

        } finally {
            lock.unlock();
        }
    }

    public void esperaFinalizarCarrera() {
        try {
            semAdminCarrera.acquire();
            System.out.println(ColoresSout.BLUE + Thread.currentThread().getName()
                    + " TERMINO LA CARRERA Y LOS VISITANTES PUEDEN BAJAR DE LOS GOMONES" + ColoresSout.RESET);
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

    // -------------------------------------------------------------------------------------------------//
    // Metodo para el hilo controladorParque
    public void cerrarCarrera() {
        lock.lock();
        try {
            System.out.println(ColoresSout.BOLD + ColoresSout.RED + "HA CERRADO LA CARRERA DE GOMONES POR EL RIO"
                    + ColoresSout.RESET);
            sigueAbierto = false;
            // Esperas de los visitantes
            esperaIndiv.signalAll();
            esperaDoble.signalAll();
            esperaCompa.signalAll();
            esperaProxLargada.signalAll();
            if (!carreraComenzo) {
                // si no llego a completarse la cantidad obligo a que se bajen puesto que no
                // llegaran mas hilos dado que ya se fueron
                largada.signalAll();
            }
            carreraComenzo = false;
            // Espera de los gomones
            semIndividual.release(individuales);
            semDoble.release(dobles);
            controlLargada.release(gomonesParaLargada);
            // Espera del adminCarreraGomones
            esperaAdminCarrera.signal();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }
}
