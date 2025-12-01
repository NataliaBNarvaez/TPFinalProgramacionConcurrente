package TPOConcurrente;

import java.util.concurrent.Semaphore;

public class Shop {
    private Semaphore cajas, tienda;
    private boolean sigueAbierto, aux;

    public Shop() {
        cajas = new Semaphore(2, true);
        tienda = new Semaphore(1, true);
        this.sigueAbierto = true;
    }

    public boolean adquirirSouvenir() throws InterruptedException {
        if (aux = puedeEntrar()) {
            System.out.println("El visitante " + Thread.currentThread().getName() + " esta eligiendo un souvenir");
            Thread.sleep(3000);
        } else {
            System.out.println("El visitante " + Thread.currentThread().getName()
                    + " NO puede eligir un souvenir ya que cerro la tienda");
        }
        return aux;
    }

    public void pagarEnCaja() throws InterruptedException {
        cajas.acquire();
        Thread.sleep(1000);
        System.out.println(ColoresSout.GREEN + "El visitante " + Thread.currentThread().getName()
                + " ha pagado por un souvenir en una de las cajas." + ColoresSout.RESET);
        cajas.release();
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
