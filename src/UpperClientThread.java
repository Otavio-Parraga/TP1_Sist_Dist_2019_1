import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UpperClientThread extends Thread {

	protected DatagramSocket socket = null;
	protected InetAddress endereco;
	

	public UpperClientThread(String number, String ipServer) throws IOException {
		socket = new DatagramSocket(Integer.parseInt(number));
		endereco = InetAddress.getByName(ipServer);
		;

	}

	public void run() {
		while (true) {
			if (socket.getLocalPort() == 4800) { // manda mensagem para o server confirmando
				while (true) {
					byte[] texto = new byte[256];
					texto = "Checado".getBytes();
					DatagramPacket pacote = new DatagramPacket(texto, texto.length, endereco, 4700);
					try {
						socket.send(pacote);
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			} else if (socket.getLocalPort() == 4666) { // recebe requisi��o de download
				// https://stackoverflow.com/questions/9520911/java-sending-and-receiving-file-byte-over-sockets

			}
		}
	}
}
