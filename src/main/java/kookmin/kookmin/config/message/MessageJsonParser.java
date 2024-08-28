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

        Reader reader = new FileReader("/Users/kimdonghyun/Desktop/sumichan/spring_boot_study/message.json");
        messageArray = (JSONObject) parser.parse(reader);
        this.setMessageData();
    }

    public void setMessageData() {
        messageComponent.setLOG_IN((String) messageArray.get("LOG_IN"));
        messageComponent.setNO_MEMBER((String) messageArray.get("NO_MEMBER"));
        messageComponent.setREGISTER_SUCCESS((String) messageArray.get("REGISTER_SUCCESS"));
        messageComponent.setAUTH_FAIL((String) messageArray.get("AUTH_FAIL"));
        messageComponent.setAUTH_SUCCESS((String) messageArray.get("AUTH_SUCCESS"));
        messageComponent.setEXPIRED_TOKEN((String) messageArray.get("EXPIRED_TOKEN"));
        messageComponent.setSUCCESS((String) messageArray.get("SUCCESS"));
        messageComponent.setFAIL((String) messageArray.get("FAIL"));
        messageComponent.setINVAILD_ACCESS((String) messageArray.get("INVAILD_ACCESS"));
    }
}