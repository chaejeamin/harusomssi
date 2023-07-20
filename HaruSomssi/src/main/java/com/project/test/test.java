package com.project.test;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import com.project.util.AES256;

public class test {
	public static void main(String[] args) throws NoSuchAlgorithmException, GeneralSecurityException {
		String var = "wpals418";
		
		try {
			AES256 aes = new AES256();
			System.out.println(aes.encrypt("wpals418@naver.com"));
			System.out.println(aes.decrypt("jQiS4RJRWzE36Rgd5lGOKA==")); 
		} catch (UnsupportedEncodingException e) {
			e.toString();
		}
	}
}
