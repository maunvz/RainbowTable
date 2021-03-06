/*
 * This class needs to hold the hash, reduction,
 * and other necessary functions. We will do all the
 * operations on byte[] instead of String because it
 * is faster to do it that way
 */

package com.cs55n.rainbowTable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MathOps {
	int passwordLength;
	MessageDigest digest;
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	Random rand;
	public MathOps(int passwordLength){
		this.passwordLength = passwordLength;
		try{ digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e){}
		rand = new Random();
	}
	//Returns true if arr1 and arr2 have the same bytes
	public static boolean bytesEqual(byte[] arr1, byte[] arr2){
		for(int i=0; i<arr1.length&&i<arr2.length; i++){
			if(arr1[i]!=arr2[i])return false;
		}
		return true;
	}
	//returns a 32-byte hash of bytes
	byte[] hash(byte[] strBytes){
		return digest.digest(strBytes);
	}
	//returns a valid password representation of hash
	/*
	byte[] reduce(byte[] bytes){
		byte[] reduced = new byte[passwordLength];
		int[] holders = new int[passwordLength];
		for(int i=0; i<5; i++)holders[0] += bytes[i]+128;
		for(int i=5; i<10; i++)holders[1] += bytes[i]+128;
		for(int i=10; i<15; i++)holders[2] += bytes[i]+128;
		for(int i=15; i<20; i++)holders[3] += bytes[i]+128;
		for(int i=20; i<26; i++)holders[4] += bytes[i]+128;
		for(int i=26; i<32; i++)holders[5] += bytes[i]+128;
		holders[0] += holders[1]%(4+holders[2]/10);
		holders[2] += holders[3]%(7+holders[3]/2);
		holders[4] += holders[5]%(23+holders[4]/5);
		
		reduced[0] = (byte)((holders[0]%26)+97);
		reduced[1] = (byte)((holders[1]%26)+97);
		reduced[2] = (byte)((holders[2]%26)+97);
		reduced[3] = (byte)((holders[3]%26)+97);
		reduced[4] = (byte)((holders[4]%26)+97);
		reduced[5] = (byte)((holders[5]%26)+97);
		return reduced;
	}*/
	byte[] reduce(byte[] bytes, int k){
		int mask = 1;
		for(int i=0; i<passwordLength; i++)mask*=2;
		mask-=1;
		mask = mask << 32-passwordLength;
		mask = mask | k; //creates an int whose 32-bit representation has at least passwordLength 1's in it
		
		byte[] reduced = new byte[passwordLength];
		int byteNo = 0;
		for(int i=0; i<32&&byteNo<passwordLength; i++){
			if((mask & 1) == 1){
				reduced[byteNo++] = (byte)(97+((bytes[i]+128)%26));
			}
			mask = mask >> 1;
		}
		return reduced;
	}
	public static String hexToString(String hex) {
	    StringBuilder output = new StringBuilder();
	    for (int i = 0; i < hex.length(); i+=2) {
	        String str = hex.substring(i, i+2);
	        output.append((char)Integer.parseInt(str, 16));
	    }
	    return output.toString();
	}	
	//converts bytes to a hex string for output
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	//converts a hex string to a byte array for input
	public static byte[] hexToBytes(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	//byte array to int, where b is 4 long
	public static int bytesToInt(byte[] b){
	    int value = 0;
	    for (int i = 0; i < 4; i++) {
	        int shift = (4 - 1 - i) * 8;
	        value += (b[i] & 0x000000FF) << shift;
	    }
	    return value;
	}
	//returns 1 if b1 is greater, -1 if b2 is greater, 0 is equal
	public static int compareBytes(byte[] b1, byte[] b2){
		for(int i=0; i<b1.length&&i<b2.length;i++){
			if(b1[i]>b2[i])return 1;
			if(b2[i]>b1[i])return -1;
		}
		return 0;
	}
	//copy an array of bytes
	public static byte[] copyBytes(byte[] arr){
		byte[] copy = new byte[arr.length];
		for(int i=0; i<arr.length; i++){
			copy[i]=arr[i];
		}
		return copy;
	}
}