package com.kmbl.tax.camel.processor;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.remote.FtpConfiguration;
import org.apache.camel.component.file.remote.SftpConfiguration;
import org.apache.camel.component.file.remote.SftpEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kmbl.tax.camel.entity.Audit;
import com.kmbl.tax.camel.repository.UtilityAudit;
import com.kmbl.tax.camel.service.CamelServiceImpl;
import com.kmbl.tax.utils.CommonConstants;
import com.kmbl.tax.utils.CommonUtils;

@Service(value = "FTPFileReadProcessor")
public class SFTPFileReadProcessor implements Processor {
	private static final Logger logger = LoggerFactory.getLogger(SFTPFileReadProcessor.class);

	@SuppressWarnings({ "unchecked" })
	@Override
	public void process(Exchange exchange) throws Exception {

		try {
			Message message = exchange.getIn();
			GenericFile<File> genFile = (GenericFile<File>) message.getBody();
		
			String msgId = (String) message.getMessageId();

			logger.info(
					" Inside CamelServiceImpl ::processSFTPPollerFiles() method :: >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Started the process on the recived file with id >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "
							+ msgId);

			String inboxFolder = (String) message.getHeader(CommonConstants.INBOX_FOLDER);
			String successFolder = (String) message.getHeader(CommonConstants.SUCCESS_FOLDER);
			String failedFolder = (String) message.getHeader(CommonConstants.FAILED_FOLDER);
			String fileDownloadPath = (String) message.getHeader(CommonConstants.FILE_DOWNLOAD_PATH);
			String configType = (String) message.getHeader(CommonConstants.MODULE_TYPE);

			logger.info(
					" Inside SFTPFileReadProcessor ::process() method :: Recived details is : inboxFolder name is : "
							+ inboxFolder + ", successFolder Name is : " + successFolder + ", failedFolder name is : "
							+ failedFolder + ", fileDownloadPath is : " + fileDownloadPath + ", configType : "
							+ configType);

			FtpConfiguration ftpConfig = null;
			SftpConfiguration sftpConfig = null;
			File file = null;
			if (null != configType && CommonConstants.SFTP.equalsIgnoreCase(configType)) {
				SftpEndpoint sftpEndpoint = (SftpEndpoint) exchange.getFromEndpoint();
				sftpConfig = sftpEndpoint.getConfiguration();
				file = new File(genFile.getAbsoluteFilePath());
			}
			
			Audit audit = CommonUtils.createAuditObj(0,CommonConstants.CLASS_NAME,file.getName(),0,CommonConstants.METHOD_NAME,0,null,CommonConstants.INPROCESS,CommonConstants.UTILITY_NAME,msgId); 
			int auditId = UtilityAudit.saveAuditDetails(audit);

			CamelServiceImpl camelService = new CamelServiceImpl();
			try {
				switch (configType.toUpperCase()) {
				case CommonConstants.SFTP:
					logger.info(
							" Inside SFTPFileReadProcessor ::process() method :: Calling to the CamelServiceImpl for process the recived Files ");
					camelService.processSFTPPollerFiles(file, inboxFolder, fileDownloadPath, ftpConfig, successFolder,
							failedFolder, sftpConfig, configType, msgId,auditId);
					break;
				default:
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.error(" Inside SFTPFileReadProcessor ::process() method :: Error ocured inside process : "
						+ e.getMessage());
			}
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" Inside SFTPFileReadProcessor ::process() method :: Error ocured inside process : "
					+ e.getMessage());
		}
	}
}
