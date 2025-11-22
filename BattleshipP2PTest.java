package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BattleshipP2PTest {

    // --- obtenerDisparoJugador ---
    @Test
    void testObtenerDisparoJugador_RechazaFueraDeRango() {
        BattleshipP2P p = new BattleshipP2P();

        assertThrows(IllegalArgumentException.class, () -> p.obtenerDisparoJugador(-1, 5));
        assertThrows(IllegalArgumentException.class, () -> p.obtenerDisparoJugador(12, 2));
    }

    // --- turnoLocal ---
    @Test
    void testTurnoLocal_ActualizaTableroConRespuestaRemota() {

        BattleshipP2P p = new BattleshipP2P();
        p.juego.colocarBarcosAutomaticamente();

        // Disparo válido
        int[] pos = {3, 4};

        // Simular respuesta remota
        String respuesta = "RESULTADO:IMPACTO";

        p.turnoLocal(pos[0], pos[1], respuesta);

        assertTrue(p.juego.yaDisparado(pos[0], pos[1]));
    }
}
