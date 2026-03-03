package com.prtech.svarog.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.prtech.svarog.Sv;
import com.prtech.svarog.SvConf;
import com.prtech.svarog.SvException;
import com.prtech.svarog.SvLink;
import com.prtech.svarog.SvParameter;
import com.prtech.svarog.SvReader;
import com.prtech.svarog.SvSecurity;
import com.prtech.svarog.SvUtil;
import com.prtech.svarog.SvWriter;
import com.prtech.svarog.svCONST;
import com.prtech.svarog_common.DbDataArray;
import com.prtech.svarog_common.DbDataObject;
import com.prtech.svarog_common.DbSearchCriterion;
import com.prtech.svarog_common.DbSearchExpression;
import com.prtech.svarog_common.DbSearchCriterion.DbCompareOperand;

public class DatabaseCustomConfiguration {

	static final Logger log4j = SvConf.getLogger(DatabaseCustomConfiguration.class);

	public String defaultUserPassword = "welcome13";
	public String defaultUserPin = "123456789";
	public String defaultUserFirstName = "test";
	public String defaultUserLastName = "testLastName";
	public String defaultGroupType = Tc.DATA_ENTRY_CLERK;
	public String defaultUserPasswordForReporterUser = "Naits1234";
	public String defaultNewUserPassword = "naits2024";

	public String groupName1 = Tc.CVIRO;
	public String groupName2 = Tc.FVIRO;
	public String groupName3 = Tc.LABORANT;
	public String groupName4 = Tc.DATA_ENTRY_CLERK;
	public String groupName5 = Tc.HOLDING_REGISTRATORS;
	public String groupName6 = Tc.ANIMAL_REGISTRATORS;
	public String groupName7 = Tc.HOLDING_ADMINISTRATORS;
	public String groupName8 = Tc.AHSMT_ADMINS;
	public String groupName9 = Tc.QUARANTINE_ADMINISTRATORS;
	public String groupName10 = Tc.DIM_ADMINISTRATORS;
	public String groupName11 = Tc.SLAUGHTERHOUSE_ADMINISTRATORS;
	public String groupName12 = Tc.RYSK_ANALYZE_ADMINISTRATORS;
	public String groupName13 = Tc.PET_VETERINARIANS;
	public String groupName14 = Tc.BANK_AND_INSURANCE_COMPANIES;
	public String groupName15 = Tc.NAITS_ADMINISTRATORS;
	public String groupName16 = Tc.CUSTOMS_OFFICER;
	public String groupName17 = Tc.SHELTER_OPERATOR;

	public String userName1 = Tc.T_CVIRO;
	public String userName2 = Tc.T_FVIRO;
	public String userName3 = Tc.T_LABORANT;
	public String userName4 = Tc.T_DATA_ENTRY_CLERK;
	public String userName5 = Tc.T_HOLDING_REGISTRATORS;
	public String userName6 = Tc.T_ANIMAL_REGISTRATORS;
	public String userName7 = Tc.T_HOLDING_ADMINISTRATORS;
	public String userName8 = Tc.T_AHSMT_ADMINS;
	public String userName9 = Tc.T_QUARANTINE_ADMINISTRATORS;
	public String userName10 = Tc.T_DIM_ADMINISTRATORS;
	public String userName11 = Tc.T_SLAUGHTERHOUSE_ADMINISTRATORS;
	public String userName12 = Tc.T_RYSK_ANALYZE_ADMINISTRATORS;
	public String userName13 = Tc.T_PET_VETERINARIANS;
	public String userName14 = Tc.T_BANK_AND_INSURANCE_COMPANIES;
	public String userName15 = Tc.T_ADMIN;
	public String userName16 = Tc.T_CUSTOMS_OFFICER;
	public String userName17 = Tc.T_SHELTER_OPERATOR;

	public String permissionForUserGroups1 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.CVIRO
			+ ":SUBJECT.FULL,MESSAGE.FULL,card.spatial_access,card.naits_access,CARD_CONF.FULL,system.null_geometry,SECURITY_PERUN.LOGIN_PERUN,VILLAGE.FULL,HOLDING.READ,HOLDING_RESPONSIBLE.READ,ANIMAL.READ,FLOCK.READ,VACCINATION_BOOK.FULL,VACCINATION_EVENT.FULL,VACCINATION_RESULTS.FULL,ANIMAL_MOVEMENT.READ,\r\n"
			+ "FLOCK_MOVEMENT.READ,QUARANTINE.READ,AREA.FULL,AREA_HEALTH.READ,DISEASE.READ,DISEASE.FULL,ANIMAL_TYPE.READ,CRITERIA_TYPE.FULL,LABORATORY.READ,LABORATORY.FULL,LAB_SAMPLE.FULL,LAB_TEST_RESULT.FULL,LAB_TEST_TYPE.FULL,PET.READ";

	public String permissionForUserGroups2 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.FVIRO
			+ ":SUBJECT.FULL,MESSAGE.FULL,card.spatial_access,card.naits_access,CARD_CONF.FULL,system.null_geometry,SECURITY_PERUN.LOGIN_PERUN,VILLAGE.FULL,HOLDING.READ,HOLDING_RESPONSIBLE.READ,ANIMAL.FULL,FLOCK.READ,VACCINATION_BOOK.FULL,\r\n"
			+ "VACCINATION_EVENT.FULL,VACCINATION_RESULTS.FULL,ANIMAL_MOVEMENT.FULL,FLOCK_MOVEMENT.FULL,INVENTORY_ITEM.FULL,\r\n"
			+ "QUARANTINE.READ,AREA.FULL,AREA_HEALTH.READ,DISEASE.FULL,ANIMAL_TYPE.READ,CRITERIA_TYPE.FULL,SPOT_CHECK.FULL,EAR_TAG_REPLC.FULL,LABORATORY.FULL,LAB_SAMPLE.FULL,LAB_TEST_RESULT.FULL,LAB_TEST_TYPE.FULL,\r\n"
			+ "MOVEMENT_DOC.FULL,MOVEMENT_DOC_BLOCK.FULL,PET.READ,RFID_INPUT.FULL,RFID_INPUT_RESULT.FULL,RFID_INPUT_STATE.FULL,\r\n"
			+ "custom.invoice_report,custom.blank_report,HERD.READ,HERD_MOVEMENT.FULL";

	public String permissionForUserGroups3 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.LABORANT
			+ ":SUBJECT.FULL,MESSAGE.FULL,card.spatial_access,card.naits_access,CARD_CONF.FULL,system.null_geometry,SECURITY_PERUN.LOGIN_PERUN,VILLAGE.FULL,HOLDING.READ,HOLDING_RESPONSIBLE.READ,ANIMAL.READ,VACCINATION_RESULTS.FULL,QUARANTINE.READ,AREA.FULL,\r\n"
			+ "AREA_HEALTH.READ,DISEASE.FULL,ANIMAL_TYPE.READ,CRITERIA_TYPE.FULL,LABORATORY.FULL,LAB_SAMPLE.FULL,LAB_TEST_RESULT.FULL,\r\n"
			+ "LAB_TEST_TYPE.FULL";

	public String permissionForUserGroups4 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.DATA_ENTRY_CLERK
			+ ":SUBJECT.FULL,MESSAGE.FULL,card.spatial_access,card.naits_access,CARD_CONF.FULL,system.null_geometry,SECURITY_PERUN.LOGIN_PERUN,VILLAGE.FULL,HOLDING.FULL,HOLDING_RESPONSIBLE.FULL,ANIMAL.FULL,FLOCK.FULL,OTHER_ANIMALS.FULL,VACCINATION_BOOK.FULL,\r\n"
			+ "VACCINATION_EVENT.FULL,VACCINATION_RESULTS.FULL,ANIMAL_MOVEMENT.FULL,FLOCK_MOVEMENT.FULL,PRE_SLAUGHT_FORM.FULL,\r\n"
			+ "POST_SLAUGHT_FORM.FULL,SUPPLIER.FULL,ORDER.FULL,INVENTRY_ITEM.FULL,RANGE.FULL,TRANSFER.FULL,INVENTORY_ITEM.FULL,\r\n"
			+ "QUARANTINE.FULL,AREA.FULL,AREA_HEALTH.FULL,DISEASE.FULL,EXPORT_CERT.FULL,ANIMAL_TYPE.FULL,ANIMAL_HEALTH.FULL,\r\n"
			+ "POPULATION.FULL,SAMPLE.FULL,SELECTION_RESULT.FULL,CRITERIA.FULL,CRITERIA_TYPE.FULL,STRAT_FILTER.FULL,\r\n"
			+ "QUARANTINE_GEOMETRY.FULL,ANIMAL_ACTIVITY.FULL,ANIMAL_ORIGIN.FULL,SPOT_CHECK.FULL,EAR_TAG_REPLC.FULL,LABORATORY.READ,\r\n"
			+ "LAB_SAMPLE.FULL,LAB_TEST_RESULT.READ,LAB_TEST_TYPE.READ,MOVEMENT_DOC.FULL,MOVEMENT_DOC_BLOCK.FULL,PET.FULL,\r\n"
			+ "PET_HEALTH_BOOK.FULL,PET_PASSPORT.FULL,PASSPORT_REQUEST.FULL,custom.statistical_report,HEALTH_PASSPORT.FULL,\r\n"
			+ "STRAY_PET_LOCATION.FULL,PET_MOVEMENT.FULL,MEASUREMENT.FULL,custom.invoice_report,custom.village_specific_report,\r\n"
			+ "custom.blank_report,custom.general_report";

