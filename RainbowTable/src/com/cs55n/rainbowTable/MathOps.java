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
	//returns an 8 byte long representation of bytes
	//change this to use K different reduction formulas, one per col
	byte[] reduce(byte[] bytes){
		byte[] reduced = new byte[passwordLength];
		for(int i=0; i<passwordLength; i++){
			reduced[i] = bytes[i];
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
}