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
	char[] charset = {'a','b','c','d','e','f'};
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
	//Generates a random passwordLength-long password using the characters in charset 
	byte[] randomPassword(){
		String pass="";
		for(int i=0; i<passwordLength; i++){
			pass+=charset[rand.nextInt(charset.length)];
		}
		return pass.getBytes();
	}
	//returns a 32-byte hash of bytes
	byte[] hash(byte[] strBytes){
		return digest.digest(strBytes);
	}
	//returns a passwordLength byte long representation of bytes, using k to choose which bytes are used
	//works for k up to (2^passwordLength+1)-2. After that, every 2^n-1 where n is greater than passwordLength will
	//be the same as all the other 2^n-1's where n is greater than passwordLength
	//currently max k is 510 when passwordLength is 8
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
				reduced[byteNo++] = bytes[i];
			}
			mask = mask >> 1;
		}
		return reduced;
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
}