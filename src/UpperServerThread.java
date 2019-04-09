import java.io.*;
import java.net.*;
import java.util.*;

public class UpperServerThread extends Thread {

  protected DatagramSocket socket = null;
  protected static ArrayList<Peer> clubeDoBolinha;

  public UpperServerThread() throws IOException {
    this("UpperServerThread");
  }

  public UpperServerThread(String name) throws IOException {
    super(name);
    socket = new DatagramSocket(Integer.parseInt(name));
    clubeDoBolinha = new ArrayList<Peer>();
  }

  public void run() {
    while (true) {
    	if(socket.getLocalPort() == 4500) { //porta para login
          try {
//            byte[] texto = new byte[256];
//            // recebe datagrama
//            DatagramPacket pacote = new DatagramPacket(texto, texto.length);
//            socket.receive(pacote);
//            // processa o que foi recebido
//            String recebido = new String(pacote.getData(), 0, pacote.getLength());
//            recebido = recebido.toUpperCase();
//            texto = recebido.getBytes();
//            // envia a resposta de volta ao cliente
//            InetAddress endereco = pacote.getAddress();
//            int porta = pacote.getPort();
//            pacote = new DatagramPacket(texto, texto.length, endereco, porta);
//            socket.send(pacote);
//            if ( recebido.equals("FIM") )
//               break;
        	  byte[] texto = new byte[256];
            // recebe datagrama
            DatagramPacket pacote = new DatagramPacket(texto, texto.length);
            socket.receive(pacote);
            // processa o que foi recebido
            String recebido = new String(pacote.getData(), 0, pacote.getLength());
            //System.out.println(recebido.toString());
            //System.out.println(pacote.getAddress().toString());
            ArrayList<String> archives = new ArrayList<String>(Arrays.asList(recebido.split(",")));
            //formato da mensagem ip nome arquivos,separados,por,virgula
            Peer newPeer = new Peer(pacote.getAddress().toString(), archives);
            clubeDoBolinha.add(newPeer);
            // envia a resposta de volta ao cliente
            for(Peer p: clubeDoBolinha) {
            	System.out.println("Peer: ");
    	      	System.out.println("IP: "+p.getIp());
    	      	System.out.println("Resources: "+p.getResources().toString()+"\n------------------------\n");
    	      }
            InetAddress endereco = pacote.getAddress();
            int porta = pacote.getPort();
            pacote = new DatagramPacket(texto, texto.length, endereco, porta);
            socket.send(pacote);
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