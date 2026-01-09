package cn.ymypay.team.utils;

import cn.ymypay.team.exception.YmwlException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 文件处理工具类
 */
public class FileUtils {
    // 静态资源目录
    private static final String STATIC_PATH = System.getProperty("user.dir") + File.separator + "static" + File.separator + "upload" + File.separator;
    
    /**
     * 保存文件到静态资源目录
     * @param file 文件
     * @return 文件相对路径
     */
    public static String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new YmwlException(400, "文件不存在或为空");
        }

        try {
            // 创建年月日目录
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String uploadPath = STATIC_PATH + datePath.replace("/", File.separator);
            
            // 确保目录存在
            Path directory = Paths.get(uploadPath);
            Files.createDirectories(directory);
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = StringUtils.getFilenameExtension(originalFilename);
            String newFilename = UUID.randomUUID().toString();
            if (fileExtension != null) {
                newFilename += "." + fileExtension;
            }
            
            // 目标文件路径
            Path targetPath = directory.resolve(newFilename);
            
            // 直接保存文件
            file.transferTo(targetPath.toFile());
            
            // 返回相对路径（使用正斜杠作为分隔符）
            return "upload/" + datePath + "/" + newFilename;
            
        } catch (IOException e) {
            throw new YmwlException(500, "文件保存失败：" + e.getMessage());
        }
    }

    /**
     * 批量保存文件到静态资源目录
     * @param files 文件列表
     * @return 文件相对路径列表
     */
    public static List<String> saveFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new YmwlException(400, "文件列表不存在或为空");
        }

        List<String> paths = new ArrayList<>();
        for (MultipartFile file : files) {
            paths.add(saveFile(file));
        }

        return paths;
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        } else {
            return true;
        }
    }

    /**
     * thumbnailator压缩图片
     * @param path
     */
    public static void compressPicForThumbnailator(String path) {
        try {
            Thumbnails.of(path).scale(1f).outputQuality(0.35f).toFile(path);
        } catch (IOException e) {
            throw new YmwlException("上传图片失败");
        }
    }


}