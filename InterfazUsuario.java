public interface InterfazUsuario {
    
    void mostrarMensaje(String mensaje);

    
    void mostrarTablero(char[][] tablero, String titulo);

 
    String obtenerOpcionModo();

   
    String solicitarNombre();

    String solicitarIP();

    int[] obtenerDisparoJugador(boolean yaDisparado); 
}