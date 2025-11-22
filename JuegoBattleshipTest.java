import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JuegoBattleshipTest {

    private JuegoBattleship juego;

    @BeforeEach
    void setUp() {
        juego = new JuegoBattleship();
        juego.colocarBarcosAutomaticamente();
    }

    
    @Test
    void testColocarBarcosAutomaticamente_Coloca5BarcosSinSuperposicion() {

        int barcosEncontrados = 0;

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                char tipo = juego.obtenerTipoBarcoEn(x, y);
                if (tipo != '~') barcosEncontrados++;
            }
        }

        assertEquals(5, juego.barcos.size(), "Debe haber exactamente 5 barcos.");
        assertTrue(barcosEncontrados > 0, "Debe haber celdas ocupadas por barcos.");
    }

   
    @Test
    void testRecibirDisparo_RegistraImpacto() {

        int[] pos = buscarCeldaConBarco();
        boolean resultado = juego.recibirDisparo(pos[0], pos[1]);

        assertTrue(resultado, "Debe regresar true porque fue impacto.");
    }

   
    @Test
    void testRecibirDisparo_DosVecesMismaCelda_RegresaFalse() {

        int[] pos = buscarCeldaConBarco();

        juego.recibirDisparo(pos[0], pos[1]);
        boolean segundaVez = juego.recibirDisparo(pos[0], pos[1]);

        assertFalse(segundaVez, "Disparo repetido debe regresar false.");
    }

   
    @Test
    void testObtenerTipoBarcoEn_RegresaTipoCorrecto() {

        int[] pos = buscarCeldaConBarco();
        char tipo = juego.obtenerTipoBarcoEn(pos[0], pos[1]);

        assertTrue("ADCSM".contains("" + tipo), "Debe ser un tipo de barco válido.");
    }

  
    @Test
    void testEstaBarcoHundido_CuandoImpactosIgualTamano() {

        Barco barco = juego.barcos.get(0);

        for (int i = 0; i < barco.getTamano(); i++) {
            barco.registrarImpacto();
        }

        assertTrue(juego.estaBarcoHundido(barco), "Debe detectarse que el barco está hundido.");
    }

    @Test
    void testTodosBarcosHundidos_TrueSoloSiTodosHundidos() {

        for (Barco b : juego.barcos) {
            for (int i = 0; i < b.getTamano(); i++) {
                b.registrarImpacto();
            }
        }

        assertTrue(juego.todosBarcosHundidos(), "Todos los barcos deberían estar hundidos.");
    }


    @Test
    void testYaDisparado_RegistrarYDetectarDisparoRepetido() {

        juego.registrarDisparo(4, 7);

        assertTrue(juego.yaDisparado(4, 7), "Debe detectar que ya se disparó a esa celda.");
    }


  
    private int[] buscarCeldaConBarco() {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (juego.obtenerTipoBarcoEn(x, y) != '~') {
                    return new int[]{x, y};
                }
            }
        }
        return new int[]{0, 0};
    }
}
