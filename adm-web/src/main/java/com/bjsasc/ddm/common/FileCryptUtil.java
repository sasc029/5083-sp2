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
//	// �����㷨/����ģʽ/���ģʽ
//	final static String  CIPHER_INSTANCE_STR = "DESede/CBC/PKCS5Padding";
//	// �����㷨����
//	final static String  CRYPT_METHOD_STR = "DESede";
//	//��Կ���ȣ���Կ����Ϊ168��ע����Ϊ��DESede���Դ˳��ȿ���Ϊ112��168��
//	final static int  KEY_LENGTH = 168;
//	//IV���ȣ�CBCģʽ��ҪIV���ȣ�
//	final static int  IV_LENGTH = 8;
	
	
//	// �����㷨/����ģʽ/���ģʽ
//	final static String  CIPHER_INSTANCE_STR = "AES/ECB/PKCS5Padding";
////	final static String  CIPHER_INSTANCE_STR = "AES/ECB/NoPadding";
////	final static String  CIPHER_INSTANCE_STR = "AES/CBC/PKCS5Padding";
////	final static String  CIPHER_INSTANCE_STR = "AES/CBC/NoPadding";
//	// �����㷨����
//	final static String  CRYPT_METHOD_STR = "AES";
//	//��Կ����
//	final static int  KEY_LENGTH = 128;
//	//IV���ȣ�CBCģʽ��ҪIV���ȣ�
//	final static int  IV_LENGTH = 16;
	

	// �����㷨/����ģʽ/���ģʽ
//	final static String  CIPHER_INSTANCE_STR = "DES/ECB/NoPadding";
	final static String  CIPHER_INSTANCE_STR = "DES/CBC/PKCS5Padding";
//	final static String  CIPHER_INSTANCE_STR = "DES/ECB/PKCS5Padding";
//	final static String  CIPHER_INSTANCE_STR = "DES/ECB/NoPadding";
//	final static String  CIPHER_INSTANCE_STR = "DES/CBC/NoPadding";
//	// �����㷨����
	final static String  CRYPT_METHOD_STR = "DES";
//	//��Կ����
	final static int  KEY_LENGTH = 56;
