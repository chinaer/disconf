package com.baidu.disconf2.web.web.config.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.baidu.dsp.common.exception.FieldException;
import com.baidu.ub.common.log.AopLogFactory;

/**
 * 
 * @author liaoqiqi
 * @version 2014-2-20
 */
@Component
public class FileUploadValidator {

    protected final static Logger LOG = AopLogFactory
            .getLogger(FileUploadValidator.class);

    /**
     * 验证文件大小，文件名，文件后缀
     * 
     * @param file
     * @param maxLength
     * @param allowExtName
     */
    public void validateFile(MultipartFile file, long maxLength,
            String[] allowExtName) {

        if (file.isEmpty()) {
            throw new FieldException("file", "您没有上传文件", null);
        }

        // 文件大小
        if (file.getSize() < 0 || file.getSize() > maxLength) {

            throw new FieldException("file", "文件不允许超过"
                    + String.valueOf(maxLength), null);
        }

        //
        // 处理不选择文件点击上传时，也会有MultipartFile对象，在此进行过滤
        //
        String filename = file.getOriginalFilename();

        if (filename == "") {
            throw new FieldException("file", "文件名不能为空", null);
        }

        //
        // 文件名后缀
        //
        String extName = filename.substring(filename.lastIndexOf("."))
                .toLowerCase();
        if (allowExtName == null || allowExtName.length == 0
                || Arrays.binarySearch(allowExtName, extName) != -1) {
        } else {
            throw new FieldException("file", "文件后缀不允许", null);
        }
    }

    /**
     * 验证文件大小，文件名，文件后缀
     * 
     * @param file
     * @param maxLength
     * @param allowExtName
     */
    public List<MultipartFile> validateFiles(List<MultipartFile> files,
            long maxLength, String[] allowExtName) {

        List<MultipartFile> retFiles = new ArrayList<MultipartFile>();

        for (MultipartFile multipartFile : files) {

            try {

                this.validateFile(multipartFile, maxLength, allowExtName);
                retFiles.add(multipartFile);

            } catch (Exception e) {

                LOG.warn(e.toString());
            }
        }

        return retFiles;
    }
}