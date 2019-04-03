import java.io.*;

public class UpperServer {
  public static void main(String[] args) throws IOException {
    new UpperServerThread("4500").start(); // thread para login
    new UpperServerThread("4600").start(); // thread para procurar por recursos
    new UpperServerThread("4700").start(); // thread para certificar que esta na rede
  }
}