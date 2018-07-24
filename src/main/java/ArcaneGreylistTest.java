import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;

public class ArcaneGreylistTest {

    public static void main(String[] args) {
        int port;

        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException | NullPointerException e) {
            System.out.println("Port number required.");
            return;
        }

        try {
            ServerSocket sock = new ServerSocket(port);
            HashSet<String> set = new HashSet<>();

            while (true){
                //init the client
                try (Socket incoming = sock.accept()) {
                    DataInputStream dis = new DataInputStream(incoming.getInputStream());
                    String action = dis.readUTF();
                    String user = dis.readUTF();

                    DataOutputStream dos = new DataOutputStream(incoming.getOutputStream());

                    if (!action.equals("greylist")) {
                        dos.writeBoolean(false);
                        dos.writeUTF("Action unsupported. :(");
                    } else if (!set.add(user)) {
                        dos.writeBoolean(false);
                        dos.writeUTF("User is already greylisted!");
                    } else {
                        dos.writeBoolean(true);
                    }

                } catch (SocketException e) {
                    e.printStackTrace();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}