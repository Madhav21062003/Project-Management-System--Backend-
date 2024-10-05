package com.madhav.controller;

import com.madhav.entities.PlanType;
import com.madhav.entities.User;
import com.madhav.response.PaymentLinkResponse;
import com.madhav.service.interfaces.UserService;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {


    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    @Autowired
    private UserService userService;

    @PostMapping("/{planType}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @PathVariable PlanType planType,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        int amount = 799 * 100;
        if (planType.equals(PlanType.ANNUALLY)) {
            amount = amount * 12;
            // Giving 30% off
            amount = (int) (amount * 0.7);
        }


        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", amount);
        paymentLinkRequest.put("currency", "INR");

        JSONObject customer = new JSONObject();
        customer.put("name", user.getFullName());
        customer.put("email", user.getEmail());

        paymentLinkRequest.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("email", true);
        paymentLinkRequest.put("notify", notify);

        paymentLinkRequest.put("callback_url", "http://localhost:5173/updrade_plan/success?planType" + planType);

        PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
        String paymentLinkId = payment.get("id");
        String paymentLinkUrl = payment.get("short_url");
        PaymentLinkResponse response = new PaymentLinkResponse();
        response.setPayment_link_url(paymentLinkUrl);
        response.setPayment_link_id(paymentLinkId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

}