	public String permissionForUserGroups5 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.HOLDING_REGISTRATORS
			+ ":SUBJECT.FULL,MESSAGE.FULL,card.spatial_access,card.naits_access,CARD_CONF.FULL,system.null_geometry,SECURITY_PERUN.LOGIN_PERUN,VILLAGE.FULL,HOLDING.FULL,HOLDING_RESPONSIBLE.FULL,QUARANTINE.READ,AREA.FULL,AREA_HEALTH.READ,DISEASE.FULL,\r\n"
			+ "CRITERIA_TYPE.FULL";

	public String permissionForUserGroups6 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.ANIMAL_REGISTRATORS
			+ ":SUBJECT.FULL,MESSAGE.FULL,card.spatial_access,card.naits_access,CARD_CONF.FULL,system.null_geometry,SECURITY_PERUN.LOGIN_PERUN,VILLAGE.FULL,HOLDING.READ,HOLDING_RESPONSIBLE.READ,ANIMAL.FULL,FLOCK.FULL,ANIMAL_MOVEMENT.READ,FLOCK_MOVEMENT.READ,\r\n"
			+ "INVENTORY_ITEM.FULL,QUARANTINE.READ,AREA.FULL,AREA_HEALTH.READ,DISEASE.FULL,CRITERIA_TYPE.FULL,EAR_TAG_REPLC.FULL,\r\n"
			+ "PET.READ";

	public String permissionForUserGroups7 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.HOLDING_ADMINISTRATORS
			+ ":SUBJECT.FULL,MESSAGE.FULL,card.spatial_access,card.naits_access,CARD_CONF.FULL,system.null_geometry,SECURITY_PERUN.LOGIN_PERUN,VILLAGE.FULL,HOLDING.FULL,HOLDING_RESPONSIBLE.FULL,ANIMAL.FULL,FLOCK.FULL,VACCINATION_BOOK.FULL,VACCINATION_EVENT.FULL,\r\n"
			+ "VACCINATION_RESULTS.FULL,ANIMAL_MOVEMENT.FULL,FLOCK_MOVEMENT.FULL,INVENTORY_ITEM.FULL,QUARANTINE.READ,AREA.FULL,\r\n"
			+ "AREA_HEALTH.READ,DISEASE.READ,DISEASE.FULL,ANIMAL_TYPE.READ,CRITERIA_TYPE.FULL,SPOT_CHECK.FULL,EAR_TAG_REPLC.FULL,\r\n"
			+ "LAB_SAMPLE.READ,MOVEMENT_DOC.FULL,MOVEMENT_DOC_BLOCK.FULL,PET.FULL";

	public String permissionForUserGroups8 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.AHSMT_ADMINS
			+ ":SUBJECT.FULL,MESSAGE.FULL,card.spatial_access,card.naits_access,CARD_CONF.FULL,system.null_geometry,SECURITY_PERUN.LOGIN_PERUN,VILLAGE.FULL,QUARANTINE.READ,AREA.FULL,AREA_HEALTH.FULL,DISEASE.FULL,CRITERIA_TYPE.FULL";

	public String permissionForUserGroups9 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.QUARANTINE_ADMINISTRATORS
			+ ":SUBJECT.FULL,MESSAGE.FULL,card.spatial_access,card.naits_access,CARD_CONF.FULL,system.null_geometry,SECURITY_PERUN.LOGIN_PERUN,VILLAGE.FULL,HOLDING.READ,HOLDING_RESPONSIBLE.READ,ANIMAL.READ,FLOCK.READ,ANIMAL_MOVEMENT.READ,FLOCK_MOVEMENT.READ,\r\n"
			+ "INVENTORY_ITEM.READ,QUARANTINE.FULL,AREA.FULL,AREA_HEALTH.READ,DISEASE.FULL,EXPORT_CERT.FULL,ANIMAL_TYPE.READ,\r\n"
			+ "CRITERIA_TYPE.FULL,ANIMAL_ACTIVITY.FULL,ANIMAL_ORIGIN.FULL,MOVEMENT_DOC.READ,MOVEMENT_DOC_BLOCK.READ,PET.READ,\r\n"
			+ "undo_retire_export_anim,PET_QUARANTINE.FULL";

	public String permissionForUserGroups10 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.DIM_ADMINISTRATORS
			+ ":SUBJECT.FULL,MESSAGE.FULL,card.spatial_access,card.naits_access,CARD_CONF.FULL,system.null_geometry,SECURITY_PERUN.LOGIN_PERUN,VILLAGE.FULL,ANIMAL.FULL,SUPPLIER.FULL,ORDER.FULL,RANGE.FULL,TRANSFER.FULL,INVENTORY_ITEM.FULL,QUARANTINE.READ,AREA.FULL,\r\n"
			+ "AREA_HEALTH.READ,DISEASE.FULL,ANIMAL_TYPE.READ,CRITERIA_TYPE.FULL,PET.FULL";

	public String permissionForUserGroups11 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.SLAUGHTERHOUSE_ADMINISTRATORS
			+ ":SUBJECT.FULL,MESSAGE.FULL,card.spatial_access,card.naits_access,CARD_CONF.FULL,system.null_geometry,SECURITY_PERUN.LOGIN_PERUN,VILLAGE.FULL,HOLDING.FULL,HOLDING_RESPONSIBLE.FULL,ANIMAL.FULL,FLOCK.FULL,VACCINATION_BOOK.FULL,VACCINATION_EVENT.FULL,\r\n"
			+ "VACCINATION_RESULTS.FULL,ANIMAL_MOVEMENT.FULL,FLOCK_MOVEMENT.FULL,PRE_SLAUGHT_FORM.FULL,POST_SLAUGHT_FORM.FULL,\r\n"
			+ "INVENTORY_ITEM.FULL,QUARANTINE.READ,AREA.FULL,AREA_HEALTH.FULL,DISEASE.FULL,ANIMAL_TYPE.READ,CRITERIA_TYPE.FULL,\r\n"
			+ "SPOT_CHECK.FULL,EAR_TAG_REPLC.FULL,LAB_SAMPLE.READ,LAB_TEST_RESULT.READ,MOVEMENT_DOC.FULL,MOVEMENT_DOC_BLOCK.FULL";

	public String permissionForUserGroups12 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.RYSK_ANALYZE_ADMINISTRATORS
			+ ":SUBJECT.FULL,MESSAGE.FULL,card.spatial_access,card.naits_access,CARD_CONF.FULL,system.null_geometry,SECURITY_PERUN.LOGIN_PERUN,VILLAGE.FULL,HOLDING.READ,HOLDING_RESPONSIBLE.READ,ANIMAL.READ,QUARANTINE.READ,AREA.FULL,AREA_HEALTH.READ,DISEASE.FULL,\r\n"
			+ "ANIMAL_TYPE.READ,POPULATION.FULL,SAMPLE.FULL,SELECTION_RESULT.FULL,CRITERIA.FULL,CRITERIA_TYPE.FULL,STRAT_FILTER.FULL,\r\n"
			+ "POPULATION_LOCATION.FULL";

	public String permissionForUserGroups13 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.PET_VETERINARIANS
			+ ":SUBJECT.FULL,MESSAGE.FULL,card.spatial_access,card.naits_access,CARD_CONF.FULL,system.null_geometry,SECURITY_PERUN.LOGIN_PERUN,HOLDING.FULL,HOLDING_RESPONSIBLE.READ,ANIMAL.READ,VACCINATION_BOOK.FULL,VACCINATION_EVENT.READ,INVENTORY_ITEM.FULL,\r\n"
			+ "AREA.READ,DISEASE.READ,ANIMAL_TYPE.READ,LABORATORY.READ,LAB_SAMPLE.FULL,LAB_TEST_RESULT.READ,LAB_TEST_TYPE.READ,\r\n"
			+ "PET.FULL,PET_HEALTH_BOOK.FULL,PET_PASSPORT.FULL,STRAY_PET.FULL,PASSPORT_REQUEST.FULL,HEALTH_PASSPORT.FULL,\r\n"
			+ "STRAY_PET_LOCATION.FULL,PET_MOVEMENT.FULL,MEASUREMENT.FULL,PET_QUARANTINE.FULL";

	public String permissionForUserGroups14 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.BANK_AND_INSURANCE_COMPANIES
			+ ":SUBJECT.FULL,MESSAGE.FULL,card.spatial_access,card.naits_access,CARD_CONF.FULL,system.null_geometry,SECURITY_PERUN.LOGIN_PERUN,HOLDING.READ,HOLDING_RESPONSIBLE.READ,ANIMAL.READ,EMAIL_PROPERTIES.READ";

