package TPOConcurrente;

import java.util.Random;

/*
Disfruta  de  Snorkel  ilimitado:  existe  la  posibilidad  de  realizar  snorkel  en  una 
laguna, para lo cual es necesario adquirir previamente el equipo de snorkel, salvavidas 
y patas de ranas, que deberán ser devueltos al momento de finalizar la actividad. En 
el  ingreso  a  la  actividad  hay  un  stand  donde  dos  asistentes  entregan  el  equipo 
mencionado.  La  única  limitación  en  cuanto  a  capacidad  es  dada  por  la  cantidad  de 
equipos completos (snorkel, salvavidas y patas de rana) existentes. 
--->MONITORES
*/
public class Snorkel {
    private int equiposDeSnorkell;
    private Random asistente;

    public Snorkel(int cantEquipos) {
        this.equiposDeSnorkell = cantEquipos;
        this.asistente = new Random();
    }

    public synchronized void adquirirEquipoSnorkell() throws InterruptedException {
        while (equiposDeSnorkell <= 0) {
            System.out.println("El visitante " + Thread.currentThread().getName()
                    + " esta esperando en el stand por un equipo completo.");
            wait();
        }
        equiposDeSnorkell--;
        System.out.println("El asistente " + (asistente.nextInt(2) + 1) + " le entrego al visitante "
                + Thread.currentThread().getName() + " el equipo completo de snorkell y esta listo para comenzar!");
    }

    public synchronized void devolverEquipoSnorkell() throws InterruptedException {
        equiposDeSnorkell++;
        System.out.println("El visitante " + Thread.currentThread().getName() + " ha devuelto el equipo de snorkell.");
        notifyAll();
    }
}
