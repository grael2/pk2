package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import siec.Klient;
import szyfrowanie.AES;

public class RamkaKlienta extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	public String[] tablicaUczestnikow = { "Adam", "Jan", "Wojciech", "Robert" };
	public String mojeImie = "Piotr";
	public String rozmowa = "Ładowanie rozmowy z serwera...";
	public boolean zalogowany = true;
	Klient klient;
	JScrollPane areaScrollPane, areaScrollPane2;
	JPanel panel, panel2;
	JButton wyslij;
	ListenerLogowanie lLog = new ListenerLogowanie();
	ListenerStworzKonto lStworz = new ListenerStworzKonto(this);
	JTextField loginT2;
	JPasswordField hasloT2, hasloT2P;
	JTextArea textArea, oknoRozmowy;
	JButton zaloguj = new JButton("Zaloguj");
	public ListenerWyslij lWyslij = new ListenerWyslij(this);

	// Aplikacja client-server zapewniająca bezpieczne "e-negocjacje"
	// (bezpieczne logowanie, hash MD5 hasła przechowywany na serwerze, funkcja
	// zmiany hasła, szyfrowanie komunikatów)
	public RamkaKlienta(String mojeImie, String rozmowa,
			String[] tablicaUczestnikow) {

		init(mojeImie, rozmowa, tablicaUczestnikow);

	}

	public void aktualizujRozmowe() {

		remove(areaScrollPane2);
		dodajRozmowe();
		oknoRozmowy.setCaretPosition(oknoRozmowy.getDocument().getLength()); // ustawia
																				// scroll
																				// na
																				// dole
		revalidate();

	}

	public void init(String mojeImie, String rozmowa,
			String[] tablicaUczestnikow) {
		// this.mojeImie=mojeImie;
		// this.rozmowa=rozmowa;
		// this.tablicaUczestnikow= tablicaUczestnikow;

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		if (zalogowany) {
			setTitle("E-negocjacje");

			setJMenuBar(dodajMenu());
			dodajRozmowe();
			dodajUczestnikow();
			dodajEdytor();
		} else {
			getContentPane().removeAll();
			setJMenuBar(null);
			setTitle("E-negocjacje - logowanie");
			tworzEkranLogowania();

		}

		setVisible(true);
		pack();

	}

	private void tworzEkranLogowania() {
		panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		panel2.setVisible(true);

		JPanel logowanie = new JPanel();
		logowanie.setLayout(new GridLayout(3, 3));
		JPanel zalozKonto = new JPanel();
		zalozKonto.setLayout(new GridLayout(4, 3));
		JScrollPane areaScrollPane = new JScrollPane(logowanie);
		JScrollPane areaScrollPane2 = new JScrollPane(zalozKonto);
		// logowanie

		JLabel login = new JLabel("Login: ");
		JLabel haslo = new JLabel("Hasło: ");
		JTextField loginT = new JTextField(10);

		JPasswordField hasloT = new JPasswordField(10);

		zaloguj.addActionListener(lLog);

		logowanie.add(login);
		logowanie.add(loginT);
		logowanie.add(haslo);
		logowanie.add(hasloT);
		logowanie.add(zaloguj);

		// zalozKonto

		JLabel loginNowy = new JLabel("Login: ");
		JLabel hasloNowy = new JLabel("Hasło: ");
		JLabel hasloNowyPowtorz = new JLabel("Powtórz hasło: ");
		loginT2 = new JTextField(10);
		hasloT2 = new JPasswordField(10);
		hasloT2P = new JPasswordField(10);
		JButton stworz = new JButton("Stwórz konto");
		stworz.addActionListener(lStworz);

		zalozKonto.add(loginNowy);
		zalozKonto.add(loginT2);
		zalozKonto.add(hasloNowy);
		zalozKonto.add(hasloT2);
		zalozKonto.add(hasloNowyPowtorz);
		zalozKonto.add(hasloT2P);
		zalozKonto.add(stworz);

		// obramowania
		areaScrollPane.setPreferredSize(new Dimension(270, 140));
		areaScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Zaloguj się"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)),
				areaScrollPane.getBorder()));

		areaScrollPane2.setPreferredSize(new Dimension(270, 140));
		areaScrollPane2.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Załóż konto"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)),
				areaScrollPane2.getBorder()));
		// stworzono obramowania

		panel2.add(areaScrollPane, BorderLayout.WEST);
		panel2.add(areaScrollPane2, BorderLayout.EAST);
		add(panel2, BorderLayout.CENTER);
	}

	private void dodajEdytor() {

		panel = new JPanel();
		panel.setVisible(true);
		panel.setLayout(new BorderLayout());

		textArea = new JTextArea("wiadomosc testowa");

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane areaScrollPane = new JScrollPane(textArea);
		areaScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(500, 100));
		areaScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Moja wiadomość"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)),
				areaScrollPane.getBorder()));

		wyslij = new JButton("Wyslij");
		wyslij.addActionListener(lWyslij);
		wyslij.setVisible(true);

		panel.add(areaScrollPane, BorderLayout.CENTER);
		panel.add(wyslij, BorderLayout.EAST);

		add(panel, BorderLayout.SOUTH);
	}

	private void dodajUczestnikow() {

		String uczestnicy = "";
		uczestnicy += "1. " + mojeImie + "\n";

		for (int i = 0; i < tablicaUczestnikow.length; i++) {

			uczestnicy += (i + 2) + ". " + tablicaUczestnikow[i] + "\n";
		}

		JTextArea textArea = new JTextArea(uczestnicy);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		areaScrollPane = new JScrollPane(textArea);
		areaScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(150, 250));
		areaScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Uczestnicy"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)),
				areaScrollPane.getBorder()));
		areaScrollPane.setVisible(true);

		textArea.setVisible(true);
		add(areaScrollPane, BorderLayout.WEST);

	}

	private void dodajRozmowe() {

		try {
		
			rozmowa = AES.odszyfruj(rozmowa);
		} catch (Exception e) {
		//	System.out.println("błąd odszyfrowywania, bo "+rozmowa);
		}

		oknoRozmowy = new JTextArea(rozmowa);
		oknoRozmowy.setEditable(false);
		oknoRozmowy.setLineWrap(true);
		oknoRozmowy.setWrapStyleWord(true);

		areaScrollPane2 = new JScrollPane(oknoRozmowy);
		areaScrollPane2
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		areaScrollPane2.setPreferredSize(new Dimension(450, 250));
		areaScrollPane2.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Rozmowa"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)),
				areaScrollPane2.getBorder()));
		areaScrollPane2.setVisible(true);

		oknoRozmowy.setVisible(true);

		add(areaScrollPane2, BorderLayout.CENTER);

	}

	private JMenuBar dodajMenu() {

		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		menuBar = new JMenuBar();

		menu = new JMenu("Menu");
		menuBar.add(menu);

		menuItem = new JMenuItem("Zmien haslo");
		menuItem.addActionListener(new ListenerLogowanie());
		menu.add(menuItem);

		menuItem = new JMenuItem("Wyloguj");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		return menuBar;

	}

	public void actionPerformed(ActionEvent e) {

		setVisible(false);
		zalogowany = false;

	}

}
