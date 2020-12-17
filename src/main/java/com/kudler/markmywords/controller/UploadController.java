package com.kudler.markmywords.controller;

import com.kudler.markmywords.exception.BadParameterException;
import com.kudler.markmywords.response.MarkovChainResponse;
import com.kudler.markmywords.service.FileService;
import com.kudler.markmywords.service.MarkovService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller used to declare our API endpoint that will accept text files and return resulting Markov Chains.
 * @author  Eric Kudler
 * @version 1.0
 * @since   2020-12-11
 */
@RestController
public class UploadController {

    @Autowired
    FileService fileService;
    @Autowired
    MarkovService markovService;

    /**
     * This method intializes an endpoint for accepting
     * a text file and some optional parameters to be transformed
     * via Markov chain.
     * @param file File containing text that will used to create chain
     * @param n Size of the ngram, which determines how many
     *          adjacent words must be grouped together to create a
     *          prefix, defaults to 1
     * @param length Number of words in the markov chain.  If length < 1,
     *               the chain will continue to run until it chooses the
     *               last word in the original string.
     * @param prefix The words that will begin the markov chain.
     *               If empty, the chain will begin with the text
     *               file's first "n" words.
     * @return An object containing both our
     *               original text string from the uploaded file,
     *               and the Markov Chain generated
     */
    @PostMapping("/upload")
    public MarkovChainResponse upload(@RequestParam(required = true) MultipartFile file,
                                      @RequestParam(defaultValue = "1", required = false) Integer n,
                                      @RequestParam(defaultValue = "0", required = false) Integer length,
                                      @RequestParam(required = false) String prefix) {
        if (file.isEmpty()) {
            throw new BadParameterException("Sorry, one of your parameters was invalid.  " +
                    "Please make sure you have a 'file' field of type 'File' that contains at least one word.");
        }

        if (n < 1) {
            throw new BadParameterException("Sorry, your prefix size must be one or greater.");
        }

        if (length > 0 && length <= n) {
            throw new BadParameterException("Sorry, the number of words allowed must be larger than the number " +
                    "of words in the prefix.  If you do not want an upper bound on words, set length to '0' or don't " +
                    "include it.");
        }

        //Service for uploading the file and returning the text inside
        String fileContent = fileService.uploadFile(file);
        String[] words = fileContent.split(MarkovService.DELIMITER_REGEX);
        if (n > words.length) {
            throw new BadParameterException("Sorry, you can't have a prefix larger than the size of your text.");
        }

        //Generate the Markov Chain using the service, then return it as a JSON
        String result = markovService.chain(fileContent, n, length, prefix);
        return new MarkovChainResponse(fileContent, result);
    }
}
