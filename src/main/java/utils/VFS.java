package utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by ilya on 01.11.15.
 */
public class VFS{
    private String root;

    public VFS(String root) {
        this.root = root;
    }

    public VFS() {
        this.root = "";
    }

    public boolean isDirectory(String path) {
        return new File(root + path).isDirectory();
    }

    @NotNull
    public Iterator<String> getIterator(String startDir) {
        return new FileIterator(startDir);
    }

    private class FileIterator implements Iterator<String> {
        private Queue<File> files = new LinkedList<>();

        public FileIterator(String path) {
            files.add(new File(root + path));
        }

        public boolean hasNext() {
            return !files.isEmpty();
        }

        public String next() {
            File file = files.peek();
            if (file != null && file.isDirectory()) {
                for (File subFile : file.listFiles()) {
                    files.add(subFile);
                }
            }

            return files.poll().getAbsolutePath();
        }

        public void remove() {

        }

    }

    public String getAbsolutePath(String file) {
        return new File(root + file).getAbsolutePath();
    }

}
