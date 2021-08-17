package com.paymybuddy.api.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

public class PaginationUtils {

    static void paginationBuilder(Model model, Pageable pageable, long totalElements, String baseUri) {
        int actualPageNumber = pageable.getPageNumber();
        model.addAttribute("intActualPage", actualPageNumber);
        String actualPageUrl = baseUri + actualPageNumber;
        model.addAttribute("actualPageUrl", actualPageUrl);

        if (pageable.getPageNumber() >= 1) {
            int previousPage = pageable.getPageNumber() - 1;
            String previousPageUrl = baseUri + previousPage;
            model.addAttribute("previousPageUrl", previousPageUrl);
            model.addAttribute("intPreviousPage", previousPage);
        }
        if (totalElements > (long) (pageable.getPageNumber() + 1) * pageable.getPageSize()) {
            int nextPage = pageable.getPageNumber() + 1;
            model.addAttribute("intNextPage", nextPage);
            String nextPageUrl = baseUri + nextPage;
            model.addAttribute("nextPageUrl", nextPageUrl);
        }
    }
}
