package com.prtech.svarog.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.prtech.svarog.I18n;
import com.prtech.svarog.SvCore;
import com.prtech.svarog.SvException;
import com.prtech.svarog.SvLink;
import com.prtech.svarog.SvReader;
import com.prtech.svarog.SvSecurity;
import com.prtech.svarog.SvSequence;
import com.prtech.svarog.SvUtil;
import com.prtech.svarog.SvWriter;
import com.prtech.svarog.svCONST;
import com.prtech.svarog.Sv;
import com.prtech.svarog_common.DbDataArray;
import com.prtech.svarog_common.DbDataObject;


public class DbWriter {

	static final Logger log4j = LogManager.getLogger(DbWriter.class.getName());

	/**
	 * Method in which we generate id for the new Disaster_claim
	 * 
	 * @param svr
	 * @return String
	 */
	public String generateUniqueId(String tableName, SvReader svr) throws SvException {
		Long objType = SvCore.getTypeIdByName(tableName);
		String sequence = Sv.EMPTY_STRING;
		String generateId = Sv.EMPTY_STRING;
		try (SvSequence svs = new SvSequence(svr.getSessionId())) {
			Long seqId = svs.getSeqNextVal(objType.toString(), false);
			Thread.sleep(2);
			sequence = String.format("%06d", Integer.valueOf(seqId.toString()));
			generateId = objType + sequence;
			svs.dbCommit();
		} catch (SvException | InterruptedException e) {
			log4j.error(e);
		}
		return generateId;
	}

	public void grantPermissions(DbDataObject dboSID, DbDataArray dbaCurrentAclObjects, List<String> list, SvReader svr)
			throws SvException {
		try (SvSecurity svs = new SvSecurity(svr)) {
			for (String permissionCode : list) {
				if (!checkIfDbDataArrayContainsPermission(dbaCurrentAclObjects, permissionCode))
					if (dboUserGroupHasPermission(dboSID, permissionCode, svr))
						svs.revokePermission(dboSID, permissionCode);
					else
						svs.grantPermission(dboSID, permissionCode);
			}
		}
	}

	/**
	 * Create user with the given parameters
	 * 
	 * @param userName
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param pin
	 * @param password
	 * @param svr
	 * @return
	 * @throws SvException
	 */
	public DbDataObject createDefaultTestUser(String userName, String firstName, String lastName, String email,
			String pin, String password, SvReader svr) throws SvException {
		DbDataObject dboUser = new DbReader().searchDbObjectBySingleFilter(svCONST.OBJECT_TYPE_USER, "USER_NAME",
				userName, svr);
		if (dboUser == null) {
			dboUser = new DbDataObject();
			dboUser.setObjectType(svCONST.OBJECT_TYPE_USER);
			dboUser.setVal("USER_TYPE", "INTERNAL");
			dboUser.setVal("USER_UID", SvUtil.getUUID());
			dboUser.setVal("USER_NAME", userName);
			dboUser.setVal("FIRST_NAME", firstName);
			dboUser.setVal("LAST_NAME", lastName);
			dboUser.setVal("PIN", pin);
			dboUser.setVal("E_MAIL", email);
			dboUser.setVal("PASSWORD_HASH", SvUtil.getMD5(SvUtil.getMD5(password)));
		} else {
			throw (new SvException("error.createUser.userAlreadyExist", svr.getInstanceUser()));
		}
		return dboUser;
	}

	/**
	 * Method that creates SVAROG links between users and groups
	 * 
	 * @param linkTypeObjId
	 * @param dbo1
	 * @param dbo2
	 * @return
	 */
	public DbDataObject createSvarogLink(Long linkTypeObjId, DbDataObject dbo1, DbDataObject dbo2) {
		DbDataObject dbLink = new DbDataObject();
		dbLink.setObjectType(svCONST.OBJECT_TYPE_LINK);
		dbLink.setVal(Svu.LINK_TYPE_ID, linkTypeObjId);
		dbLink.setVal(Svu.LINK_OBJ_ID_1, dbo1.getObjectId());
		dbLink.setVal(Svu.LINK_OBJ_ID_2, dbo2.getObjectId());
		return dbLink;
	}

