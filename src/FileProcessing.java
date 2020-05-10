import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;

public class FileProcessing {
	
	static Scanner sampleWordScanner, cipherScanner;
	
	ArrayList<String> words = new ArrayList<>();
	String cipherText = "";
	char cipherOfE;
	int occurenceOfE, occurenceOfT;
	int keyA, keyB;
	String decryptedText = "";
	
	public int getKeyA() {
		return keyA;
	}

	public void setKeyA(int keyA) {
		this.keyA = keyA;
	}

	public int getKeyB() {
		return keyB;
	}

	public void setKeyB(int keyB) {
		this.keyB = keyB;
	}

	public String getCipherText() {
		return cipherText;
	}

	public void setCipherText(String cipherText) {
		this.cipherText = cipherText;
	}
	
	public void setDecryptedText(String decryptedText) {
		this.decryptedText = decryptedText;
	}

	public String getDecryptedText() {
		return decryptedText;
	}

	public FileProcessing() {
		
		try {
			sampleWordScanner = new Scanner(new File("words.txt"));
			cipherScanner = new Scanner(new File("24-e.txt"));
			cipherText = cipherScanner.next();
			while (sampleWordScanner.hasNextLine())
				words.add(sampleWordScanner.nextLine());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int countOccurences(String text, char letter, int index) {
		if (index >= text.length())
			return 0;
			     
		int count = text.charAt(index) == letter ? 1 : 0;
		return count + countOccurences(text, letter, index + 1);
	}
	
	public void printOccurences() {
		int[] counts = new int[123];
		
		System.out.println("Letter occurences:");
		for (int i = 97; i <= 122; i++) {
			int c = countOccurences(cipherText, (char)i, 0);
			System.out.println((char)i + ":" + c);
			counts[i] = c;
			
			}
		
		int maxAt = 97, secondMaxAt = 97;

		for (int i = 97; i <= 122; i++) {
			if (counts[i] > counts[maxAt]) {
				secondMaxAt = maxAt;
				maxAt = i;
			}
			
			
		    //maxAt = counts[i] > counts[maxAt] ? i : maxAt;
		}
		
		occurenceOfE = counts[maxAt];
		occurenceOfT = counts[secondMaxAt];
		cipherOfE = (char)maxAt;
		
		System.out.println("\nMaximum occurence: " +(char)maxAt);
		System.out.println("Second maximum occurrence: " +(char)secondMaxAt);
		
		System.out.println("\nWe can conclude that within this ciphertext, the letter "
				+ (char)maxAt + " corresponds to the letter E, and the letter "
				+ (char)secondMaxAt + " corresponds to the letter T\n");
	}
	
	public void findCipherKeys(){
		String decrypted;
		for (int a = 1; a < 26; a += 2) { // a does not have even number modular multiplicative inverses with mod 26 
			if (a == 13)
				continue;
			decrypted = "";
			for (int b = 1; b <= 26; b++) {
				
				System.out.println("Brute-forcing A value of " + a
						+ " with B value of " + b);
				
				decrypted = "";
				for (int d = 0; d < cipherText.length(); d++) {
					char affine = cipherText.charAt(d);
					int decryptedAffine = decryptLetterAffine(a, b, affine - 'a');
					
					decrypted += (char)(decryptedAffine + 97);
				}
				if (makesSense(decrypted)) {
					setDecryptedText(decrypted);
					setKeyA(a);
					setKeyB(b);
					return;
				}
			}
			
		}
	}
	
	public int findModularInverse(int a) {
		
		int modInverse = 0;
		
		while ((a * modInverse) % 26 != 1)
			modInverse++;
		
		return modInverse;
	}
	
	public int decryptLetterAffine(int a, int b, int affineLetter) {
		
		// int decrypted = ((a * affineLetter) + b) % 26;
		
		int dec = (findModularInverse(a) * (affineLetter - b)) % 26;
		
		if (dec < 0)
			dec += 26;
		
		return dec;
	}

	private boolean makesSense(String text) {
		int count = 0;
		
		for (String word: words) 
			if (text.contains(word))
				count++;
		
		return count > 100;
		
	}
	
	public int affineToASCII(int affine) {
		return affine + 'A';
	}
	
}
