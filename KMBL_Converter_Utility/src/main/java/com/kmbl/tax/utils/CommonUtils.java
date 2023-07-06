package com.kmbl.tax.utils;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.camel.component.file.remote.FtpConfiguration;
import org.apache.camel.component.file.remote.SftpConfiguration;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.kmbl.tax.camel.entity.Audit;

@Component
public class CommonUtils {

	private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

	public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	public static String convertLogDateTimeToString(Date currentDate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return dateFormat.format(currentDate);
	}

	public void fileMovement(FtpConfiguration ftpConfig, String fileName, String fileMoveFolderName,
			SftpConfiguration sftpConfig, String configType, File downlodedFile) throws IOException {

		if (null != fileName && !fileName.isEmpty() && null != fileMoveFolderName && !fileMoveFolderName.isEmpty()) {

			logger.info(" Inside CommonUtils :: fileMovement() method :: File movement to sftp started >>>>  ");

			if (null != configType && CommonConstants.FTP.equalsIgnoreCase(configType)) {
				ftpFileMovent(ftpConfig, fileName, fileMoveFolderName);
			} else if (null != configType && CommonConstants.SFTP.equalsIgnoreCase(configType)) {
				sftpFileMovent(sftpConfig, fileName, fileMoveFolderName, downlodedFile);
			}
			logger.info(
					" Inside CommonUtils :: fileMovement() method :: File movement to sftp Ended to the folder  >>>>  "
							+ fileMoveFolderName);
		}

	}

	private void sftpFileMovent(SftpConfiguration sftpConfig, String fileName, String fileMoveFolderName,
			File downlodedFile) throws IOException {

		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getHost(), sftpConfig.getPort());
			session.setPassword(sftpConfig.getPassword());
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			logger.info(
					" Inside CommonUtils :: sftpFileMovent() method :: Connection done with the SFTP successfully  ");
			channel.put(downlodedFile.toString(), fileMoveFolderName + CommonConstants.FRWD_SLASH + fileName);
			channel.disconnect();
			session.disconnect();
			logger.info(" Inside CommonUtils :: sftpFileMovent() method :: File moved to sftp successfully!!!  ");
		} catch (Exception e) {
			logger.error(
					" Inside CommonUtils :: sftpFileMovent() method :: Exception occurred while SFTP file movement. :: "
							+ e.getMessage());
		}

	}

	private void ftpFileMovent(FtpConfiguration ftpConfig, String fileName, String fileMoveFolderName)
			throws SocketException, IOException {

		FTPClient client = new FTPClient();
		client.connect(ftpConfig.getHost());
		client.login(ftpConfig.getUsername(), ftpConfig.getPassword());
		client.rename(ftpConfig.getDirectoryName() + CommonConstants.FRWD_SLASH + fileName,
				fileMoveFolderName + CommonConstants.FRWD_SLASH + fileName);
		client.logout();
		client.disconnect();

	}

	public void deletefile(String srcDir) throws Exception {

		String srcFilePath = srcDir;
		File srcFile = new File(srcFilePath);
		if (srcFile.exists()) {
			Files.delete(srcFile.toPath());
		}
	}

	public void deletefileFromLocal(boolean flag, File filePath) throws Exception {
		if (flag) {
			Path pathOfFile = Paths.get(filePath.toString());
			try {
				Files.deleteIfExists(pathOfFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean serverFileMovement(String fileRecivedFolderPath, String fileMovementFolderPath, String fileName) {
		boolean flag = false;
		
		
//		  Path sourcePath = Path.of(fileRecivedFolderPath);
//		 Path destinationPath = Path.of(fileMovementFolderPath + "/" + fileName);

		 Path sourcePath = Paths.get(fileRecivedFolderPath);
	     Path destinationPath = Paths.get(fileMovementFolderPath, fileName);
		
		try {

			File folder = new File(fileMovementFolderPath);
			if (!folder.exists()) {
				// Create the folder if it doesn't exist
				boolean folderCreated = folder.mkdirs();
				if (!folderCreated) {
					System.out.println("Failed to create the folder.");
					return false;
				}
			}
			
			Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
			logger.info(
					" Inside CommonUtils :: serverFileMovement() method :: File moved successfully, :: folder path :: "
							+ fileMovementFolderPath);
			flag = true;
		
		} catch (IOException e) {
			logger.error(
					" Inside CommonUtils :: sftpFileMovent() method :: Exception occurred while moving the server file :: "
							+ e.getMessage());
		}
		return flag;
	}

	public void csvFileMoventToSFTP(String username, String password, String host, String fileName,
			String fileMoveFolderName, String downlodedFile) {

		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(username, host, 22);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			logger.info(
					" Inside CommonUtils :: sftpFileMovent() method :: Connection done with the SFTP successfully  ");
		
			channel.put(downlodedFile.toString(), fileMoveFolderName + CommonConstants.FRWD_SLASH + fileName);
			channel.disconnect();
			session.disconnect();
			logger.info(" Inside CommonUtils :: sftpFileMovent() method :: File moved to SFTP successfully!!! :: file Path is :: "+fileMoveFolderName);
		} catch (Exception e) {
			logger.error(
					" Inside CommonUtils :: sftpFileMovent() method :: Exception occurred while SFTP file movement. :: "
							+ e.getMessage());
			
			e.printStackTrace();
		}

	}
	
	public static Audit createAuditObj(int auditLogId,String className, String fileName,int inputRecordCnt, String methodName, int processedRecordCnt, String processDetails,
			String status,String utilityName, String msgId) {
		Audit audit = new Audit();
		audit.setAuditLogId(auditLogId);
		audit.setClassName(className);
		audit.setInputFileName(fileName);
		audit.setInputRecordCount(auditLogId);
		audit.setMethodName(methodName);
		audit.setProcesedRecordCount(auditLogId);
		audit.setProcessStartTime(dateGenerate());
		audit.setStatus(status);
		audit.setUtilityName(utilityName);
		audit.setProcessId(msgId);
		logger.info(
				" Inside CommonUtils :: createAuditObj() method :: Created audit object for saving the audit  ");
		return audit;
	}
	
	public static String dateGenerate() {
		/*
		 * Date currentDate = new Date(); String dateFormatPattern = "yyyy-MM-dd";
		 * SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
         *return dateFormat.format(currentDate); */
		
		LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }

}
