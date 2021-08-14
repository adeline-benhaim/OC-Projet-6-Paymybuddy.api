package com.paymybuddy.api.service;

import com.paymybuddy.api.model.Commission;

import java.util.List;

public interface CommissionService {

    List<Commission> getDailyCommissions();

    List<Commission> getMonthlyCommissions();

    List<Commission> getYearCommissions();

    double getTotalDay();

    double getTotalMonth();

    double getTotalYear();

}




