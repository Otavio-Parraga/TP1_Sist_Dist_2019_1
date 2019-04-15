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
				System.out.println(recebido[0]);
				
				File myFile = new File ("../files/"+recebido[0]);
				String stringFile = new String(Files.readAllBytes(myFile.toPath()));
	            String str = recebido[0].concat("  "+stringFile);
	            DatagramPacket sendPacket = new DatagramPacket(str.getBytes(), str.getBytes().length, pacote.getAddress(), pacote.getPort());
	            socket.send(sendPacket);
	            socket.close();
				
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
}
