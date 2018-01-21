package org.mycoin.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class Helper {
	static Logger logger = Logger.getLogger(Helper.class);

	public static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public static byte[] getHash(String string) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			final byte[] encodedHash = digest.digest(string.getBytes(StandardCharsets.UTF_8));
			return encodedHash;
		} catch (NoSuchAlgorithmException e) {
			logger.error("NoSuchAlgorithmException caught", e);
			return null;
		}
	}
	
	public static String hash(String string) {
		return bytesToHex(getHash(string));
	}
	
	public static void main(String[] args) {
		String str = "oqKWEJoijfwroij875983475khkfhkgqhrkmvhrgopihgwqopehfwqoeihfhvIHIJHKJhokjh93847593irhgf9834hfg";
		for (int i = 0; i < 10; i++) {
			String hash = bytesToHex(getHash(str));
			logger.info("[" + i + "]: " + hash);
		}
	}
    
}