	public String permissionForUserGroups15 = Tc.ADMINISTRATORS + Tc.COMMA_DELIMITER + Tc.NAITS_ADMINISTRATORS
			+ ":custom.rfid_move_to_cert,custom.rfid_export,custom.rfid_registration,custom.rfid_transfer,custom.rfid_action,custom.rfid_input_search,custom.general_report,custom.blank_report,custom.village_specific_report,custom.invoice_report,custom.file_deletion,SUBJECT.FULL,MESSAGE.FULL,card.spatial_access,card.naits_access,VILLAGE.FULL,HOLDING.FULL,HOLDING_RESPONSIBLE.FULL,LABORATORY.FULL,STRAY_PET_LOCATION.FULL,ANIMAL.FULL,FLOCK.FULL,OTHER_ANIMALS.FULL,VACCINATION_BOOK.FULL,VACCINATION_EVENT.FULL,VACCINATION_RESULTS.FULL,"
			+ "MOVEMENT_DOC.FULL,ANIMAL_MOVEMENT.FULL,FLOCK_MOVEMENT.FULL,MOVEMENT_DOC_BLOCK.FULL,PRE_SLAUGHT_FORM.FULL,POST_SLAUGHT_FORM.FULL,SUPPLIER.FULL,ORDER.FULL,INVENTORY_ITEM.FULL,RANGE.FULL,TRANSFER.FULL,EXPORT_CERT.FULL,ANIMAL_ORIGIN.FULL,ANIMAL_ACTIVITY.FULL,"
			+ "QUARANTINE.FULL,AREA.FULL,AREA_HEALTH.FULL,DISEASE.FULL,ANIMAL_HEALTH.FULL,ANIMAL_TYPE.FULL,POPULATION.FULL,SAMPLE.FULL,SELECTION_RESULT.FULL,CRITERIA.FULL,CRITERIA_TYPE.FULL,STRAT_FILTER.FULL,QUARANTINE_GEOMETRY.FULL,SPOT_CHECK.FULL,EAR_TAG_REPLC.FULL,"
			+ "LAB_SAMPLE.FULL,LAB_SAMPLE_RESULT.FULL,LAB_TEST_TYPE.FULL,LAB_TEST_RESULT.FULL,INVOICE_PRICE.FULL,PET.FULL,PET_HEALTH_BOOK.FULL,PET_PASSPORT.FULL,STRAY_PET.FULL,PASSPORT_REQUEST.FULL,HEALTH_PASSPORT.FULL,PET_MOVEMENT.FULL,PET_QUARANTINE.FULL,MEASUREMENT.FULL, "
			+ "POPULATION_LOCATION.FULL,RFID_INPUT.FULL,RFID_INPUT_STATE.FULL,RFID_INPUT_RESULT.FULL,SUBJECT.FULL,MESSAGE.FULL,MSG_ATTACHEMENT.FULL,HERD.FULL,HERD_MOVEMENT.FULL,HERD_HEALTH_BOOK.FULL,FFT_SCORE.FULL,FF_SCORE.FULL,system.null_geometry,border_point_management,VMP.FULL,EMAIL_PROPERTIES.FULL";
	public String permissionForReportUser = "system.null_geometry,SVAROG_RE.FULL, SVAROG_USERS.FULL, SVAROG_JOB.FULL, SVAROG_TASK.FULL,"
			+ "SVAROG_RESULTS.FULL,SVAROG_RENDER_ENGINE.FULL,SVAROG_TABLES.FULL,SVAROG_TASK_TYPE.FULL,SVAROG_UI_STRUCT_TYPE.FULL,SVAROG_SID_ACL.FULL,SVAROG_ORG_UNITS.FULL,"
			+ "SVAROG_FORM_TYPE.FULL,SVAROG_SEQUENCE.FULL,SVAROG_RULES.FULL,SVAROG_EXECUTIONS.FULL,SVAROG_LOCALES.FULL,SVAROG_NOTES.FULL,"
			+ "SVAROG_FORM_FIELD_TYPE.FULL,SVAROG_SDI_COVER.FULL,SVAROG_WORKFLOW.FULL,SVAROG_LINK.FULL,SVAROG_SDI.FULL,SVAROG_USER_GROUPS.FULL,"
			+ "SVAROG_FORM_FIELD.FULL,SVAROG_FORM.FULL,SVAROG_SDI_USE.FULL,SVAROG_ACL.FULL,SVAROG_PARAM_VALUE.FULL,SVAROG_SDI_BOUNDS.FULL,"
			+ "SVAROG_FILES.FULL,SVAROG_FIELDS.FULL,SVAROG_ACTIONS.FULL,SVAROG_JOB_TASK.FULL,SVAROG.FULL,SVAROG_SDI_SERVICE.FULL,"
			+ "SVAROG_UI_STRUCT_SOURCE.FULL,SVAROG_CODES.FULL,SVAROG_JOB_TYPE.FULL,SVAROG_SDI_DESCRIPTOR.FULL,SVAROG_PARAM.FULL,"
			+ "SVAROG_SECURITY_LOG.FULL,SVAROG_PARAM_TYPE.FULL,SVAROG_NOTIFICATION.FULL,SVAROG_CONTACT_DATA.FULL,SVAROG_SDI_UNITS.FULL,"
			+ "SVAROG_LINK_TYPE.FULL,SVAROG_EVENT.FULL,SVAROG_TASK_DETAIL.FULL,SVAROG_LABELS.FULL,SVAROG_JOB_OBJECT.FULL,HOLDING.READ,"
			+ "HOLDING.FULL,HOLDING_RESPONSIBLE.READ,HOLDING_RESPONSIBLE.FULL,ANIMAL.READ,ANIMAL.FULL,FLOCK.READ,"
			+ "FLOCK.FULL,VACCINATION_BOOK.READ,VACCINATION_BOOK.FULL,VACCINATION_EVENT.READ,VACCINATION_EVENT.FULL,VACCINATION_RESULTS.FULL,"
			+ "ANIMAL_MOVEMENT.READ,ANIMAL_MOVEMENT.FULL,FLOCK_MOVEMENT.READ,FLOCK_MOVEMENT.FULL,PRE_SLAUGHT_FORM.READ,"
			+ "PRE_SLAUGHT_FORM.FULL,POST_SLAUGHT_FORM.READ,POST_SLAUGHT_FORM.FULL,SVAROG_MESSAGE.FULL,INVENTORY_ITEM.READ,INVENTORY_ITEM.FULL,"
			+ "QUARANTINE.READ,AREA.READ,AREA.FULL,AREA_HEALTH.READ,AREA_HEALTH.FULL,DISEASE.READ,ANIMAL_TYPE.READ,SVAROG_CONVERSATION.FULL,"
			+ "SPOT_CHECK.FULL,EAR_TAG_REPLC.READ,EAR_TAG_REPLC.FULL,LAB_SAMPLE.READ,LAB_TEST_RESULT.READ,MOVEMENT_DOC.FULL,MOVEMENT_DOC_BLOCK.FULL,PET.READ";

	public String permissionsForUsersUsergroup = "card.spatial_access,card.naits_access,CARD_CONF.FULL,system.null_geometry,SECURITY_PERUN.LOGIN_PERUN,HOLDING.READ,HOLDING_RESPONSIBLE.READ,"
			+ "ANIMAL.READ,FLOCK.READ,VACCINATION_BOOK.FULL,VACCINATION_EVENT.FULL,VACCINATION_RESULTS.FULL,ANIMAL_MOVEMENT.READ,FLOCK_MOVEMENT.READ,DISEASE.READ,ANIMAL_TYPE.READ,"
			+ "LABORATORY.FULL,LAB_SAMPLE.FULL,LAB_TEST_RESULT.FULL,LAB_TEST_TYPE.FULL,PET.READ, RFID_INPUT.FULL,RFID_INPUT_RESULT.FULL,RFID_INPUT_STATE.FULL,VMP.READ,SVAROG_FORM.READ,SVAROG_FORM_FIELD.READ";

	public String permissionForUserGroups16 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.CUSTOMS_OFFICER
			+ ":QUARANTINE.FULL,QUARANTINE_GEOMETRY.FULL,EXPORT_CERT.FULL,DISEASE.FULL,ANIMAL_ACTIVITY.FULL,ANIMAL_ORIGIN.FULL,PET.FULL,PET_QUARANTINE.FULL";

	public String permissionForUserGroups17 = defaultGroupType + Tc.COMMA_DELIMITER + Tc.SHELTER_OPERATOR
			+ ":PET.FULL,PET_HEALTH_BOOK.FULL,PET_PASSPORT.FULL,PET_MOVEMENT.FULL,STRAY_PET.FULL,STRAY_PET_LOCATION.FULL,PET_QUARANTINE.FULL,LAB_SAMPLE.FULL,HEALTH_PASSPORT.FULL";

