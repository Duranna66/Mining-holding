package ru.holding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public  class IncomeExpenseDynamicsResponse {
    private List<String> months;
    private List<Integer> incomes;
    private List<Integer> expenses;
}
