import java.io.*;
import java.net.*;

public class ManejadorConexion {
    private static final int PUERTO = 1091;
    private Socket socket;
    private ServerSocket serverSocket;
    private PrintWriter salida;
    private BufferedReader entrada;

    private boolean esServidor;


    public boolean esperarConexion(InterfazUsuario ui) {
        this.esServidor = true;
        try {
            ui.mostrarMensaje("\nIniciando servidor en puerto " + PUERTO + "...");
            serverSocket = new ServerSocket(PUERTO);
            ui.mostrarMensaje("Esperando conexión de otro jugador...");

            socket = serverSocket.accept();
            ui.mostrarMensaje("¡Jugador conectado desde: " + socket.getInetAddress() + "!");

            configurarFlujos();
            return true;
        } catch (IOException e) {
            ui.mostrarMensaje("Error al esperar conexión: " + e.getMessage());
            return false;
        }
    }

    public boolean conectarAPartida(String ip, InterfazUsuario ui) {
        this.esServidor = false;
        try {
            ui.mostrarMensaje("Conectando a " + ip + ":" + PUERTO + "...");
            socket = new Socket(ip, PUERTO);
            ui.mostrarMensaje("¡Conectado exitosamente!");

            configurarFlujos();
            return true;
        } catch (IOException e) {
            ui.mostrarMensaje("Error al conectar: " + e.getMessage());
            return false;
        }
    }

    private void configurarFlujos() throws IOException {
        salida = new PrintWriter(socket.getOutputStream(), true);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String intercambiarNombres(String nombreLocal) throws IOException {
        if (esServidor) {
           
            String nombreOponente = entrada.readLine();
            salida.println(nombreLocal);
            return nombreOponente;
        } else {
          
            salida.println(nombreLocal);
            String nombreOponente = entrada.readLine();
            return nombreOponente;
        }
    }


    public void enviarMensaje(String mensaje) {
        if (salida != null) {
            salida.println(mensaje);
        }
    }

    public String recibirMensaje() throws IOException {
        return entrada.readLine();
    }

    public boolean esServidor() {
        return esServidor;
    }



    public void cerrarConexion(InterfazUsuario ui) {
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
            ui.mostrarMensaje("Conexión cerrada.");
        } catch (IOException e) {
            ui.mostrarMensaje("Error al cerrar conexión: " + e.getMessage());
        }
    }
}