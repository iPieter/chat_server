package be.ipieter.chat.files.images;

import be.ipieter.chat.config.StorageStrategy;
import be.ipieter.chat.user.InvalidUsernameOrPasswordException;
import be.ipieter.chat.user.User;
import be.ipieter.chat.user.UserComponent;
import be.ipieter.chat.user.UsernameNotFoundException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.util.Random;

/**
 * @author Pieter
 * @version 1.0
 */
@RestController
@RequestMapping( "/images" )
public class ImageController
{
    private static final Logger LOGGER                  = LoggerFactory.getLogger( ImageController.class );
    private static final int    IMAGE_IDENTIFIER_LENGTH = 32;

    @Autowired
    private StorageStrategy imageStorage;

    @Autowired
    private UserComponent userComponent;

    public ImageController()
    {
    }

    @RequestMapping( value = "{image:.+}", method = RequestMethod.GET )
    public ResponseEntity getImage( @PathVariable String image ) throws FileNotFoundException
    {
        LOGGER.info( "Requested image {}.", image );


        return ResponseEntity.ok( imageStorage.getInputStream( image ) );
    }

    @RequestMapping( value = "/", method = RequestMethod.POST )
    public ResponseEntity postImage( @RequestParam( "file" ) MultipartFile file )
    {
        LOGGER.info( "Posting image initiated." );

        if ( file.isEmpty() )
        {

            LOGGER.info( "Invalid file, file is empty." );

            return ResponseEntity.badRequest().build();
        }

        String identifier = createImage( file );

        return ResponseEntity.ok( new Image( identifier, file.getContentType() ) );
    }

    @RequestMapping( value = "/profile", method = RequestMethod.POST )
    public ResponseEntity postImage( @RequestParam String username,
                                     @RequestParam String password,
                                     @RequestParam( "file" ) MultipartFile file ) throws UsernameNotFoundException, InvalidUsernameOrPasswordException
    {
        LOGGER.info( "Posting profile image initiated for username {}.", username );

        User user = userComponent.getUserForEmailWithPasswordCheck( username, password );

        if ( file.isEmpty() )
        {

            LOGGER.info( "Invalid file, file is empty." );

            return ResponseEntity.badRequest().build();
        }

        String identifier = createImage( file );

        userComponent.updateProfileImage( identifier, user );

        return ResponseEntity.ok( new Image( identifier, file.getContentType() ) );
    }

    /**
     * Create a identifier and save the file to persistent storage.
     *
     * @param file
     * @return
     */
    private String createImage( MultipartFile file )
    {
        //Generate identifier
        byte[] r      = new byte[ IMAGE_IDENTIFIER_LENGTH ];
        Random random = new Random();
        random.nextBytes( r );
        String identifier = Base64.encodeBase64URLSafeString( r );

        LOGGER.info( "Setting identifier={} for image.", identifier );

        try
        {
            DataOutputStream dataOutputStream = imageStorage.createNewFile( identifier );
            InputStream      inputStream      = file.getInputStream();


            byte[] buffer = new byte[ 8 * 1024 ];
            int    bytesRead;
            while ( ( bytesRead = inputStream.read( buffer ) ) != -1 )
            {
                dataOutputStream.write( buffer, 0, bytesRead );
            }

            inputStream.close();

        }
        catch ( FileAlreadyExistsException e )
        {

            LOGGER.warn( "Attempted to create already existing file {}.", identifier );

        }
        catch ( IOException e )
        {

            LOGGER.error( "Error reading {}", identifier );
        }

        return identifier;
    }


}
