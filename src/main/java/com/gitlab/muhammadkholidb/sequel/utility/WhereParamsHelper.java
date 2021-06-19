package com.gitlab.muhammadkholidb.sequel.utility;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WhereParamsHelper {
    private StringBuilder queryAppend = new StringBuilder();
    private List<Object> params = new ArrayList<>();
}
