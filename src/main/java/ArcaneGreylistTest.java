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
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Port number required.");
            return;
        }

        try {
            ServerSocket sock = new ServerSocket(port);
            HashSet<String> set = new HashSet<>();

            System.out.println("Socket server listening on port " + port + ".");

            while (true){
                //init the client
                try (Socket incoming = sock.accept()) {
                    DataInputStream dis = new DataInputStream(incoming.getInputStream());
                    DataOutputStream dos = new DataOutputStream(incoming.getOutputStream());

                    String action = dis.readUTF();


                    if (!action.equals("greylist")) {
                        dos.writeBoolean(false);
                        String msg = action + " action unsupported. :(";
                        dos.writeUTF(msg);
                        System.out.println("Sent: " + msg);
                        continue;
                    }

                    String user = dis.readUTF();

                    if (!set.add(user)) {
                        dos.writeBoolean(false);
                        String msg = user + " user is already greylisted!";
                        dos.writeUTF(msg);
                        System.out.println("Sent: " + msg);
                        continue;
                    } else {
                        dos.writeBoolean(true);
                        System.out.println("Successfully greylisted " + user);
                        continue;
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