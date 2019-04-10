import java.io.*;
import java.net.*;
import java.util.*;

public class UpperClient {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		byte[] texto = new byte[256];
		DatagramSocket socket = new DatagramSocket();
		String auxiliar;
		
		if (args.length != 2) {
			System.out.println("Uso: java UpperClient <maquina> <texto>");
			return;
		}
		
		while (true) {
			auxiliar  = in.nextLine();
			if (auxiliar.equals("1")) { // realiza o login
				// cria um socket datagrama
				// envia um pacote
				texto = args[1].getBytes();
				InetAddress endereco = InetAddress.getByName(args[0]);
				DatagramPacket pacote = new DatagramPacket(texto, texto.length, endereco, 4500);
				socket.send(pacote);
				// obtem a resposta
				pacote = new DatagramPacket(texto, texto.length);
				socket.receive(pacote);
				// mostra a resposta
				String resposta = new String(pacote.getData(), 0, pacote.getLength());
				System.out.println("Texto recebido do servidor: " + resposta);
			} else if(auxiliar.equals("2")) { //realiza uma pesquisa por peers com o recurso
				System.out.println("What resource do you want?");
				texto = in.nextLine().getBytes();
				//envia o pacote perguntando o recurso
				InetAddress endereco = InetAddress.getByName(args[0]);
				DatagramPacket pacote = new DatagramPacket(texto, texto.length, endereco, 4600);
				socket.send(pacote);
				
				//recebe string com os diferentes peers no que o servidor possui
				pacote = new DatagramPacket(texto, texto.length);
				socket.receive(pacote);
				
				//printa a resposta
				String resposta = new String(pacote.getData(), pacote.getOffset(), pacote.getLength());
				System.out.println("Peers que possuem o recurso: " + resposta);
				
			} else if(auxiliar.equals("3")) { //
				System.out.println("Get resource from peer");
				
			}else if (auxiliar.equals("0")) {
				System.out.println("Closed");
				break;
			}else if(auxiliar != null) {
				break;
			} 
			
		}
		// fecha o socket
		socket.close();
		in.close();
	}

}