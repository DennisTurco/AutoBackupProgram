package application;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TreeCopyFileVisitor extends SimpleFileVisitor<Path> {
		
    private Path source;
    private final Path target;
    AutoBackupProgram abp = new AutoBackupProgram();

    public TreeCopyFileVisitor(String source, String target) {
        this.source = Paths.get(source);
        this.target = Paths.get(target);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir,
        BasicFileAttributes attrs) throws IOException {

        Path resolve = target.resolve(source.relativize(dir));
        if (Files.notExists(resolve)) {
            Files.createDirectories(resolve);
            try {
            	BufferedWriter bw = new BufferedWriter(new FileWriter(".//res//log_file"));
            	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
    			LocalDateTime now = LocalDateTime.now();
    			String date = dtf.format(now);
    			bw.write("last backup: " + date);
    			bw.write("Create directories : " + resolve);
    			bw.write("\n");
    			bw.close();

            } catch(Exception ex) {
            	System.out.println(ex);
            }
            System.out.println("Create directories : " + resolve);
            abp.setCopied(true); //setto a true perchè la copia è avvenuta con successo 
        }
        return FileVisitResult.CONTINUE;

    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
        throws IOException {

        Path resolve = target.resolve(source.relativize(file));
        Files.copy(file, resolve, StandardCopyOption.REPLACE_EXISTING);
        try {
        	BufferedWriter bw = new BufferedWriter(new FileWriter(".//res//log_file"));
			bw.write("Copy File from \t" + file + "\t to" + resolve);
			bw.write("\n");
			bw.close();
        } catch(Exception ex) {
        	System.out.println(ex);
        }
        System.out.println(
                String.format("Copy File from \t'%s' to \t'%s'", file, resolve)
        );
        abp.setCopied(true);//setto a true perchè la copia è avvenuta con successo 
        return FileVisitResult.CONTINUE;

    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.format("Unable to copy: %s: %s%n", file, exc);
        abp.setCopied(false); //setto a false perchè la copia NON è avvenuta con successo 
        return FileVisitResult.CONTINUE;
    }

}
