package com.kmbl.tax.camel.routebuilder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.util.URISupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kmbl.tax.camel.config.Configrations;
import com.kmbl.tax.camel.model.ConfigModel;
import com.kmbl.tax.camel.processor.SFTPFileReadProcessor;
import com.kmbl.tax.utils.CommonConstants;

@Service
public class SftpPollerRouteBuilder extends RouteBuilder {

	private static final Logger logger = LoggerFactory.getLogger(SftpPollerRouteBuilder.class);
	
	Configrations configrations = new Configrations();
	ConfigModel configModel = configrations.getConfigrationDetails();

	@Override
	public void configure() throws Exception {
		logger.info(" Inside SftpPollerRouteBuilder ::configure() method :: Successfully collected the configuration details from properties file.");
		try {
			if (null != configModel && !configModel.toString().isEmpty()) {
				logger.info(" Inside SftpPollerRouteBuilder ::configure() method :: configModel : " + configModel);
				String routeString = configModel.getConfigType().toLowerCase().trim() + CommonConstants.COLON
						+ CommonConstants.DOUBLE_FRWD_SLASH + configModel.getUsername() + CommonConstants.AT_SIGN
						+ configModel.getHost() + CommonConstants.COLON + configModel.getPort()
						+ CommonConstants.DOUBLE_FRWD_SLASH + configModel.getInboxFolder() + "?strictHostKeyChecking=no"
						+CommonConstants.AND + CommonConstants.PASSWORD + CommonConstants.EQUAL+ URISupport.normalizeUri(configModel.getPassword())
						+CommonConstants.AND+"preferredAuthentications=publickey,keyboard-interactive,password";

				String ftpOptions = configModel.getOptions();
				String camelFileDownloadPath = configModel.getFileDownloadOnServerPath() + CommonConstants.FRWD_SLASH;
				logger.info(" Inside SftpPollerRouteBuilder ::configure() method :: generated route String is : "
						+ routeString);
				logger.info(" Inside SftpPollerRouteBuilder ::configure() method :: Route creating started >>> : ");
				try {
					
					from(routeString + ftpOptions).to("file:" + camelFileDownloadPath)
							.setHeader(CommonConstants.INBOX_FOLDER, constant(configModel.getInboxFolder()))
							.setHeader(CommonConstants.SUCCESS_FOLDER, constant(configModel.getSuccessFileMovementOnServerPath()))
							.setHeader(CommonConstants.FAILED_FOLDER, constant(configModel.getFailedFileMovementOnServerPath()))
							.setHeader(CommonConstants.FILE_DOWNLOAD_PATH, constant(camelFileDownloadPath))
							.setHeader(CommonConstants.MODULE_TYPE, constant(configModel.getConfigType()))
							.process(new SFTPFileReadProcessor()).end();

					logger.info(
							" Inside SftpPollerRouteBuilder ::configure() method :: Rout Created successfully!!! : ");
				} catch (Exception e) {
					logger.error(
							" Inside SftpPollerRouteBuilder ::configure() method :: Error occured while creating the route : "
									+ e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error(
					" Inside SftpPollerRouteBuilder ::configure() method :: Error occured while creating the route >>> : "
							+ e.getMessage());
		}
	}
}
