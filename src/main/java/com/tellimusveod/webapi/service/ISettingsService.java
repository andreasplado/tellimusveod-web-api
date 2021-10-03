package com.tellimusveod.webapi.service;

import com.tellimusveod.webapi.dao.entity.SettingsEntity;

import java.util.List;

public interface ISettingsService {

    SettingsEntity save(SettingsEntity settingsEntity);
    boolean exists(Integer userId);
    List<SettingsEntity> findAll();
    SettingsEntity getUserSettings(Integer userId);
    void updateRadius(Integer userId, Double radius);
    void updateViewByDefault(Integer userId, String value);
    void updateAskPermissionBeforeDeletingAJob(Integer userId, Boolean value);
    void updateShowInformationOnStartup(Integer userId, Boolean value);
    void updateBiometric(Integer userId, Boolean value);
    void removeAllPersonsWhoAreNotMemberAnyMore();
    void purchaseMember(String role, Integer userId);
}
