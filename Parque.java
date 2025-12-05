package TPOConcurrente;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

public class Parque {
    Shop shop;
    Snorkel snorkell;
    Restaurante[] restaurantes = new Restaurante[3];
    Faro faro;
    Tren tren;
    StandDeBicis standBicis;
    CarreraGomones carreraGomones;
    Colectivo colectivo;
    private int cantMolinetes, nro, molinetesEnUso;
    private Lock lock;
    private Condition esperaApertura, controlador, molinetes;
    private boolean abierto, comenzarActividad;

    public Parque(int m, int indiv, int dob) {
        // Actividades del parque
        this.shop = new Shop();
        this.snorkell = new Snorkel(6);
        this.restaurantes[0] = new Restaurante(1, 4);
        this.restaurantes[1] = new Restaurante(2, 3);
        this.restaurantes[2] = new Restaurante(3, 5);
        this.faro = new Faro(5);
        this.tren = new Tren();
        this.standBicis = new StandDeBicis(11);
        this.carreraGomones = new CarreraGomones(indiv, dob, 6);
        this.colectivo = new Colectivo();
        // Condiciones para la apertura y cierre del parque
        this.lock = new ReentrantLock(true);
        this.esperaApertura = lock.newCondition();
        this.controlador = lock.newCondition();
        this.abierto = false;
        this.comenzarActividad = false;
        this.nro = 0; // para el nro de pulsera de los visitantes
        this.cantMolinetes = m;
        this.molinetes = lock.newCondition();
        this.molinetesEnUso = 0;
    }

    // Metodos del controladorParque
    public void abrirParque() {
        lock.lock();
        try {
            System.out
                    .println(ColoresSout.BOLD + ColoresSout.CYAN
                            + "   --||               ABRIO EL PARQUE               ||-- "
                            + ColoresSout.RESET);
            abierto = true;
            comenzarActividad = true;
            esperaApertura.signalAll();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void cerrarIngresoParque() {
        lock.lock();
        try {
            controlador.await(15, TimeUnit.SECONDS); // cada hora dura aprox. 3 segundos
            System.out
                    .println(ColoresSout.BOLD + ColoresSout.CYAN
                            + "   --||               CERRO EL INGRESO AL PARQUE               ||-- "
                            + ColoresSout.RESET);
            abierto = false;

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public void cerrarActividades() {
        lock.lock();
        try {
            controlador.await(10, TimeUnit.SECONDS);
            comenzarActividad = false;
            esperaApertura.signalAll(); // para que los q llegaron tarde no queden trabados por siempre
            System.out
                    .println(ColoresSout.BOLD + ColoresSout.CYAN
                            + "   --||               CERRARON LAS ACTIVIDADES DEL PARQUE, RETIRARSE LUEGO DE TERMINAR LA ACTIVIDAD ACTUAL               ||-- "
                            + ColoresSout.RESET);
            shop.cerrarShop();
            snorkell.cerrarSnorkel();
            restaurantes[0].cerrarRestaurante();
            restaurantes[1].cerrarRestaurante();
            restaurantes[2].cerrarRestaurante();
            faro.cerrarFaro();
            carreraGomones.cerrarCarrera();
            standBicis.cerrarStandBicis();
            tren.cerrarTren();
            nro = 0;

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    // Metodos para los visitantes
    public void entrarAlParque() {
        lock.lock();
        try {
            while (!abierto && comenzarActividad) {
                System.out.println(ColoresSout.PASTEL_PEACH + "El visitante " + Thread.currentThread().getName()
                        + " NO PUEDE ingresar al parque porque ESTA CERRADO." + ColoresSout.RESET);
                esperaApertura.await();
            }
            if (abierto) {// x si se despertaron cuando cerraron las actividades, para q no salga el msj
                while (molinetesEnUso > cantMolinetes - 1) { // -1 para que no pase uno de mas
                    molinetes.await();
                }
                molinetesEnUso++;
                System.out.println(ColoresSout.PASTEL_YELLOW + "El visitante " + Thread.currentThread().getName()
                        + " esta pasando por el molinete: " + molinetesEnUso + ColoresSout.RESET);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public int recibirPulsera() throws InterruptedException {
        lock.lock();
        try {
            System.out.println(ColoresSout.PASTEL_PEACH + "El visitante " + Thread.currentThread().getName()
                    + " ha entrado al parque con pulsera nro: " + (nro + 1) + " y libero un molinete."
                    + ColoresSout.RESET);
            molinetesEnUso--;
            molinetes.signalAll();
            return nro++;

        } finally {
            lock.unlock();
        }
    }

    public void retirarse() {
        System.out.println(ColoresSout.PASTEL_PEACH + "__ El visitante " + Thread.currentThread().getName()
                + " se ha retirado del parque __" + ColoresSout.RESET);
    }

    // Metodos que usaran multiples hilos para verificar si el parque sigue
    // abierto o si aun se puede entrar
    public boolean puedeEntrar() throws InterruptedException {
        lock.lock();
        try {
            return abierto;

        } finally {
            lock.unlock();
        }
    }

    public boolean puedeContinuar() throws InterruptedException {
        lock.lock();
        try {
            return comenzarActividad;

        } finally {
            lock.unlock();
        }
    }
}