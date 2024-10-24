package com.mycompany.autobackupprogram;

import static com.mycompany.autobackupprogram.BackupManagerGUI.OpenExceptionMessage;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

public class TreeCopyFileVisitor extends SimpleFileVisitor<Path> implements Runnable {
    private final Path source;
    private final Path target;
    private boolean copied = false;
    private final FileCopyListener listener;
    private int copiedFilesCount;
    private final int totalFilesCount;

    public TreeCopyFileVisitor(String source, String target, int totalFilesCount, FileCopyListener listener) {
        this.source = Paths.get(source);
        this.target = Paths.get(target);
        this.listener = listener;
        this.copiedFilesCount = 0;
        this.totalFilesCount = totalFilesCount;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path resolve = target.resolve(source.relativize(dir));
        if (Files.notExists(resolve)) {
            Files.createDirectories(resolve);
            copied = true;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path resolve = target.resolve(source.relativize(file));
        Files.copy(file, resolve, StandardCopyOption.REPLACE_EXISTING);
        copiedFilesCount++;
        int progress = (int) (((double) copiedFilesCount / totalFilesCount) * 100);
        listener.onFileCopied(progress);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.format("Error -> Unable to copy: %s: %s%n", file, exc);
        copied = false;
        return FileVisitResult.CONTINUE;
    }

    public boolean isCopied() {
        return copied;
    }

    @Override
    public void run() {
        try {
            Files.walkFileTree(source, this);
        } catch (IOException ex) {
            Logger.logMessage("Exception --> " + ex);
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        }
    }
}
