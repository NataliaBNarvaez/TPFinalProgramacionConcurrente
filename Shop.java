package TPOConcurrente;

import java.util.concurrent.Semaphore;

/*
En el shop se pueden adquirir suvenires de distinta clase, los cuales se pueden abonar en 
una de las dos cajas disponibles. 
--->SEMAFOROS
*/
public class Shop {
    private Semaphore tienda, caja1, caja2;
    private boolean sigueAbierto, aux;

    public Shop() {
        tienda = new Semaphore(1);
        caja1 = new Semaphore(1, true);
        caja2 = new Semaphore(1, true);
        this.sigueAbierto = true;
    }

    public boolean adquirirSouvenir() throws InterruptedException {
        // deberia poder elegir diferentes tipos de souvenirs?
        if (aux = puedeEntrar()) {
            System.out.println("El visitante " + Thread.currentThread().getName() + " esta eligiendo un souvenir");
            Thread.sleep(3000);
        } else {
            System.out.println("El visitante " + Thread.currentThread().getName()
                    + " NO puede eligir un souvenir ya que cerro la tienda");
        }
        return aux;
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

    public boolean puedeEntrar() throws InterruptedException {
        tienda.acquire();
        aux = sigueAbierto;
        tienda.release();
        return aux;
    }

    public void cerrarShop() throws InterruptedException {
        tienda.acquire();
        System.out.println(
                ColoresSout.BOLD + ColoresSout.GREEN + "HA CERRADO LA TIENDA DE SOUVENIRES" + ColoresSout.RESET);
        sigueAbierto = false;
        tienda.release();
    }
}
