package be.ipieter.chat.files.emoji;

import be.ipieter.chat.config.StorageStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * @author Pieter
 * @version 1.0
 */
@RestController
@RequestMapping( "/emojis" )
public class EmojiController
{
    @Autowired
    private StorageStrategy emojiStorage;

    public EmojiController()
    {
    }

    @RequestMapping( value = "{emoji:.+}", method = RequestMethod.GET )
    public ResponseEntity getEmoji( @PathVariable String emoji ) throws FileNotFoundException
    {
        return ResponseEntity.ok( emojiStorage.getInputStream( emoji ));
    }

    @RequestMapping( value = "/", method = RequestMethod.GET )
    public ResponseEntity getEmojiList() throws FileNotFoundException
    {
        return ResponseEntity.ok( emojiStorage.getFilesInStorage());
    }

}
