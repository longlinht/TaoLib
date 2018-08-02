package io.github.longlinht.library.zip;

import android.support.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import io.github.longlinht.library.utils.FileIOUtils;
import io.github.longlinht.library.utils.Files;

import static io.github.longlinht.library.guava.Preconditions.checkArgument;


/**
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */

public class GZipUtils {

    /**
     * Member cache 文件解压处理
     *
     * @param bytes 要解压的数据
     * @throws IOException 如果解压缩过程中发生异常
     */
    public static byte[] unGzip(byte[] bytes) throws IOException {
        GZIPInputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new GZIPInputStream(new ByteArrayInputStream(bytes));
            out = new ByteArrayOutputStream(bytes.length);

            Files.copyStream(in, out);
            out.flush();

            return out.toByteArray();
        } finally {
            FileIOUtils.closeQuietly(out);
            FileIOUtils.closeQuietly(in);
        }
    }

    /**
     * 压缩字节数组
     *
     * @param bytes 要压缩的数据
     * @throws IOException 如果压缩过程中发生异常
     */
    public static byte[] gzip(byte[] bytes) throws IOException {

        GZIPOutputStream out = null;
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            out = new GZIPOutputStream(bos);
            out.write(bytes, 0, bytes.length);
            out.finish();

            out.flush();
            bos.flush();
            return bos.toByteArray();
        } finally {
            FileIOUtils.closeQuietly(out);
        }
    }

    /**
     * 对文件进行压缩
     *
     * @param source 源文件
     * @param target 目标文件
     * @throws IOException 如果压缩过程中发生义仓你
     */
    public static void zipFile(@NonNull File source, @NonNull File target) throws IOException {

        checkArgument(source.exists());

        InputStream fin = null;
        GZIPOutputStream out = null;
        try {

            fin = new FileInputStream(source);
            out = new GZIPOutputStream(new FileOutputStream(target));

            Files.copyStream(fin, out);
            out.finish();

            out.flush();

        } finally {
            FileIOUtils.closeQuietly(out);
            FileIOUtils.closeQuietly(fin);
        }
    }

    /**
     * 解压文件
     *
     * @param source 要解压的源文件
     * @param target 解压到的目标文件
     * @throws IOException 如果解压过程中发生异常
     */
    public static void unZipFile(File source, File target) throws IOException {
        GZIPInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new GZIPInputStream(new FileInputStream(source));
            out = new FileOutputStream(target);

            Files.copyStream(in, out);
            out.flush();

        } finally {
            FileIOUtils.closeQuietly(in);
            FileIOUtils.closeQuietly(out);
        }
    }
}


