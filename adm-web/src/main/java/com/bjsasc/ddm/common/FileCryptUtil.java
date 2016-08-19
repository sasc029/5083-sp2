/*
 * Created on 2015-9-9
 * Author zhangguoqiang
 *
 */
package com.bjsasc.ddm.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import org.apache.commons.codec.binary.Base64;

public class FileCryptUtil{

	final static int  FILE_COPY_CACHE_SIZE =  64 * 1024;
//	// 加密算法/工作模式/填充模式
//	final static String  CIPHER_INSTANCE_STR = "DESede/CBC/PKCS5Padding";
//	// 加密算法名称
//	final static String  CRYPT_METHOD_STR = "DESede";
//	//密钥长度（密钥长度为168。注：因为是DESede所以此长度可以为112或168）
//	final static int  KEY_LENGTH = 168;
//	//IV长度（CBC模式需要IV长度）
//	final static int  IV_LENGTH = 8;
	
	
//	// 加密算法/工作模式/填充模式
//	final static String  CIPHER_INSTANCE_STR = "AES/ECB/PKCS5Padding";
////	final static String  CIPHER_INSTANCE_STR = "AES/ECB/NoPadding";
////	final static String  CIPHER_INSTANCE_STR = "AES/CBC/PKCS5Padding";
////	final static String  CIPHER_INSTANCE_STR = "AES/CBC/NoPadding";
//	// 加密算法名称
//	final static String  CRYPT_METHOD_STR = "AES";
//	//密钥长度
//	final static int  KEY_LENGTH = 128;
//	//IV长度（CBC模式需要IV长度）
//	final static int  IV_LENGTH = 16;
	

	// 加密算法/工作模式/填充模式
//	final static String  CIPHER_INSTANCE_STR = "DES/ECB/NoPadding";
	final static String  CIPHER_INSTANCE_STR = "DES/CBC/PKCS5Padding";
//	final static String  CIPHER_INSTANCE_STR = "DES/ECB/PKCS5Padding";
//	final static String  CIPHER_INSTANCE_STR = "DES/ECB/NoPadding";
//	final static String  CIPHER_INSTANCE_STR = "DES/CBC/NoPadding";
//	// 加密算法名称
	final static String  CRYPT_METHOD_STR = "DES";
//	//密钥长度
	final static int  KEY_LENGTH = 56;
//	//IV长度（CBC模式需要IV长度）
	final static int  IV_LENGTH = 8;
	
	
	
	private static final Logger LOG = Logger
			.getLogger(FileCryptUtil.class);
	
	/**
	 * 进行Base64编码，使用Apache Commons工具而不是sun.misc中的工具，避免非SUN JDK时的问题
	 * 
	 * @param inputBytes
	 * @return
	 */
	public static String encodeBase64(byte[] inputBytes) {
		return Base64.encodeBase64String(inputBytes);
	}

	/**
	 * 进行Base64解码，使用Apache Commons工具而不是sun.misc中的工具，避免非SUN JDK时的问题
	 * 
	 * @param inputString
	 * @return
	 */
	public static byte[] decodeBase64(String inputString) {
		return Base64.decodeBase64(inputString);
	}
	
	/**
	 * 生成密钥Key，并将密钥对象存储到指定密钥文件
	 * 
	 * @param keyFileName（包含路径）
	 * @return
	 * @throws Exception
	 */
	public static void createKeyFileByByte(String keyFileName)
			throws Exception {
		//获取密钥生成器
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance(CRYPT_METHOD_STR);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("createKeyFile生成密钥失败", e);
			throw new RuntimeException("createKeyFile生成密钥失败", e);
		}
		//初始化密钥生成器
		keyGenerator.init(KEY_LENGTH);
		//生成密钥
		Key key = keyGenerator.generateKey();
		
		byte[] keyByte = key.getEncoded();
		
