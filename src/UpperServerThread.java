import java.io.*;
import java.net.*;
import java.util.*;

public class UpperServerThread extends Thread {

  protected DatagramSocket socket = null;
  protected static HashMap<Peer, String> clubeDoBolinha;

  public UpperServerThread() throws IOException {
    this("UpperServerThread");
  }

  public UpperServerThread(String name) throws IOException {
    super(name);
    socket = new DatagramSocket(Integer.parseInt(name));
  }

  public void run() {
    while (true) {
    	if(socket.getLocalPort() == 4500) { //porta para login
          try {
            byte[] texto = new byte[256];
            // recebe datagrama
            DatagramPacket pacote = new DatagramPacket(texto, texto.length);
            socket.receive(pacote);
            // processa o que foi recebido
            String recebido = new String(pacote.getData(), 0, pacote.getLength());
            recebido = recebido.toUpperCase();
            texto = recebido.getBytes();
            // envia a resposta de volta ao cliente
            InetAddress endereco = pacote.getAddress();
            int porta = pacote.getPort();
            pacote = new DatagramPacket(texto, texto.length, endereco, porta);
            socket.send(pacote);
            if ( recebido.equals("FIM") )
               break;
          } catch (IOException e) {
            e.printStackTrace();
            break;
          }
    	} else if (socket.getLocalPort() == 4600) { //porta para responder as queries
    		
    	} else if (socket.getLocalPort() == 4700) { //porta para checar se peer se encontra na rede
    		
    	}
    }
    socket.close();
    System.out.println("Servidor encerrado...");
  }

}