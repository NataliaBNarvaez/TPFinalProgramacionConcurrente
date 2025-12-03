// TPO Programacion Concurrente (Cursada 2022) ~ Natalia Narvaez FAI-3198.
package TPOConcurrente;

public class Main {
    public static void main(String[] args) {
        int cantVisitantes1 = 10;
        int cantVisitantes2 = 6;
        int individuales = 5;
        int dobles = 5;
        Parque parque = new Parque(individuales, dobles);
        Gomon[] gIndividuales = new Gomon[individuales];
        Thread[] hilosGIndividuales = new Thread[individuales];
        Gomon[] gDobles = new Gomon[dobles];
        Thread[] hilosGDobles = new Thread[dobles];
        Visitante[] visitantes = new Visitante[cantVisitantes1];
        Thread[] hilosVisitantes = new Thread[cantVisitantes1];
        Thread controladorParque = new Thread(new ControladorParque(parque));
        Thread adminTobogan = new Thread(new AdminTobogan(parque));
        Thread adminCarreraGomones = new Thread(new AdminCarreraGomones(parque));
        Thread maquinista = new Thread(new Maquinista(parque));
        Thread colectivero = new Thread(new Colectivero(parque));
        controladorParque.start();
        adminTobogan.start();
        adminCarreraGomones.start();
        maquinista.start();
        colectivero.start();

        for (int i = 0; i < individuales; i++) {
            gIndividuales[i] = new Gomon(1, parque);
            hilosGIndividuales[i] = new Thread(gIndividuales[i]);
            hilosGIndividuales[i].start();
        }

        for (int i = 0; i < dobles; i++) {
            gDobles[i] = new Gomon(2, parque);
            hilosGDobles[i] = new Thread(gDobles[i]);
            hilosGDobles[i].start();
        }

        for (int i = 0; i < cantVisitantes1; i++) {
            visitantes[i] = new Visitante(parque);
            hilosVisitantes[i] = new Thread(visitantes[i]);
            hilosVisitantes[i].start();
        }

        // Para poder ver que pueden entrar luego de la largada iniciar
        try {
            Thread.sleep(6000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < cantVisitantes2; i++) {
            visitantes[i] = new Visitante(parque);
            hilosVisitantes[i] = new Thread(visitantes[i]);
            hilosVisitantes[i].start();
        }

        // Para poder ver que una vez cerro ya no pueden entrar
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < cantVisitantes2; i++) {
            visitantes[i] = new Visitante(parque);
            hilosVisitantes[i] = new Thread(visitantes[i]);
            hilosVisitantes[i].start();
        }
    }
}
