package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListenerWyslij implements ActionListener {

	RamkaKlienta ramka;
	String wiadomosc;
	public boolean gotowe =false;

	public ListenerWyslij(RamkaKlienta ramkaKlienta) {
		this.ramka = ramkaKlienta;
	}

	public void actionPerformed(ActionEvent e) {

		gotowe=true;
		wiadomosc = ramka.textArea.getText();
		try {
			Thread.sleep(11);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		ramka.textArea.setText("");

	}

	public String getMessage() throws InterruptedException {

		return wiadomosc;
	}

}
