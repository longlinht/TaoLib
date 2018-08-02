package io.github.longlinht.library.utils;

import android.os.StatFs;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.github.longlinht.library.log.Logger;
import io.github.longlinht.library.utils.constants.Bytes;
import rx.functions.Action2;

import static android.os.Environment.MEDIA_MOUNTED;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStorageState;
import static io.github.longlinht.library.guava.Preconditions.checkArgument;
import static io.github.longlinht.library.guava.Preconditions.checkNotNull;
import static io.github.longlinht.library.utils.GlobalContext.isDebug;

/**
 * 文件读写，操作工具类
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public class Files {

    private Files() {
        //no instance
    }

    ///////////////////////////////////////////////////////////////////////////
    // 读取文件
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Reads all bytes from a file into a byte array.
     *
     * @param file the file to read from
     * @return a byte array containing all the bytes from file
     * @throws IOException if an I/O error occurs
     */
    public static byte[] toByteArray(File file) throws IOException {

        if (isDebug()) {
            // 提示用户文件太大
            if (file.exists() && file.isFile() && file.length() > 10 * Bytes.MB) {
                Logger.w("文件大小超过10M，请确认是否正常 file：%s, size: %s", file, file.length());
            }
        }

        InputStream ins = null;
        ByteArrayOutputStream out = null;
        try {
            final byte[] buffer = new byte[2048];

            // 内置Buffer 默认大小是8k
            ins = new BufferedInputStream(new FileInputStream(file));

            int len;
            out = new ByteArrayOutputStream();
            while ((len = ins.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            return out.toByteArray();

        } finally {
            FileIOUtils.closeQuietly(out);
            FileIOUtils.closeQuietly(ins);
        }
    }

    /**
     * user utf-8 Reads all characters from a file into a {@link String}, using the given character set.
     *
     * @param file the file to read from
     * @return a string containing all the characters from the file
     * @throws IOException if an I/O error occurs
     */
    public static String toString(@NonNull File file) throws IOException {
        return toString(file, Charset.defaultCharset());
    }

    /**
     * Reads all characters from a file into a {@link String}, using the given character set.
     *
     * @param file    the file to read from
     * @param charset the charset used to decode the input stream;
     * @return a string containing all the characters from the file
     * @throws IOException if an I/O error occurs
     */
    public static String toString(@NonNull File file, @NonNull Charset charset) throws IOException {

        if (isDebug()) {
            // 提示用户文件太大
            if (file.exists() && file.isFile() && file.length() > 10 * Bytes.MB) {
                Logger.w("文件大小超过10M，请确认是否正常 file：%s, size: %s", file, file.length());
            }
        }

        BufferedReader reader = null;
        try {
            reader = newReader(file, charset);

            final char[] buffer = new char[2048];
            // StringWriter 基于StringBuilder实现，不用close
            final StringWriter writer = new StringWriter(buffer.length);

            int len;
            while ((len = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, len);
            }

            return writer.toString();

        } finally {
            FileIOUtils.closeQuietly(reader);
        }
    }

    /**
     * use UTF-8 Reads all of the lines from a file. The lines do not include line-termination characters, but
     * do include other leading and trailing whitespace.
     * <p>
     * <p>This method returns a mutable {@code List}. For an {@code ImmutableList}, use {@code
     * Files.asCharSource(file, charset).readLines()}.
     *
     * @param file the file to read from
     * @return a mutable {@link List} containing all the lines
     * @throws IOException if an I/O error occurs
     */
    public static List<String> readLines(File file) throws IOException {
        return readLines(file, Charset.defaultCharset());
    }

    /**
     * Reads all of the lines from a file. The lines do not include line-termination characters, but
     * do include other leading and trailing whitespace.
     * <p>
     * <p>This method returns a mutable {@code List}. For an {@code ImmutableList}, use {@code
     * Files.asCharSource(file, charset).readLines()}.
     *
     * @param file    the file to read from
     * @param charset the charset used to decode the input stream;
     * @return a mutable {@link List} containing all the lines
     * @throws IOException if an I/O error occurs
     */
    public static List<String> readLines(File file, Charset charset) throws IOException {

        LineNumberReader reader = null;

        try {
            final ArrayList<String> result = new ArrayList<>();
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), charset));

            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }

            return result;
        } finally {
            FileIOUtils.closeQuietly(reader);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 写文件
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Overwrites a file with the contents of a byte array.
     *
     * @param from the bytes to write
     * @param to   the destination file
     * @throws IOException if an I/O error occurs
     */
    public static void write(byte[] from, File to) throws IOException {

        OutputStream out = null;

        try {

            out = new BufferedOutputStream(new FileOutputStream(to));
            out.write(from);
            out.flush();

        } finally {
            FileIOUtils.closeQuietly(out);
        }
    }

    /**
     * use utf-8 Writes a character sequence (such as a string) to a file using the given character set.
     *
     * @param from the character sequence to write
     * @param to   the destination file
     * @throws IOException if an I/O error occurs
     */
    public static void write(CharSequence from, File to) throws IOException {
        write(from, to, Charset.defaultCharset());
    }

    /**
     * Writes a character sequence (such as a string) to a file using the given character set.
     *
     * @param from    the character sequence to write
     * @param to      the destination file
     * @param charset the charset used to encode the output stream;
     * @throws IOException if an I/O error occurs
     */
    public static void write(CharSequence from, File to, Charset charset) throws IOException {

        doWrite(from, to, false, charset);
    }

    private static void doWrite(CharSequence from, File to, boolean append, Charset charset) throws IOException {
        Writer writer = null;

        try {
            writer = newWriter(to, append, charset);

            final char[] buffer = new char[2048];

            int sourceIndex = 0;
            int bufIndex = 0;

            while (sourceIndex < from.length()) {

                final char c = from.charAt(sourceIndex);

                if (bufIndex < buffer.length) {
                    buffer[bufIndex] = c;
                    bufIndex++;
                } else {
                    writer.write(buffer, 0, bufIndex + 1);
                    bufIndex = 0;
                }

                sourceIndex++;
            }

            if (bufIndex > 0) {
                writer.write(buffer, 0, bufIndex);
            }
        } finally {
            FileIOUtils.closeQuietly(writer);
        }
    }

    /**
     * Appends a character sequence (such as a string) to a file using the given character set.
     *
     * @param from the character sequence to append
     * @param to   the destination file
     * @throws IOException if an I/O error occurs
     */
    public static void append(CharSequence from, File to) throws IOException {
        append(from, to, Charset.defaultCharset());
    }

    /**
     * Appends a character sequence (such as a string) to a file using the given character set.
     *
     * @param from    the character sequence to append
     * @param to      the destination file
     * @param charset the charset used to encode the output stream;
     * @throws IOException if an I/O error occurs
     */
    public static void append(CharSequence from, File to, Charset charset) throws IOException {
        doWrite(from, to, true, charset);
    }

    ///////////////////////////////////////////////////////////////////////////
    // reader/writer factoryMethod
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Returns a buffered reader that reads from a file using the given character set.
     *
     * @param file    the file to read from
     * @param charset the charset used to decode the input stream;
     * @return the buffered reader
     */
    public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
        checkNotNull(file);
        checkNotNull(charset);
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
    }

    /**
     * Returns a buffered writer that writes to a file using the given character set.
     *
     * @param file    the file to write to
     * @param charset the charset used to encode the output stream;
     * @return the buffered writer
     */
    public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
        return newWriter(file, false, charset);
    }

    /**
     * Returns a buffered writer that writes to a file using the given character set.
     *
     * @param file    the file to write to
     * @param charset the charset used to encode the output stream;
     * @param append  is open as append mode
     * @return the buffered writer
     */
    public static BufferedWriter newWriter(File file, boolean append, Charset charset) throws FileNotFoundException {
        checkNotNull(file);
        checkNotNull(charset);
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset));
    }

    /**
     * copy stream content from InputStream to OutputStream
     *
     * @param in  InputStream
     * @param out OutputStream
     * @throws IOException if any IOException happens in copy
     */
    public static void copyStream(@NonNull InputStream in, @NonNull OutputStream out) throws IOException {

        final byte[] bytes = new byte[2048];
        int count;
        while ((count = in.read(bytes)) != -1) {
            out.write(bytes, 0, count);
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // file extension
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Returns the <a href="http://en.wikipedia.org/wiki/Filename_extension">file extension</a> for
     * the given file name, or the empty string if the file has no extension. The result does not
     * include the '{@code .}'.
     * <p>
     * <p><b>Note:</b> This method simply returns everything after the last '{@code .}' in the file's
     * name as determined by {@link File#getName}. It does not account for any filesystem-specific
     * behavior that the {@link File} API does not already account for. For example, on NTFS it will
     * report {@code "txt"} as the extension for the filename {@code "foo.exe:.txt"} even though NTFS
     * will drop the {@code ":.txt"} part of the name when the file is actually created on the
     * filesystem due to NTFS's <a href="https://goo.gl/vTpJi4">Alternate Data Streams</a>.
     *
     * @since 11.0
     */
    public static String getFileExtension(String fullName) {
        checkNotNull(fullName);
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * Returns the file name without its
     * <a href="http://en.wikipedia.org/wiki/Filename_extension">file extension</a> or path. This is
     * similar to the {@code basename} unix command. The result does not include the '{@code .}'.
     *
     * @param file The name of the file to trim the extension from. This can be either a fully
     *             qualified file name (including a path) or just a file name.
     * @return The file name without its path or extension.
     * @since 14.0
     */
    public static String getNameWithoutExtension(String file) {
        checkNotNull(file);
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    public static String dirName(@NonNull File file) {
        return dirName(file.getAbsolutePath());
    }

    public static String dirName(String filePath) {
        final int index = filePath.lastIndexOf(File.separatorChar);
        if (index > 0) {
            return filePath.substring(0, index);
        } else {
            return filePath;
        }
    }

    public static String baseName(File file) {
        return baseName(file.getAbsolutePath());
    }

    public static String baseName(String filePath) {
        final int index = filePath.lastIndexOf(File.separatorChar);
        if (index != -1 && index + 1 < filePath.length()) {
            return filePath.substring(index + 1, filePath.length());
        } else {
            return filePath;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // file size
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 计算文件大小，如果是文件夹, 递归获取文件文件大小
     * 如果是文件，直接返回文件的大小
     *
     * @param file
     * @return
     */
    public static long getFileLength(final File file) {

        if (!file.exists()) {
            return 0;
        }

        if (file.isDirectory()) {

            final Capture<Long> fileLength = Capture.of(0L);

            depthFirstTraverse(file, 0, MAX_SCAN_DEPTH, new Action2<Integer, File>() {
                @Override
                public void call(Integer integer, File file) {
                    if (file.isFile()) {
                        fileLength.set(fileLength.get() + file.length());
                    }
                }
            });

            return fileLength.get();
        } else {
            return file.length();
        }

    }

    /**
     * 获取人类可读的文件大小
     *
     * @param file 文件
     */
    public static String getHumanReadableFileSize(File file) {
        final long fileLength = getFileLength(file);
        return getHumanReadableFileSize(fileLength);
    }

    /**
     * 获取人类可读的文件大小
     *
     * @param fileLength 文件长度
     */
    public static String getHumanReadableFileSize(@IntRange(from = 0) long fileLength) {

        checkArgument(fileLength >= 0);

        if (fileLength > Bytes.GB) {
            return String.format(Locale.US, "%.2fG", (double) fileLength / Bytes.GB);
        } else if (fileLength > Bytes.MB) {
            return String.format(Locale.US, "%.2fM", (double) fileLength / Bytes.MB);
        } else if (fileLength > Bytes.KB) {
            return String.format(Locale.US, "%.2fK", (double) fileLength / Bytes.KB);
        } else {
            return String.format(Locale.US, "%dB", fileLength);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // file operate 删除，移动，copy
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 默认最多搜索1000层
     */
    public static final int MAX_SCAN_DEPTH = 1000;

    @VisibleForTesting
    static void depthFirstTraverse(File file, int depth, int maxDepth, Action2<Integer, File> action1) {

        if (depth > maxDepth) {
            Logger.w("超过最大查找深度 depth: %s, max: %s", depth, maxDepth);
            return;
        }

        if (!file.exists()) {
            return;
        }

        action1.call(depth, file);

        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (files != null) {
                for (File _file : files) {
                    depthFirstTraverse(_file, depth + 1, maxDepth, action1);
                }
            }
        }
    }


    /**
     * 清空文件夹
     *
     * @param file 要清除的文件夹
     */
    public static void clean(File file, @IntRange(from = 1) int maxDepth) {
        depthFirstTraverse(file, 0, maxDepth, new Action2<Integer, File>() {
            @Override
            public void call(Integer integer, File file) {
                if (file.isFile()) {
                    if (!file.delete()) {
                        Logger.w("删除文件失败, file: %s", file);
                    }
                }
            }
        });
    }

    public static void clean(File file) {
        clean(file, MAX_SCAN_DEPTH);
    }

    /**
     * Copies all the bytes from one file to another.
     * <p>
     * <p>Copying is not an atomic operation - in the case of an I/O error, power loss, process
     * termination, or other problems, {@code to} may not be a complete copy of {@code from}. If you
     * need to guard against those conditions, you should employ other file-level synchronization.
     * <p>
     * <p><b>Warning:</b> If {@code to} represents an existing file, that file will be overwritten
     * with the contents of {@code from}. If {@code to} and {@code from} refer to the <i>same</i>
     * file, the contents of that file will be deleted.
     *
     * @param from the source file
     * @param to   the destination file
     * @throws IOException              if an I/O error occurs
     * @throws IllegalArgumentException if {@code from.equals(to)}
     */
    public static void copy(File from, File to) throws IOException {
        checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);

        InputStream in = null;
        OutputStream out = null;

        try {
            in = new BufferedInputStream(new FileInputStream(from));
            out = new BufferedOutputStream(new FileOutputStream(to));

            copyStream(in, out);

        } finally {
            FileIOUtils.closeQuietly(in);
            FileIOUtils.closeQuietly(out);
        }
    }

    /**
     * Creates an empty file or updates the last updated timestamp on the same as the unix command of
     * the same name.
     *
     * @param file the file to create or update
     * @throws IOException if an I/O error occurs
     */
    public static void touch(File file) throws IOException {
        checkNotNull(file);
        if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
            throw new IOException("Unable to update modification time of " + file);
        }
    }

    /**
     * Creates any necessary but nonexistent parent directories of the specified file. Note that if
     * this operation fails it may have succeeded in creating some (but not all) of the necessary
     * parent directories.
     *
     * @throws IOException if an I/O error occurs, or if any necessary but nonexistent parent
     *                     directories of the specified file could not be created.
     * @since 4.0
     */
    public static void createParentDirs(File file) throws IOException {
        checkNotNull(file);
        File parent = file.getCanonicalFile().getParentFile();
        if (parent == null) {
      /*
       * The given directory is a filesystem root. All zero of its ancestors exist. This doesn't
       * mean that the root itself exists -- consider x:\ on a Windows machine without such a drive
       * -- or even that the caller can create it, but this method makes no such guarantees even for
       * non-root files.
       */
            return;
        }
        parent.mkdirs();
        if (!parent.isDirectory()) {
            throw new IOException("Unable to create parent directories of " + file);
        }
    }

    /**
     * Moves a file from one path to another. This method can rename a file and/or move it to a
     * different directory. In either case {@code to} must be the target path for the file itself; not
     * just the new name for the file or the path to the new parent directory.
     *
     * @param from the source file
     * @param to   the destination file
     * @throws IOException              if an I/O error occurs
     * @throws IllegalArgumentException if {@code from.equals(to)}
     */
    public static void move(File from, File to) throws IOException {
        checkNotNull(from);
        checkNotNull(to);
        checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);

        if (!from.renameTo(to)) {
            copy(from, to);
            if (!from.delete()) {
                if (!to.delete()) {
                    throw new IOException("Unable to delete " + to);
                }
                throw new IOException("Unable to delete " + from);
            }
        }
    }

    /**
     * sd卡上是否有足够的存储空间
     *
     * @param sizeInMb 需要的字节数目，以MB为单位
     */
    public static boolean isSpaceAvailableIsSdcard(int sizeInMb) {

        if (!getExternalStorageState().equals(MEDIA_MOUNTED)) {

            Logger.e("sdcard un mount");
            return false;
        }

        String sdcard = getExternalStorageDirectory().getPath();
        StatFs statFs = new StatFs(sdcard);
        long blockSize = statFs.getBlockSize();
        long blocks = statFs.getAvailableBlocks();
        long availableSpare = (blocks * blockSize) / Bytes.MB;
        return availableSpare > sizeInMb;
    }
}
