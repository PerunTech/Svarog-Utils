package com.prtech.svarog.utils;

import java.util.ArrayList;
import java.util.Objects;

import org.apache.logging.log4j.Logger;


import com.prtech.svarog.Sv;
import com.prtech.svarog.SvConf;
import com.prtech.svarog.SvException;
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

	/**
	 * Method that checks if user group is already created
	 * 
	 * @param userGroupType
	 * @param userGroupName
	 * @param svr
	 * @return
	 * @throws SvException
	 */
	public static DbDataObject createUserGroupIfNotExist(String userGroupType, String userGroupName, String groupSecurityType,
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
	public static DbDataObject searchUserGroupInDb(Long objToSearchIn, String groupType, String groupName, SvReader svr)
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
		dboGroup.setVal(Sv.GROUP_NAME, groupName);
		dboGroup.setVal(Sv.E_MAIL, "admin@admin.com");
		dboGroup.setVal(Sv.GROUP_SECURITY_TYPE, groupSecurityType);
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
	public static DbDataObject searchUserInDb(String usernameFromConfiguration, SvReader svr) throws SvException {
		DbReader rdr = new DbReader();
		DbDataObject existingUser = rdr.searchDbObjectBySingleFilter(svCONST.OBJECT_TYPE_USER, Sv.USER_NAME.toString(),
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
	public static DbDataObject createUserIfNotExist(String pin, String userName, String email, String password,
			String firstName, String lastName, DbReader rdr, DbWriter dbw, SvReader svr, SvWriter svw)
			throws SvException {
		DbDataObject dboUser = searchUserInDb(userName, svr);
		if (dboUser == null) {
			dboUser = dbw.createDefaultTestUser(userName, firstName, lastName, email, pin, password, svr);
			log4j.trace("USER:" + dboUser.getVal(Sv.USER_NAME).toString() + ": created.");
		}
		return dboUser;
	}

	/**
	 * Method that returns link type for default user group membership
	 * 
	 * @return
	 * @throws SvException
	 */
	public static DbDataObject getUserLinkedToUserGroupDefault() throws SvException {
		DbDataObject linkTypeBetweenUserAndUserGroup = SvReader.getLinkType(Sv.USER_DEFAULT_GROUP,
				SvReader.getTypeIdByName(Tc.SVAROG_USERS), SvReader.getTypeIdByName(Tc.SVAROG_USER_GROUPS));
		return linkTypeBetweenUserAndUserGroup;
	}

	/**
	 * Method that returns link type for additional user group membership
	 * 
	 * @return
	 * @throws SvException
	 */
	public static DbDataObject getUserLinkedToUserGroupAdditional() throws SvException {
		DbDataObject linkBetweenUserAndUserGroup = SvReader.getLinkType(Sv.USER_GROUP,
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
	public static void createLinksBetweenUsersAndUserGroups(DbDataObject user, DbDataObject userGroup,
			DbDataArray finalDbDataArray, String userGroupType, SvReader svr) throws SvException {
		if (user.getObjectId() != 0L && userGroup.getObjectId() != 0L) {
			DbWriter dbw = new DbWriter();
			DbDataObject linkType = new DbDataObject();
			DbDataObject defaultGroup = searchUserGroupInDb(svCONST.OBJECT_TYPE_GROUP, userGroupType, userGroupType,
					svr);
			DbDataObject link = DbReader.findLink(user.getObjectId(), defaultGroup.getObjectId(), Sv.USER_DEFAULT_GROUP,
					svr);
			if (Objects.isNull(link)) {
				linkType = getUserLinkedToUserGroupDefault();
				finalDbDataArray.addDataItem(dbw.createSvarogLink(linkType.getObjectId(), user, defaultGroup));
			}
			link = DbReader.findLink(user.getObjectId(), userGroup.getObjectId(), Sv.USER_GROUP, svr);
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
	public static boolean setSidPermission(Boolean shouldCommit, String sidName, Long sidType,
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
	public static void attachPermissions(DbDataObject dboGroup, String permissionType, ArrayList<String> permissions,
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
	public static ArrayList<String> getSvarogCoreTablePermission(SvReader svr) throws SvException {
		ArrayList<String> corePermissions = new ArrayList<String>();
		DbSearchCriterion cr11 = new DbSearchCriterion(Tc.SYSTEM_TABLE, DbCompareOperand.EQUAL, true);
		DbDataArray dbArray1 = svr.getObjects(new DbSearchExpression().addDbSearchItem(cr11), svCONST.OBJECT_TYPE_TABLE,
				null, 0, 0);
		for (DbDataObject tempDbo : dbArray1.getItems()) {
			corePermissions.add(tempDbo.getVal(Sv.TABLE_NAME) + ".FULL");
		}
		return corePermissions;
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
	public static void createSysParamIfNotExist(String paramName, String paramValue, String paramType, boolean shouldCommit,
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
	public static Boolean checkIfSysParamExists(String paramNameValue, SvReader svr) throws SvException {
		DbReader rdr = new DbReader();
		Boolean result = false;
		DbDataObject dbo = rdr.searchDbObjectBySingleFilter(svCONST.OBJECT_TYPE_SYS_PARAMS, Sv.PARAM_NAME,
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
	public static DbDataObject createSysParam(String paramName, String paramValue, String paramType) {
		DbDataObject dboSysParam = new DbDataObject();
		dboSysParam.setObjectType(svCONST.OBJECT_TYPE_SYS_PARAMS);
		dboSysParam.setVal(Sv.PARAM_NAME, paramName);
		dboSysParam.setVal(Sv.PARAM_VALUE, paramValue);
		dboSysParam.setVal(Sv.PARAM_TYPE, paramType);
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
	public static DbDataObject checkIfACLexists(Long objToSearchIn, String labelCode, String accessType, SvReader svr)
			throws SvException {
		DbDataObject result = null;
		DbDataArray results = null;
		DbSearchCriterion cr1 = new DbSearchCriterion(Sv.LABEL_CODE.toString(), DbCompareOperand.EQUAL, labelCode);
		DbSearchCriterion cr2 = new DbSearchCriterion(Sv.ACCESS_TYPE.toString(), DbCompareOperand.EQUAL, accessType);
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
	public static void createAclObject(String labelCode, String aclConfigUnq, Long aclObjectType, String accessType,
			boolean shouldCommit, SvWriter svw, SvReader svr) throws SvException {
		DbDataObject searchedObject = checkIfACLexists(svCONST.OBJECT_TYPE_ACL, labelCode, accessType, svr);
		if (searchedObject == null) {
			DbDataObject aclObject = new DbDataObject();
			aclObject.setObjectType(svCONST.OBJECT_TYPE_ACL);
			aclObject.setVal(Sv.LABEL_CODE.toString(), labelCode);
			aclObject.setVal(Sv.ACL_CONFIG_UNQ, aclConfigUnq);
			aclObject.setVal(Sv.ACL_OBJECT_TYPE, aclObjectType);
			aclObject.setVal(Sv.ACCESS_TYPE, accessType);
			aclObject.setVal(Sv.ACL_OBJECT_ID, 0);
			svw.saveObject(aclObject, shouldCommit);
		} else {
			log4j.info("ACL object " + labelCode + " ,already exists.");
		}
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
	public static void createParamTypeIfNotPresent(String labelCode, String dataType, String inputType, Reader rdr,
			SvWriter svw, SvReader svr) throws SvException {
		DbDataObject paramType = rdr.searchDbObjectBySingleFilter(svCONST.OBJECT_TYPE_PARAM_TYPE, Sv.LABEL_CODE.toString(),
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
	public static DbDataObject createParamType(String labelCode, String dataType, String inputType) {
		DbDataObject dboSysParam = new DbDataObject();
		dboSysParam.setObjectType(svCONST.OBJECT_TYPE_PARAM_TYPE);
		dboSysParam.setVal(Sv.LABEL_CODE, labelCode);
		dboSysParam.setVal(Sv.DATA_TYPE, dataType);
		dboSysParam.setVal(Sv.INPUT_TYPE, inputType);
		return dboSysParam;
	}
}