	/**
	 * Method that checks if user group is already created
	 * 
	 * @param userGroupType
	 * @param userGroupName
	 * @param svr
	 * @return
	 * @throws SvException
	 */
	public DbDataObject createUserGroupIfNotExist(String userGroupType, String userGroupName, String groupSecurityType,
			SvReader svr) throws SvException {
		DbDataObject tempUserGroup = searchUserGroupInDb(svCONST.OBJECT_TYPE_GROUP, userGroupType, userGroupName, svr);
		if (tempUserGroup == null) {
			return createUserGroup(userGroupType, userGroupName, groupSecurityType);
		} else {
			return null;
		}
	}

	/**
	 * Method for Searching userGroup object in database
	 * 
	 * @param objToSearchIn
	 * @param groupType
	 * @param groupName
	 * @param svr
	 * @return
	 * @throws SvException
	 */
	public DbDataObject searchUserGroupInDb(Long objToSearchIn, String groupType, String groupName, SvReader svr)
			throws SvException {
		DbDataObject result = null;
		DbSearchCriterion cr1 = new DbSearchCriterion(Tc.GROUP_TYPE, DbCompareOperand.EQUAL, groupType);
		DbSearchCriterion cr2 = new DbSearchCriterion(Tc.GROUP_NAME, DbCompareOperand.EQUAL, groupName);
		DbSearchExpression dbse = new DbSearchExpression().addDbSearchItem(cr1).addDbSearchItem(cr2);
		DbDataArray results = svr.getObjects(dbse, objToSearchIn, null, 0, 0);
		if (!results.isEmpty()) {
			result = results.get(0);
		}
		return result;
	}

	/**
	 * Method that creates a user group with poa security
	 * 
	 * @param groupType
	 * @param groupName
	 * @return
	 */
	public static DbDataObject createUserGroup(String groupType, String groupName, String groupSecurityType) {
		DbDataObject dboGroup = new DbDataObject();
		dboGroup.setObjectType(svCONST.OBJECT_TYPE_GROUP);
		dboGroup.setVal(Tc.GROUP_TYPE, groupType);
		dboGroup.setVal(Tc.GROUP_UID, SvUtil.getUUID());
		dboGroup.setVal(Tc.GROUP_NAME, groupName);
		dboGroup.setVal(Tc.E_MAIL, "admin@admin.com");
		dboGroup.setVal(Tc.GROUP_SECURITY_TYPE, groupSecurityType);
		return dboGroup;
	}

	/**
	 * Method for Searching user object in database
	 * 
	 * @param usernameFromConfiguration - the userName to be searched
	 * @param svr                       - reference to the class that reads from
	 *                                  database
	 * @throws SvException
	 */
	public DbDataObject searchUserInDb(String usernameFromConfiguration, SvReader svr) throws SvException {
		DbReader rdr = new DbReader();
		DbDataObject existingUser = rdr.searchDbObjectBySingleFilter(svCONST.OBJECT_TYPE_USER, Tc.USER_NAME,
				usernameFromConfiguration, svr);
		return existingUser;
	}

	/**
	 * Method for creating users
	 * 
	 * @param pin
	 * @param userName
	 * @param email
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param rdr
	 * @param dbw
	 * @param svr
	 * @param svw
	 * @return
	 * @throws SvException
	 */
	public DbDataObject createUserIfNotExist(String pin, String userName, String email, String password,
			String firstName, String lastName, DbReader rdr, DbWriter dbw, SvReader svr, SvWriter svw)
			throws SvException {
		DbDataObject dboUser = searchUserInDb(userName, svr);
		if (dboUser == null) {
			dboUser = dbw.createDefaultTestUser(userName, firstName, lastName, email, pin, password, svr);
			log4j.trace("USER:" + dboUser.getVal(Tc.USER_NAME).toString() + ": created.");
		}
		return dboUser;
	}

	/**
	 * Method that returns link type for default user group membership
	 * 
	 * @return
	 * @throws SvException
	 */
	public DbDataObject getUserLinkedToUserGroupDefault() throws SvException {
		DbDataObject linkTypeBetweenUserAndUserGroup = SvReader.getLinkType(Tc.USER_DEFAULT_GROUP,
				SvReader.getTypeIdByName(Tc.SVAROG_USERS), SvReader.getTypeIdByName(Tc.SVAROG_USER_GROUPS));
		return linkTypeBetweenUserAndUserGroup;
	}

	/**
	 * Method that returns link type for additional user group membership
	 * 
	 * @return
	 * @throws SvException
	 */
	public DbDataObject getUserLinkedToUserGroupAdditional() throws SvException {
		DbDataObject linkBetweenUserAndUserGroup = SvReader.getLinkType(Tc.USER_GROUP,
				SvReader.getTypeIdByName(Tc.SVAROG_USERS), SvReader.getTypeIdByName(Tc.SVAROG_USER_GROUPS));
		return linkBetweenUserAndUserGroup;
	}

	/**
	 * Method that creates links between user and user group
	 * 
	 * @param user
	 * @param userGroup
	 * @param userGroupType
	 * @param finalDbDataArray
	 * @param svr
	 * @throws SvException
	 */
	public void createLinksBetweenUsersAndUserGroups(DbDataObject user, DbDataObject userGroup,
			DbDataArray finalDbDataArray, String userGroupType, SvReader svr) throws SvException {
		if (user.getObjectId() != 0L && userGroup.getObjectId() != 0L) {
			DbWriter dbw = new DbWriter();
			DbDataObject linkType = new DbDataObject();
			DbDataObject defaultGroup = searchUserGroupInDb(svCONST.OBJECT_TYPE_GROUP, userGroupType, userGroupType,
					svr);
			DbDataObject link = DbReader.findLink(user.getObjectId(), defaultGroup.getObjectId(), Tc.USER_DEFAULT_GROUP,
					svr);
			if (Objects.isNull(link)) {
				linkType = getUserLinkedToUserGroupDefault();
				finalDbDataArray.addDataItem(dbw.createSvarogLink(linkType.getObjectId(), user, defaultGroup));
			}
			link = DbReader.findLink(user.getObjectId(), userGroup.getObjectId(), Tc.USER_GROUP, svr);
			if (Objects.isNull(link)) {
				linkType = getUserLinkedToUserGroupAdditional();
				finalDbDataArray.addDataItem(dbw.createSvarogLink(linkType.getObjectId(), user, userGroup));
			}
		}
	}

	/**
	 * * Method that sets a permission to a given user group.
	 * 
	 * @param accessType
	 * @param shouldCommit
	 * @param sidName
	 * @param sidType
	 * @param permissionKeys
	 * @param operation
	 * @param svr
	 * @param svs
	 * @return
	 */
	public boolean setSidPermission(Boolean shouldCommit, String sidName, Long sidType,
			ArrayList<String> permissionKeys, String operation, SvReader svr, SvSecurity svs) {
		DbDataObject sid = null;
		try {
			// svr.switchUser("ADMIN");
			svs.setAutoCommit(false);
			sid = svs.getSid(sidName, sidType);
			for (String permissionKey : permissionKeys) {
				log4j.info(permissionKey.trim());
				if (operation.equals(Tc.GRANT))
					svs.grantPermission(sid, permissionKey.trim());
				else if (operation.equals(Tc.REVOKE))
					svs.revokePermission(sid, permissionKey.trim());
				else
					log4j.info("Operation must be either GRANT or REVOKE, Wrong operation:" + operation);
			}
			if (shouldCommit) {
				svs.dbCommit();
			}

		} catch (SvException e) {
			log4j.info(e);
			return false;
		}
		return true;
	}

	/**
	 * Method that attaches permissions for given user group
	 * 
	 * @param dboGroup
	 * @param permissionType
	 * @param permissions
	 * @param shouldCommit
	 * @param svr
	 * @param svc
	 * @throws SvException
	 * 
	 */
	public void attachPermissions(DbDataObject dboGroup, String permissionType, ArrayList<String> permissions,
			Boolean shouldCommit, SvReader svr, SvSecurity svc) throws SvException {
		if (setSidPermission(shouldCommit, dboGroup.getVal(Tc.GROUP_NAME).toString(), svCONST.OBJECT_TYPE_GROUP,
				permissions, Tc.GRANT, svr, svc))
			log4j.info(permissionType + " permissions granted.");
		else
			log4j.info(permissionType + " permissions not granted.");
	}

	/**
	 * Method that fetches svarog core table permissions
	 * 
	 * @param svr
	 * @return
	 * @throws SvException
	 */
	public ArrayList<String> getSvarogCoreTablePermission(SvReader svr) throws SvException {
		ArrayList<String> corePermissions = new ArrayList<String>();
		DbSearchCriterion cr11 = new DbSearchCriterion(Tc.SYSTEM_TABLE, DbCompareOperand.EQUAL, true);
		DbDataArray dbArray1 = svr.getObjects(new DbSearchExpression().addDbSearchItem(cr11), svCONST.OBJECT_TYPE_TABLE,
				null, 0, 0);
		for (DbDataObject tempDbo : dbArray1.getItems()) {
			corePermissions.add(tempDbo.getVal(Tc.TABLE_NAME) + ".FULL");
		}
		return corePermissions;
	}

