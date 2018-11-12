package be.ipieter.chat.config;

import org.springframework.core.io.InputStreamResource;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Assets (images for example) can be stored with various storage providers. To streamline migrating
 * to another storage, we recommend using this strategy.
 *
 * @author Pieter
 * @version 0.2
 */
public interface StorageStrategy {

    /**
     * Locate a file on the specified storage. Throws an FileNotFoundException if---you guessed
     * it---a file is not found.
     *
     * @param identifier The identifier that is used to locate the file.
     * @return An inputStream of the file.
     * @throws FileNotFoundException Throws an exception if the file is not found, this could mean the identifier is
     *                               wrong.
     */
    InputStreamResource getInputStream( String identifier ) throws FileNotFoundException;

    /**
     * Returns the size of the file.
     *
     * @param identifier
     * @return
     */
    Long getSize( String identifier );

    /**
     * Provides a DataOutputStream for a given identifier. The identifier should be unique. If this is
     * not the case, a FileAlreadyExistsException will be thrown.
     * <p>
     * The identifier may also include the file extension (like *.pdf), but this is not required. This
     * only means the hosting FS can not directly infer the type of all files if it's not provided.
     *
     * @param identifier The identifier of the file, may contain a extension (e.g. *.pdf) or not.
     * @return A DataOutputStream for the given identifier.
     * @throws java.nio.file.FileAlreadyExistsException When the identifier is not unique.
     * @throws IOException If a general IO exception occurred.
     */
    DataOutputStream createNewFile( String identifier ) throws IOException;

    /**
     * Deletes a file for a given identifier.
     *
     * @param identifier The identifier of the file to be deleted.
     * @throws FileNotFoundException When the identifier is not valid.
     */
    void deleteFile( String identifier ) throws FileNotFoundException;

    /**
     * This is a simple, non-binding util method that provides a list of all the files
     * in the storage cluster.
     *
     * @return A List of filenames, as strings.
     */
    List<String> getFilesInStorage();
}
