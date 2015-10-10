package com.sohu.focus.framework.util;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;

import android.util.Base64;


/**
 * Copyright (C), nanyang205380@sohu-inc.com.
 * 
 * @ClassName: RSAUtils
 * @Description:RSA加密解密
 * @date 2015年1月28日 上午9:36:30
 */
public final class RSAUtils {

    static {
        System.loadLibrary("focus");
		try {
			keys = loadKey();
		} catch (Exception e) {
		}
    }
    
    public static native String  getPublicKey();
    
	/** 加密算法 */
	public final static String KEY_ALGORITHM = "RSA";
	/** 获取公钥 */
	public final static String PUBLIC_KEY = "RSAPublicKey";
	/** 获取私钥 */
	public final static String PRIVATE_KEY = "RSAPrivateKey";
	/** RSA最大加密明文大小 */
	public final static int MAX_ENCRYPT_BLOCK = 117;
	/** RSA最大解密密文大小 */
	public final static int MAX_DECRYPT_BLOCK = 128;
	/** RSA生成密钥的初始化大小 */
	public final static int MAX_KEY_SIZE = 1024;
	/** 后缀名*/
	private final static String SUFFIX = ".xml";
	
	/** 密钥对 */
	private static Map<String, String> keys = new HashMap<String, String>();
	
	/**
	 * @author: YangNan(杨楠)
	 * @date: 2015年1月28日 上午9:44:17
	 * @Title: genKeyPair
	 * @Description: 生成密钥对,公钥和私钥
	 * @throws:
	 */
	public static Map<String, Object> genKeyPair() throws Exception {

		KeyPairGenerator keyPairGenerator = KeyPairGenerator
				.getInstance(KEY_ALGORITHM);
		keyPairGenerator.initialize(MAX_KEY_SIZE, new SecureRandom());
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		// 公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		// 私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PUBLIC_KEY, publicKey);
		map.put(PRIVATE_KEY, privateKey);

		return map;
	}

	/**
	 * @author: YangNan(杨楠)
	 * @date: 2015年1月28日 上午9:56:18
	 * @Title: decrypt
	 * @Description: 私钥解密
	 * @param encryptedData
	 *            待解密的加密数据
	 * @param privateKey
	 *            私钥(BASE64编码)
	 * @throws:
	 */
	public static byte[] decrypt(byte[] encryptedData)
			throws Exception {
		byte[] keyBytes = Base64.decode(getPrivateKey(), Base64.DEFAULT);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		// 获取私钥
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

		// 对数据解密
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);

		int length = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 分段解密,解密最大值为128
		while (length - offSet > 0) {
			if (length - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher
						.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, length - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}

		byte[] decryptedData = out.toByteArray();
		out.close();

		return decryptedData;
	}

	/**
	 * @author: YangNan(杨楠)
	 * @date: 2015年1月28日 上午10:26:46
	 * @Title: encrypt
	 * @Description: 公钥加密
	 * @param data
	 *            待加密的数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @throws:
	 */
	public static byte[] encrypt(byte[] data)
			throws Exception {
		byte[] keyBytes = Base64.decode(getPublicKey(),Base64.DEFAULT);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		// 获取公钥
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(keySpec);

		// 对数据加密
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		int length = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 分段加密,加密的最大值为117
		while (length - offSet > 0) {
			if (length - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, length - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}

		byte[] encryptedData = out.toByteArray();
		out.close();

		return encryptedData;
	}

	/**
	 * @author: YangNan(杨楠)
	 * @date: 2015年1月28日 上午10:34:04
	 * @Title: getPrivateKey
	 * @Description: 获取私钥,返回base64编码的字符串
	 * @param keyMap
	 */
	public static String getPrivateKey(Map<String, Object> keyMap) {
		RSAPrivateKey privateKey = (RSAPrivateKey) keyMap.get(PRIVATE_KEY);
		return Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT);
	}
	
	/**
	 * @author: YangNan(杨楠)
	 * @date: 2015年1月28日 上午10:34:04
	 * @Title: getPrivateKey
	 * @Description: 获取私钥,返回base64编码的字符串
	 * @param keyMap
	 */
	public static String getPrivateKey() {
		return "";
	}

