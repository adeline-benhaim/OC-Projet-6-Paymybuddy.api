package com.paymybuddy.api.service;

import com.paymybuddy.api.model.Commission;
import com.paymybuddy.api.repository.CommissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommissionServiceImpl implements CommissionService {

    @Autowired
    private CommissionRepository commissionRepository;

    public List<Commission> getDailyCommissions() {

        Date date = new Date();
        Timestamp start = new Timestamp(date.getTime());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Timestamp end = new Timestamp(c.getTimeInMillis());
        return commissionRepository.findByDateBetween(start, end);
    }

    public double getTotalDay() {
        List<Commission> commissionList = getDailyCommissions();
        List<Double> amountList = commissionList
                .stream()
                .map(Commission::getAmount)
                .collect(Collectors.toList());
        return amountList.stream()
                .mapToDouble(a -> a)
                .sum();
    }

    public List<Commission> getMonthlyCommissions() {

        Date date = new Date();
        Timestamp start = new Timestamp(date.getTime());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -30);
        Timestamp end = new Timestamp(c.getTimeInMillis());
        return commissionRepository.findByDateBetween(start, end);
    }

    public double getTotalMonth() {
        List<Commission> commissionList = getMonthlyCommissions();
        List<Double> amountList = commissionList
                .stream()
                .map(Commission::getAmount)
                .collect(Collectors.toList());
        return amountList.stream()
                .mapToDouble(a -> a)
                .sum();
    }

    public List<Commission> getYearCommissions() {

        Date date = new Date();
        Timestamp start = new Timestamp(date.getTime());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -365);
        Timestamp end = new Timestamp(c.getTimeInMillis());
        return commissionRepository.findByDateBetween(start, end);
    }

    public double getTotalYear() {
        List<Commission> commissionList = getYearCommissions();
        List<Double> amountList = commissionList
                .stream()
                .map(Commission::getAmount)
                .collect(Collectors.toList());
        return amountList.stream()
                .mapToDouble(a -> a)
                .sum();
    }

}

