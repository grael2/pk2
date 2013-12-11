package siec;

public class Refresh extends Thread {

	WatekKomunikacji komunikacja;

	public Refresh(WatekKomunikacji komunikacja) {
		this.komunikacja = komunikacja;

	}

	public void run() {

		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			komunikacja.odswiezOutput();
		}

	}

}
