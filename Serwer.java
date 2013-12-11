package siec;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serwer {

	public static void main(String[] args) {

		Baza baza = new Baza();

		while (true) {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(4444);
			} catch (IOException e) {
				System.err.println("SERVER: Could not listen on port: 4444, "
						+ e);
				System.exit(1);
			}
			System.out
					.println("SERVER: Server connection opened on port 4444.");

			Socket clientSocket = null;
			try {
				// after this method server stops and waits for new client
				// connection

				clientSocket = serverSocket.accept();
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("SERVER: Accept failed: 4444, " + e);
				System.exit(1);
			}
			System.out
					.println("SERVER: Accepted client connecion on port 4444.");

			WatekKomunikacji thread = new WatekKomunikacji(clientSocket, baza);
			thread.start();
		}

	}

}
