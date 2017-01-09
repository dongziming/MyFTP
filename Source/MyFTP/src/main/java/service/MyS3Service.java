package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.MyS3Object;

import org.apache.commons.io.FileUtils;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.model.S3Object;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Service("myS3Service")
public class MyS3Service {
	public Boolean uploadObject(CommonsMultipartFile file,String path, S3Service s3Service,
			String bucketName) {
		File destFile = new File(path);
		try {
			// FileUtils.copyInputStreamToFile()这个方法里对IO进行了自动操作，不需要额外的再去关闭IO流
			FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);// 复制临时文件到指定目录下
			// start upload file to S3
//			File fileData = new File(path);
			// Create an empty object with a key/name, and print the object’s details.
			S3Object fileObject = new S3Object(destFile);
			//System.out.println("S3Object before upload: " + fileObject);
			// Upload the object to our test bucket in S3.
			s3Service.putObject(bucketName, fileObject);
			// Print the details about the uploaded object, which contains more information.  
			//System.out.println("S3Object after upload: " + fileObject);
			return true;
		} catch (IOException | NoSuchAlgorithmException | S3ServiceException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void deleteObject(String bucketName, String fileName,
			S3Service s3Service) throws ServiceException {
		s3Service.deleteObject(bucketName, fileName);
	}

	public List<MyS3Object> getFiles(S3Service s3Service,String bucketName) {
		List<MyS3Object> list = new ArrayList<MyS3Object>();
		try {
			S3Object[] objects = s3Service.listObjects(bucketName);
			SimpleDateFormat dateformat2=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E ");  
			for(S3Object object:objects){
				MyS3Object myObject = new MyS3Object(object.getKey(), object.getContentLength(), dateformat2.format(object.getLastModifiedDate()));
				list.add(myObject);
			}
		} catch (S3ServiceException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void downloadObject(String bucketName, String localpath,
			String fileName, S3Service s3Service) throws Exception {
		File path = new File(localpath);
		if(!path.exists()){
			path.mkdirs();
		}
		FileWriter fw = new FileWriter(localpath+"/"+fileName);
		BufferedWriter bw = new BufferedWriter(fw);
		// Retrieve the HEAD of the data object we created previously.
		S3Object objectComplete = s3Service.getObject(bucketName, fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(objectComplete.getDataInputStream(),"utf-8"));
		String data = null;
		while ((data = reader.readLine()) != null) {
			System.out.println(data);
			bw.write(new String(data.getBytes())+"\n");
		}
		bw.close();
		fw.close();
		reader.close();
	}
}
