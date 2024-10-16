package com.spoctox.campaign_planner_service.internal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spoctox.campaign_planner_service.common.constants.RequestHeadersConstants;

import com.spoctox.campaign_planner_service.internal.dto.ResponseInfoDto;
import com.spoctox.campaign_planner_service.internal.models.Campaign;

import com.spoctox.campaign_planner_service.internal.services.CampaignService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/collections/api/v1/campaign-planner")
public class CampaignController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CampaignController.class);
    private final CampaignService campaignService;

    private ObjectMapper objectMapper;


    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @PostMapping("/campaigns")
    public ResponseEntity<ResponseInfoDto> createCampaign( @RequestHeader(RequestHeadersConstants.KEY_ENTITY_ID) String entityId,
                                                           @RequestHeader(name = RequestHeadersConstants.KEY_USER_ID) String userId, @RequestHeader(name = RequestHeadersConstants.KEY_USER_NAME) String userName,
                                                           @Valid @RequestBody Campaign campaignRequest) throws Exception {
        try {
            LOGGER.info("Received request to create a new campaign.");
            LOGGER.info("Campaign request: {}", objectMapper.writeValueAsString(campaignRequest));
            campaignRequest.setUserId(userId);
            campaignRequest.setCreatedBy(userName);
            ResponseInfoDto responseCampaign = campaignService.createCampaign(campaignRequest);
            return ResponseEntity.status(HttpStatus.OK).body(responseCampaign);
        }catch (HttpClientErrorException e) {
            LOGGER.error("Error while creating campaign: {}", e.getMessage());
            throw new HttpClientErrorException(e.getStatusCode(),e.getMessage());
        }

    }


}