	/**
	 * Method that install user groups and their proper access level of existing
	 * objects (permissions)
	 * 
	 * @param shouldCommit
	 * @param svr
	 * @param svw
	 * @param svc
	 * @throws SvException
	 */
	public void installACLpermissionsForUserGroups(boolean shouldCommit, SvReader svr, SvWriter svw, SvSecurity svc)
			throws SvException {
		String userGroupType = null;
		String userGroupName = null;
		DbDataObject dboGroup = null;
		List<String> permissionsForUserGroups = new ArrayList<>();
		permissionsForUserGroups.add(permissionForUserGroups1);
		permissionsForUserGroups.add(permissionForUserGroups2);
		permissionsForUserGroups.add(permissionForUserGroups3);
		permissionsForUserGroups.add(permissionForUserGroups4);
		permissionsForUserGroups.add(permissionForUserGroups5);
		permissionsForUserGroups.add(permissionForUserGroups6);
		permissionsForUserGroups.add(permissionForUserGroups7);
		permissionsForUserGroups.add(permissionForUserGroups8);
		permissionsForUserGroups.add(permissionForUserGroups9);
		permissionsForUserGroups.add(permissionForUserGroups10);
		permissionsForUserGroups.add(permissionForUserGroups11);
		permissionsForUserGroups.add(permissionForUserGroups12);
		permissionsForUserGroups.add(permissionForUserGroups13);
		permissionsForUserGroups.add(permissionForUserGroups14);
		permissionsForUserGroups.add(permissionForUserGroups15);
		permissionsForUserGroups.add(permissionForUserGroups16);
		permissionsForUserGroups.add(permissionForUserGroups17);
		for (String str : permissionsForUserGroups) {
			String item[] = str.split(":");
			if (item.length == 2) {
				String userGroupDetails[] = item[0].split(Tc.COMMA_DELIMITER);
				String userGroupPermissions = item[1];
				if (userGroupDetails.length == 2) {
					userGroupType = userGroupDetails[0].trim();
					userGroupName = userGroupDetails[1].trim();
					dboGroup = searchUserGroupInDb(svCONST.OBJECT_TYPE_GROUP, userGroupType, userGroupName, svr);
					if (dboGroup == null) {
						log4j.info("The group " + userGroupName + " does not exist in DB");
						continue;
					}
					// attach svarog core permissions
					attachPermissions(dboGroup, "SVAROG core", getSvarogCoreTablePermission(svr), shouldCommit, svr,
							svc);
					// attach custom group permissions
					attachPermissions(dboGroup, "User group custom",
							new ArrayList<String>(Arrays.asList(userGroupPermissions.split(Tc.COMMA_DELIMITER))),
							shouldCommit, svr, svc);
					svc.dbCommit();
				}
			}
		}
	}

	/**
	 * Method that creates a batch of users and groups with links between them
	 * 
	 * @param rdr
	 * @param dbw
	 * @param svr
	 * @param svw
	 * @param svp
	 * @param svl
	 * @return
	 * @throws SvException
	 */
	public DbDataArray createUsersAndGroups(DbReader rdr, DbWriter dbw, SvReader svr, SvWriter svw, SvParameter svp,
			SvLink svl) throws SvException {
		DbDataArray usersAndGroups = new DbDataArray();
		DbDataArray usersAndGroupsForSave = new DbDataArray();
		DbDataArray linksUserAndGroups = new DbDataArray();
		DbDataObject dboUserGroup1 = createUserGroupIfNotExist(defaultGroupType, groupName1, Tc.POA, svr);
		if (dboUserGroup1 != null)
			usersAndGroups.addDataItem(dboUserGroup1);
		DbDataObject dboUserGroup2 = createUserGroupIfNotExist(defaultGroupType, groupName2, Tc.POA, svr);
		if (dboUserGroup2 != null)
			usersAndGroups.addDataItem(dboUserGroup2);
		DbDataObject dboUserGroup3 = createUserGroupIfNotExist(defaultGroupType, groupName3, Tc.POA, svr);
		if (dboUserGroup3 != null)
			usersAndGroups.addDataItem(dboUserGroup3);
		DbDataObject dboUserGroup4 = createUserGroupIfNotExist(defaultGroupType, groupName4, Tc.POA, svr);
		if (dboUserGroup4 != null)
			usersAndGroups.addDataItem(dboUserGroup4);
		DbDataObject dboUserGroup5 = createUserGroupIfNotExist(defaultGroupType, groupName5, Tc.POA, svr);
		if (dboUserGroup5 != null)
			usersAndGroups.addDataItem(dboUserGroup5);
		DbDataObject dboUserGroup6 = createUserGroupIfNotExist(defaultGroupType, groupName6, Tc.POA, svr);
		if (dboUserGroup6 != null)
			usersAndGroups.addDataItem(dboUserGroup6);
		DbDataObject dboUserGroup7 = createUserGroupIfNotExist(defaultGroupType, groupName7, Tc.POA, svr);
		if (dboUserGroup7 != null)
			usersAndGroups.addDataItem(dboUserGroup7);
		DbDataObject dboUserGroup8 = createUserGroupIfNotExist(defaultGroupType, groupName8, Tc.POA, svr);
		if (dboUserGroup8 != null)
			usersAndGroups.addDataItem(dboUserGroup8);
		DbDataObject dboUserGroup9 = createUserGroupIfNotExist(defaultGroupType, groupName9, Tc.POA, svr);
		if (dboUserGroup9 != null)
			usersAndGroups.addDataItem(dboUserGroup9);
		DbDataObject dboUserGroup10 = createUserGroupIfNotExist(defaultGroupType, groupName10, Tc.POA, svr);
		if (dboUserGroup10 != null)
			usersAndGroups.addDataItem(dboUserGroup10);
		DbDataObject dboUserGroup11 = createUserGroupIfNotExist(defaultGroupType, groupName11, Tc.POA, svr);
		if (dboUserGroup11 != null)
			usersAndGroups.addDataItem(dboUserGroup11);		
		DbDataObject dboUserGroup12 = createUserGroupIfNotExist(defaultGroupType, groupName12, Tc.POA, svr);
		if (dboUserGroup12 != null)
			usersAndGroups.addDataItem(dboUserGroup12);
		DbDataObject dboUserGroup13 = createUserGroupIfNotExist(defaultGroupType, groupName13, Tc.POA, svr);
		if (dboUserGroup13 != null)
			usersAndGroups.addDataItem(dboUserGroup13);
		DbDataObject dboUserGroup14 = createUserGroupIfNotExist(defaultGroupType, groupName14, Tc.POA, svr);
		if (dboUserGroup14 != null)
			usersAndGroups.addDataItem(dboUserGroup14);
		DbDataObject dboUserGroup15 = createUserGroupIfNotExist(Tc.ADMINISTRATORS, groupName15, Tc.FULL, svr);
		if (dboUserGroup15 != null)
			usersAndGroups.addDataItem(dboUserGroup15);
		DbDataObject dboUserGroup16 = createUserGroupIfNotExist(defaultGroupType, groupName16, Tc.POA, svr);
		if (dboUserGroup16 != null)
			usersAndGroups.addDataItem(dboUserGroup16);
		DbDataObject dboUserGroup17 = createUserGroupIfNotExist(defaultGroupType, groupName17, Tc.POA, svr);
		if (dboUserGroup17 != null)
			usersAndGroups.addDataItem(dboUserGroup17);
		DbDataObject user1 = createUserIfNotExist(defaultUserPin, userName1, "cviro@test.com", defaultUserPassword,
				defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user1);
		DbDataObject user2 = createUserIfNotExist(defaultUserPin, userName2, "fviro@test.com", defaultUserPassword,
				defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user2);
		DbDataObject user3 = createUserIfNotExist(defaultUserPin, userName3, "laborant@test.com", defaultUserPassword,
				defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user3);
		DbDataObject user4 = createUserIfNotExist(defaultUserPin, userName4, "dataEntryClerk@test.com",
				defaultUserPassword, defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user4);
		DbDataObject user5 = createUserIfNotExist(defaultUserPin, userName5, "holdingRegistrators@test.com",
				defaultUserPassword, defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user5);
		DbDataObject user6 = createUserIfNotExist(defaultUserPin, userName6, "animalRegistrators@test.com",
				defaultUserPassword, defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user6);
		DbDataObject user7 = createUserIfNotExist(defaultUserPin, userName7, "holdingAdministrators@test.com",
				defaultUserPassword, defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user7);
		DbDataObject user8 = createUserIfNotExist(defaultUserPin, userName8, "ahsmtAdmins@test.com",
				defaultUserPassword, defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user8);
		DbDataObject user9 = createUserIfNotExist(defaultUserPin, userName9, "quarantineAdministrators@test.com",
				defaultUserPassword, defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user9);
		DbDataObject user10 = createUserIfNotExist(defaultUserPin, userName10, "dimAdministrators@test.com",
				defaultUserPassword, defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user10);
		DbDataObject user11 = createUserIfNotExist(defaultUserPin, userName11, "slaughterAdmins@test.com",
				defaultUserPassword, defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user11);
		DbDataObject user12 = createUserIfNotExist(defaultUserPin, userName12, "ryskAdmins@test.com",
				defaultUserPassword, defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user12);
		DbDataObject user13 = createUserIfNotExist(defaultUserPin, userName13, "petVets@test.com", defaultUserPassword,
				defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user13);
		DbDataObject user14 = createUserIfNotExist(defaultUserPin, userName14, "BankInsurance@test.com",
				defaultUserPassword, defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user14);
		DbDataObject user15 = createUserIfNotExist(defaultUserPin, userName15, "naitsAdmin@test.com",
				defaultUserPassword, defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user15);
		DbDataObject user16 = createUserIfNotExist(defaultUserPin, userName16, "customsOfficers@test.com",
				defaultNewUserPassword, defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user16);
		DbDataObject user17 = createUserIfNotExist(defaultUserPin, userName17, "shelterOperator@test.com",
				defaultNewUserPassword, defaultUserFirstName, defaultUserLastName, rdr, dbw, svr, svw);
		usersAndGroups.addDataItem(user17);
		for (DbDataObject tempDbo : usersAndGroups.getItems()) {
			if (tempDbo.getObjectId() == 0L) {
				usersAndGroupsForSave.addDataItem(tempDbo);
			}
		}
		if (!usersAndGroupsForSave.isEmpty()) {
			svw.saveObject(usersAndGroupsForSave);
			svw.dbCommit();
		}
		createLinksBetweenUsersAndUserGroups(user1, dboUserGroup1, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user2, dboUserGroup2, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user3, dboUserGroup3, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user4, dboUserGroup4, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user5, dboUserGroup5, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user6, dboUserGroup6, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user7, dboUserGroup7, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user8, dboUserGroup8, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user9, dboUserGroup9, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user10, dboUserGroup10, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user11, dboUserGroup11, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user12, dboUserGroup12, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user13, dboUserGroup13, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user14, dboUserGroup14, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user15, dboUserGroup15, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user16, dboUserGroup16, linksUserAndGroups, defaultGroupType, svr);
		createLinksBetweenUsersAndUserGroups(user17, dboUserGroup17, linksUserAndGroups, defaultGroupType, svr);
		if (!linksUserAndGroups.isEmpty()) {
			svw.saveObject(linksUserAndGroups);
			svw.dbCommit();
		}
		return usersAndGroups;
	}

