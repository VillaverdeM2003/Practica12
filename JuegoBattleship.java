import java.util.HashSet;
import java.util.Set;
public class JuegoBattleship {

        private Tablero tableroPropio;
        private Tablero tableroEnemigo;
        private Set<String> posicionesDisparadas; 
        public JuegoBattleship() {
           
            this.tableroPropio = new Tablero();
            this.tableroEnemigo = new Tablero(true);
            this.posicionesDisparadas = new HashSet<>();
        }


        public void colocarBarcos() {
            tableroPropio.colocarBarcosAutomaticamente();
        }

        public boolean todosBarcosHundidos() {
            return tableroPropio.todosBarcosHundidos();
        }

        public char[][] getTableroPropioGrid() {
            return tableroPropio.getGrid();
        }

        public char[][] getTableroEnemigoGrid() {
            return tableroEnemigo.getGrid();
        }


        public boolean procesarDisparoOponente(int fila, int columna) {

            return tableroPropio.recibirDisparo(fila, columna);
        }

        public boolean estaBarcoHundido(String tipoBarco) {
            return tableroPropio.estaBarcoHundido(tipoBarco);
        }

        public String obtenerTipoBarcoEn(int fila, int columna) {
            return tableroPropio.obtenerTipoBarcoEn(fila, columna);
        }



        public void registrarResultadoDisparoPropio(String comando, int fila, int columna) {
            if (ManejadorMensajes.IMPACTO.equals(comando) || ManejadorMensajes.HUNDIDO.equals(comando)) {
                tableroEnemigo.registrarImpactoEnemigo(fila, columna);
            } else if (ManejadorMensajes.FALLO.equals(comando)) {
                tableroEnemigo.registrarFalloEnemigo(fila, columna);
            }

            posicionesDisparadas.add(fila + "," + columna);
        }

        public boolean yaDisparado(int fila, int columna) {
            return posicionesDisparadas.contains(fila + "," + columna);
        }
        public boolean misBarcosEstanHundidos() {
    return tableroPropio.todosBarcosHundidos();
}

}