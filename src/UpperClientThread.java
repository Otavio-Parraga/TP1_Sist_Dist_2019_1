import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;

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

			} else if (socket.getLocalPort() == 4666) { // recebe requisicao de download
				try{
				byte[] texto = new byte[256];
				DatagramPacket pacote = new DatagramPacket(texto, texto.length);
				socket.receive(pacote);
				String[] recebido = new String(pacote.getData(), 0, pacote.getLength()).split(" ");
				
				File myFile = new File ("..\\files\\"+recebido[1]);
				String stringFile = new String(Files.readAllBytes(myFile.toPath()));
	            String str = recebido[1].concat("-"+stringFile);
	            byte[] data = str.getBytes();
	            DatagramPacket sendPacket = new DatagramPacket(data, data.length, pacote.getAddress(), 4666);
	            socket.send(sendPacket);
	            socket.close();
				
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
}
