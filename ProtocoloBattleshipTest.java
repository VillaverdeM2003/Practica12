package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProtocoloBattleshipTest {

    @Test
    void testParsearMensaje_FormatoCorrecto() {

        String msg = "DISPARAR:3,5";
        ProtocoloBattleship.ProtocoloMensaje m = ProtocoloBattleship.parsearMensaje(msg);

        assertEquals("DISPARAR", m.comando);
        assertEquals(3, m.x);
        assertEquals(5, m.y);
    }

    @Test
    void testConstruirMensajeDisparo() {

        String msg = ProtocoloBattleship.construirMensajeDisparo(4, 7);

        assertEquals("DISPARAR:4,7", msg);
    }

    // --- construirMensajeResultado ---
    @Test
    void testConstruirMensajeResultado() {

        assertEquals("RESULTADO:IMPACTO", ProtocoloBattleship.construirMensajeResultado("IMPACTO"));
        assertEquals("RESULTADO:FALLO", ProtocoloBattleship.construirMensajeResultado("FALLO"));
        assertEquals("RESULTADO:HUNDIDO", ProtocoloBattleship.construirMensajeResultado("HUNDIDO"));
    }
}
