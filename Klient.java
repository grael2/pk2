package siec;

import gui.RamkaKlienta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Klient {

	static RamkaKlienta ramka;

	public void wlacz() {

		String[] pusta = {};
		ramka = new RamkaKlienta(null, null, pusta);

		try {
			Socket socket = new Socket("localhost", 4444);
			System.out.println("CLIENT: Server connected on port 4444");

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			System.out
					.println("CLIENT: IN and OUT streams opened. Starting sending data...");

			String userInput, serverResponse = "";

			while (true) {
				Thread.sleep(100);
				if (ramka.zalogowany == true)
					break;
			}

			out.println("---- " + ramka.mojeImie + " dolaczyl do rozmowy ----");

			while (true) {

				serverResponse = in.readLine();

				while (true) {
					Thread.sleep(100);

					ramka.rozmowa = serverResponse;
					ramka.aktualizujRozmowe();

					if (ramka.lWyslij.gotowe)
						break;

				}
				ramka.lWyslij.gotowe = false;

				userInput = ramka.mojeImie + ": " + ramka.lWyslij.getMessage();
				// wprowadza sie
				// wiadomosc do serwera

				// ramka.rozmowa += ramka.mojeImie + ": " + userInput + "\n";
				// ramka.aktualizujRozmowe();

				out.println(userInput);
				out.flush();

				if (userInput.equals("koniec")) {
					break;
				}
			}

			System.out
					.println("CLIENT: Ending server connection. Closing client streams and socket.");
			out.close();
			in.close();
			socket.close();
			System.exit(0);
		} catch (UnknownHostException e) {
			System.err.println("CLIENT: Trying to connect to unknown host: "
					+ e);
			System.exit(1);
		} catch (Exception e) {
			System.err.println("CLIENT: Exception:  " + e);
			System.exit(1);
		}

	}
}
