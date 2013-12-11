package siec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import szyfrowanie.AES;

public class WatekKomunikacji extends Thread {

	private Socket clientSocket;
	private Baza baza;
	private String szyfr;
	String inputLine = null, outputLine;
	PrintWriter out;

	public WatekKomunikacji(Socket clientSocket, Baza baza) {
		this.clientSocket = clientSocket;
		this.baza = baza;

	}

	@Override
	public void run() {

		// TODO Auto-generated method stub
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			System.out
					.println("SERVER: IN and OUT streams opened. Starting receiving data...");

			// out.println("Witam na serwerze. Czekam na dane.");

			// out.println(baza.getRozmowa());
			// out.flush();

			Refresh odswiezacz = new Refresh(this);
			// odswiezacz.start();//TODO

			while (true) {

				inputLine = in.readLine();

				baza.dodajDoRozmowy(inputLine);
				outputLine = baza.getRozmowa();

				szyfr = zaszyfrujTo(outputLine);// szyfruje

				out.println(szyfr);// szyfr
				out.flush();

				// if (inputLine.equals("jaki mam IP?")) {
				// String addr = clientSocket.getInetAddress()
				// .getHostAddress();
				// outputLine = addr;
				// out.println("Twój IP: " + outputLine);
				// out.flush();
				//
				// }

				if (inputLine.equals("koniec"))
					break;

			}
			System.out
					.println("SERVER: Ending sequence received. Closing streams and sockets.");
			out.close();
			in.close();
			clientSocket.close();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String zaszyfrujTo(String tekst) throws Exception {

		return AES.zaszyfruj(tekst);

	}

	public void odswiezOutput() {

		outputLine = baza.getRozmowa();

		try {
			szyfr = zaszyfrujTo(outputLine);
		} catch (Exception e) {

			e.printStackTrace();
		}

		out.println(szyfr);
		out.flush();
	}

}
