package com.med.vnpayservice.controller;

import com.med.vnpayservice.service.VNPayService;
import com.med.vnpayservice.config.VNPayConfig;
import com.med.vnpayservice.model.VNPayResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class VNPayController {

    @Autowired
    private VNPayService vnpayService;

    @GetMapping("/create-payment")
    public ResponseEntity<?> create(
            @RequestParam("amount") int amount,
            @RequestParam("orderInfo") int orderInfo,
            HttpServletRequest request) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);

        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_IpAddr", VNPayConfig.getIpAddress(request));
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", String.valueOf(orderInfo));
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_Locale", "vn");


        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        VNPayResponse res = new VNPayResponse();
        res.setStatus("Ok");
        res.setMessage("Success");
        res.setURL(paymentUrl);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/payment-response")
    public ResponseEntity updatePaidAppointment(@RequestParam("vnp_OrderInfo") String orderInfo,
                                                @RequestParam("vnp_PayDate") String paymentTime) {
        try {
//            vnpayService.updatePaidAppointment(orderInfo, paymentTime);
            vnpayService.updatePaidAppointment(orderInfo, paymentTime).subscribe(
                    response -> {
                        System.out.println("Response: " + response);
                    },
                    error -> {
                        System.err.println("Error: " + error);
                    }
            );
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    public static String convertToFormattedDateTime(String input) {
        try {
            // Extract components from the input string
            int year = Integer.parseInt(input.substring(0, 4));
            int month = Integer.parseInt(input.substring(4, 6));
            int day = Integer.parseInt(input.substring(6, 8));
            int hour = Integer.parseInt(input.substring(8, 10));
            int minute = Integer.parseInt(input.substring(10, 12));
            int second = Integer.parseInt(input.substring(12, 14));

            // Format the components into the desired format
            String formattedOutput = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);

            return formattedOutput;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle parsing or formatting errors as needed
        }
    }
}
