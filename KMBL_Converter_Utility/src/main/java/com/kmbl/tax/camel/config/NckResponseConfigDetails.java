package com.kmbl.tax.camel.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kmbl.tax.camel.model.NckResponseConfig;
import com.kmbl.tax.utils.CommonConstants;

public class NckResponseConfigDetails {
	
	private static final Logger logger = LoggerFactory.getLogger(Configrations.class);
	
	public NckResponseConfig getNckResponseDetails() {

		logger.info(" Inside NckResponseConfig :: getNckResponseDetails() method ::  to collect the nck response Configrations ");
		NckResponseConfig model = new NckResponseConfig();
		Properties prop = new Properties();
		try (InputStream inputStream = getClass().getResourceAsStream(CommonConstants.PROPERTY_FILE_PATH)) {
			prop.load(inputStream);
		} catch (IOException e) {
			// Handle the exception
		}
		
		model.setHeader(prop.getProperty(CommonConstants.NCK_FILE_HEADER));
		model.setCorruptedFileErrorAndDescription(prop.getProperty(CommonConstants.NCK_CURRUPTED_FILE_ERROR_CODE_AND_DESC));
		model.setDuplicateFileErrorAndDescription(prop.getProperty(CommonConstants.NCK_DUPLICATE_FILE_ERROR_CODE_AND_DESC));
		model.setDuplicateFileNameErrorAndDescription(prop.getProperty(CommonConstants.NCK_DUPLICATE_FILE_NAME_ERROR_CODE_AND_DESC));
		model.setInvalidFileCorporateErrorAndDescription(prop.getProperty(CommonConstants.NCK_INVALID_FILE_CORPORATE_ERROR_CODE_AND_DESC));
		model.setInvalidFileDetailsErrorAndDescription(prop.getProperty(CommonConstants.NCK_FILE_DETAILS_ERROR_CODE_AND_DESC));
		model.setInvalidFileHeadersErrorAndDescription(prop.getProperty(CommonConstants.NCK_FILE_HEADER_ERROR_CODE_AND_DESC));
		model.setInvalidFileNameErrorAndDescription(prop.getProperty(CommonConstants.NCK_INVALID_FILE_NAME_NAME_ERROR_CODE_AND_DESC));
		model.setInvalidInputDateFileErrorAndDescription(prop.getProperty(CommonConstants.NCK_DATE_FILE_ERROR_CODE_AND_DESC));
		model.setUnsupportedFileFormatErrorAndDescription(prop.getProperty(CommonConstants.NCK_UNSUPPORTED_FILE_FORMAT_ERROR_CODE_AND_DESC));
		model.setUnsupportedFileSizeErrorAndDescription(prop.getProperty(CommonConstants.NCK_UNSUPPORTED_FILE_SIZE_ERROR_CODE_AND_DESC));
		model.setInvalidAmountDetailsErrorAndDescription(prop.getProperty(CommonConstants.NCK_INVALID_AMOUNT_ERROR_CODE_AND_DESC));
		model.setScriptingTagErrorAndDescription(prop.getProperty(CommonConstants.NCK_SCRIPTING_TAG_ERROR_CODE_AND_DESC));
		
		return model;
		
	}


}
