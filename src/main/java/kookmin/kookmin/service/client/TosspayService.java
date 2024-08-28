//package kookmin.kookmin.service.client;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessException;
//import org.springframework.stereotype.Service;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import kookmin.kookmin.config.TosspayConfig;
//import kookmin.kookmin.dto.client.order.FinalOrderDto;
//import kookmin.kookmin.dto.client.order.OrderFailDto;
//import kookmin.kookmin.dto.client.order.PreOrderDto;
//import kookmin.kookmin.mapper.client.TosspayMapper;
//
//import java.util.HashMap;
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//
//@Service
//@Slf4j
//public class TosspayService {
//    @Autowired
//    private TosspayMapper tosspayMapper;
//    @Autowired
//    private TosspayConfig tosspayConfig;
//    public boolean preOrderService(PreOrderDto preOrderDto)
//    {
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("orderId", preOrderDto.getOrderId());
//        paramMap.put("amount", preOrderDto.getAmount());
//        paramMap.put("status",  "결제 진행중");
//
//        try
//        {
//            tosspayMapper.preOrderMapper(paramMap);
//            return true;
//        }
//        catch (DataAccessException e)
//        {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean orderFailService(OrderFailDto orderFailDto)
//    {
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("orderId", orderFailDto.getOrderId());
//        try
//        {
//            tosspayMapper.orderFailMapper(paramMap);
//            return true;
//        }
//        catch (DataAccessException e)
//        {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean finalOrderService(FinalOrderDto finalOrderDto) throws Exception
//    {
//        //Model model = new Model();
//        //결제 금액 맞는지 확인좀요
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("orderId", finalOrderDto.getOrderId());
//        if ((int) tosspayMapper.findPreOrder(paramMap).get("Amount") != finalOrderDto.getAmount())
//        {
//            throw new RuntimeException("결제 금액이 다릅니다.");
//        }
//
//        //찐 결제 하는 부분!
//        String secretKey = tosspayConfig.getSecretApiKey();
//
//        Base64.Encoder encoder = Base64.getEncoder();
//        byte[] encodedBytes = encoder.encode(secretKey.getBytes("UTF-8"));
//        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);
//
//        URL url = new URL("https://api.tosspayments.com/v1/payments/" + finalOrderDto.getPaymentKey());
//
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestProperty("Authorization", authorizations);
//        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setRequestMethod("POST");
//        connection.setDoOutput(true);
//        JSONObject obj = new JSONObject();
//        obj.put("orderId", finalOrderDto.getOrderId());
//        obj.put("amount", finalOrderDto.getAmount());
//
//        OutputStream outputStream = connection.getOutputStream();
//        outputStream.write(obj.toString().getBytes("UTF-8"));
//
//        int code = connection.getResponseCode();
//        boolean isSuccess = code == 200 ? true : false;
//
//        if (!isSuccess)
//        {
//            return false;
//        }
//
//        //model.addAttribute("isSuccess", isSuccess);
//
//        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();
//
//        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
//        JSONParser parser = new JSONParser();
//        JSONObject jsonObject = (JSONObject) parser.parse(reader);
//        responseStream.close();
//        //model.addAttribute("responseStr", jsonObject.toJSONString());
//        //System.out.println(jsonObject.toJSONString());
//
//        paramMap.put("orderId",  (String) jsonObject.get("orderId"));
//        paramMap.put("paymentKey",  (String) jsonObject.get("paymentKey"));
//        paramMap.put("amount",  (String) jsonObject.get("totalAmount"));
//        paramMap.put("orderName",  (String) jsonObject.get("orderName"));
//        paramMap.put("status",  "결제 완료");
//        paramMap.put("requestedAt",  (String) jsonObject.get("requestedAt"));
//        paramMap.put("approvedAt",  (String) jsonObject.get("approvedAt"));
//        paramMap.put("receipt",  (String) ((JSONObject) jsonObject.get("receipt")).get("url"));
//        paramMap.put("method",  (String) jsonObject.get("method"));
//        paramMap.put("qty",  finalOrderDto.getQty());
//        paramMap.put("phone",  finalOrderDto.getPhone());
//        paramMap.put("address",  finalOrderDto.getAddress());
//        paramMap.put("deliveryDateTime",  finalOrderDto.getDeliveryDateTime());
//
///*
//        // 여기는 그냥 json parsing하는 부분이네~
//        model.addAttribute("method", (String) jsonObject.get("method"));
//        model.addAttribute("orderName", (String) jsonObject.get("orderName"));
//
//        if (((String) jsonObject.get("method")) != null) {
//            if (((String) jsonObject.get("method")).equals("카드")) {
//                model.addAttribute("cardNumber", (String) ((JSONObject) jsonObject.get("card")).get("number"));
//            } else if (((String) jsonObject.get("method")).equals("가상계좌")) {
//                model.addAttribute("accountNumber", (String) ((JSONObject) jsonObject.get("virtualAccount")).get("accountNumber"));
//            } else if (((String) jsonObject.get("method")).equals("계좌이체")) {
//                model.addAttribute("bank", (String) ((JSONObject) jsonObject.get("transfer")).get("bank"));
//            } else if (((String) jsonObject.get("method")).equals("휴대폰")) {
//                model.addAttribute("customerMobilePhone", (String) ((JSONObject) jsonObject.get("mobilePhone")).get("customerMobilePhone"));
//            }
//        } else {
//            model.addAttribute("code", (String) jsonObject.get("code"));
//            model.addAttribute("message", (String) jsonObject.get("message"));
//        }
//*/
//
//        try
//        {
//            tosspayMapper.finalOrderMapper(paramMap);
//        }
//        catch (DataAccessException e)
//        {
//            e.printStackTrace();
//        }
//
//        return true;
//    }
//}
