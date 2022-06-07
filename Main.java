package practice_3_hash_function;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import sun.security.x509.UniqueIdentity;

public class Main {
	
	// ������� ������������ ������ ����� � ��������� �����������
	private static long rotateLeft (long value, int distanse) {
		String bValue = String.format("%31s", Long.toBinaryString(value)).replace(' ', '0');
		String rValue = bValue.substring(0, distanse-1);
		String lValue = bValue.substring(distanse-1);
		for (int i = 0; i < lValue.length(); i++) {
			rValue = lValue.substring(i, i + 1) + rValue;
		}
		return Long.parseLong(rValue, 2);
	}
	
	// ������� ����������� GetSHA1Hash ���������� 
	public static String GetSHA1Hash(String message) throws UnsupportedEncodingException {
		
		//�������������� ������� ������ ��������� � �������� ���
		byte [] bytes = message.getBytes();
		String binaryCode = "";

		for (int i = 0; i < bytes.length; i++) {			
			if (bytes[i] < 0) {
				binaryCode = binaryCode + String.format("%8s", Integer.toBinaryString(bytes[i] ^ -1<<8)).replace(' ', '0');
			} else {
				binaryCode = binaryCode + String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0');
			}
		}
		
		// ���������� ������ �������������� ��� � ���� ������
		String hash = "";
		
		// ���������� � ������������� 32-������ �������� � ���� ����������������� ������
		String h0 = "67452301";
		String h1 = "EFCDAB89";
		String h2 = "98BADCFE";
		String h3 = "10325476";
		String h4 = "C3D2E1F0"; 
		
		// ���������� ���������� ��� ���������� �������� ��������
		long a;
		long b;
		long c;
		long d;
		long e;
		long f = 0;
		long k = 0;
		long temp;
		
		// ����� ���������, ���������������� � �������� ���
		int l = binaryCode.length();
		
		// ����������� ������� � ��������� � ���� ��������� ����
		String binarycodeWithOne = binaryCode + "1";
		
		// ��������� �����, �������� 512 �����
		int y = (l+1+64)%512;
		int x = 512 - y;
		
		// ���������� ����� ����������� �����, ������ � 
		for (int i = 1; i <= x; i++) {
			binarycodeWithOne = binarycodeWithOne + "0";		
		}
		
		// ���������� 64 ���� � ����� ������ � �������� �������
		int byteLength = binaryCode.length();
		String s1 = String.format("%64s", Integer.toBinaryString(byteLength)).replace(' ', '0');
		
		// ���������� ������ ������������ ������� ������ � �������� ������� 
		String preprocessingStr = binarycodeWithOne + s1;
		// ��������� ������ ������ ��������� �������� 512 ���
		String[] block512 = new String [preprocessingStr.length()/512];
		// ������ ��� �������� ����� �������� 32 ���
		String w32 = "";
				
		// ���� ��������� ������������ ������� ������ �� ��������� �������� �� 512 ��� ������
	    for (int i = 0; i < block512.length; i++) {
	    	block512[i] = preprocessingStr.substring(i*512, (i+1)*512);
	    	long binaryToDec[] = new long[80];
	    	// ���� ��������� ������ �������� 512 ��� �� ��������� �������� �� 32 ����
	    	for (int j = 0; j < 16; j++) {
		    	w32 = block512[i].substring(j*32, (j+1)*32);
		    	// ������� �������� � �������� ���
		    	for (int t = 31; t >= 0; t--) {
		    		binaryToDec[j] += new Integer(w32.substring(t, t+1))*Math.pow(2, 31-t);		 
		    	}	 
		    }
	    	
	    	
	    	for (int j = 16; j < 80; j++) {
	    		binaryToDec[j] = rotateLeft(binaryToDec[j-3] ^ binaryToDec[j-8] ^ binaryToDec[j-14] ^ binaryToDec[j-16], 1);	
	    	}
	    	
	    	// ������� �������� �� ���������� ������������������ ������� � ������� ������������� ���
	    	long intH0 = Long.parseLong(h0, 16);
			long intH1 = Long.parseLong(h1, 16);
			long intH2 = Long.parseLong(h2, 16);
			long intH3 = Long.parseLong(h3, 16);
			long intH4 = Long.parseLong(h4, 16);
			
			// ������������� a, b, c, d, e ������������ ����������
			a = intH0;
			b = intH1;
			c = intH2;
			d = intH3;
			e = intH4;
			
			// ������������ ������� ���������
			for (int j = 0; j < 80; j++) {
				 if (j <= 19) {
			            f = (b & c) | ((~b) & d);
			            k = Long.parseLong("5A827999", 16);
				 } else if (j <= 39) {
			            f = b ^ c ^ d;
			            k = Long.parseLong("6ED9EBA1", 16);
			     } else if (j <= 59) {
			            f = (b & c) | (b & d) | (c & d);
			            k = Long.parseLong("8F1BBCDC", 16);
			     } else if (j <= 79) {
			            f = b ^ c ^ d;
			            k = Long.parseLong("CA62C1D6", 16); 
			     }
				 
			     temp = rotateLeft(a, 5) + f + e + k + binaryToDec[j];
			     e = d;
			     d = c;
			     c = rotateLeft(b, 30);
			     b = a;
			     a = temp;		     
			}
			// ���������� ������������� �������� ��������
			 intH0 = intH0 + a;
			 intH1 = intH1 + b;
			 intH2 = intH2 + c;
			 intH3 = intH3 + d;
			 intH4 = intH4 + e;

			hash += (intH0 << 128) | (intH1 << 96) | (intH2 << 64) | (intH3 << 32) | intH4;
	    }
	    return hash;
	    
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
	String input = "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq";
	System.out.println(GetSHA1Hash(input));
	
	}

}