	/**
	 * Create Custom ACL object
	 * 
	 * @param permission
	 * @return
	 */
	public DbDataObject createCustomDboAcl(String permission) {
		DbDataObject dboCustomAcl = new DbDataObject();
		dboCustomAcl.setObjectType(svCONST.OBJECT_TYPE_ACL);
		dboCustomAcl.setVal(Sv.ACCESS_TYPE, "READ");
		dboCustomAcl.setVal(Sv.ACL_OBJECT_ID, 0L);
		dboCustomAcl.setVal(Sv.ACL_OBJECT_TYPE, 50L);
		dboCustomAcl.setVal(Sv.ACL_CONFIG_UNQ, permission);
		dboCustomAcl.setVal(Sv.LABEL_CODE, permission);
		return dboCustomAcl;
	}

	/**
	 * Create custom SVAROG_CODE with the given parameters
	 * 
	 * @param codeValue
	 * @param labelCode
	 * @param parenLabelValue
	 * @param dbr
	 * @param svr
	 * @param svw
	 * @throws SvException
	 */
	public void createCustomCode(String codeValue, String labelCode, String parenLabelValue, DbReader dbr, SvReader svr,
			SvWriter svw) throws SvException {
		DbDataObject dbo = new DbDataObject();
		dbo.setObjectType(svCONST.OBJECT_TYPE_CODE);
		dbo.setParentId(dbr.findCodelistObjectId(parenLabelValue, svr));
		dbo.setVal(Svu.CODE_TYPE, null);
		dbo.setVal(Sv.CODE_VALUE, codeValue);
		dbo.setVal(Sv.LABEL_CODE, labelCode);
		dbo.setVal(Sv.SORT_ORDER, 0L);
		dbo.setVal(Sv.PARENT_CODE_VALUE, parenLabelValue);
		svw.saveObject(dbo);
	}

	/**
	 * Create custom SVAROG_LABEL with the given parameters
	 * 
	 * @param labelCode
	 * @param labelText
	 * @param localeId
	 * @param svr
	 * @param svw
	 * @throws SvException
	 */
	public void createCustomLabel(String labelCode, String labelText, String localeId, SvReader svr, SvWriter svw)
			throws SvException {
		if (localeId.equals(Sv.EMPTY_STRING)) {
			localeId = "en_US";
		}
		DbDataObject localeObj = new DbReader().searchDbObjectBySingleFilter(svCONST.OBJECT_TYPE_LOCALE, Sv.LOCALE_ID,
				localeId, svr);
		if (localeObj != null) {
			DbDataObject dbo = new DbDataObject();
			dbo.setObjectType(svCONST.OBJECT_TYPE_LABEL);
			dbo.setParentId(localeObj.getObjectId());
			dbo.setVal(Sv.LABEL_CODE, labelCode);
			dbo.setVal(Sv.LABEL_TEXT, labelText);
			dbo.setVal(Sv.LOCALE_ID, localeId);
			svw.saveObject(dbo);
			I18n.invalidateLabelsCache(dbo);
		}
	}
	
	public static boolean checkIfDbDataArrayContainsPermission(DbDataArray dbArrayPermissions, String permission) {
		boolean result = false;
		for (DbDataObject dbo : dbArrayPermissions.getItems()) {
			if (dbo.getVal(Sv.LABEL_CODE).equals(permission)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static boolean dboUserGroupHasPermission(DbDataObject dboUserGroup, String permissionCode, SvReader svr)
			throws SvException {
		DbReader reader = new DbReader();
		DbDataObject dboAcl = reader.getPermissionPerUserOrUserGroup(dboUserGroup, permissionCode, svr);
		if (dboAcl == null)
			return false;
		else
			return true;
	}
	
}
