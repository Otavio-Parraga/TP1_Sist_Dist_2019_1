import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class UpperClientThread extends Thread {

	protected DatagramSocket socket = null;
	protected InetAddress endereco;


	public UpperClientThread(String number, String ipServer) throws IOException {
		socket = new DatagramSocket(Integer.parseInt(number));
		endereco = InetAddress.getByName(ipServer);;
		
	}

	public void run() {
		while (true) {
			if (socket.getLocalPort() == 4800) { // manda mensagem para o server confirmando
				while(true) {
					System.out.println("Checking!");
					byte[] texto = new byte[256];
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

			} else if (socket.getLocalPort() == 4700) { // recebe requisi��o de download
				//https://stackoverflow.com/questions/9520911/java-sending-and-receiving-file-byte-over-sockets
				ServerSocket serverSocket = null;
				try {
					try {
						serverSocket = new ServerSocket(4700);
					} catch (IOException e) {
						System.out.println("Can't setup server on this port number. ");
						e.printStackTrace();
					}
					Socket socket = null;
					InputStream in = null;
					OutputStream out = null;
					socket = serverSocket.accept();

					in = socket.getInputStream();

					out = new FileOutputStream("../archives/test1.txt");
					byte[] bytes = new byte[16 * 1024];

					int count;
					while ((count = in.read(bytes)) > 0) {
						out.write(bytes, 0, count);
					}

					out.close();
					in.close();
					socket.close();
					serverSocket.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
