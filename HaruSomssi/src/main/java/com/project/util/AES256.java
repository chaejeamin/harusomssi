package com.project.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key; 
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec; 
import javax.crypto.spec.SecretKeySpec; 

import org.apache.commons.codec.binary.Base64;

public class AES256 {
	/*
	 * @autowired
	 * AES256 aes;
	 * 
	 * 암호화 : aes.encrypt(문자열);
	 * 복호화 : aes.decrypt(문자열);
	 */
	
	
	private static String iv = "00000000000000001";
	private static Key keySpec;
	

	/*
	 * 16자리의 키값을 입력하여 객체를 생성한다.
	 * @param key 암/복호화를 위한 키값
	 * @throws UnsupportedEncodingException 키값의 길이가 16이하일 경우 발생
	 */
	
	
	public AES256() throws UnsupportedEncodingException {
		AES256.iv = iv.substring(0,16);
		byte[] keyBytes = new byte[16];
		byte[] b = iv.getBytes("UTF-8");
		int len = b.length;
		
		if(len> keyBytes.length) {
			len = keyBytes.length;
		}
		
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		AES256.keySpec = keySpec;
	}
	

	/*
	 * AES 256 암호화
	 *  str : 암호화 시킬 문자열
	 */

	
	public static String encrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		String enStr = new String(Base64.encodeBase64(encrypted));
		
		return enStr;
	}
	
	

	/*
	 * AES 256 복호화
	 *  str : 복호화 시킬 문자열
	 */

	public static String decrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		byte[] byteStr = Base64.decodeBase64(str.getBytes()); 
		
		return new String(c.doFinal(byteStr), "UTF-8");
	}
	
	
	

}
