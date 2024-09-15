package kookmin.kookmin.config.message;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
@Configuration
@Component
public class MessageJsonParser {
    private JSONObject messageArray;

    @Autowired
    private MessageComponent messageComponent;
    public MessageJsonParser(MessageComponent messageComponent) throws IOException, ParseException {
        this.messageComponent = messageComponent;
        JSONParser parser = new JSONParser();
        // JSON file read
        //Reader reader = new FileReader("/Users/kimdonghyun/Desktop/sumichan/message.json");
        // 하드 코딩인데이거... 시발 이걸 절대경로로 박아두네ㅠ 쩝....

        Reader reader = new FileReader("/Users/kimdonghyun/Desktop/kookmin/message.json");
        messageArray = (JSONObject) parser.parse(reader);
        this.setMessageData();
    }

    public void setMessageData() {
        messageComponent.setEMAIL_CHECK_FAILED((String) messageArray.get("EMAIL_CHECK_FAILED"));
        messageComponent.setPWD_CHECK_FAILED((String) messageArray.get("PWD_CHECK_FAILED"));
        messageComponent.setPWD_IS_NOTEQUAL((String) messageArray.get("PWD_IS_NOTEQUAL"));
        messageComponent.setNICKNAME_IS_NOT_KOREAN((String) messageArray.get("NICKNAME_IS_NOT_KOREAN"));
        messageComponent.setSIGNUP_SUCCESS((String) messageArray.get("SIGNUP_SUCCESS"));
    }
}