package TPOConcurrente;

/*
Al parque se puede acceder en forma particular o por tour, en el caso del tour, se trasladan 
a través de colectivos folklóricos con una capacidad no mayor a 25 personas, que llegan 
a  un  estacionamiento  destinado  para  tal  fin.  Al  momento  de  arribar  al  parque  se  le 
entregarán pulseras a los visitantes que le permitirán el acceso al parque. 
El ingreso al parque está indicado a través del paso de k molinetes. Una vez ingresado, el 
visitante puede optar por ir al shop o disfrutar de las actividades del parque.

El complejo se encuentra abierto para el ingreso de 09:00 a 17:00hs. Considere que las 
actividades cierran a las 18.00 hrs. 
*/
public class Parque {
    Shop shop;
    Snorkel snorkell;
    Restaurante[] restaurantes = new Restaurante[3];
    Faro faro;
    Tren tren;
    StandDeBicis standBicis;
    CarreraGomones carreraGomones;
    Colectivo colectivo;
    int nro;

    public Parque(int indiv, int dob) {
        this.shop = new Shop();
        this.snorkell = new Snorkel(4);
        this.restaurantes[0] = new Restaurante(1, 4);
        this.restaurantes[1] = new Restaurante(2, 5);
        this.restaurantes[2] = new Restaurante(3, 6);
        this.faro = new Faro(6);
        this.tren = new Tren();
        this.standBicis = new StandDeBicis(7);
        this.carreraGomones = new CarreraGomones(indiv, dob, 7);
        this.colectivo = new Colectivo();
        this.nro = 0;
    }

    public int recibirPulseraYPasarMolinete() {
        System.out.println("El visitante " + Thread.currentThread().getName() + " ha entrado al parque.");
        return nro++;
    }
}