	/**
	 * Method that creates SVAROG SYS PARAM if does not exist
	 * 
	 * @param paramName
	 * @param paramValue
	 * @param paramType
	 * @param svw        - SvWriter instance
	 * @param svr        - SvReader instance
	 * @return
	 * @throws SvException
	 */
	public void createSysParamIfNotExist(String paramName, String paramValue, String paramType, boolean shouldCommit,
			SvWriter svw, SvReader svr) throws SvException {
		if (!checkIfSysParamExists(paramName, svr)) {
			DbDataObject dboSysParam = createSysParam(paramName, paramValue, paramType);
			svw.saveObject(dboSysParam, shouldCommit);
		}
	}

	/**
	 * Method that checks if SVAROG SYS PARAM exist
	 * 
	 * @param paramName
	 * @param svr       - SvReader instance
	 * @return Boolean
	 * @throws SvException
	 */
	public Boolean checkIfSysParamExists(String paramNameValue, SvReader svr) throws SvException {
		DbReader rdr = new DbReader();
		Boolean result = false;
		DbDataObject dbo = rdr.searchDbObjectBySingleFilter(svCONST.OBJECT_TYPE_SYS_PARAMS, Tc.PARAM_NAME,
				paramNameValue, svr);
		if (dbo != null) {
			result = true;
		}
		return result;
	}

	/**
	 * Method that create/constructs SVAROG SYS PARAM according input params
	 * 
	 * @param paramName
	 * @param paramValue
	 * @param paramType
	 * @return DbDataObject
	 */
	public DbDataObject createSysParam(String paramName, String paramValue, String paramType) {
		DbDataObject dboSysParam = new DbDataObject();
		dboSysParam.setObjectType(svCONST.OBJECT_TYPE_SYS_PARAMS);
		dboSysParam.setVal(Tc.PARAM_NAME, paramName);
		dboSysParam.setVal(Tc.PARAM_VALUE, paramValue);
		dboSysParam.setVal(Tc.PARAM_TYPE, paramType);
		return dboSysParam;
	}

	/**
	 * Method that checks if an SVAROG_ACL object exists in DB
	 * 
	 * @param objToSearchIn
	 * @param labelCode
	 * @param accessType
	 * @param svr
	 * @return
	 * @throws SvException
	 */
	public DbDataObject checkIfACLexists(Long objToSearchIn, String labelCode, String accessType, SvReader svr)
			throws SvException {
		DbDataObject result = null;
		DbDataArray results = null;
		DbSearchCriterion cr1 = new DbSearchCriterion(Tc.LABEL_CODE, DbCompareOperand.EQUAL, labelCode);
		DbSearchCriterion cr2 = new DbSearchCriterion(Tc.ACCESS_TYPE, DbCompareOperand.EQUAL, accessType);
		results = svr.getObjects(new DbSearchExpression().addDbSearchItem(cr1).addDbSearchItem(cr2), objToSearchIn,
				null, 0, 0);
		if (!results.isEmpty()) {
			result = results.get(0);
		}
		return result;
	}

	/**
	 * Method that creates SVAROG_ACL object if does not exist
	 * 
	 * @param labelCode
	 * @param aclConfigUnq
	 * @param aclObjectType
	 * @param accessType
	 * @param shouldCommit
	 * @param svw
	 * @param svr
	 * @return
	 * @throws SvException
	 */
	public void createAclObject(String labelCode, String aclConfigUnq, Long aclObjectType, String accessType,
			boolean shouldCommit, SvWriter svw, SvReader svr) throws SvException {
		DbDataObject searchedObject = checkIfACLexists(svCONST.OBJECT_TYPE_ACL, labelCode, accessType, svr);
		if (searchedObject == null) {
			DbDataObject aclObject = new DbDataObject();
			aclObject.setObjectType(svCONST.OBJECT_TYPE_ACL);
			aclObject.setVal(Tc.LABEL_CODE, labelCode);
			aclObject.setVal(Tc.ACL_CONFIG_UNQ, aclConfigUnq);
			aclObject.setVal(Sv.ACL_OBJECT_TYPE, aclObjectType);
			aclObject.setVal(Tc.ACCESS_TYPE, accessType);
			aclObject.setVal(Tc.ACL_OBJECT_ID, 0);
			svw.saveObject(aclObject, shouldCommit);
		} else {
			log4j.info("ACL object " + labelCode + " ,already exists.");
		}
	}

	/**
	 * Method that creates custom acl objects
	 * 
	 * @param svw
	 * @param svr
	 * @throws SvException
	 */
	public void createCustomAclObjects(SvWriter svw, SvReader svr) throws SvException {
		createAclObject("system.null_geometry", "system.null_geometry", svCONST.OBJECT_TYPE_TABLE, Tc.EXECUTE, true,
				svw, svr);
		createAclObject("custom.file_deletion", "custom.file_deletion", svCONST.OBJECT_TYPE_TABLE, Tc.EXECUTE, true,
				svw, svr);
		createAclObject("card.naits_access", "card.naits_access", svCONST.OBJECT_TYPE_TABLE, Tc.READ, true, svw, svr);
		createAclObject("card.spatial_access", "card.spatial_access", svCONST.OBJECT_TYPE_TABLE, Tc.READ, true, svw,
				svr);
		createAclObject("border_point_management", "border_point_management", svCONST.OBJECT_TYPE_TABLE, Tc.READ, true,
				svw, svr);
		createAclObject("custom.invoice_report", "custom.invoice_report", svCONST.OBJECT_TYPE_TABLE, Tc.EXECUTE, true,
				svw, svr);
		createAclObject("custom.village_specific_report", "custom.village_specific_report", svCONST.OBJECT_TYPE_TABLE,
				Tc.EXECUTE, true, svw, svr);
		createAclObject("custom.blank_report", "custom.blank_report", svCONST.OBJECT_TYPE_TABLE, Tc.EXECUTE, true, svw,
				svr);
		createAclObject("custom.general_report", "custom.general_report", svCONST.OBJECT_TYPE_TABLE, Tc.EXECUTE, true,
				svw, svr);
		createAclObject("custom.rfid_input_search", "custom.rfid_input_search", svCONST.OBJECT_TYPE_TABLE, Tc.EXECUTE,
				true, svw, svr);
		createAclObject("custom.rfid_registration", "custom.rfid_registration", svCONST.OBJECT_TYPE_TABLE, Tc.EXECUTE,
				true, svw, svr);
		createAclObject("custom.rfid_export", "custom.rfid_export", svCONST.OBJECT_TYPE_TABLE, Tc.EXECUTE, true, svw,
				svr);
		createAclObject("custom.rfid_move_to_cert", "custom.rfid_move_to_cert", svCONST.OBJECT_TYPE_TABLE, Tc.EXECUTE,
				true, svw, svr);
		createAclObject("custom.rfid_transfer", "custom.rfid_transfer", svCONST.OBJECT_TYPE_TABLE, Tc.EXECUTE, true,
				svw, svr);
		createAclObject("custom.rfid_action", "custom.rfid_action", svCONST.OBJECT_TYPE_TABLE, Tc.EXECUTE, true, svw,
				svr);
		createAclObject("custom.statistical_report", "custom.statistical_report", svCONST.OBJECT_TYPE_TABLE, Tc.EXECUTE,
				true, svw, svr);
		createAclObject("undo_retire_export_anim", "undo_retire_export_anim", svCONST.OBJECT_TYPE_TABLE, Tc.EXECUTE,
				true, svw, svr);
		createAclObject("SECURITY_PERUN.LOGIN_PERUN", "SECURITY_PERUN.LOGIN_PERUN", svCONST.OBJECT_TYPE_TABLE,
				Tc.EXECUTE, true, svw, svr);
	}

