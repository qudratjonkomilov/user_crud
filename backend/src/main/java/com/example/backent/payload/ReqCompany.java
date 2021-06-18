package com.example.backent.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCompany {

    private Long id;
    private String name;
//    **** data for edit ****
    private List<Long> deleteFile;

    public ReqCompany(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
