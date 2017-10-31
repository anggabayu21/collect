package com.gic.collect.android.utilities;

import org.opendatakit.httpclientandroidlib.HttpEntity;
import org.opendatakit.httpclientandroidlib.util.EntityUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import timber.log.Timber;

/**
 * Created by Angga Bayu on 31/10/17.
 * The purpose of this class is to handle the XML parsing
 * of the server responses
 * @author Angga Bayu (angga.bayu.marthafifsa@gmail.com)
 */

public class ResponseMessageRegisterUserParser {
    private HttpEntity httpEntity;
    private static final String MESSAGE_XML_TAG = "message";
    public boolean isValid;
    public String messageResponse;

    public ResponseMessageRegisterUserParser(HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
        this.messageResponse = parseHTMLMessage();
        if (messageResponse != null) {
            this.isValid = true;
        }
    }

    private HttpEntity getHttpEntity() {
        return httpEntity;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public String getMessageResponse() {
        return this.messageResponse;
    }

    public String parseHTMLMessage() {
        String message = null;

        try{
            String httpEntityString = EntityUtils.toString(httpEntity);
            Timber.i("Test httpEntityString = " + httpEntityString);
            if (httpEntityString.contains("errorlist")) {
                Timber.i("Test Error list");

                if(httpEntityString.contains("email address is already in use")){
                    Timber.i("Test Email address is already in use");
                    return "Error : Email address is already in use";
                }
                else if(httpEntityString.contains("already exists")){
                    Timber.i("Test Username already exists");
                    return "Error : Username already exists";
                }
                else if(httpEntityString.contains("Enter a valid email address")){
                    Timber.i("Test Enter a valid email address");
                    return "Error : Enter a valid email address";
                }
                else if(httpEntityString.contains("The two password fields didn&#39;t match")) {
                    Timber.i("Test The two password fields didn't match");
                    return "Error : The two password fields didn't match";
                }
                else{
                    Timber.i("Test error");
                    return "Error";
                }
            }
        }
        catch(Exception e){
            return "Error = "+ e.getMessage();
        }

        return message;


    }


}
