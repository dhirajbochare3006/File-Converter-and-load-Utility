package com.kmbl.tax.camel.service;

import java.io.File;

import org.apache.camel.component.file.remote.FtpConfiguration;
import org.apache.camel.component.file.remote.SftpConfiguration;

public interface CamelService {

	boolean processSFTPPollerFiles(File file, String inboxFolder,
			String fileDownloadPath, FtpConfiguration ftpConfig, String successFolder,
			String failedFolder,SftpConfiguration sftpConfig,String configType, String msgId,int auditId);
}
