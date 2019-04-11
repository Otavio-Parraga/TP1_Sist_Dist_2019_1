import java.io.*;
import java.net.*;
import java.util.*;

public class Upper {

	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		byte[] texto = new byte[256];
		DatagramSocket socket = new DatagramSocket();
		String auxiliar;
	

		if (args[0].equals( "-c")) {
			System.out.println("Iniciado o cliente!");
			while (true) {
				System.out.println("Digite 1 para login, 2 para procurar um recurso e 3 para coletar um recurso.");
				auxiliar = in.nextLine();
				if (auxiliar.equals("1")) { // realiza o login
					System.out.println("Digite os <recursos>");
					String entrada = in.nextLine();
					// cria um socket datagrama
					// envia um pacote
					texto = entrada.getBytes();
					InetAddress endereco = InetAddress.getByName(args[1]);
					DatagramPacket pacote = new DatagramPacket(texto, texto.length, endereco, 4500);
					socket.send(pacote);
					// obtem a resposta
					texto = new byte[256];
					pacote = new DatagramPacket(texto, texto.length);
					socket.receive(pacote);
					// mostra a resposta
					String resposta = new String(pacote.getData(), 0, pacote.getLength());
					System.out.println("Texto recebido do servidor: " + resposta);
					//new UpperClientThread("4700", args[1]).start();

				} else if (auxiliar.equals("2")) { // realiza uma pesquisa por peers com o recurso
					System.out.println("Por qual(is) recursos voce procura?");
					texto = in.nextLine().getBytes();
					// envia o pacote perguntando o recurso
					InetAddress endereco = InetAddress.getByName(args[1]);
					DatagramPacket pacote = new DatagramPacket(texto, texto.length, endereco, 4600);
					socket.send(pacote);

					// recebe string com os diferentes peers no que o servidor possui
					texto = new byte[256];
					pacote = new DatagramPacket(texto, texto.length);
					socket.receive(pacote);

					// printa a resposta
					String resposta = new String(pacote.getData(), 0, pacote.getLength());
					System.out.println("Resposta do Servidor: ");
					System.out.println(resposta);

				} else if (auxiliar.equals("3")) { // pega recurso de outro peer
					System.out.println("<ip_peer> <name_resource>");
					String ipAndResource = in.nextLine();
					// envia msg para outro peer
					InetAddress endereco = InetAddress.getByName(ipAndResource.split(" ")[0]);
					texto = ipAndResource.getBytes();
					DatagramPacket pacote = new DatagramPacket(texto, texto.length, endereco, 4800);
					socket.send(pacote);

					// recebe arquivo

				} else if (auxiliar.equals("0")) {
					System.out.println("Closed");
					break;
				} else if (auxiliar != null) {
					break;
				}

			}

		} else{
			System.out.println("Iniciado o servidor");
			new UpperServerThread("4500").start(); // thread para login
			new UpperServerThread("4600").start(); // thread para procurar por recursos
			new UpperServerThread("4700").start(); // thread para certificar que esta na rede
		}
		// fecha o socket
		socket.close();
		in.close();
	}

}