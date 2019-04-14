import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

public class UpperServerThread extends Thread {

	protected DatagramSocket socket;
	protected static Map<Peer, Integer> clubeDoBolinha;

	public UpperServerThread() throws IOException {
	}

	public UpperServerThread(String name) throws IOException {
		socket = new DatagramSocket(Integer.parseInt(name));
		clubeDoBolinha = new HashMap<Peer, Integer>();
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
					if (checkForIp(pacote.getAddress().toString())) {
						Peer newPeer = new Peer(pacote.getAddress().toString(), archives);
						clubeDoBolinha.put(newPeer, 2);
						// printa no servdor quais os peers na rede
						Iterator<Entry<Peer, Integer>> i = clubeDoBolinha.entrySet().iterator();
						System.out.println("Peers na rede:");
						while (i.hasNext()) {
							Map.Entry<Peer, Integer> mapElement = (Map.Entry<Peer, Integer>) i.next();
							System.out.println("Peer: ");
							System.out.println("IP: " + mapElement.getKey().getIp());
							System.out.println("Resources: " + mapElement.getKey().getResources().toString()
									+ "\n------------------------\n");
						}
						// envia a resposta de volta ao cliente
						texto = "Login-Efetuado".getBytes();
						sendPacket(texto, pacote.getAddress(), pacote.getPort());
					} else {
						texto = "Peer ja presente na rede".getBytes();
						InetAddress endereco = pacote.getAddress();
						int porta = pacote.getPort();
						pacote = new DatagramPacket(texto, texto.length, endereco, porta);
						socket.send(pacote);
					}
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
					// interpreta o que foi recebido
					String recebido = new String(pacote.getData(), 0, pacote.getLength());
					//ArrayList<String> recursosSocilitados = new ArrayList<String>(Arrays.asList(recebido.split(",")));
					String[] recursosSocilitados = recebido.split(",");
					String peersWithResources = "";
					for (String recurso : recursosSocilitados) {
						peersWithResources = peersWithResources.concat("Recurso " + recurso + " possuido pelos peers");
						Iterator<Entry<Peer, Integer>> i = clubeDoBolinha.entrySet().iterator();
						while (i.hasNext()) {
							Map.Entry<Peer, Integer> mapElement = (Map.Entry<Peer, Integer>) i.next();
							if (mapElement.getKey().getResources().toString().contains(recurso)) {
								peersWithResources = peersWithResources.concat(mapElement.getKey().getIp() + ",");
							}
						}
					}
					texto = peersWithResources.getBytes();
					sendPacket(texto, pacote.getAddress(), pacote.getPort());

				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			} else if (socket.getLocalPort() == 4700) { // porta para checar se peer se encontra na rede
				try {
					byte[] texto = new byte[256];
					// recebe datagrama
					DatagramPacket pacote = new DatagramPacket(texto, texto.length);
					socket.receive(pacote);
					// apresenta qual peer checou	
					confirmPeer(pacote.getAddress().toString());
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		}
		socket.close();
		System.out.println("Servidor encerrado...");
	}

	public boolean checkForIp(String ip) {
		Iterator<Entry<Peer, Integer>> i = clubeDoBolinha.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<Peer, Integer> mapElement = (Map.Entry<Peer, Integer>) i.next();
			if (mapElement.getKey().getIp().equals(ip)) {
				return false;
			}
		}
		return true;
	}

	public void confirmPeer(String ip) {
		Iterator<Entry<Peer, Integer>> i = clubeDoBolinha.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<Peer, Integer> mapElement = (Map.Entry<Peer, Integer>) i.next();
			if (mapElement.getKey().getIp().equals(ip)) {
				clubeDoBolinha.replace(mapElement.getKey(), 2);
			}else {
				System.out.println("Peer de ip: " + ip+" enviou mensagem, mas nao esta na rede");
			}
		}
	}

	public void sendPacket(byte[] texto, InetAddress endereco, int porta) throws IOException {
		socket.send(new DatagramPacket(texto, texto.length, endereco, porta));
	}

}