	/**
	 * Method that fetches the naits plugin
	 * 
	 * @param svr
	 * @return
	 * @throws SvException
	 */
	public DbDataObject getNaitsPlugin(SvReader svr) throws SvException {
		DbReader rdr = new DbReader();
		DbDataObject plugin = null;
		plugin = rdr.searchDbObjectBySingleFilter(svCONST.OBJECT_TYPE_PERUN_PLUGIN, Tc.CONTEXT_NAME, "naits", svr);
		return plugin;
	}

	/**
	 * Method that sets direct access to selected plugin
	 * 
	 * @param svr
	 * @param svw
	 * @throws SvException
	 */
	public void enableDirectAccessOnPlugin(SvReader svr, SvWriter svw) throws SvException {
		DbDataObject plugin = getNaitsPlugin(svr);
		if (plugin != null && (plugin.getVal(Tc.GUI_METADATA) == null)) {
			plugin.setVal(Tc.GUI_METADATA, "{\"directAccess\":true}");
			svw.saveObject(plugin);
		}
	}

	/**
	 * Method that creates permissions for reporter user
	 * 
	 * @return
	 */
	public JsonObject createJobPermissionsFromString(String permissions) {
		JsonObject job = new JsonObject();
		JsonArray jarr = new JsonArray();
		String pomPermissionsPerReportUser[] = permissions.split(",");
		for (int i = 0; i < pomPermissionsPerReportUser.length; i++) {
			jarr.add(pomPermissionsPerReportUser[i].toString().trim());
		}
		job.add("permissions", jarr);
		return job;
	}

