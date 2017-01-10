package controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.MyS3Object;

import org.jets3t.service.S3Service;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.security.AWSCredentials;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import service.MyS3Service;


@Controller
public class S3Controller {
	
	@Resource(name="myS3Service")
	private MyS3Service myS3Service;
	
	String key = "AKIAJFJS7TP3BTYOJYKA IMJxIMIT5gqKCIziOxwyoq41RcmJ6zZE9d08IYdE";
	AWSCredentials awsCredentials = new AWSCredentials(key.split(" ")[0], key.split(" ")[1]);
	S3Service s3Service = new RestS3Service(awsCredentials);
	String bucketName = "myftp001";
	
	String result = null;

	@RequestMapping("/fileManagement.do")
	public String fileManagement(HttpServletRequest request){
		if(result!=null){
			request.setAttribute("result", result);
			result = null;
		}
		return "fileManagement";
	}
	
	@RequestMapping(value = "uploadobject", method = RequestMethod.POST)
	public String uploadObject(@RequestParam("uploadfile") CommonsMultipartFile file, HttpServletRequest request) {
		// MultipartFile是对当前上传的文件的封装，当要同时上传多个文件时，可以给定多个 MultipartFile参数(数组)
		if (!file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			try{
				String path = request.getSession().getServletContext().getRealPath("/upload/" + fileName);
				myS3Service.uploadObject(file, path, s3Service, bucketName);
			}catch(Exception e){
				result = e.getMessage();
				return "redirect:fileManagement.do";
			}
			result = "文件上传成功";
			return "redirect:fileManagement.do";
		} else {
			result = "未添加上传的文件";
			return "redirect:fileManagement.do";
		}
	}
	
	@RequestMapping(value = "getFiles", method = RequestMethod.POST)
	@ResponseBody
	public List<MyS3Object> getFiles(){
		return myS3Service.getFiles(s3Service,bucketName);
	}
	
	@RequestMapping(value="delete", method = RequestMethod.GET)
	public String delete(String fileName, HttpServletRequest request){
		try{
			myS3Service.deleteObject(bucketName,fileName,s3Service);
		}catch(Exception e){
			result = e.getMessage();
			return "redirect:fileManagement.do";
		}
		result = "文件删除成功："+fileName;
		return "redirect:fileManagement.do";
	}
	
	@RequestMapping(value="download", method = RequestMethod.GET)
	public String downloadObject(@RequestParam("fileName") String fileName,HttpServletRequest request,
			HttpServletResponse response){
		try{
			myS3Service.downloadObject(bucketName, fileName, s3Service,response);
		}catch(Exception e){
			result = e.getMessage();
			return "redirect:fileManagement.do";
		}
		return "redirect:fileManagement.do";
	}
}
