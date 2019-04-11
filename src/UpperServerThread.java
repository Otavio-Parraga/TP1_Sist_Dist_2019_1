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
			if (socket.getLocalPort() == 4500) { // porta para login
				try {
					byte[] texto = new byte[256];
					// recebe datagrama
					DatagramPacket pacote = new DatagramPacket(texto, texto.length);
					socket.receive(pacote);
					// processa o que foi recebido
					String recebido = new String(pacote.getData(), 0, pacote.getLength());
					ArrayList<String> archives = new ArrayList<String>(Arrays.asList(recebido.split(",")));
					// formato da mensagem ip nome arquivos,separados,por,virgula
					Peer newPeer = new Peer(pacote.getAddress().toString(), archives);
					clubeDoBolinha.add(newPeer);
					//printa no servdor quais os peers na rede
					System.out.println("Peers na rede:");
					for (Peer p : clubeDoBolinha) {
						System.out.println("Peer: ");
						System.out.println("IP: " + p.getIp());
						System.out
								.println("Resources: " + p.getResources().toString() + "\n------------------------\n");
					}
					// envia a resposta de volta ao cliente
					texto = "Login-Efetuado".getBytes();
					InetAddress endereco = pacote.getAddress();
					int porta = pacote.getPort();
					pacote = new DatagramPacket(texto, texto.length, endereco, porta);
					socket.send(pacote);
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			} else if (socket.getLocalPort() == 4600) { // porta para responder as queries
				try {
					byte[] texto = new byte[256];
					// recebe datagrama
					DatagramPacket pacote = new DatagramPacket(texto, texto.length);
					socket.receive(pacote);
					//interpreta o que foi recebido
					String recebido = new String(pacote.getData(), 0, pacote.getLength());
					ArrayList<String> recursosSocilitados = new ArrayList<String>(Arrays.asList(recebido.split(",")));
					String peersWithResources = "";
					for (String recurso : recursosSocilitados) {
						peersWithResources = peersWithResources.concat("Recurso "+recurso+" possuido pelos peers");
						for (Peer p : clubeDoBolinha) {
							if(p.getResources().toString().contains(recurso)) {
								peersWithResources = peersWithResources.concat(p.getIp()+",");
								//lembrar de remover o ultimo valor da lista no cliente
							}
						}
					}
					
					InetAddress endereco = pacote.getAddress();
					int porta = pacote.getPort();
					texto = peersWithResources.getBytes();
					pacote = new DatagramPacket(texto, texto.length, endereco, porta);
					socket.send(pacote);
					
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			} else if (socket.getLocalPort() == 4700) { // porta para checar se peer se encontra na rede

			}
		}
		socket.close();
		System.out.println("Servidor encerrado...");
	}

}