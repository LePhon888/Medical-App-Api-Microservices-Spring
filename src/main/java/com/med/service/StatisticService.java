package com.med.service;

import com.med.repository.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatisticService {
    @Autowired
    private StatisticRepository statsRepository;

    public List<Object[]> countFrequentPatientVisitsByTime(Map<String, Object> params) {

        if (params != null) {

            Object year = params.get("year" );
            if (year != null) {
                    params.put("year", Integer.valueOf(year.toString()));
            } else {
                params.put("year", new Date().getYear());
            }

            Object quarter = params.get("quarter" );
            if (quarter != null) {
                    params.put("quarter", Integer.valueOf(quarter.toString()));
            } else {
                params.put("quarter", 0);
            }

            Object month = params.get("month" );
            if (month != null) {
                    params.put("month", Integer.valueOf(month.toString()));
            } else {
                params.put("month", 0);
            }

        }
        List<Object[]> list = new ArrayList<>();
        list.addAll(this.statsRepository.countTotalPatientsByTime(params));
        list.addAll(this.statsRepository.countFrequentPatientVisitsByTime(params));
        return list;
    }

    public List<Object[]> getRevenueByTime(Map<String, Object> params) {
        if (params != null) {

            Object year = params.get("year" );
            if (year != null) {
                    params.put("year", Integer.valueOf(year.toString()));
            } else {
                params.put("year", new Date().getYear());
            }

            Object quarter = params.get("quarter" );
            if (quarter != null) {
                    params.put("quarter", Integer.valueOf(quarter.toString()));
            } else {
                params.put("quarter", 0);
            }

            Object month = params.get("month" );
            if (month != null) {
                    params.put("month", Integer.valueOf(month.toString()));
            } else {
                params.put("month", 0);
            }

        }
        return this.statsRepository.getRevenueByTime(params);
    }
}