	/**
	 * @author: YangNan(杨楠)
	 * @date: 2015年1月28日 上午10:34:04
	 * @Title: getPrivateKey
	 * @Description: 获取私钥 返回base64编码的字符串
	 * @param keyMap
	 */
	public static String getPublicKey(Map<String, Object> keyMap) {
		RSAPublicKey publicKey = (RSAPublicKey) keyMap.get(PUBLIC_KEY);
		return Base64.encodeToString(publicKey.getEncoded(),Base64.DEFAULT);
	}
	
	/**
	 * @author: YangNan(杨楠)
	 * @date: 2015年1月28日 上午10:34:04
	 * @Title: getPrivateKey
	 * @Description: 获取私钥 返回base64编码的字符串
	 * @param keyMap
	 */
	public static String getPublicKey2() {
		return "";
	}

	/**
	 * @author: YangNan(杨楠)
	 * @date: 2015年1月28日 上午11:10:42
	 * @Title: saveKey
	 * @Description: 保存密钥对
	 * @throws:
	 */
//	public static void saveKey() throws Exception {
//		Map<String, Object> keyMap = genKeyPair();
//		String privateKey = getPrivateKey(keyMap);
//		String publicKey = getPublicKey(keyMap);
//		String filePath = RSAUtils.class.getResource("/").toURI().getPath();
//		filePath = filePath.replace("target/classes/", "src/main/java/rsa/");
//		// 保存私钥
//		XMLConfiguration privateConfig = new XMLConfiguration();
//		privateConfig.setProperty(PRIVATE_KEY, privateKey);
//		privateConfig.save(filePath + PRIVATE_KEY + SUFFIX);
//
//		// 保存公钥
//		XMLConfiguration publicConfig = new XMLConfiguration();
//		publicConfig.setProperty(PUBLIC_KEY, publicKey);
//		publicConfig.save(filePath + PUBLIC_KEY + SUFFIX);
//
//	}
	
	/**
	 * @author: YangNan(杨楠)  
	 * @date: 2015年1月28日 上午11:50:01 
	 * @Title: loadKey   
	 * @Description: 从文件中获取密钥对   
	 * @throws:
	 */
	public static Map<String, String> loadKey() throws Exception {
		
		Map<String, String> key = new HashMap<String, String>();
//		//String filePath = RSAUtils.class.getClassLoader().getResource("").toURI().getPath();
//		String filePath  = "rsa/";
//		// 获取私钥
//		XMLConfiguration privateConfig = new XMLConfiguration(filePath + PRIVATE_KEY + SUFFIX);
//		key.put(PRIVATE_KEY, privateConfig.getString(PRIVATE_KEY));
//
//		// 获取公钥
//		XMLConfiguration publicConfig = new XMLConfiguration(filePath + PUBLIC_KEY + SUFFIX);
//		key.put(PUBLIC_KEY, publicConfig.getString(PUBLIC_KEY));
		
		return key;
	}

	public static void main(String[] args) throws Exception {

		 String privateKey = getPrivateKey();
		 String publicKey = getPublicKey();
		
		 System.out.println("公钥:" + publicKey);
		 System.out.println("私钥:" + privateKey);
		
		 
//		 long start = System.currentTimeMillis();
//		 byte[] encryptData = encrypt(data);
//		 System.out.println("耗时:"+(System.currentTimeMillis() - start));
//		 String encode = Base64.encodeBase64String(encryptData);
//		 System.out.println("加密数据:"+encode);
//		 start = System.currentTimeMillis();
//		 //解密
//		 byte[] decryptData = decrypt(Base64.decodeBase64(encode));
//		 System.out.println("耗时:"+(System.currentTimeMillis() - start));
//		 System.out.println("解密数据:"+new String(decryptData));
		 
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(15, 20, 100L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory());
//		for(int i=0; i<= 10000 ;++i) {
//			threadPool.execute(new Runnable() {
//				public void run() {
//					long start = System.currentTimeMillis();
//					 byte[] encryptData = null;
//					try {
//						encryptData = encrypt(data);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
////					 System.out.println("加密耗时:"+(System.currentTimeMillis() - start));
//					 String encode = Base64.encodeBase64String(encryptData);
//					 start = System.currentTimeMillis();
//					 //解密
//					 try {
//						byte[] decryptData = decrypt(Base64.decodeBase64(encode));
//						long count = System.currentTimeMillis() - start;
//						 System.out.println("解密耗时:"+ count);
//						 if(count >= sum) { 
//							 sum = count; 
//							 System.out.println("解密最大值:"+sum);
//						 }
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
//		}
		 
	}
	
	private static long sum = 0;
	
	
}
