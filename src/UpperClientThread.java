import java.io.IOException;
import java.net.DatagramSocket;

public class UpperClientThread extends Thread{
	
	protected DatagramSocket socket = null;
	
	public UpperClientThread() throws IOException {
		this("UpperClientThread");
	}

	public UpperClientThread(String name) throws IOException {
		super(name);
		socket = new DatagramSocket(Integer.parseInt(name));
	}
	
	public void run() {
		while(true) {
			if(socket.getLocalPort() == 4800) { //manda mensagem para o server confirmando
				
			} else if(socket.getLocalPort() == 4700) { // recebe requisição de download
				
			}
		}
	}
}
