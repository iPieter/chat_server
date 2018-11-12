package be.ipieter.chat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

/**
 * Assets (images for example) can be stored with various storage providers; this implements a
 * local filesystem storage option. Assets are saved in a folder under the home dir of the account
 * running the server.
 *
 * @author Pieter
 * @version 0.2
 */
public class LocalFSStorageStrategy implements StorageStrategy
{

    private final String RESOURCE_DIR;

    private static final Logger LOGGER = LoggerFactory.getLogger( LocalFSStorageStrategy.class );

    /**
     * When the object is created, this will verify it the resource directory exists.
     */
    public LocalFSStorageStrategy( String addition )
    {

        RESOURCE_DIR = System.getProperty( "user.home" ) + addition;

        //Checking if it's a directory will also check it's existence.
        if ( !new File( RESOURCE_DIR ).isDirectory() )
        {
            LOGGER.warn( "The resource location ({}) doesn't exist, it will be created.", RESOURCE_DIR );


            if ( !new File( RESOURCE_DIR ).mkdirs() )
            {
                LOGGER.warn( "The resource location ({}) could not be created.", RESOURCE_DIR );
            }

        }
    }

    /**
     * Locate a file on local file storage. Throws an FileNotFoundException if---you guessed
     * it---a file is not found.
     *
     * @param identifier The identifier that is used to locate the file on the FS.
     * @return An inputStream of the file on the local file system.
     * @throws FileNotFoundException Throws an exception if the file is not found, this could mean the identifier is
     *                               wrong.
     */
    @Override
    public InputStreamResource getInputStream( String identifier ) throws FileNotFoundException
    {

        String filePath = RESOURCE_DIR + identifier;

        File file = new File( filePath );

        return new InputStreamResource( new FileInputStream( file ) );
    }

    @Override
    public Long getSize( String identifier )
    {
        String filePath = RESOURCE_DIR + identifier;

        File file = new File( filePath );

        return file.length();
    }

    /**
     * Provides a DataOutputStream for a given identifier. The identifier should be unique. If this is
     * not the case, a FileAlreadyExistsException will be thrown.
     * <p>
     * The identifier may also include the file extension (like *.pdf), but this is not required. This
     * only means the local FS can not directly infer the type of all files if it's not provided.
     *
     * @param identifier The identifier of the file, may contain a extension (e.g. *.pdf) or not.
     * @return A DataOutputStream for the given identifier.
     * @throws FileAlreadyExistsException When the identifier is not unique.
     * @throws IOException                If a general IO exception occured.
     */
    @Override
    public DataOutputStream createNewFile( String identifier ) throws IOException
    {

        String filePath = RESOURCE_DIR + identifier;

        LOGGER.info( "Saving file to {}", filePath );

        File file = new File( filePath );

        if ( file.exists() )
        {
            throw new FileAlreadyExistsException( "This file already exists" );
        }

        return new DataOutputStream( new FileOutputStream( file ) );
    }

    /**
     * Deletes a file for a given identifier.
     *
     * @param identifier The identifier of the file to be deleted.
     * @throws FileNotFoundException When the identifier is not valid.
     */
    @Override
    public void deleteFile( String identifier ) throws FileNotFoundException
    {
        String filePath = RESOURCE_DIR + identifier;


        File file = new File( filePath );

        if ( file.exists() )
        {

            if ( file.delete() )
            {
                LOGGER.info( "Deleted file {}", filePath );

            }
        }
        else
        {
            throw new FileNotFoundException( "File with this identifier is not found." );
        }
    }

    /**
     * This is a simple, non-binding util method that provides a list of all the files
     * in the storage cluster.
     *
     * @return A List of filenames, as strings.
     */
    @Override
    public List <String> getFilesInStorage()
    {
        List <String> results = new ArrayList <String>();


        File[] files = new File( RESOURCE_DIR ).listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.

        for ( File file : files )
        {
            if ( file.isFile() )
            {
                results.add( file.getName() );
            }
        }

        return results;
    }


}