		File file = new File(keyFileName);
		//如果密钥文件存在则删除文件，重新生成
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(keyByte);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("createKeyFileByByte生成密钥文件失败", e);
			throw new RuntimeException("createKeyFileByByte生成密钥文件失败", e);
		} finally {
			try {
				fos.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				LOG.error("createKeyFileByByte生成密钥文件失败", ex);
			}
		}
	}
	
	/**
	 * 生成密钥Key，并将密钥对象存储到指定密钥文件
	 * 
	 * @param keyFileName（包含路径）
	 * @return
	 * @throws Exception
	 */
	public static void createKeyFileByObject(String keyFileName)
			throws Exception {
		//获取密钥生成器
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance(CRYPT_METHOD_STR);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("createKeyFile生成密钥失败", e);
			throw new RuntimeException("createKeyFile生成密钥失败", e);
		}
		//初始化密钥生成器（密钥长度为168。注：因为是DESede所以此长度可以为112或168）
		keyGenerator.init(KEY_LENGTH);
		//生成密钥
		Key key = keyGenerator.generateKey();

		File file = new File(keyFileName);
		//如果密钥文件存在则删除文件，重新生成
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream fos = null;
		ObjectOutputStream obos = null;
		try {
			fos = new FileOutputStream(file);
			obos = new ObjectOutputStream(fos);
			obos.writeObject(key);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("createKeyFileByObject生成密钥文件失败", e);
			throw new RuntimeException("createKeyFileByObject生成密钥文件失败", e);
		} finally {
			try {
				fos.close();
				obos.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				LOG.error("createKeyFileByObject生成密钥文件失败", ex);
			}
		}
	}
	
	/**
	 * 获取密钥Key
	 * 
	 * @param keyFileName（包含路径）
	 * @return Key
	 * @throws Exception
	 */
	public static Key getKeyFromByteKeyFile(String keyFileName)
			throws Exception {
		//蒋转换后的密钥保存到密钥文件中
		File file = new File(keyFileName);
		if (!file.exists()) {
			LOG.error("getKeyFromByteKeyFile密钥文件『"
					+ keyFileName + "』不存在");
			throw new RuntimeException("getKeyFromByteKeyFile密钥文件『"
										+ keyFileName + "』不存在");
		}
		FileInputStream fis = null;
		Key key = null;
		try{
			fis = new FileInputStream(file);
			byte[] KeyBytes = new byte[(int) file.length()];
			fis.read(KeyBytes);
			key = new SecretKeySpec(KeyBytes, CRYPT_METHOD_STR);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error("从文件『"	+ keyFileName 
					+ "』取得密钥内容失败", ex);
			throw new RuntimeException("从文件『"	+ keyFileName 
					+ "』取得密钥内容失败", ex);
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error(e);
			}
		}
		return key;
	}

	/**
	 * 获取密钥Key
	 * 
	 * @param keyFileName（包含路径）
	 * @return Key
	 * @throws Exception
	 */
	public static Key getKeyFromObjectKeyFile(String keyFileName)
			throws Exception {
		//蒋转换后的密钥保存到密钥文件中
		File file = new File(keyFileName);
		if (!file.exists()) {
			LOG.error("getKeyFromObjectKeyFile密钥文件『"
					+ keyFileName + "』不存在");
			throw new RuntimeException("getKeyFromObjectKeyFile密钥文件『"
										+ keyFileName + "』不存在");
		}
		FileInputStream fis = null;
		ObjectInputStream obis = null;
		Key key = null;
		try{
			fis = new FileInputStream(file);
			obis= new ObjectInputStream(fis);
			key = (Key)obis.readObject();

		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error("从文件『"	+ keyFileName 
					+ "』取得密钥内容失败", ex);
			throw new RuntimeException("从文件『"	+ keyFileName 
					+ "』取得密钥内容失败", ex);
		} finally {
			try {
				fis.close();
				obis.close();
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error(e);
			}
		}
		return key;
	}
	
	/**
	 * 对文件流进行加密处理，使用指定的密钥
	 * 
	 * @param key 密钥
	 * @param fos 输出流
	 * @return Cipher 
	 * @throws Exception
	 */
	public static Cipher getEncryptCipher(Key key,
			OutputStream fos) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_STR);
			if(CIPHER_INSTANCE_STR.indexOf("ECB") != -1){
				cipher.init(Cipher.ENCRYPT_MODE, key);
			}else{
				// 输出流中先写入盐
				SecureRandom random = new SecureRandom();
				byte[] iv = new byte[IV_LENGTH];
				random.nextBytes(iv);
				fos.write(iv);
				IvParameterSpec spec = new IvParameterSpec(iv);
				cipher.init(Cipher.ENCRYPT_MODE, key, spec);
			}
			return cipher;
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error("getEncryptCipher获取对应的加密工具失败", ex);
			throw new RuntimeException("getEncryptCipher获取对应的加密工具失败", ex);
		}
	}

	/**
	 * 对文件流进行解密处理，使用指定的密钥
	 * 
	 * @param key 密钥
	 * @param fis 输入流
	 * @return Cipher
	 * @throws Exception
	 */
	public static Cipher getDecryptCipher(Key key,
			InputStream fis) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_STR);
			if(CIPHER_INSTANCE_STR.indexOf("ECB") != -1){
				cipher.init(Cipher.DECRYPT_MODE, key);
			}else{
				byte[] iv = new byte[IV_LENGTH];
				fis.read(iv);
				IvParameterSpec spec = new IvParameterSpec(iv);
				cipher.init(Cipher.DECRYPT_MODE, key, spec);
			}
			return cipher;
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error("getDecryptCipher获取对应的加密工具失败", ex);
			throw new RuntimeException("getDecryptCipher获取对应的加密工具失败", ex);
		}
	}
	
	
	/**
	 * 获取指定输出流的加密后的输出流
	 * 
	 * @param key 密钥
	 * @param fos 输出流
	 * @return OutputStream
	 * @throws Exception
	 */
	public static OutputStream encryptFileOutputStream(Key key,
			FileOutputStream fos) throws Exception {
		try {
			//算法/工作模式/填充模式
			Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_STR);

			if(CIPHER_INSTANCE_STR.indexOf("ECB") != -1){
				cipher.init(Cipher.ENCRYPT_MODE, key);
			}else{
				SecureRandom random = new SecureRandom();
				byte[] iv = new byte[IV_LENGTH];
				random.nextBytes(iv);

				fos.write(iv);
				IvParameterSpec spec = new IvParameterSpec(iv);

				cipher.init(Cipher.ENCRYPT_MODE, key, spec);
				
			}

			CipherOutputStream cos = new CipherOutputStream(fos, cipher);

			return cos;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("加密输出流失败", e);
			throw new RuntimeException("加密输出流失败", e);
		}
	}
	
	/**
	 * 获取指定输入流的解密后的输入流
	 * 
	 * @param key 密钥
	 * @param fos 输出流
	 * @return OutputStream
	 * @throws Exception
	 */
	public static InputStream decryptFileOutputStream(Key key,
			FileInputStream fis) throws Exception {
		try {
			//算法/工作模式/填充模式
			Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_STR);

			if(CIPHER_INSTANCE_STR.indexOf("ECB") != -1){
				cipher.init(Cipher.DECRYPT_MODE, key);
			}else{
				byte[] iv = new byte[IV_LENGTH];
	
				fis.read(iv);
				IvParameterSpec spec = new IvParameterSpec(iv);
	
				cipher.init(Cipher.DECRYPT_MODE, key, spec);
			}
			CipherInputStream cis = new CipherInputStream(fis, cipher);

			return cis;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("解密输出流失败", e);
			throw new RuntimeException("解密输出流失败", e);
		}
	}
	

	/**
	 * 对文件进行加密处理，使用指定的密钥
	 * 
	 * @param key
	 *            ，密钥
	 * @param srcFile
	 *            ，需要加密的文件
	 * @param encFileName
	 *            ，加密后的文件（含绝对路径）
	 * @return true/false
	 * @throws Exception
	 */
	public static boolean encryptFile(Key key, File srcFile,
			String encFileName) throws Exception {
		// 判断输入输出文件文件是否相同，相同时可能导致死循环
		if (encFileName.equals(srcFile.getAbsolutePath())) {
			LOG.error("将文件『" + srcFile.getAbsolutePath() + "』加密存储到『"
					+ encFileName + "』失败，因为两个文件实际上是同一个文件");
			throw new RuntimeException("将文件『" + srcFile.getAbsolutePath() + "』加密存储到『"
					+ encFileName + "』失败，因为两个文件实际上是同一个文件");
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		CipherOutputStream cos = null;
		try {


			Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_STR);
			// 文件输入输出流
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(encFileName);

			if(CIPHER_INSTANCE_STR.indexOf("ECB") != -1){
				cipher.init(Cipher.ENCRYPT_MODE, key);
			}else{
				SecureRandom random = new SecureRandom();
				byte[] iv = new byte[IV_LENGTH];
				random.nextBytes(iv);
				fos.write(iv);
				IvParameterSpec spec = new IvParameterSpec(iv);
				cipher.init(Cipher.ENCRYPT_MODE, key, spec);
			}
			
			cos = new CipherOutputStream(fos, cipher);

			byte[] buff = new byte[FILE_COPY_CACHE_SIZE];
			int bytesRead = 0;
			while (-1 != (bytesRead = fis.read(buff, 0,
					FILE_COPY_CACHE_SIZE))) {
				cos.write(buff, 0, bytesRead);
			}

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error("将文件『"
					+ srcFile.getAbsolutePath() + "』加密存储为『" + encFileName
					+ "』失败", ex);
			throw new RuntimeException("将文件『"
					+ srcFile.getAbsolutePath() + "』加密存储为『" + encFileName
					+ "』失败", ex);
		} finally {
			try {
				fis.close();
				cos.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error(e);
			}
		}
	}

	
	/**
	 * 将指定文件以明文方式保存到Path指定的路径中，如果源文件加密，
	 * 将解密存储 用现显示锁锁定该文件的读锁
	 * 
	 * @param key
	 *            ，密钥
	 * @param srcFile
	 *            ，需要解密的文件
	 * @param decFileName
	 *            ，解密后的明文文件名（含绝对路径）
	 * @return true/false
	 * @throws Exception
	 */
	public static boolean decryptFile(Key key, File srcFile,
			String decFileName) throws Exception {
		FileInputStream fis = null;
		CipherInputStream cis = null;
		FileOutputStream fos = null;
		try {
			// 文件输入输出流
			fis = new FileInputStream(srcFile);
			File dst = new File(decFileName);
			if (!dst.exists()) {
				dst.createNewFile();
			}

			// 判断输入输出文件文件是否相同，相同时可能导致死循环
			if (decFileName.equals(srcFile.getAbsolutePath())) {
				LOG.error("将文件『" + srcFile.getAbsolutePath() + "』解密存储到『"
						+ decFileName + "』失败，因为两个文件实际上是同一个文件");
				throw new RuntimeException("将文件『" + srcFile.getAbsolutePath() + "』解密存储到『"
						+ decFileName + "』失败，因为两个文件实际上是同一个文件");
			}

			fos = new FileOutputStream(decFileName);
			Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_STR);
			if(CIPHER_INSTANCE_STR.indexOf("ECB") != -1){
				cipher.init(Cipher.DECRYPT_MODE, key);
			}else{
				byte[] iv = new byte[IV_LENGTH];
				fis.read(iv);
				IvParameterSpec spec = new IvParameterSpec(iv);
				cipher.init(Cipher.DECRYPT_MODE, key, spec);

			}
			cis = new CipherInputStream(fis, cipher);
			byte[] buff = new byte[FILE_COPY_CACHE_SIZE];
			int bytesRead = 0;
			while (-1 != (bytesRead = cis.read(buff, 0,
					FILE_COPY_CACHE_SIZE))) {
				fos.write(buff, 0, bytesRead);
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error("将文件『"
					+ srcFile.getAbsolutePath() + "』解密存储到『" + decFileName
					+ "』失败", ex);
			throw new RuntimeException("将文件『"
					+ srcFile.getAbsolutePath() + "』解密存储到『" + decFileName
					+ "』失败", ex);
		} finally {
			try {
				fis.close();
				cis.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error(e);
			}
		}
	}
	/**
	 * 测试用程序
	 * @param args
	 */
	public static void main(String[] args) {
//		//生成密钥文件
//		createKeyFile();
//		//加密解密文件
//		testEncryptDecryptFile();
	}
	private static void createKeyFile() {

		System.out.println("createKeyFile Start！！！");
		
		try {
//			createKeyFileByObject("D:/key.dat");
			createKeyFileByByte("D:/test/key.dat");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("createKeyFile End！！！");
	}

	private static void testEncryptDecryptFile() {

		System.out.println("testEncryptDecryptFile Start！！！");
		
		Key key = null;
		try {
//			//取得加密解密密钥（Object）
//			key = getKeyFromObjectKeyFile("D:/key.dat");
			//取得加密解密密钥（byte）
			key = getKeyFromByteKeyFile("D:/test/key.dat");
			
//			//加密文件
//			File testFile = new File("D:/test/a.txt");
//			encryptFile(key,testFile,"D:/test/am.txt");
//			
//			//解密文件
//			File testFilem = new File("D:/test/am.txt");
//			decryptFile(key,testFilem,"D:/test/amb.txt");
			
			//加密文件
			File testFile = new File("D:/test/aes.docx");
			encryptFile(key,testFile,"D:/test/aes_encrypt.docx");
			
			//解密文件
			File testFilem = new File("D:/test/aes_encrypt.docx");
			decryptFile(key,testFilem,"D:/test/aes_decrypt.docx");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("testEncryptDecryptFile End！！！");
	}
}