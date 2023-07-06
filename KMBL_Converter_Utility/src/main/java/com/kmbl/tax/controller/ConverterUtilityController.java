package com.kmbl.tax.controller;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kmbl.tax.camel.config.Configrations;
import com.kmbl.tax.camel.config.NckResponseConfigDetails;
import com.kmbl.tax.camel.entity.Audit;
import com.kmbl.tax.camel.model.ConfigModel;
import com.kmbl.tax.camel.model.NckResponseConfig;
import com.kmbl.tax.camel.repository.UtilityAudit;
import com.kmbl.tax.converter.ExcelConverter;
import com.kmbl.tax.utils.CommonConstants;
import com.kmbl.tax.utils.CommonUtils;

@RestController
@RequestMapping("api/v3")
public class ConverterUtilityController {

	private static final Logger logger = LoggerFactory.getLogger(ConverterUtilityController.class);
	CommonUtils utils = new CommonUtils();
	Configrations configrations = new Configrations();
	ExcelConverter con = new ExcelConverter();
	public String convertFile(String filePath, int auditId) {

		logger.info(
				" Inside ConverterUtilityController :: convertFile() method :: Received file for converting, The file path is : "
						+ filePath);
		String res = CommonConstants.FAIL;

		String fileName = extractFileName(filePath);
		if (!fileName.trim().isEmpty()) {
			String fileExtension = extractFileExtension(fileName);
			logger.info(" Inside ConverterUtilityController :: convertFile() method :: Received file Extension is : "
					+ fileExtension);
			if (!fileExtension.trim().isEmpty()) {
				if (fileExtension.trim().equals("xls") || fileExtension.trim().equals("xlsx")) {
					res = con.convert(filePath, auditId);
				} else {
					NckResponseConfigDetails nckDetails = new NckResponseConfigDetails();
					ConfigModel configModel = configrations.getNckFileName();
					
					NckResponseConfig nckResponseModel = nckDetails.getNckResponseDetails();
					String seqNo = con.getSequenceNumber(filePath);
					String crnNo = configrations.getCrnNo();
					try {
						con.generateNckFileAndUpload(crnNo, seqNo, configModel,auditId,nckResponseModel.getHeader(),nckResponseModel.getUnsupportedFileFormatErrorAndDescription(),CommonConstants.FAILED_PROCESS_DETAILS+CommonConstants.INCORRECT_FILE_EXTENSION);
						//utils.deletefileFromLocal(true, new File(filePath));
					} catch (Exception e) {

						logger.error(
								" Inside ConverterUtilityController :: convertFile() method :: Exception occured while generate the nck file :: "+e.getMessage());
					}
					logger.info(
							" Inside ConverterUtilityController :: convertFile() method :: File Extension does not match with the converter ");
				}
			}
		} else {
			logger.info(
					" Inside ConverterUtilityController :: convertFile() method :: Received file path is empty : ");
		}
		return res;
	}

	private String extractFileName(String filePath) {
		File file = new File(filePath);
		String fileName = file.getName();
		return fileName;
	}

	private String extractFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
			return ""; // No extension or the dot is the last character
		} else {
			return fileName.substring(dotIndex + 1);
		}
	}
}
