package szyfrowanie;

import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	static String IV = "AAAAAAAAAAAAAAAA";
	String plainText = "test text 12345\0"; // maksymalnie 16 znaków,
											// dopełniać można za pomocą
											// \0\0\0 <- beda to puste 3
											// znaki; tutaj ten String nie jest
											// używany
	private static String encryptionKey = "6gT3QCuwR9FRpXfd";// klucz 128 bitów
							
	public static byte[] encrypt(String plainText, String encryptionKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
		SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"),
				"AES");
		cipher.init(Cipher.ENCRYPT_MODE, key,
				new IvParameterSpec(IV.getBytes("UTF-8")));
		return cipher.doFinal(plainText.getBytes("UTF-8"));
	}

	public static String decrypt(byte[] cipherText, String encryptionKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
		SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"),
				"AES");
		cipher.init(Cipher.DECRYPT_MODE, key,
				new IvParameterSpec(IV.getBytes("UTF-8")));
		return new String(cipher.doFinal(cipherText), "UTF-8");
	}

	public static String zaszyfruj(String tekst) {

		if(tekst==null )return null;
		
		// System.out.println("|"+tekst+"|");

		int x = tekst.length() / 16;
		if (tekst.length() % 16 != 0)
			x++;

		String[] doZaszyfrowania = new String[x];

		int a = 0;

		for (int i = 0; i < x - 1; i++) {

			doZaszyfrowania[i] = tekst.substring(a, a + 16);

			a += 16;
		}

		if (tekst.length() % 16 != 0) {// jezeli jest koncowka

			doZaszyfrowania[x - 1] = tekst.substring((x - 1) * 16);
		}

		//System.out.println("|"+doZaszyfrowania[x - 1] +"|");
		
		if (doZaszyfrowania[x - 1].length() != 16) {// ! x-1

			while (doZaszyfrowania[x - 1].length() != 16)
				doZaszyfrowania[x - 1] += "\0";

			// dodanie odpowiedniej ilosci pustych znaków, żeby szyfrowanie
			// zadziałało
		}

		byte[] szyfr = null;

		for (int i = 0; i < doZaszyfrowania.length; i++) {

			try {
				szyfr = encrypt(doZaszyfrowania[i], encryptionKey);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			doZaszyfrowania[i] = Arrays.toString(szyfr);

		}

		return Arrays.toString(doZaszyfrowania);
	}

	public static String odszyfruj(String tekst) throws Exception {
		// trzeba podzielic na byte[16], przekazywac do decrypt i zlaczyc w
		// calosc
		
		if(tekst==null)return null;
		tekst = tekst.substring(1, tekst.length() - 1);// usunieto boczne
														// nawiasy
		// System.out.println(tekst);

		ArrayList<byte[]> listaTablic = new ArrayList<byte[]>();
		byte[] szyfr = new byte[16];
		int x = 0;
		String liczba = "";

		for (int i = 0; i < tekst.length(); i++) {

			if (tekst.charAt(i) == ']') {
				listaTablic.add(szyfr);
				x = 0;
				szyfr = new byte[16];

			}
			if (tekst.charAt(i) == '[' | tekst.charAt(i) == ','
					| tekst.charAt(i) == ']') {

				continue;
			}

			if (tekst.charAt(i) != ' ')
				liczba += tekst.charAt(i);

			if (tekst.charAt(i + 1) == ',' | tekst.charAt(i + 1) == ']') {
				// System.out.println(liczba);
				szyfr[x] = Byte.parseByte(liczba);
				liczba = "";
				x++;
			}

		}

		StringBuffer wynik = new StringBuffer();

		for (int i = 0; i < listaTablic.size(); i++) {
			wynik.append(decrypt(listaTablic.get(i), encryptionKey));

		}

		return wynik.toString();
	}

}
