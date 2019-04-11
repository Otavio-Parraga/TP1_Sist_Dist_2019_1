import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class UpperClientThread extends Thread {

	protected DatagramSocket socket = null;

	public UpperClientThread() throws IOException {
		this("UpperClientThread");
	}

	public UpperClientThread(String name) throws IOException {
		super(name);
		socket = new DatagramSocket(Integer.parseInt(name));
	}

	public void run() {
		while (true) {
			if (socket.getLocalPort() == 4800) { // manda mensagem para o server confirmando

			} else if (socket.getLocalPort() == 4700) { // recebe requisição de download
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
