package com.madhav.service.interfaces;

import com.madhav.entities.PlanType;
import com.madhav.entities.Subscription;
import com.madhav.entities.User;

public interface SubscriptionService {

    Subscription createSubscription(User user);

    Subscription getUserSubscription(Long userId)throws Exception;

    Subscription upgrdeSubscription(Long userId, PlanType planType);

    boolean isValid(Subscription subscription);
}
