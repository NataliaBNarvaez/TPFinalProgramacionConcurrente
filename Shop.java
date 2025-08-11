package TPOConcurrente;
import java.util.concurrent.Semaphore;

public class Shop {
    Semaphore caja1,caja2;
    public Shop(){
        caja1 = new Semaphore(1,true);
        caja2 = new Semaphore(1,true);
    }

    public void pagarEnCaja1() throws InterruptedException{
        caja1.acquire();
        System.out.println("El visitante ha pagado por un souvenir en caja 1");
        caja1.release();
    }

    public void pagarEnCaja2() throws InterruptedException {
        caja2.acquire();
        System.out.println("El visitante ha pagado por un souvenir en caja 2");
        caja2.release();
    }
}