//	//IV���ȣ�CBCģʽ��ҪIV���ȣ�
	final static int  IV_LENGTH = 8;
	
	
	
	private static final Logger LOG = Logger
			.getLogger(FileCryptUtil.class);
	
	/**
	 * ����Base64���룬ʹ��Apache Commons���߶�����sun.misc�еĹ��ߣ������SUN JDKʱ������
	 * 
	 * @param inputBytes
	 * @return
	 */
	public static String encodeBase64(byte[] inputBytes) {
		return Base64.encodeBase64String(inputBytes);
	}

	/**
	 * ����Base64���룬ʹ��Apache Commons���߶�����sun.misc�еĹ��ߣ������SUN JDKʱ������
	 * 
	 * @param inputString
	 * @return
	 */
	public static byte[] decodeBase64(String inputString) {
		return Base64.decodeBase64(inputString);
	}
	
	/**
	 * ������ԿKey��������Կ����洢��ָ����Կ�ļ�
	 * 
	 * @param keyFileName������·����
	 * @return
	 * @throws Exception
	 */
	public static void createKeyFileByByte(String keyFileName)
			throws Exception {
		//��ȡ��Կ������
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance(CRYPT_METHOD_STR);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("createKeyFile������Կʧ��", e);
			throw new RuntimeException("createKeyFile������Կʧ��", e);
		}
		//��ʼ����Կ������
		keyGenerator.init(KEY_LENGTH);
		//������Կ
		Key key = keyGenerator.generateKey();
		
		byte[] keyByte = key.getEncoded();
		
		File file = new File(keyFileName);
		//�����Կ�ļ�������ɾ���ļ�����������
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(keyByte);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("createKeyFileByByte������Կ�ļ�ʧ��", e);
			throw new RuntimeException("createKeyFileByByte������Կ�ļ�ʧ��", e);
		} finally {
			try {
				fos.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				LOG.error("createKeyFileByByte������Կ�ļ�ʧ��", ex);
			}
		}
	}
	
	/**
	 * ������ԿKey��������Կ����洢��ָ����Կ�ļ�
	 * 
	 * @param keyFileName������·����
	 * @return
	 * @throws Exception
	 */
	public static void createKeyFileByObject(String keyFileName)
			throws Exception {
		//��ȡ��Կ������
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance(CRYPT_METHOD_STR);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("createKeyFile������Կʧ��", e);
			throw new RuntimeException("createKeyFile������Կʧ��", e);
		}
		//��ʼ����Կ����������Կ����Ϊ168��ע����Ϊ��DESede���Դ˳��ȿ���Ϊ112��168��
		keyGenerator.init(KEY_LENGTH);
		//������Կ
		Key key = keyGenerator.generateKey();

		File file = new File(keyFileName);
		//�����Կ�ļ�������ɾ���ļ�����������
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
			LOG.error("createKeyFileByObject������Կ�ļ�ʧ��", e);
			throw new RuntimeException("createKeyFileByObject������Կ�ļ�ʧ��", e);
		} finally {
			try {
				fos.close();
				obos.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				LOG.error("createKeyFileByObject������Կ�ļ�ʧ��", ex);
			}
		}
	}
	
	/**
	 * ��ȡ��ԿKey
	 * 
	 * @param keyFileName������·����
	 * @return Key
	 * @throws Exception
	 */
	public static Key getKeyFromByteKeyFile(String keyFileName)
			throws Exception {
		//��ת�������Կ���浽��Կ�ļ���
		File file = new File(keyFileName);
		if (!file.exists()) {
			LOG.error("getKeyFromByteKeyFile��Կ�ļ���"
					+ keyFileName + "��������");
			throw new RuntimeException("getKeyFromByteKeyFile��Կ�ļ���"
										+ keyFileName + "��������");
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
			LOG.error("���ļ���"	+ keyFileName 
					+ "��ȡ����Կ����ʧ��", ex);
			throw new RuntimeException("���ļ���"	+ keyFileName 
					+ "��ȡ����Կ����ʧ��", ex);
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
	 * ��ȡ��ԿKey
	 * 
	 * @param keyFileName������·����
	 * @return Key
	 * @throws Exception
	 */
	public static Key getKeyFromObjectKeyFile(String keyFileName)
			throws Exception {
		//��ת�������Կ���浽��Կ�ļ���
		File file = new File(keyFileName);
		if (!file.exists()) {
			LOG.error("getKeyFromObjectKeyFile��Կ�ļ���"
					+ keyFileName + "��������");
			throw new RuntimeException("getKeyFromObjectKeyFile��Կ�ļ���"
										+ keyFileName + "��������");
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
			LOG.error("���ļ���"	+ keyFileName 
					+ "��ȡ����Կ����ʧ��", ex);
			throw new RuntimeException("���ļ���"	+ keyFileName 
					+ "��ȡ����Կ����ʧ��", ex);
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
	 * ���ļ������м��ܴ���ʹ��ָ������Կ
	 * 
	 * @param key ��Կ
	 * @param fos �����
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
				// ���������д����
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
			LOG.error("getEncryptCipher��ȡ��Ӧ�ļ��ܹ���ʧ��", ex);
			throw new RuntimeException("getEncryptCipher��ȡ��Ӧ�ļ��ܹ���ʧ��", ex);
		}
	}

	/**
	 * ���ļ������н��ܴ���ʹ��ָ������Կ
	 * 
	 * @param key ��Կ
	 * @param fis ������
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
			LOG.error("getDecryptCipher��ȡ��Ӧ�ļ��ܹ���ʧ��", ex);
			throw new RuntimeException("getDecryptCipher��ȡ��Ӧ�ļ��ܹ���ʧ��", ex);
		}
	}
	
	
	/**
	 * ��ȡָ��������ļ��ܺ�������
	 * 
	 * @param key ��Կ
	 * @param fos �����
	 * @return OutputStream
	 * @throws Exception
	 */
	public static OutputStream encryptFileOutputStream(Key key,
			FileOutputStream fos) throws Exception {
		try {
			//�㷨/����ģʽ/���ģʽ
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
			LOG.error("���������ʧ��", e);
			throw new RuntimeException("���������ʧ��", e);
		}
	}
	
	/**
	 * ��ȡָ���������Ľ��ܺ��������
	 * 
	 * @param key ��Կ
	 * @param fos �����
	 * @return OutputStream
	 * @throws Exception
	 */
	public static InputStream decryptFileOutputStream(Key key,
			FileInputStream fis) throws Exception {
		try {
			//�㷨/����ģʽ/���ģʽ
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
			LOG.error("���������ʧ��", e);
			throw new RuntimeException("���������ʧ��", e);
		}
	}
	

	/**
	 * ���ļ����м��ܴ���ʹ��ָ������Կ
	 * 
	 * @param key
	 *            ����Կ
	 * @param srcFile
	 *            ����Ҫ���ܵ��ļ�
	 * @param encFileName
	 *            �����ܺ���ļ���������·����
	 * @return true/false
	 * @throws Exception
	 */
	public static boolean encryptFile(Key key, File srcFile,
			String encFileName) throws Exception {
		// �ж���������ļ��ļ��Ƿ���ͬ����ͬʱ���ܵ�����ѭ��
		if (encFileName.equals(srcFile.getAbsolutePath())) {
			LOG.error("���ļ���" + srcFile.getAbsolutePath() + "�����ܴ洢����"
					+ encFileName + "��ʧ�ܣ���Ϊ�����ļ�ʵ������ͬһ���ļ�");
			throw new RuntimeException("���ļ���" + srcFile.getAbsolutePath() + "�����ܴ洢����"
					+ encFileName + "��ʧ�ܣ���Ϊ�����ļ�ʵ������ͬһ���ļ�");
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		CipherOutputStream cos = null;
		try {


			Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_STR);
			// �ļ����������
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
			LOG.error("���ļ���"
					+ srcFile.getAbsolutePath() + "�����ܴ洢Ϊ��" + encFileName
					+ "��ʧ��", ex);
			throw new RuntimeException("���ļ���"
					+ srcFile.getAbsolutePath() + "�����ܴ洢Ϊ��" + encFileName
					+ "��ʧ��", ex);
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
	 * ��ָ���ļ������ķ�ʽ���浽Pathָ����·���У����Դ�ļ����ܣ�
	 * �����ܴ洢 ������ʾ���������ļ��Ķ���
	 * 
	 * @param key
	 *            ����Կ
	 * @param srcFile
	 *            ����Ҫ���ܵ��ļ�
	 * @param decFileName
	 *            �����ܺ�������ļ�����������·����
	 * @return true/false
	 * @throws Exception
	 */
	public static boolean decryptFile(Key key, File srcFile,
			String decFileName) throws Exception {
		FileInputStream fis = null;
		CipherInputStream cis = null;
		FileOutputStream fos = null;
		try {
			// �ļ����������
			fis = new FileInputStream(srcFile);
			File dst = new File(decFileName);
			if (!dst.exists()) {
				dst.createNewFile();
			}

			// �ж���������ļ��ļ��Ƿ���ͬ����ͬʱ���ܵ�����ѭ��
			if (decFileName.equals(srcFile.getAbsolutePath())) {
				LOG.error("���ļ���" + srcFile.getAbsolutePath() + "�����ܴ洢����"
						+ decFileName + "��ʧ�ܣ���Ϊ�����ļ�ʵ������ͬһ���ļ�");
				throw new RuntimeException("���ļ���" + srcFile.getAbsolutePath() + "�����ܴ洢����"
						+ decFileName + "��ʧ�ܣ���Ϊ�����ļ�ʵ������ͬһ���ļ�");
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
			LOG.error("���ļ���"
					+ srcFile.getAbsolutePath() + "�����ܴ洢����" + decFileName
					+ "��ʧ��", ex);
			throw new RuntimeException("���ļ���"
					+ srcFile.getAbsolutePath() + "�����ܴ洢����" + decFileName
					+ "��ʧ��", ex);
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
	 * �����ó���
	 * @param args
	 */
	public static void main(String[] args) {
//		//������Կ�ļ�
//		createKeyFile();
//		//���ܽ����ļ�
//		testEncryptDecryptFile();
	}
	private static void createKeyFile() {

		System.out.println("createKeyFile Start������");
		
		try {
//			createKeyFileByObject("D:/key.dat");
			createKeyFileByByte("D:/test/key.dat");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("createKeyFile End������");
	}

	private static void testEncryptDecryptFile() {

		System.out.println("testEncryptDecryptFile Start������");
		
		Key key = null;
		try {
//			//ȡ�ü��ܽ�����Կ��Object��
//			key = getKeyFromObjectKeyFile("D:/key.dat");
			//ȡ�ü��ܽ�����Կ��byte��
			key = getKeyFromByteKeyFile("D:/test/key.dat");
			
//			//�����ļ�
//			File testFile = new File("D:/test/a.txt");
//			encryptFile(key,testFile,"D:/test/am.txt");
//			
//			//�����ļ�
//			File testFilem = new File("D:/test/am.txt");
//			decryptFile(key,testFilem,"D:/test/amb.txt");
			
			//�����ļ�
			File testFile = new File("D:/test/aes.docx");
			encryptFile(key,testFile,"D:/test/aes_encrypt.docx");
			
			//�����ļ�
			File testFilem = new File("D:/test/aes_encrypt.docx");
			decryptFile(key,testFilem,"D:/test/aes_decrypt.docx");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("testEncryptDecryptFile End������");
	}
}