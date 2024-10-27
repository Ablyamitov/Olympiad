package com.cfuv.olympus.web.dto.contest.GetAllContests;

import lombok.Data;

import java.util.List;

@Data
public class GetAllContestsResponse {
    private Long count;
    private List<ContestsInfo> contestsInfos;
}
