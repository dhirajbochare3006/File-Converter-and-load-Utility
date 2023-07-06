package com.kmbl.tax.camel.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.camel.component.file.remote.FtpConfiguration;
import org.apache.camel.component.file.remote.SftpConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kmbl.tax.camel.entity.Audit;
import com.kmbl.tax.camel.repository.UtilityAudit;
import com.kmbl.tax.controller.ConverterUtilityController;
import com.kmbl.tax.utils.CommonConstants;
import com.kmbl.tax.utils.CommonUtils;

@Service
public class CamelServiceImpl implements CamelService {

	private static final Logger logger = LoggerFactory.getLogger(CamelServiceImpl.class);

	final String className = this.getClass().toString();
	static CommonUtils utils = new CommonUtils();

	@Override
	public boolean processSFTPPollerFiles(File file, String inboxFolder, String fileDownloadPath,
			FtpConfiguration ftpConfig, String successFolder, String failedFolder, SftpConfiguration sftpConfig,
			String configType, String msgId, int auditId) {

		boolean flag = false;
		try {
			File fileList = new File(fileDownloadPath + inboxFolder);
			File filePathList[] = null;

			if (fileList.exists() && fileList.isDirectory()) {
				filePathList = fileList.listFiles();
				logger.info(
						" Inside CamelServiceImpl ::processSFTPPollerFiles() method ::  The file exists for the specified path :: File Path :: "
								+ fileDownloadPath);
			} else {
				logger.info(
						" Inside CamelServiceImpl ::processSFTPPollerFiles() method :: The file does not exist for the specified path :: "
								+ fileDownloadPath + " path.");
			}

			for (File filePath : filePathList) {
				flag = callToConverterUtilityAndFileMovement(filePath, ftpConfig, successFolder, failedFolder,
						sftpConfig, configType, auditId);
			}
			logger.info(
					" Inside CamelServiceImpl ::processSFTPPollerFiles() method :: >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Ended the process on the recived file with id >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "
							+ msgId);
		} catch (Exception ex) {
			logger.error(
					" Inside CamelServiceImpl ::processSFTPPollerFiles() method :: Exception occurred while performing file operation. : "
							+ ex.getMessage());
			return false;
		}
		return flag;
	}

	private boolean callToConverterUtilityAndFileMovement(File filePath, FtpConfiguration ftpConfig,
			String successFolder, String failedFolder, SftpConfiguration sftpConfig, String configType, int auditId)
			throws Exception {
		ConverterUtilityController controller = new ConverterUtilityController();
		boolean flag = false;
		File downlodedFile = null;
		downlodedFile = new File(filePath.toString());
		logger.info(
				" Inside CamelServiceImpl :: callingToConverterUtilityAndFileMovement() method :: Calling to the file converter, and waiting for the response.......... ");

		String docConverterStatus = controller.convertFile(filePath.toString(), auditId);

		logger.info(
				" Inside CamelServiceImpl :: callingToConverterUtilityAndFileMovement() method :: Recived response from the file converter, and the response is ::  "
						+ docConverterStatus);


		if (downlodedFile.exists())
			flag = fileMovement(docConverterStatus, downlodedFile.getName(), successFolder, failedFolder, downlodedFile);

		return flag;
	}

	private boolean fileMovement(String fileConverterStatus, String fileName, String successFolder, String failedFolder,
			File downlodedFile) throws IOException {

		boolean flag = false;

		logger.info(
				" Inside CamelServiceImpl :: SetCrawlStatusAndFileMovement() method :: Based on the converter utility status starting the file movement  ");

		if (fileConverterStatus.equalsIgnoreCase(CommonConstants.SUCCESS)) {
			flag = utils.serverFileMovement(downlodedFile.toString(), successFolder, fileName);
		} else {
			flag = utils.serverFileMovement(downlodedFile.toString(), failedFolder, fileName);
		}
		return flag;
	}
}
