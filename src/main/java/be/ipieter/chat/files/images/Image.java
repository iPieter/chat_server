package be.ipieter.chat.files.images;

import lombok.Data;
import org.springframework.http.MediaType;

/**
 * @author Pieter
 * @version 1.0
 */
@Data
public class Image
{
    private String identifier;
    private String mediaType;

    public Image( String identifier, String mediaType )
    {
        this.identifier = identifier;
        this.mediaType = mediaType;
    }
}
