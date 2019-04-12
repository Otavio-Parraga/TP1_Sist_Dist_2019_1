import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class UpperManagerThread extends UpperServerThread {

	public UpperManagerThread() throws IOException {
		super();
	}

	public void run() {
		while (true) {
			checkPeers();
			System.out.println("Atualmente " + clubeDoBolinha.size() + " peers na rede");
			for (Map.Entry<Peer, Integer> entry : clubeDoBolinha.entrySet()) {
				clubeDoBolinha.put(entry.getKey(), entry.getValue() - 1);
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void checkPeers() {
		Iterator<Entry<Peer, Integer>> i = clubeDoBolinha.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<Peer, Integer> mapElement = (Map.Entry<Peer, Integer>) i.next();
			if (mapElement.getValue() == 0) {
				clubeDoBolinha.remove(mapElement.getKey());
			}
		}
	}
}
