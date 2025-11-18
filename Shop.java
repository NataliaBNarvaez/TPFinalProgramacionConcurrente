package TPOConcurrente;

import java.util.concurrent.Semaphore;

/*
En el shop se pueden adquirir suvenires de distinta clase, los cuales se pueden abonar en 
una de las dos cajas disponibles. 
--->SEMAFOROS
*/
public class Shop {
    Semaphore caja1, caja2;

    public Shop() {
        caja1 = new Semaphore(1, true);
        caja2 = new Semaphore(1, true);
    }

    public void adquirirSouvenir() throws InterruptedException {
        // deberia elegir diferentes tipos de souvenirs? como lo simulo?
        System.out.println("El visitante " + Thread.currentThread().getName() + " esta eligiendo un souvenir");
        Thread.sleep(3000);
    }

    public void pagarEnCaja1() throws InterruptedException {
        caja1.acquire();
        Thread.sleep(1000);
        System.out.println(ColoresSout.GREEN + "El visitante " + Thread.currentThread().getName()
                + " ha pagado por un souvenir en caja 1" + ColoresSout.RESET);
        caja1.release();
    }

    public void pagarEnCaja2() throws InterruptedException {
        caja2.acquire();
        Thread.sleep(500);
        System.out.println(ColoresSout.GREEN + "El visitante " + Thread.currentThread().getName()
                + " ha pagado por un souvenir en caja 2" + ColoresSout.RESET);
        caja2.release();
    }
}