	/**
	 * Methods that adds permissions to a reporter user
	 * 
	 * @param rd
	 * @param dbw
	 * @param svr
	 * @param svw
	 * @param svl
	 * @param svc
	 */
	public void createAndlinkPermissionsToReporterUser(DbReader rd, DbWriter dbw, SvReader svr, SvWriter svw,
			SvLink svl, SvSecurity svc) {
		DbDataObject dboUser = null;
		DbDataObject dboGroup = null;
		DbDataObject userRport = null;
		ArrayList<String> permissions = new ArrayList<>();
		try {
			JsonObject jsonObject = createJobPermissionsFromString(permissionForReportUser);
			dboUser = searchUserInDb(Tc.reporterUserName, svr);
			if (dboUser == null) {
				userRport = createUserIfNotExist("0000", Tc.reporterUserName, "user@reporter.com",
						defaultUserPasswordForReporterUser, "REPORTER", "USER", rd, dbw, svr, svw);
				svw.saveObject(userRport, true);
			} else {
				userRport = dboUser;
			}
			dboGroup = searchUserGroupInDb(svCONST.OBJECT_TYPE_GROUP, Tc.USERS, Tc.USERS, svr);
			if (dboGroup != null) {
				try {
					svl.linkObjects(userRport, dboGroup, Tc.USER_DEFAULT_GROUP, null, true);
					log4j.info("Linked default group");
				} catch (SvException e) {
					log4j.info("Already linked");
				}
			}
			for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				JsonArray jArrayPermissions = entry.getValue().getAsJsonArray();
				for (JsonElement jPermission : jArrayPermissions) {
					String permission = jPermission.getAsString();
					permissions.add(permission);
				}
			}
			setSidPermission(true, userRport.getVal(Tc.USER_NAME).toString(), svCONST.OBJECT_TYPE_USER, permissions,
					Tc.GRANT, svr, svc);
			log4j.info("Reporter user has been granted with " + permissions);
		} catch (SvException e) {
			log4j.error(e.getFormattedMessage());
		}
	}

	/**
	 * Method that adds permissions to a USERS userGroup
	 * 
	 * @param shouldCommit
	 * @param rd
	 * @param dbw
	 * @param svr
	 * @param svw
	 * @param svl
	 * @param svc
	 * @throws SvException
	 */
	public void addPermissionsToUsersUsergroup(boolean shouldCommit, DbReader rd, DbWriter dbw, SvReader svr,
			SvWriter svw, SvLink svl, SvSecurity svc) throws SvException {
		DbDataObject dboGroup = null;
		ArrayList<String> permissionsForUserGroups = new ArrayList<>();
		JsonObject jsonObject = createJobPermissionsFromString(permissionsForUsersUsergroup);
		if (jsonObject != null) {
			for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				JsonArray jArrayPermissions = entry.getValue().getAsJsonArray();
				for (JsonElement jPermission : jArrayPermissions) {
					String permission = jPermission.getAsString();
					permissionsForUserGroups.add(permission);
				}
			}
			dboGroup = searchUserGroupInDb(svCONST.OBJECT_TYPE_GROUP, Tc.USERS, Tc.USERS, svr);
			if (dboGroup != null) {
				// attach permissions
				attachPermissions(dboGroup, "User group custom", permissionsForUserGroups, shouldCommit, svr, svc);
				log4j.info("Reporter user has been granted with " + permissionsForUserGroups);
				svc.dbCommit();
			}
		}
	}

	/**
	 * Method that edits the fields properites after upgrade
	 * 
	 * @param svr
	 * @param svw
	 * @throws SvException
	 */
	public void editFieldsAfterUpgrade(SvReader svr, SvWriter svw, boolean shouldCommit) throws SvException {
		editFieldProperties(Tc.TRANSFER, Tc.SUBJECT_FROM, "{\"SV_ISLABEL\":true}", Tc.EXTENDED_PARAMS, svr, svw,
				shouldCommit);
		editFieldProperties(Tc.TRANSFER, Tc.SUBJECT_TO, "{\"SV_ISLABEL\":true}", Tc.EXTENDED_PARAMS, svr, svw,
				shouldCommit);
		editFieldProperties(Tc.PRE_SLAUGHT_FORM, Tc.DISEASE, "{\"SV_MULTISELECT\":true}", Tc.EXTENDED_PARAMS, svr, svw,
				shouldCommit);
		editFieldProperties(Tc.POST_SLAUGHT_FORM, Tc.DISIESE_FINDING, "{\"SV_MULTISELECT\":true}", Tc.EXTENDED_PARAMS,
				svr, svw, shouldCommit);
		editFieldProperties(Tc.VACCINATION_EVENT, Tc.ANIMAL_TYPE, "{\"SV_MULTISELECT\":true}", Tc.EXTENDED_PARAMS, svr,
				svw, shouldCommit);
		editFieldProperties(Tc.SVAROG_ORG_UNITS, Tc.NAME, "{\"SV_ISLABEL\":true}", Tc.EXTENDED_PARAMS, svr, svw,
				shouldCommit);
		editFieldProperties(Tc.SVAROG_ORG_UNITS, Tc.NAME,
				"{\"react\":{\"conversationInfo\":true,\"filterable\":true,\"visible\":true,\"sortable\":true,\"resizable\":true,\"editable\":true}}",
				Tc.GUI_METADATA, svr, svw, shouldCommit);
		editFieldProperties(Tc.HOLDING, Tc.VILLAGE_CODE, "{\"IS_GEOSTAT\":true}", Tc.EXTENDED_PARAMS, svr, svw,
				shouldCommit);
		editFieldProperties(Tc.RFID_INPUT_RESULT, Tc.ERROR_DESCRIPTION, "{\"SV_ISLABEL\":true}", Tc.EXTENDED_PARAMS,
				svr, svw, shouldCommit);
		editFieldProperties(Tc.RFID_INPUT_STATE, Tc.EXECUTED_ACTIONS, "{\"SV_MULTISELECT\":true}", Tc.EXTENDED_PARAMS,
				svr, svw, shouldCommit);
		// editFieldProperties(Tc.PET, Tc.PET_COLOR, "{\"SV_MULTISELECT\":true}",
		// Tc.EXTENDED_PARAMS, svr, svw,shouldCommit);
		editFieldProperties(Tc.SVAROG_USERS, Tc.PASS_HSH,
				"{\"react\":{\"filterable\":false,\"visible\":false,\"sortable\":false,\"resizable\":false,\"uischema\":{\"ui:widget\":\"hidden\"}}}",
				Tc.GUI_METADATA, svr, svw, shouldCommit);
		editFieldProperties(Tc.SVAROG_USERS, Tc.CONFIRM_PASS_HSH,
				"{\"react\":{\"filterable\":false,\"visible\":false,\"sortable\":false,\"resizable\":false,\"uischema\":{\"ui:widget\":\"hidden\"}}}",
				Tc.GUI_METADATA, svr, svw, shouldCommit);
		editFieldProperties(Tc.SVAROG_USERS, Tc.PIN,
				"{\"react\":{\"filterable\":false,\"visible\":false,\"sortable\":false,\"resizable\":false,\"uischema\":{\"ui:widget\": \"hidden\"}}}",
				Tc.GUI_METADATA, svr, svw, shouldCommit);
		editFieldProperties(Tc.SVAROG_USERS, Tc.TAX_ID,
				"{\"react\":{\"filterable\":false,\"visible\":false,\"sortable\":false,\"resizable\":false,\"uischema\":{\"ui:widget\": \"hidden\"}}}",
				Tc.GUI_METADATA, svr, svw, shouldCommit);
		editFieldProperties(Tc.SVAROG_USER_GROUPS, Tc.GROUP_UID,
				"{\"react\":{\"filterable\":false,\"visible\":false,\"sortable\":false,\"resizable\":false,\"uischema\":{\"ui:widget\": \"hidden\"}}}",
				Tc.GUI_METADATA, svr, svw, shouldCommit);
		editFieldProperties(Tc.SVAROG_USER_GROUPS, Tc.GROUP_UID, "true", Tc.IS_NULL, svr, svw, shouldCommit);
		editFieldProperties(Tc.SVAROG_USERS, Tc.USER_UID,
				"{\"react\":{\"filterable\":false,\"visible\":false,\"sortable\":false,\"resizable\":false,\"uischema\":{\"ui:widget\": \"hidden\"}}}",
				Tc.GUI_METADATA, svr, svw, shouldCommit);
		editFieldProperties(Tc.SVAROG_USERS, Tc.USER_TYPE,
				"{\"react\":{\"filterable\":false,\"visible\":false,\"resizable\":false,\"uischema\":{\"ui:widget\":\"hidden\"}}}",
				Tc.GUI_METADATA, svr, svw, shouldCommit);
		editFieldProperties(Tc.SVAROG_USERS, Tc.USER_NAME,
				"{\"react\":{\"conversationInfo\":true,\"filterable\":true,\"visible\":true,\"sortable\":true,\"resizable\":true,\"editable\":true}}",
				Tc.GUI_METADATA, svr, svw, shouldCommit);
		editFieldProperties(Tc.SVAROG_USER_GROUPS, Tc.GROUP_SECURITY_TYPE,
				"{\"react\":{\"filterable\":false,\"visible\":false,\"default\":POA,\"resizable\":false,\"uischema\":{\"ui:widget\":\"hidden\"}}}",
				Tc.GUI_METADATA, svr, svw, shouldCommit);
		editFieldProperties(Tc.SVAROG_USER_GROUPS, Tc.GROUP_SECURITY_TYPE, "true", Tc.IS_NULL, svr, svw, shouldCommit);
		editFieldProperties(Tc.SVAROG_USER_GROUPS, Tc.GROUP_TYPE,
				"{\"react\":{\"filterable\":false,\"visible\":false,\"default\":DATA_ENTRY_CLERK,\"resizable\":false,\"uischema\":{\"ui:readonly\":true}}}",
				Tc.GUI_METADATA, svr, svw, shouldCommit);
	}

	/**
	 * Helper method for editing the desired field properties
	 * 
	 * @param tableName - the name of the table of the field
	 * @param fieldName - the name of the desired field which properties would be
	 *                  set
	 * @param value     - the value to be added for the field property
	 * @param action    - the property name of the desired field to be edited
	 * @param svr       - SvCore instance
	 * @param svw       - SvCore instance
	 * @throws SvException
	 */
	public void editFieldProperties(String tableName, String fieldName, String value, String action, SvReader svr,
			SvWriter svw, boolean shouldCommit) throws SvException {
		DbDataObject field = null;
		DbDataObject table = SvReader.getDbtByName(tableName.toUpperCase());

		if (table == null) {
			throw new SvException("ERROR table named " + tableName + " is not found! ", svr.getInstanceUser());
		}

		DbSearchExpression dbse = new DbSearchExpression();
		DbSearchCriterion dbsc1 = new DbSearchCriterion(Tc.PARENT_ID, DbCompareOperand.EQUAL, table.getObjectId());
		DbSearchCriterion dbsc2 = new DbSearchCriterion(Tc.FIELD_NAME, DbCompareOperand.EQUAL, fieldName.toUpperCase());
		dbse.addDbSearchItem(dbsc1);
		dbse.addDbSearchItem(dbsc2);
		DbDataArray fields = svr.getObjects(dbse, SvReader.getTypeIdByName(Tc.SVAROG_FIELDS), null, 0, 0);

		if (fields != null && !fields.isEmpty()) {
			field = fields.get(0);
		} else {
			throw new SvException("ERROR field named " + fieldName + " is not found! ", svr.getInstanceUser());
		}

		if (action.equals(Tc.IS_NULL)) {
			field.setVal(action, true);
		} else {
			field.setVal(action, value);
		}
		if (field.getIsDirty()) {
			svw.saveObject(field, shouldCommit);
		}

		log4j.info("Object SVAROG_FIELDS: " + fieldName + " updated with new " + action);
	}

	/**
	 * Create Default SVAROG_PARAM_TYPE
	 * 
	 * @param rdr
	 * @param svw
	 * @param svr
	 * @throws SvException
	 */
	public void createDefaultParamTypes(Reader rdr, SvWriter svw, SvReader svr) throws SvException {
		createParamTypeIfNotPresent("param.activity_from", Tc.DATE, Tc.TEXT_AREA, rdr, svw, svr);
		createParamTypeIfNotPresent("param.activity_to", Tc.DATE, Tc.TEXT_AREA, rdr, svw, svr);
		createParamTypeIfNotPresent("param.population_sample.is_running", Tc.NVARCHAR, Tc.TEXT_AREA, rdr, svw, svr);
		createParamTypeIfNotPresent("param.population_sample.num_holdings", Tc.NVARCHAR, Tc.TEXT_AREA, rdr, svw, svr);
		createParamTypeIfNotPresent("param.population_sample.num_animals", Tc.NVARCHAR, Tc.TEXT_AREA, rdr, svw, svr);
		createParamTypeIfNotPresent("param.sv_form_field_type.multiple_answers", Tc.NVARCHAR, Tc.TEXT_AREA, rdr, svw,
				svr);
		createParamTypeIfNotPresent("param.sv_form_field_type.no_answers_opt", Tc.NVARCHAR, Tc.TEXT_AREA, rdr, svw,
				svr);
		createParamTypeIfNotPresent("param.sv_form.final_score", Tc.NVARCHAR, Tc.TEXT_AREA, rdr, svw, svr);
		createParamTypeIfNotPresent("param.sv_form_field_type.is_mandatory", Tc.NVARCHAR, Tc.TEXT_AREA, rdr, svw, svr);
		createParamTypeIfNotPresent("param.doc_ref", Tc.NVARCHAR, Tc.TEXT_AREA, rdr, svw, svr);
		createParamTypeIfNotPresent("param.rev_date", Tc.NVARCHAR, Tc.TEXT_AREA, rdr, svw, svr);
		createParamTypeIfNotPresent("param.recommendation", Tc.NVARCHAR, Tc.TEXT_AREA, rdr, svw, svr);
		createParamTypeIfNotPresent("param.note", Tc.NVARCHAR, Tc.TEXT_AREA, rdr, svw, svr);
	}

	/**
	 * Create SVAROG_PARAM_TYPE if it is not present
	 * 
	 * @param labelCode
	 * @param rdr
	 * @param svw
	 * @param svr
	 * @throws SvException
	 */
	public void createParamTypeIfNotPresent(String labelCode, String dataType, String inputType, Reader rdr,
			SvWriter svw, SvReader svr) throws SvException {
		DbDataObject paramType = rdr.searchDbObjectBySingleFilter(svCONST.OBJECT_TYPE_PARAM_TYPE, Tc.LABEL_CODE,
				labelCode, svr);
		if (paramType == null) {
			paramType = createParamType(labelCode, dataType, inputType);
			svw.saveObject(paramType);
		}
	}

	/**
	 * Create SVAROG_PARAM_TYPE
	 * 
	 * @param labelCode
	 * @param dataType
	 * @param inputType
	 * @return
	 */
	public DbDataObject createParamType(String labelCode, String dataType, String inputType) {
		DbDataObject dboSysParam = new DbDataObject();
		dboSysParam.setObjectType(svCONST.OBJECT_TYPE_PARAM_TYPE);
		dboSysParam.setVal(Tc.LABEL_CODE, labelCode);
		dboSysParam.setVal(Tc.DATA_TYPE, dataType);
		dboSysParam.setVal(Tc.INPUT_TYPE, inputType);
		return dboSysParam;
	}
}
