import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;

public class FileProcessing {
	
	static Scanner sampleWordScanner, cipherScanner;
	
	ArrayList<String> words = new ArrayList<>();
	String cipherText = "";
	int affineCipherOfE, affineCipherOfT;
	int occurenceOfE, occurenceOfT;
	int keyA, keyB;
	
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
		affineCipherOfE = (char)maxAt - 97;
		affineCipherOfT = (char)secondMaxAt - 97;
		
		
		System.out.println("\nMaximum occurence: " +(char)maxAt);
		System.out.println("Second maximum occurrence: " +(char)secondMaxAt);
		
		System.out.println("\nWe can conclude that within this ciphertext, the letter "
				+ (char)maxAt + " corresponds to the letter E, and the letter "
				+ (char)secondMaxAt + " corresponds to the letter T\n");
	}
	
	public String affineDecrypt(boolean bruteForce){
		mainloop: for (int a = 1; a < 26; a += 2) { // a does not have even number modular multiplicative inverses with mod 26 
			if (a == 13)
				continue;
			for (int b = 1; b <= 26; b++) {
				
				boolean ESatisfied = false, TSatisfied = false;
				String decrypted = "";
				
				if (bruteForce)
					System.out.println("Brute-forcing A value of " + a
							+ " with B value of " + b);
				
				
				for (int d = 0; d < cipherText.length(); d++) {
					int affine = cipherText.charAt(d) - 'a';
					int decryptedAffine = decryptLetterAffine(a, b, affine);
					
					if (bruteForce) {
						decrypted += (char)(decryptedAffine + 97);
					}
					else {
						if (affine == affineCipherOfE && decryptedAffine == 'e' - 97)
							ESatisfied = true;
						else if (affine == affineCipherOfT && decryptedAffine == 't' - 97)
							TSatisfied = true;
							
						if (ESatisfied && TSatisfied) {
							setKeyA(a);
							setKeyB(b);
							return decryptTextWithKeys(cipherText, a, b);
						}
					}
				}
				if (bruteForce && makesSense(decrypted)) {
					setKeyA(a);
					setKeyB(b);
					return decrypted;
				}
				
			}
			
		}
		return null;
	}
	
	private String decryptTextWithKeys(String text, int a, int b) {
		String decrypted = "";
		for (int i = 0; i < text.length(); i++) {
			int affine = text.charAt(i) - 'a';
			int decryptedAffine = decryptLetterAffine(a, b, affine);
			
			decrypted += (char)(decryptedAffine + 97);
		}
		return decrypted;
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
