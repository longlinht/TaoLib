package io.github.longlinht.library.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import io.github.longlinht.library.utils.FileIOUtils;

/**
 *
 * 文件夹压缩
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */

public class FolderZiper {

    public static void zipFolder(String inputFolderPath, String outZipPath) throws IOException {
        ZipOutputStream zos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outZipPath);
            zos = new ZipOutputStream(fos);
            File srcFile = new File(inputFolderPath);
            File[] files = srcFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(files[i]);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.finish();
            zos.flush();
        } finally {
            FileIOUtils.closeQuietly(fos);
            FileIOUtils.closeQuietly(zos);
        }
    }

    public static void zipFolderRecursively(String dir2zip, ZipOutputStream zos) throws IOException {
        //create a new File object based on the directory we have to zip
        File zipDir = new File(dir2zip);
        //get a listing of the directory content
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        //loop through dirList, and zip the files
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(zipDir, dirList[i]);
            if (f.isDirectory()) {
                //if the File object is a directory, call this
                //function again to add its content recursively
                String filePath = f.getPath();
                zipFolderRecursively(filePath, zos);
                //loop again
                continue;
            }
            //if we reached here, the File object f was not a directory
            //create a FileInputStream on top of f
            FileInputStream fis = new FileInputStream(f);
            // create a new zip entry
            ZipEntry anEntry = new ZipEntry(f.getPath());
            //place the zip entry in the ZipOutputStream object
            zos.putNextEntry(anEntry);
            //now write the content of the file to the ZipOutputStream
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            //close the Stream
            fis.close();
        }
    }
}